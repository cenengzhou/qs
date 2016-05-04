package com.gammon.qs.io;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.validator.GenericValidator;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gammon.qs.util.DateUtil;

/**
 * modified by koeyyeung
 * Aug 2, 2013 2:28:18 PM
 * migrate from poi 3.2 to poi 3.9 
 * support both .xls and .xlsx files 
 */
public class ExcelWorkbook { 
    public static final short ALIGN_H_CENTER = XSSFCellStyle.ALIGN_CENTER;
    public static final short ALIGN_H_JUSTIFY = XSSFCellStyle.ALIGN_JUSTIFY;
    public static final short ALIGN_H_LEFT = XSSFCellStyle.ALIGN_LEFT;
    public static final short ALIGN_H_RIGHT = XSSFCellStyle.ALIGN_RIGHT;
    
    public static final short ALIGN_V_TOP = XSSFCellStyle.VERTICAL_TOP;
    public static final short ALIGN_V_BOTTOM = XSSFCellStyle.VERTICAL_BOTTOM;
    public static final short ALIGN_V_CENTER = XSSFCellStyle.VERTICAL_CENTER;
    public static final short ALIGN_V_JUSTIFY = XSSFCellStyle.VERTICAL_JUSTIFY;
    
    public static final short BORDER_THIN = XSSFCellStyle.BORDER_THIN;
    public static final short BORDER_THICK = XSSFCellStyle.BORDER_THICK;
    public static final short BORDER_DOUBLE = XSSFCellStyle.BORDER_DOUBLE;
    
    public static final short PAGE_MARGIN_LEFT = XSSFSheet.LeftMargin;
    public static final short PAGE_MARGIN_RIGHT = XSSFSheet.RightMargin;
    public static final short PAGE_MARGIN_TOP = XSSFSheet.TopMargin;
    public static final short PAGE_MARGIN_BOTTOM = XSSFSheet.BottomMargin;
    
    public static final String SUMMARY_SHEET_CONTENT_FONT = "summarySheet_contentFont";
    public static final String SUMMARY_SHEET_HEADER_FONT = "summarySheet_headerFont";
    
    private XSSFWorkbook wb;
    private XSSFCellStyle boldStyle;
    private XSSFCellStyle editableStyle;
    private XSSFCellStyle negativeRedStyle; /**added by heisonwong, the style of negative integer**/
    private XSSFCellStyle dateStyle;
    private XSSFFont boldAndUnderlinedFont;
    private int workingSheet = 0;
    private XSSFFont summarySheet_fontSize_content;
    private XSSFFont summarySheet_fontSize_header;
        
    public ExcelWorkbook() {
        this.wb = new XSSFWorkbook();
        wb.createSheet();

        XSSFFont boldFont = this.wb.createFont();
        boldFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        boldStyle = wb.createCellStyle();
        boldStyle.setFont(boldFont);
        
        XSSFFont editableFont = this.wb.createFont();
        editableFont.setColor(new XSSFColor(Color.BLUE));
        editableStyle = wb.createCellStyle();
        editableStyle.setFont(editableFont);
        
        XSSFFont negativeRedFont = this.wb.createFont(); /**added by heisonwong**/
        negativeRedStyle = wb.createCellStyle();
        negativeRedStyle.setFont(negativeRedFont);
        negativeRedStyle.setAlignment(ALIGN_H_RIGHT);
        negativeRedStyle.setDataFormat (wb.createDataFormat().getFormat("#,##0.00;[Red]-#,##0.00"));
        
        dateStyle = wb.createCellStyle();
        dateStyle.setAlignment(ALIGN_H_RIGHT);
        dateStyle.setDataFormat(wb.createDataFormat().getFormat("dd/MM/yyyy"));
        
        this.boldAndUnderlinedFont = this.wb.createFont();
        this.boldAndUnderlinedFont.setUnderline(XSSFFont.U_SINGLE);
        this.boldAndUnderlinedFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        
        this.summarySheet_fontSize_content = this.wb.createFont();
        this.summarySheet_fontSize_content.setFontHeightInPoints((short)10);

        this.summarySheet_fontSize_header = this.wb.createFont();
        this.summarySheet_fontSize_header.setFontHeightInPoints((short)12);
        this.summarySheet_fontSize_header.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
    }
    
    public ExcelWorkbook(XSSFWorkbook wb) {
        this.wb = wb;
    }

    // START: Setting Cell Type
    public void setCellTypeToFormula(int startRow, int startCol, int endRow, int endCol) {
        for (int i=startRow; i<=endRow; i++ ) {
            for (int j=startCol; j<=endRow; j++) {
                this.setCellTypeToFormula(i, j);
            }
        }
    }
    
    /**
     * @author xethhung
     * Return XSSFFont
     */
    public XSSFFont getCellFont(){
    	return this.wb.createFont();
    }
    
    /**
     * @author xethhung 
     */
    public XSSFCellStyle getCellStyle(){
        return wb.createCellStyle();
    }

    
    /**
     * @author xethhung
     * Set style for an area 
     */
   public void setCellStyle(int startRow, int startColumn, int endRow, int endColumn, XSSFCellStyle style){
    	for(int i = startRow ; i <= endRow; i++){
    		for(int j = startColumn ; j <= endColumn ; j++){
    			setCellStyle(i,j,style);
    		}
    	}
        
    }

   /**
    * @author xethhung 
    * Set style for single cell
    */
    public void setCellStyle(int row, int col, XSSFCellStyle style){
    	XSSFCell cell = locateCell(row, col);
    	if(cell != null)
    		cell.setCellStyle(style);
    }

    /**
     * @author xethhung 
     * Set style for specific cell in same column
     */
    public void setCellStyle(int row, int[] cols, XSSFCellStyle style){
    	XSSFCell cell = null;
    	for(int col : cols){
    		cell = locateCell(row, col);
    		if(cell != null)
    			cell.setCellStyle(style);
    	}
    	
    	
    		
    }

    /**
     * @author xeth hung 
     */
    public void removeSheet(String sheetName){
    	int index = this.wb.getSheetIndex(sheetName);
    	this.wb.removeSheetAt(index);
    }

    public void setCellTypeToFormula(int row, int col) {
        XSSFRichTextString formula = this.wb.getSheetAt(this.workingSheet).getRow(row).getCell(col).getRichStringCellValue();
        this.wb.getSheetAt(this.workingSheet).getRow(row).getCell(col).setCellFormula(formula.getString());
        this.wb.getSheetAt(this.workingSheet).getRow(row).getCell(col).setCellType(XSSFCell.CELL_TYPE_FORMULA);
    }
    
    public void setCellFormula(String formula, int row, int col) {
        XSSFCell cell = locateCell(row, col);
        if (cell == null) {
            cell = this.wb.getSheetAt(this.workingSheet).getRow(row).getCell(col);
        }
        cell.setCellFormula(formula);
    }
    
    public void setCellTypeToString(int row, int col) {
        this.wb.getSheetAt(this.workingSheet).getRow(row).getCell(col).setCellType(XSSFCell.CELL_TYPE_STRING);
    }
    
    // START: Sheet Level Styling
    public void setColumnWidth(int column, int width) {
        this.wb.getSheetAt(this.workingSheet).setColumnWidth(column, width*256);
    }
    
    public void setRowHeight(int row, int height) {
        XSSFRow aRow = this.wb.getSheetAt(this.workingSheet).getRow(row);
        if (aRow != null) {
            aRow.setHeightInPoints((float)height);
        }
    }
    
    public void addMergedRegion(int startRow, int startCol, int endRow, int endCol) {
        this.wb.getSheetAt(this.workingSheet).addMergedRegion(new CellRangeAddress(startRow, startCol, endRow, endCol));
    }

    public void addMergedRegion(int startRow, int startCol, int endRow, int endCol, int height, int width) {
        for (int row=startRow; row<=endRow; row+=height) {
            for (int col=startCol; col<=endCol; col+=width) {
                addMergedRegion(row,col,(row+height-1),(col+width-1));
            }
        }
    }
    
    public void setPageMargin(short side, double size) {
        this.wb.getSheetAt(this.workingSheet).setMargin(side,size);
    }
    public double getPageMargin(short side) {
        return this.wb.getSheetAt(this.workingSheet).getMargin(side);
    }
    
    // END: Sheet Level Styling
    
    // START: Cell Level Styling
    public void setCellLeftBorder(int row, int col, short borderType) {
        XSSFCell cell = locateCell(row, col);
        try {
            CellUtil.setCellStyleProperty(cell, this.wb, "borderLeft", new Short(borderType));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void setCellRightBorder(int row, int col, short borderType) {
        XSSFCell cell = locateCell(row, col);
        try {
            CellUtil.setCellStyleProperty(cell, this.wb, "borderRight", new Short(borderType));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void setCellTopBorder(int row, int col, short borderType) {
        XSSFCell cell = locateCell(row, col);
        try {
            CellUtil.setCellStyleProperty(cell, this.wb, "borderTop", new Short(borderType));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void setCellBottomBorder(int row, int col, short borderType) {
        XSSFCell cell = locateCell(row, col);
        try {
            CellUtil.setCellStyleProperty(cell, this.wb, "borderBottom", new Short(borderType));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void enableTextWrapOnCell(int row, int col) {
        XSSFCell cell = locateCell(row, col);
        try {
            CellUtil.setCellStyleProperty(cell, this.wb, "wrapText", new Boolean(true));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void enableTextWrapOnCell(int startRow, int startCol, int endRow, int endCol) {
    	XSSFCell cell;
    	 for (int row=startRow; row<=endRow; row++) {
             for (int col=startCol; col<=endCol; col++) {
            	 cell = locateCell(row, col);
                 try {
                     CellUtil.setCellStyleProperty(cell, this.wb, "wrapText", new Boolean(true));
                 } catch(Exception ex) {
                     ex.printStackTrace();
                 }
             }
         }
    	
       
    }
    
    public void setCellFontBold(int row, int col) {
        XSSFCell cell = locateCell(row, col);
        if(cell != null){
        	cell.setCellStyle(boldStyle);
        }
    }
    
    public void setCellFontBold(int startRow, int startCol, int endRow, int endCol) {
        for (int row=startRow; row<=endRow; row++) {
            for (int col=startCol; col<=endCol; col++) {
                setCellFontBold(row,col);
            }
        }
    }
    
    public void setCellFontEditable(int row, int col) {
        XSSFCell cell = locateCell(row, col);
        if(cell != null){
        	cell.setCellStyle(editableStyle);
        }
    }
    
    /**added by heisonwong**/
    public void setCellNegativeRed(int row, int col) {
        XSSFCell cell = locateCell(row, col);
        if(cell != null){
        	cell.setCellStyle(negativeRedStyle);
        }
    }

    public void setDateStyle(int row, int col) {
        XSSFCell cell = locateCell(row, col);
        if(cell != null){
        	cell.setCellStyle(dateStyle);
        }
    }

    public void setCellFontColour(int row, int col, XSSFColor xssfColor) {
        XSSFCell cell = locateCell(row, col);
        if(cell != null){
        	 XSSFFont colorFont = this.wb.createFont();
             colorFont.setColor(xssfColor);
             XSSFCellStyle colorStyle = wb.createCellStyle();
             colorStyle.setFont(colorFont);
             cell.setCellStyle(colorStyle);
        }
    }
    
    public void setCellFontEditable(int startRow, int startCol, int endRow, int endCol) {
        for (int row=startRow; row<=endRow; row++) {
            for (int col=startCol; col<=endCol; col++) {
                setCellFontEditable(row,col);
            }
        }
    }
    
    public void setCellFontBoldAndUnderlined(int row, int col) {
        XSSFCell cell = locateCell(row, col);
        try {
            CellUtil.setFont(cell, this.wb, this.boldAndUnderlinedFont);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void setCellFontByType(int row, int col, String type) {
        XSSFCell cell = locateCell(row, col);
        try {
            if (type.equals(SUMMARY_SHEET_CONTENT_FONT))
                CellUtil.setFont(cell, this.wb, this.summarySheet_fontSize_content);
            else if (type.equals(SUMMARY_SHEET_HEADER_FONT)) 
                CellUtil.setFont(cell, this.wb, this.summarySheet_fontSize_header);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void setCellAlignment(short alignment, int row, int col) {
        XSSFCell cell = locateCell(row, col);
        try {
            CellUtil.setAlignment(cell, this.wb, alignment);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setCellAlignment(short alignment, int startRow, int startCol, int endRow, int endCol) {
        for (int row=startRow; row<=endRow; row++) {
            for (int col=startCol; col<=endCol; col++) {
                setCellAlignment(alignment,row,col);
            }
        }
    }
    
    public void setCellVerticalAlignmnet(short alignment, int row, int col) {
        XSSFCell cell = locateCell(row, col);
        XSSFCellStyle cs;
        if (cell.getCellStyle()==null)
            cs = this.wb.createCellStyle();
        else 
            cs = cell.getCellStyle();

        cs.setVerticalAlignment(alignment);
        cell.setCellStyle(cs);
    }
    // END: Cell Level Styling
    
    // START: Sheet Operation
    public int insertRow(String[] content) {
        int rowToInsert = this.wb.getSheetAt(this.workingSheet).getPhysicalNumberOfRows();
        XSSFRow row = this.wb.getSheetAt(this.workingSheet).getRow(rowToInsert);
        if (row == null) {
            row = this.wb.getSheetAt(this.workingSheet).createRow(rowToInsert);
        }
        for(int i=0; i<content.length;i++) {
            setCellValue(rowToInsert, i, content[i]);
        }
        return rowToInsert;
    }
    
    public void insertRow(int rowToInsert, String[] content){
    	XSSFRow row = this.wb.getSheetAt(this.workingSheet).getRow(rowToInsert);
    	if (row == null) {
            row = this.wb.getSheetAt(this.workingSheet).createRow(rowToInsert);
        }
        for(int i=0; i<content.length;i++) {
            setCellValue(rowToInsert, i, content[i]);
        }
    }
    
    public void setCellValue(int insertRow, int insertCol, String value) {
        XSSFRow row = this.wb.getSheetAt(this.workingSheet).getRow(insertRow);
        if (row == null) {
            row = this.wb.getSheetAt(this.workingSheet).createRow(insertRow);
        }
        
        XSSFCell cell = row.getCell(insertCol);
        if (cell == null) {
            cell = row.createCell(insertCol);
        }
        
        if(value == null)
        	return;
        //bpi was being parsed as a date - split the string with '/' and check the length is 3 before doing parseDate 
//        String[] splitStr = value.split("/");
//        Date tmpDate = splitStr.length == 3 ? DateUtil.parseDate(value) : null;
        Date tmpDate = isValidDate(value) ? DateUtil.parseDate(value) : null;
        if (tmpDate != null) {
            try {
                CellUtil.setCellStyleProperty(cell, this.wb, "dataFormat", new Short(HSSFDataFormat.getBuiltinFormat("m/d/yy")));
            } catch(Exception ex) {
                ex.printStackTrace();
            }            
            cell.setCellValue(tmpDate);
        } 
        else if(GenericValidator.isDouble(value)){  
            cell.setCellValue(Double.parseDouble(value)); 
            cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
        }
        else{
            cell.setCellValue(new XSSFRichTextString(value));
        }
    }
    
    /**
     * Set the cell value, the cell type will depend on the type of the <code>value</code> except <code>forceToString</code> is true
     * @author peterchan 
     * @date Jun 19, 2012
     * @param insertRow row number 
     * @param insertCol column number
     * @param value value that assign to the cell in <code>String</code> 
     * @param forceToString force the cell type to String
     */
    public void setCellValue(int insertRow, int insertCol, String value, boolean forceToString) {
        XSSFRow row = this.wb.getSheetAt(this.workingSheet).getRow(insertRow);
        if (row == null) {
            row = this.wb.getSheetAt(this.workingSheet).createRow(insertRow);
        }
        
        XSSFCell cell = row.getCell(insertCol);
        if (cell == null) {
            cell = row.createCell(insertCol);
        }
        
        if(value == null)
        	return;
        if (forceToString){
        	cell.setCellValue(new XSSFRichTextString(value));
        }else {
	        Date tmpDate = isValidDate(value) ? DateUtil.parseDate(value) : null;
	        if (tmpDate != null) {
	            try {
	                CellUtil.setCellStyleProperty(cell, this.wb, "dataFormat", new Short(HSSFDataFormat.getBuiltinFormat("m/d/yy")));
	            } catch(Exception ex) {
	                ex.printStackTrace();
	            }            
	            cell.setCellValue(tmpDate);
	        } 
	        else if(GenericValidator.isDouble(value)){  
	            cell.setCellValue(Double.parseDouble(value)); 
	            cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
	        }
	        else{
	            cell.setCellValue(new XSSFRichTextString(value));
	        }
        }
    }

    
    public byte[] toBytes() {
        byte[] returnBytes = new byte[0];
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            this.wb.write(outputStream);
            returnBytes = outputStream.toByteArray();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return returnBytes;
    }
    
    public int getWorkingSheet() {
        return this.workingSheet;
    }
    
    public void setWorkingSheet(int workingSheet) {
        this.workingSheet = workingSheet;
    }
    
    public void setWorkingSheet(String sheetName) {
        setWorkingSheet(this.wb.getSheetIndex(sheetName));
    }
    
    public void insertSheet(String sheetName) {
        this.wb.createSheet(sheetName);
        setWorkingSheet(sheetName);
    }
    
    public void setCurrentSheetName(String sheetName) {
        this.wb.setSheetName(this.workingSheet, sheetName);
    }
    
    private XSSFCell locateCell(int rowNum, int colNum) {
        XSSFRow aRow = this.wb.getSheetAt(this.workingSheet).getRow(rowNum);
        XSSFCell aCell = null;
        if (aRow != null) {
             aCell = aRow.getCell(colNum);
        }
        return aCell;
    }
    // END: Sheet Operation
    
     public String getColLetter(int colIndex)
     {
        String ch = "";
            if (colIndex  < 26)
                ch = "" + (char)((colIndex) + 65);
            else
                ch = "" + (char)((colIndex) / 26 + 65 - 1) + (char)((colIndex) % 26 + 65);
             return ch;
     }    
    
    public static String convertRowColToGridId(int row, int col) {
        String id = "";
        if (col < 26) {
            id = "" + (char)(col+65);
        } else {
            id = "" + (char)((col/26)+65-1) + (char)((col%26) + 65);
        }
        return id+(row+1);
    }
    
    public boolean isValidDate(String inDate) {

	    if (inDate == null)
	      return false;

	    //set the format to use as a constructor argument
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    
	    if (inDate.trim().length() != dateFormat.toPattern().length())
	      return false;

	    dateFormat.setLenient(false);
	    
	    try {
	      //parse the inDate parameter
	      dateFormat.parse(inDate.trim());
	    }
	    catch (ParseException pe) {
	      return false;
	    }
	    return true;
	  }

	/**
	 * created by matthewlam, 2015-01-19
	 * Bug Fix #92: rearrange column names and order for SC Provision History Panel
	 */
	public void setCellDataFormat(String format, int startRow, int startColumn,
			int endRow, int endColumn) {
		short dataFormat = wb.createDataFormat().getFormat(format);
		Cell cell;

		for (int i = startRow; i <= endRow; i++) {
			for (int j = startColumn; j <= endColumn; j++) {
				cell = locateCell(i, j);

				if (cell != null)
					CellUtil.setCellStyleProperty(cell, wb, "dataFormat", dataFormat);
			}
		}
	}
} 
