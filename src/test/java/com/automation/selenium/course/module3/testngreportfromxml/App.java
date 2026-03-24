package com.automation.selenium.course.module3.testngreportfromxml;

public class App {
    public static void main(String[] args) {
        TestNgResultsDashboard dashboard = new TestNgResultsDashboard();

        try {
            TestNgResultsDashboard.GenerationResult result = dashboard.generateReport(args);
            System.out.println("Reporte HTML generado en: " + result.outputHtml().toAbsolutePath());
            System.out.println(
                "Resumen -> total: " + result.report().total()
                    + ", passed: " + result.report().passed()
                    + ", failed: " + result.report().failed()
                    + ", skipped: " + result.report().skipped()
                    + ", ignored: " + result.report().ignored()
            );
        } catch (Exception exception) {
            System.err.println("No se pudo generar el reporte pretty de TestNG.");
            exception.printStackTrace(System.err);
        }
    }
}
