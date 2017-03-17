/**
 * koeyyeung
 * Aug 2, 2013 3:28:18 PM
 */
package com.gammon.qs.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

//read both .xsl and .xlsx files
@Component
@Scope(value = "request")
public class ExcelWorkbookProcessor {
private Logger logger = Logger.getLogger(ExcelWorkbookProcessor.class.getName());
	
	private Sheet sheet;
	private int linePointer;
	
	public void openFile(byte[] file) {
		try {
			Workbook wb = WorkbookFactory.create(new ByteArrayInputStream(file));
			int i = 0;
			while(wb.isSheetHidden(i))
				i++;
			this.sheet = wb.getSheetAt(i);
			linePointer = 0;
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}catch (IOException ex) {
			logger.log(Level.SEVERE, "ExcelWorkbookProcessor Exception",ex);
		}
	}
	
	public String[] readLine(int numCol) {
		Row row = sheet.getRow(linePointer);
		String[] line = new String[numCol];
		StringBuffer buffer = null;
		double value = 0.0;
		java.text.DecimalFormat formatter = null;
		java.text.FieldPosition fPosition = null;
		String formattingString = null;
		String resultString = null;
		formattingString = "###0.#####";
	      formatter = new java.text.DecimalFormat(formattingString);
	      fPosition = new java.text.FieldPosition(0);
	      buffer = new StringBuffer();

	      if(row != null){
	    	  for (int i=0; i<line.length;i++) {
	    		  if (row.getCell(i) != null) {
	    			  if (row.getCell(i).getCellType() == Cell.CELL_TYPE_STRING) {
	    				  line[i] = row.getCell(i).getRichStringCellValue().getString();
	    			  } 
	    			  else if (row.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC ) {
	    				  //line[i] = (new Double(row.getCell(i).getNumericCellValue())).toString();
	    				  buffer = new StringBuffer();

	    				  // Recover the numeric value from the cell
	    				  value  = row.getCell(i).getNumericCellValue();

	    				  // Format that number for display
	    				  formatter.format(value, buffer, fPosition);

	    				  resultString = buffer.toString();

	    				  line[i]=resultString;
	    				  // Simply display the result to screen
	    				  //logger.info("STRING row "+ i +" "+ resultString);

	    			  }
	    			  else if(row.getCell(i).getCellType() == Cell.CELL_TYPE_FORMULA){
	    				  if(row.getCell(i).getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC)
	    					  line[i] = Double.toString(row.getCell(i).getNumericCellValue());
	    				  else if(row.getCell(i).getCachedFormulaResultType() == Cell.CELL_TYPE_STRING)
	    					  line[i] = row.getCell(i).getRichStringCellValue().getString();
	    			  }
	    			  else if (row.getCell(i).getCellType() == Cell.CELL_TYPE_BLANK) {
	    			  } else {
	    				  logger.info("Unexpected cell type!!! " + row.getCell(i).getCellType());
	    			  }
	    		  }
	    	  }
	      }else
	    	  line = null;
		linePointer++;
		return line;
	}
	
	public int getNumRow() {
		return sheet.getLastRowNum() + 1;
	}
	
	public int getNumOfCol(){
		int numOfCol = 0;
		boolean loop=true;
		Row row = sheet.getRow(linePointer);
		while(loop){
			if(row.getCell(numOfCol)!=null && row.getCell(numOfCol).getCellType() != Cell.CELL_TYPE_BLANK){
				numOfCol++;
			}
			else{
				loop = false;
			}
				
		}
		return numOfCol;
	}
	public String[] readLineDirectly(int numCol) {
		Row row = sheet.getRow(linePointer);
		String[] line = new String[numCol];
		
		for (int i=0; i<line.length;i++) {
			if (row.getCell(i) != null) {
				if (row.getCell(i).getCellType() == Cell.CELL_TYPE_STRING) {
					line[i] = row.getCell(i).getRichStringCellValue().getString();
				} 
				else if (row.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC ) {
					if (row.getCell(i).getNumericCellValue() % 1 != 0)
						line[i] = Double.toString(row.getCell(i).getNumericCellValue());						
					else line[i] = Integer.toString((int)row.getCell(i).getNumericCellValue());
				} 
				else if (row.getCell(i).getCellType() == Cell.CELL_TYPE_BLANK) {
				} 
				else {
					logger.info("Unexpected cell type!!! " + row.getCell(i).getCellType());
				}
			}
		}
		
		linePointer++;
		return line;
	}
	
	
}

