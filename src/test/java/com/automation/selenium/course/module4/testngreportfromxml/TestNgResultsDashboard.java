package com.automation.selenium.course.module4.testngreportfromxml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class TestNgResultsDashboard {

    private static final Path DEFAULT_INPUT = Path.of("test-output", "testng-results.xml");
    private static final Path FALLBACK_INPUT = Path.of("target", "surefire-reports", "testng-results.xml");
    private static final String DEFAULT_OUTPUT_FILE_NAME = "testng-results-pretty.html";
    private static final DateTimeFormatter GENERATED_AT =
        DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss z", Locale.ROOT);

    public GenerationResult generateReport(String[] args)
        throws IOException, ParserConfigurationException, SAXException {

        Path inputXml = resolveInput(args);
        Path outputHtml = resolveOutput(args, inputXml);
        return generateReport(inputXml, outputHtml);
    }

    public GenerationResult generateReport(Path inputXml, Path outputHtml)
        throws IOException, ParserConfigurationException, SAXException {

        Path normalizedInput = inputXml.toAbsolutePath().normalize();
        Path normalizedOutput = outputHtml.toAbsolutePath().normalize();

        if (!Files.exists(normalizedInput)) {
            throw new IOException("No se encontro el archivo XML: " + normalizedInput);
        }

        Report report = parse(normalizedInput);
        String html = renderHtml(report, normalizedInput);

        if (normalizedOutput.getParent() != null) {
            Files.createDirectories(normalizedOutput.getParent());
        }

        Files.writeString(normalizedOutput, html, StandardCharsets.UTF_8);
        return new GenerationResult(normalizedInput, normalizedOutput, report);
    }

    public Path resolveInput(String[] args) throws IOException {
        if (args != null && args.length > 0 && !args[0].isBlank()) {
            return Path.of(args[0]);
        }

        if (Files.exists(DEFAULT_INPUT)) {
            return DEFAULT_INPUT;
        }

        if (Files.exists(FALLBACK_INPUT)) {
            return FALLBACK_INPUT;
        }

        throw new IOException(
            "No se encontro testng-results.xml. Busque en "
                + DEFAULT_INPUT + " y " + FALLBACK_INPUT + "."
        );
    }

    public Path resolveOutput(String[] args, Path inputXml) {
        if (args != null && args.length > 1 && !args[1].isBlank()) {
            return Path.of(args[1]);
        }

        Path parent = inputXml.getParent();
        if (parent == null) {
            return Path.of(DEFAULT_OUTPUT_FILE_NAME);
        }

        return parent.resolve(DEFAULT_OUTPUT_FILE_NAME);
    }

    Report parse(Path inputXml) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        configureFactory(factory);

        DocumentBuilder builder = factory.newDocumentBuilder();
        try (InputStream inputStream = Files.newInputStream(inputXml)) {
            Document document = builder.parse(inputStream);
            Element root = document.getDocumentElement();

            List<SuiteResult> suites = new ArrayList<>();
            for (Element suiteElement : childElements(root, "suite")) {
                suites.add(parseSuite(suiteElement));
            }

            int calculatedTotal = countExecutableMethods(suites);
            return new Report(
                intAttribute(root, "total", calculatedTotal),
                intAttribute(root, "passed", countSuiteMethodsWithStatus(suites, "PASS")),
                intAttribute(root, "failed", countSuiteMethodsWithStatus(suites, "FAIL")),
                intAttribute(root, "skipped", countSuiteMethodsWithStatus(suites, "SKIP")),
                intAttribute(root, "ignored", 0),
                extractReporterOutput(root),
                List.copyOf(suites)
            );
        }
    }

    private SuiteResult parseSuite(Element suiteElement) {
        List<TestResult> tests = new ArrayList<>();
        for (Element testElement : childElements(suiteElement, "test")) {
            tests.add(parseTest(testElement));
        }

        return new SuiteResult(
            attribute(suiteElement, "name"),
            normalizeTimestamp(attribute(suiteElement, "started-at")),
            normalizeTimestamp(attribute(suiteElement, "finished-at")),
            longAttribute(suiteElement, "duration-ms", 0L),
            extractReporterOutput(suiteElement),
            List.copyOf(tests)
        );
    }

    private TestResult parseTest(Element testElement) {
        List<ClassResult> classes = new ArrayList<>();
        for (Element classElement : childElements(testElement, "class")) {
            classes.add(parseClass(classElement));
        }

        return new TestResult(
            attribute(testElement, "name"),
            normalizeTimestamp(attribute(testElement, "started-at")),
            normalizeTimestamp(attribute(testElement, "finished-at")),
            longAttribute(testElement, "duration-ms", 0L),
            extractReporterOutput(testElement),
            List.copyOf(classes)
        );
    }

    private ClassResult parseClass(Element classElement) {
        List<MethodResult> methods = new ArrayList<>();
        for (Element methodElement : childElements(classElement, "test-method")) {
            methods.add(parseMethod(methodElement, attribute(classElement, "name")));
        }

        return new ClassResult(
            attribute(classElement, "name"),
            extractReporterOutput(classElement),
            List.copyOf(methods)
        );
    }

    private MethodResult parseMethod(Element methodElement, String className) {
        ExceptionInfo exceptionInfo = null;
        Element exceptionElement = firstChild(methodElement, "exception");

        if (exceptionElement != null) {
            exceptionInfo = new ExceptionInfo(
                attribute(exceptionElement, "class"),
                normalizeText(childText(exceptionElement, "message")),
                normalizeText(childText(exceptionElement, "full-stacktrace"))
            );
        }

        return new MethodResult(
            className,
            attribute(methodElement, "name"),
            normalizeText(attribute(methodElement, "description")),
            normalizeText(attribute(methodElement, "signature")),
            normalizeStatus(attribute(methodElement, "status")),
            Boolean.parseBoolean(attribute(methodElement, "is-config")),
            normalizeTimestamp(attribute(methodElement, "started-at")),
            normalizeTimestamp(attribute(methodElement, "finished-at")),
            longAttribute(methodElement, "duration-ms", 0L),
            extractParams(methodElement),
            extractReporterOutput(methodElement),
            exceptionInfo
        );
    }

    private String renderHtml(Report report, Path inputXml) {
        StringBuilder html = new StringBuilder(30_000);

        html.append("""
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>TestNG Pretty Report</title>
  <style>
    :root {
      --bg: #f4eee5;
      --panel: rgba(255, 251, 246, 0.9);
      --line: rgba(31, 42, 52, 0.12);
      --ink: #1f2a34;
      --muted: #61707c;
      --teal: #0f766e;
      --green: #1f8a5b;
      --amber: #b7791f;
      --red: #c84b31;
      --shadow: 0 20px 48px rgba(24, 36, 47, 0.12);
    }
    * { box-sizing: border-box; }
    body {
      margin: 0;
      color: var(--ink);
      font-family: "Trebuchet MS", "Segoe UI", sans-serif;
      background:
        radial-gradient(circle at top left, rgba(15, 118, 110, 0.18), transparent 28%),
        radial-gradient(circle at top right, rgba(200, 75, 49, 0.12), transparent 24%),
        linear-gradient(180deg, #f8f4ed 0%, #efe6d8 100%);
    }
    h1, h2, h3, h4, h5 {
      margin: 0;
      font-family: Georgia, "Palatino Linotype", serif;
    }
    code, pre {
      font-family: Consolas, "Courier New", monospace;
    }
    .page {
      width: min(1180px, calc(100% - 28px));
      margin: 28px auto 50px;
    }
    .panel {
      background: var(--panel);
      border: 1px solid var(--line);
      border-radius: 22px;
      box-shadow: var(--shadow);
      backdrop-filter: blur(10px);
    }
    .hero, .section { padding: 24px; }
    .hero {
      display: grid;
      gap: 18px;
      grid-template-columns: 1.7fr 1fr;
      position: relative;
      overflow: hidden;
    }
    .hero::after {
      content: "";
      position: absolute;
      right: -80px;
      bottom: -110px;
      width: 280px;
      height: 280px;
      border-radius: 50%;
      background: radial-gradient(circle, rgba(15, 118, 110, 0.22), transparent 70%);
    }
    .eyebrow {
      margin: 0 0 10px;
      color: var(--teal);
      text-transform: uppercase;
      letter-spacing: 0.18em;
      font-size: 0.74rem;
      font-weight: 700;
    }
    .subtitle {
      margin: 14px 0 0;
      color: var(--muted);
      line-height: 1.6;
      max-width: 70ch;
    }
    .meta-grid, .summary-grid, .method-grid {
      display: grid;
      gap: 14px;
    }
    .summary-grid {
      margin-top: 18px;
      grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
    }
    .meta-card, .summary-card, .test-block, .class-block, .method-card {
      background: rgba(255, 255, 255, 0.75);
      border: 1px solid var(--line);
      border-radius: 18px;
    }
    .meta-card, .summary-card { padding: 15px; }
    .meta-label, .label {
      display: block;
      color: var(--muted);
      text-transform: uppercase;
      letter-spacing: 0.12em;
      font-size: 0.74rem;
      font-weight: 700;
      margin-bottom: 8px;
    }
    .summary-value {
      font-size: 2rem;
      font-weight: 700;
      line-height: 1;
    }
    .summary-note, .section-meta, .class-path, .method-description {
      color: var(--muted);
      line-height: 1.55;
    }
    .summary-card.pass { border-top: 4px solid var(--green); }
    .summary-card.fail { border-top: 4px solid var(--red); }
    .summary-card.skip { border-top: 4px solid var(--amber); }
    .summary-card.config { border-top: 4px solid var(--teal); }
    .section { margin-top: 18px; }
    .section-header {
      display: flex;
      justify-content: space-between;
      gap: 16px;
      align-items: start;
      flex-wrap: wrap;
      margin-bottom: 16px;
    }
    .legend {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
    }
    .pill {
      display: inline-flex;
      align-items: center;
      padding: 6px 10px;
      border-radius: 999px;
      font-size: 0.8rem;
      font-weight: 700;
      border: 1px solid transparent;
      white-space: nowrap;
    }
    .pass { color: var(--green); background: #ddf6e8; border-color: rgba(31, 138, 91, 0.2); }
    .fail { color: var(--red); background: #ffe3dd; border-color: rgba(200, 75, 49, 0.2); }
    .skip { color: var(--amber); background: #fff2d9; border-color: rgba(183, 121, 31, 0.2); }
    .config { color: var(--teal); background: #daf5f1; border-color: rgba(15, 118, 110, 0.2); }
    .muted { color: #5d6a75; background: rgba(99, 110, 123, 0.1); border-color: rgba(99, 110, 123, 0.15); }
    .bar {
      display: flex;
      gap: 8px;
      margin-top: 16px;
      min-height: 18px;
    }
    .segment {
      min-width: 8px;
      border-radius: 999px;
    }
    .segment.pass { background: linear-gradient(90deg, #2b9a6a, #76d39b); }
    .segment.fail { background: linear-gradient(90deg, #c84b31, #ef8c77); }
    .segment.skip { background: linear-gradient(90deg, #b7791f, #e7bc6c); }
    .segment.muted { background: linear-gradient(90deg, #667788, #a7b3bf); }
    .test-block, .class-block, .method-card { padding: 18px; }
    .test-block + .test-block, .class-block + .class-block { margin-top: 16px; }
    .method-grid {
      grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
      margin-top: 16px;
    }
    .method-card { border-left: 6px solid var(--line); }
    .method-card.pass { border-left-color: var(--green); }
    .method-card.fail { border-left-color: var(--red); }
    .method-card.skip { border-left-color: var(--amber); }
    .method-card.config { border-left-color: var(--teal); }
    .method-top {
      display: flex;
      justify-content: space-between;
      gap: 12px;
      align-items: start;
    }
    .method-name {
      margin-top: 6px;
      font-size: 1.15rem;
    }
    .kv-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(110px, 1fr));
      gap: 10px;
      margin-top: 14px;
    }
    .kv {
      padding: 10px 12px;
      border-radius: 14px;
      background: rgba(15, 118, 110, 0.05);
    }
    .kv strong {
      display: block;
      margin-top: 6px;
      line-height: 1.4;
    }
    .signature, .stacktrace {
      margin-top: 14px;
      padding: 12px;
      border-radius: 14px;
      overflow-x: auto;
    }
    .signature { background: rgba(31, 42, 52, 0.05); }
    .stacktrace {
      background: #1f2730;
      color: #edf4f0;
      white-space: pre-wrap;
      word-break: break-word;
      line-height: 1.55;
    }
    .chips {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      margin-top: 14px;
    }
    .chip {
      padding: 8px 10px;
      border-radius: 12px;
      background: rgba(99, 110, 123, 0.1);
      overflow-wrap: anywhere;
    }
    details {
      margin-top: 14px;
      border: 1px solid rgba(31, 42, 52, 0.08);
      border-radius: 14px;
      background: rgba(255, 255, 255, 0.62);
      overflow: hidden;
    }
    summary {
      cursor: pointer;
      padding: 12px 14px;
      font-weight: 700;
      list-style: none;
    }
    summary::-webkit-details-marker { display: none; }
    .details-content { padding: 0 14px 14px; }
    .reporter-list {
      margin: 0;
      padding-left: 18px;
      color: var(--muted);
      line-height: 1.6;
    }
    .empty {
      padding: 18px;
      border-radius: 18px;
      border: 1px dashed rgba(31, 42, 52, 0.18);
      color: var(--muted);
      background: rgba(255, 255, 255, 0.65);
    }
    @media (max-width: 860px) {
      .hero { grid-template-columns: 1fr; }
      .page { width: min(100% - 16px, 1180px); margin-top: 18px; }
    }
  </style>
</head>
<body>
  <main class="page">
""");

        appendHero(html, report, inputXml);
        appendSummary(html, report);
        appendDistribution(html, report);

        if (!report.reporterOutput().isEmpty()) {
            html.append("<section class=\"panel section\">");
            html.append("<div class=\"section-header\"><div><h2>Reporter output</h2>");
            html.append("<p class=\"section-meta\">Mensajes encontrados en la raiz del XML.</p></div></div>");
            appendReporterOutput(html, report.reporterOutput());
            html.append("</section>");
        }

        if (report.suites().isEmpty()) {
            html.append("<section class=\"panel section\"><h2>No suites found</h2>");
            html.append("<div class=\"empty\">El XML no contiene nodos <code>suite</code>.</div></section>");
        } else {
            for (SuiteResult suite : report.suites()) {
                appendSuite(html, suite);
            }
        }

        html.append("""
  </main>
</body>
</html>
""");

        return html.toString();
    }

    private void appendHero(StringBuilder html, Report report, Path inputXml) {
        html.append("<section class=\"panel hero\">");
        html.append("<div>");
        html.append("<p class=\"eyebrow\">TestNG XML viewer</p>");
        html.append("<h1>Pretty dashboard for testng-results.xml</h1>");
        html.append("<p class=\"subtitle\">Visualizacion clara de suites, tests, clases, metodos, errores y parametros.</p>");
        html.append("</div>");
        html.append("<div class=\"meta-grid\">");
        appendMetaCard(html, "XML source", inputXml.toAbsolutePath().toString());
        appendMetaCard(html, "Generated", GENERATED_AT.format(ZonedDateTime.now()));
        appendMetaCard(
            html,
            "Coverage",
            report.suites().size() + " suite(s), " + countTests(report.suites()) + " test block(s), "
                + countClasses(report.suites()) + " class(es)"
        );
        html.append("</div></section>");
    }

    private void appendMetaCard(StringBuilder html, String label, String value) {
        html.append("<article class=\"meta-card\">");
        html.append("<span class=\"meta-label\">").append(escapeHtml(label)).append("</span>");
        html.append("<div>").append(escapeHtml(value)).append("</div>");
        html.append("</article>");
    }

    private void appendSummary(StringBuilder html, Report report) {
        html.append("<section class=\"summary-grid\">");
        appendSummaryCard(html, "Total", String.valueOf(report.total()), "Executable test methods", "");
        appendSummaryCard(html, "Passed", String.valueOf(report.passed()), "Successful methods", "pass");
        appendSummaryCard(html, "Failed", String.valueOf(report.failed()), "Methods with exceptions", "fail");
        appendSummaryCard(html, "Skipped", String.valueOf(report.skipped()), "Skipped methods", "skip");
        appendSummaryCard(html, "Ignored", String.valueOf(report.ignored()), "Ignored by TestNG", "");
        appendSummaryCard(html, "Success rate", formatPercentage(report.successRate()), "Based on executable methods", "pass");
        appendSummaryCard(html, "Config methods", String.valueOf(countConfigurationMethods(report.suites())), "Setup and teardown", "config");
        html.append("</section>");
    }

    private void appendSummaryCard(StringBuilder html, String title, String value, String note, String cssClass) {
        html.append("<article class=\"summary-card");
        if (!cssClass.isBlank()) {
            html.append(" ").append(escapeHtml(cssClass));
        }
        html.append("\">");
        html.append("<span class=\"label\">").append(escapeHtml(title)).append("</span>");
        html.append("<div class=\"summary-value\">").append(escapeHtml(value)).append("</div>");
        html.append("<div class=\"summary-note\">").append(escapeHtml(note)).append("</div>");
        html.append("</article>");
    }

    private void appendDistribution(StringBuilder html, Report report) {
        double total = Math.max(report.total() + report.ignored(), 1);
        html.append("<section class=\"panel section\">");
        html.append("<div class=\"section-header\">");
        html.append("<div><h2>Execution split</h2>");
        html.append("<p class=\"section-meta\">Distribucion de resultados de la corrida.</p></div>");
        html.append("<div class=\"legend\">");
        html.append(renderPill("pass", "PASS " + report.passed()));
        html.append(renderPill("fail", "FAIL " + report.failed()));
        html.append(renderPill("skip", "SKIP " + report.skipped()));
        html.append(renderPill("muted", "IGNORED " + report.ignored()));
        html.append("</div></div><div class=\"bar\">");
        appendSegment(html, "pass", (report.passed() / total) * 100);
        appendSegment(html, "fail", (report.failed() / total) * 100);
        appendSegment(html, "skip", (report.skipped() / total) * 100);
        appendSegment(html, "muted", (report.ignored() / total) * 100);
        html.append("</div></section>");
    }

    private void appendSegment(StringBuilder html, String cssClass, double width) {
        if (width <= 0) {
            return;
        }
        html.append("<span class=\"segment ").append(escapeHtml(cssClass)).append("\" style=\"width: ")
            .append(String.format(Locale.ROOT, "%.2f", width)).append("%\"></span>");
    }

    private void appendSuite(StringBuilder html, SuiteResult suite) {
        List<MethodResult> executableMethods = executableMethods(suite);
        html.append("<section class=\"panel section\">");
        html.append("<div class=\"section-header\"><div>");
        html.append("<h2>").append(escapeHtml(fallbackName(suite.name(), "Unnamed suite"))).append("</h2>");
        html.append("<p class=\"section-meta\">Started: ").append(escapeHtml(fallbackName(suite.startedAt(), "N/A")));
        html.append(" | Finished: ").append(escapeHtml(fallbackName(suite.finishedAt(), "N/A")));
        html.append(" | Duration: ").append(escapeHtml(formatDuration(suite.durationMs()))).append("</p>");
        html.append("</div><div class=\"legend\">");
        html.append(renderPill("pass", "PASS " + countMethodResultsWithStatus(executableMethods, "PASS")));
        html.append(renderPill("fail", "FAIL " + countMethodResultsWithStatus(executableMethods, "FAIL")));
        html.append(renderPill("skip", "SKIP " + countMethodResultsWithStatus(executableMethods, "SKIP")));
        html.append("</div></div>");
        if (!suite.reporterOutput().isEmpty()) {
            appendReporterOutput(html, suite.reporterOutput());
        }
        if (suite.tests().isEmpty()) {
            html.append("<div class=\"empty\">Esta suite no contiene nodos <code>test</code>.</div>");
        } else {
            for (TestResult test : suite.tests()) {
                appendTest(html, test);
            }
        }
        html.append("</section>");
    }

    private void appendTest(StringBuilder html, TestResult test) {
        List<MethodResult> executableMethods = executableMethods(test);
        html.append("<article class=\"test-block\">");
        html.append("<div class=\"section-header\"><div>");
        html.append("<h3>").append(escapeHtml(fallbackName(test.name(), "Unnamed test"))).append("</h3>");
        html.append("<p class=\"section-meta\">Started: ").append(escapeHtml(fallbackName(test.startedAt(), "N/A")));
        html.append(" | Finished: ").append(escapeHtml(fallbackName(test.finishedAt(), "N/A")));
        html.append(" | Duration: ").append(escapeHtml(formatDuration(test.durationMs()))).append("</p>");
        html.append("</div><div class=\"legend\">");
        html.append(renderPill("pass", "PASS " + countMethodResultsWithStatus(executableMethods, "PASS")));
        html.append(renderPill("fail", "FAIL " + countMethodResultsWithStatus(executableMethods, "FAIL")));
        html.append(renderPill("skip", "SKIP " + countMethodResultsWithStatus(executableMethods, "SKIP")));
        html.append("</div></div>");
        if (!test.reporterOutput().isEmpty()) {
            appendReporterOutput(html, test.reporterOutput());
        }
        if (test.classes().isEmpty()) {
            html.append("<div class=\"empty\">Este bloque <code>test</code> no contiene clases.</div>");
        } else {
            for (ClassResult classResult : test.classes()) {
                appendClass(html, classResult);
            }
        }
        html.append("</article>");
    }

    private void appendClass(StringBuilder html, ClassResult classResult) {
        List<MethodResult> testMethods = executableMethods(classResult);
        List<MethodResult> configMethods = configurationMethods(classResult);
        html.append("<section class=\"class-block\">");
        html.append("<div class=\"section-header\"><div>");
        html.append("<h4>").append(escapeHtml(shortClassName(classResult.name()))).append("</h4>");
        html.append("<p class=\"class-path\">").append(escapeHtml(fallbackName(classResult.name(), "Unknown class"))).append("</p>");
        html.append("</div><div class=\"legend\">");
        html.append(renderPill("pass", "PASS " + countMethodResultsWithStatus(testMethods, "PASS")));
        html.append(renderPill("fail", "FAIL " + countMethodResultsWithStatus(testMethods, "FAIL")));
        html.append(renderPill("skip", "SKIP " + countMethodResultsWithStatus(testMethods, "SKIP")));
        html.append(renderPill("config", "CONFIG " + configMethods.size()));
        html.append("</div></div>");
        if (!classResult.reporterOutput().isEmpty()) {
            appendReporterOutput(html, classResult.reporterOutput());
        }
        if (testMethods.isEmpty()) {
            html.append("<div class=\"empty\">No hay metodos de test ejecutables en esta clase.</div>");
        } else {
            html.append("<div class=\"method-grid\">");
            for (MethodResult method : testMethods) {
                appendMethodCard(html, method);
            }
            html.append("</div>");
        }
        if (!configMethods.isEmpty()) {
            html.append("<details><summary>Config methods (").append(configMethods.size()).append(")</summary>");
            html.append("<div class=\"details-content\"><div class=\"method-grid\">");
            for (MethodResult method : configMethods) {
                appendMethodCard(html, method);
            }
            html.append("</div></div></details>");
        }
        html.append("</section>");
    }

    private void appendMethodCard(StringBuilder html, MethodResult method) {
        String cssClass = method.configuration() ? "config" : cssClassForStatus(method.status());
        String type = method.configuration() ? "Configuration" : "Test method";
        html.append("<article class=\"method-card ").append(escapeHtml(cssClass)).append("\">");
        html.append("<div class=\"method-top\"><div>");
        html.append("<span class=\"label\">").append(escapeHtml(type)).append("</span>");
        html.append("<h5 class=\"method-name\">").append(escapeHtml(fallbackName(method.name(), "Unnamed method"))).append("</h5>");
        if (!method.description().isBlank()) {
            html.append("<p class=\"method-description\">").append(escapeHtml(method.description())).append("</p>");
        }
        html.append("</div>").append(renderPill(cssClass, fallbackName(method.status(), "UNKNOWN"))).append("</div>");
        html.append("<div class=\"kv-grid\">");
        appendKv(html, "Duration", formatDuration(method.durationMs()));
        appendKv(html, "Started", fallbackName(method.startedAt(), "N/A"));
        appendKv(html, "Finished", fallbackName(method.finishedAt(), "N/A"));
        appendKv(html, "Class", shortClassName(method.className()));
        html.append("</div>");
        if (!method.signature().isBlank()) {
            html.append("<div class=\"signature\"><code>").append(escapeHtml(method.signature())).append("</code></div>");
        }
        if (!method.params().isEmpty()) {
            html.append("<div class=\"chips\">");
            for (String param : method.params()) {
                html.append("<span class=\"chip\">").append(escapeHtml(param)).append("</span>");
            }
            html.append("</div>");
        }
        if (!method.reporterOutput().isEmpty()) {
            html.append("<details><summary>Reporter output</summary><div class=\"details-content\">");
            appendReporterOutput(html, method.reporterOutput());
            html.append("</div></details>");
        }
        if (method.exceptionInfo() != null) {
            html.append("<details open><summary>Failure details</summary><div class=\"details-content\">");
            if (!method.exceptionInfo().exceptionClass().isBlank()) {
                appendKv(html, "Exception", method.exceptionInfo().exceptionClass());
            }
            if (!method.exceptionInfo().message().isBlank()) {
                html.append("<div class=\"signature\"><code>")
                    .append(escapeHtml(method.exceptionInfo().message()))
                    .append("</code></div>");
            }
            if (!method.exceptionInfo().stackTrace().isBlank()) {
                html.append("<pre class=\"stacktrace\">")
                    .append(escapeHtml(method.exceptionInfo().stackTrace()))
                    .append("</pre>");
            }
            html.append("</div></details>");
        }
        html.append("</article>");
    }

    private void appendKv(StringBuilder html, String label, String value) {
        html.append("<div class=\"kv\">");
        html.append("<span class=\"label\">").append(escapeHtml(label)).append("</span>");
        html.append("<strong>").append(escapeHtml(value)).append("</strong>");
        html.append("</div>");
    }

    private void appendReporterOutput(StringBuilder html, List<String> lines) {
        html.append("<ul class=\"reporter-list\">");
        for (String line : lines) {
            html.append("<li>").append(escapeHtml(line)).append("</li>");
        }
        html.append("</ul>");
    }

    private static void configureFactory(DocumentBuilderFactory factory) throws ParserConfigurationException {
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setExpandEntityReferences(false);
        factory.setXIncludeAware(false);
        factory.setNamespaceAware(false);
    }

    private static List<Element> childElements(Element parent, String tagName) {
        List<Element> elements = new ArrayList<>();
        NodeList childNodes = parent.getChildNodes();

        for (int index = 0; index < childNodes.getLength(); index++) {
            Node node = childNodes.item(index);
            if (node.getNodeType() == Node.ELEMENT_NODE && tagName.equals(node.getNodeName())) {
                elements.add((Element) node);
            }
        }

        return elements;
    }

    private static Element firstChild(Element parent, String tagName) {
        for (Element child : childElements(parent, tagName)) {
            return child;
        }
        return null;
    }

    private static String attribute(Element element, String name) {
        if (!element.hasAttribute(name)) {
            return "";
        }
        return normalizeText(element.getAttribute(name));
    }

    private static int intAttribute(Element element, String name, int fallback) {
        String value = attribute(element, name);
        if (value.isBlank()) {
            return fallback;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    private static long longAttribute(Element element, String name, long fallback) {
        String value = attribute(element, name);
        if (value.isBlank()) {
            return fallback;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    private static String childText(Element parent, String childName) {
        Element child = firstChild(parent, childName);
        return child == null ? "" : child.getTextContent();
    }

    private static List<String> extractParams(Element methodElement) {
        Element paramsElement = firstChild(methodElement, "params");
        if (paramsElement == null) {
            return List.of();
        }

        List<String> params = new ArrayList<>();
        for (Element paramElement : childElements(paramsElement, "param")) {
            Element valueElement = firstChild(paramElement, "value");
            if (valueElement != null) {
                String value = normalizeText(valueElement.getTextContent());
                if (!value.isBlank()) {
                    params.add(value);
                }
            }
        }

        return List.copyOf(params);
    }

    private static List<String> extractReporterOutput(Element element) {
        Element reporterOutput = firstChild(element, "reporter-output");
        if (reporterOutput == null) {
            return List.of();
        }

        List<String> lines = new ArrayList<>();
        NodeList childNodes = reporterOutput.getChildNodes();
        boolean hasChildElements = false;

        for (int index = 0; index < childNodes.getLength(); index++) {
            Node node = childNodes.item(index);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                hasChildElements = true;
                String value = normalizeText(node.getTextContent());
                if (!value.isBlank()) {
                    lines.add(value);
                }
            }
        }

        if (!hasChildElements) {
            return splitLines(reporterOutput.getTextContent());
        }

        return List.copyOf(lines);
    }

    private static List<String> splitLines(String text) {
        return Arrays.stream(normalizeText(text).split("\\R"))
            .map(String::trim)
            .filter(line -> !line.isBlank())
            .toList();
    }

    private static String normalizeText(String text) {
        return text == null ? "" : text.trim();
    }

    private static String normalizeTimestamp(String text) {
        return normalizeText(text).replace('T', ' ');
    }

    private static String normalizeStatus(String status) {
        String normalized = normalizeText(status).toUpperCase(Locale.ROOT);
        return switch (normalized) {
            case "SUCCESS" -> "PASS";
            case "FAILED" -> "FAIL";
            case "SKIPPED" -> "SKIP";
            case "" -> "UNKNOWN";
            default -> normalized;
        };
    }

    private static int countExecutableMethods(List<SuiteResult> suites) {
        int total = 0;
        for (SuiteResult suite : suites) {
            total += executableMethods(suite).size();
        }
        return total;
    }

    private static int countSuiteMethodsWithStatus(List<SuiteResult> suites, String status) {
        int total = 0;
        for (SuiteResult suite : suites) {
            total += countMethodResultsWithStatus(executableMethods(suite), status);
        }
        return total;
    }

    private static int countMethodResultsWithStatus(List<MethodResult> methods, String status) {
        int total = 0;
        for (MethodResult method : methods) {
            if (status.equalsIgnoreCase(method.status())) {
                total++;
            }
        }
        return total;
    }

    private static int countConfigurationMethods(List<SuiteResult> suites) {
        int total = 0;
        for (SuiteResult suite : suites) {
            for (TestResult test : suite.tests()) {
                for (ClassResult classResult : test.classes()) {
                    total += configurationMethods(classResult).size();
                }
            }
        }
        return total;
    }

    private static int countTests(List<SuiteResult> suites) {
        int total = 0;
        for (SuiteResult suite : suites) {
            total += suite.tests().size();
        }
        return total;
    }

    private static int countClasses(List<SuiteResult> suites) {
        int total = 0;
        for (SuiteResult suite : suites) {
            for (TestResult test : suite.tests()) {
                total += test.classes().size();
            }
        }
        return total;
    }

    private static List<MethodResult> executableMethods(SuiteResult suite) {
        List<MethodResult> methods = new ArrayList<>();
        for (TestResult test : suite.tests()) {
            methods.addAll(executableMethods(test));
        }
        return methods;
    }

    private static List<MethodResult> executableMethods(TestResult test) {
        List<MethodResult> methods = new ArrayList<>();
        for (ClassResult classResult : test.classes()) {
            methods.addAll(executableMethods(classResult));
        }
        return methods;
    }

    private static List<MethodResult> executableMethods(ClassResult classResult) {
        List<MethodResult> methods = new ArrayList<>();
        for (MethodResult method : classResult.methods()) {
            if (!method.configuration()) {
                methods.add(method);
            }
        }
        return methods;
    }

    private static List<MethodResult> configurationMethods(ClassResult classResult) {
        List<MethodResult> methods = new ArrayList<>();
        for (MethodResult method : classResult.methods()) {
            if (method.configuration()) {
                methods.add(method);
            }
        }
        return methods;
    }

    private static String formatDuration(long durationMs) {
        if (durationMs < 1_000) {
            return durationMs + " ms";
        }

        if (durationMs < 60_000) {
            return String.format(Locale.ROOT, "%.2f s", durationMs / 1_000.0);
        }

        long minutes = durationMs / 60_000;
        double seconds = (durationMs % 60_000) / 1_000.0;
        return String.format(Locale.ROOT, "%d min %.2f s", minutes, seconds);
    }

    private static String formatPercentage(double value) {
        return String.format(Locale.ROOT, "%.1f%%", value);
    }

    private static String shortClassName(String className) {
        String safeName = fallbackName(className, "Unknown class");
        int index = safeName.lastIndexOf('.');
        return index >= 0 ? safeName.substring(index + 1) : safeName;
    }

    private static String fallbackName(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private static String cssClassForStatus(String status) {
        return switch (normalizeStatus(status)) {
            case "PASS" -> "pass";
            case "FAIL" -> "fail";
            case "SKIP" -> "skip";
            default -> "muted";
        };
    }

    private static String renderPill(String cssClass, String text) {
        return "<span class=\"pill " + escapeHtml(cssClass) + "\">" + escapeHtml(text) + "</span>";
    }

    private static String escapeHtml(String text) {
        if (text == null) {
            return "";
        }

        StringBuilder escaped = new StringBuilder(text.length() + 16);
        for (int index = 0; index < text.length(); index++) {
            char current = text.charAt(index);
            switch (current) {
                case '&' -> escaped.append("&amp;");
                case '<' -> escaped.append("&lt;");
                case '>' -> escaped.append("&gt;");
                case '"' -> escaped.append("&quot;");
                case '\'' -> escaped.append("&#39;");
                default -> escaped.append(current);
            }
        }
        return escaped.toString();
    }

    public record GenerationResult(Path inputXml, Path outputHtml, Report report) {
    }

    public record Report(
        int total,
        int passed,
        int failed,
        int skipped,
        int ignored,
        List<String> reporterOutput,
        List<SuiteResult> suites
    ) {
        public double successRate() {
            return total <= 0 ? 0.0 : (passed * 100.0) / total;
        }
    }

    public record SuiteResult(
        String name,
        String startedAt,
        String finishedAt,
        long durationMs,
        List<String> reporterOutput,
        List<TestResult> tests
    ) {
    }

    public record TestResult(
        String name,
        String startedAt,
        String finishedAt,
        long durationMs,
        List<String> reporterOutput,
        List<ClassResult> classes
    ) {
    }

    public record ClassResult(
        String name,
        List<String> reporterOutput,
        List<MethodResult> methods
    ) {
    }

    public record MethodResult(
        String className,
        String name,
        String description,
        String signature,
        String status,
        boolean configuration,
        String startedAt,
        String finishedAt,
        long durationMs,
        List<String> params,
        List<String> reporterOutput,
        ExceptionInfo exceptionInfo
    ) {
    }

    public record ExceptionInfo(
        String exceptionClass,
        String message,
        String stackTrace
    ) {
    }
}
