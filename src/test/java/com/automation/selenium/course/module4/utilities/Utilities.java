package com.automation.selenium.course.module4.utilities;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Utilities {
	public static Object[][] readFromExcelFile(final String path, final String sheetName) throws Exception {
		try (FileInputStream file = new FileInputStream(new File(path));
			 final XSSFWorkbook excelFile = new XSSFWorkbook(file)) {
			//instancio una hoja en base al archivo excel y asignando el nombre de la hoja
			 final XSSFSheet sheet = excelFile.getSheet(sheetName);
			
			//tomamos el numero total de filas
			 final int rows = sheet.getPhysicalNumberOfRows();
			//instanciamos las columnas
			 final int column = sheet.getRow(0).getPhysicalNumberOfCells();
			
			//instanciamos el objeto bidimensional que nos va a devolver esta funcion
			Object cellValue[][]=new Object[rows][column];
			//instanciamos una fila
			XSSFRow row;
			for (int r = 0; r < rows; r++) {
				row = sheet.getRow(r);

				if (row == null){ 
					break; 
				}else{ 
					for (int c = 0; c < column; c++) {
						final DataFormatter dataFormatter = new DataFormatter();
						cellValue[r][c] = dataFormatter.formatCellValue(sheet.getRow(r).getCell(c));
						
					} 
					
				}
				
		   }
		   return cellValue;
		}
	
    }
}
