
package com.gammon.qs.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 * koeyyeung
 * Nov 8, 2013 4:40:32 PM
 */
public class JasperReportHelper {
	public static final String OUTPUT_EXCEL = "xls";
	public static final String OUTPUT_PDF = "pdf";
	public static final String SHEET_NAMES="sheet_name";
	
	public static final String param_show_title = "param_show_title";
	public static final String param_show_page_header = "param_show_page_header";
	public static final String param_show_page_footer = "param_show_page_footer";
	public static final String param_show_lines = "param_show_lines";
	public static final String param_show_background = "param_show_background";
	
	private List<JasperPrint> reports ;
	private List<String> sheetNames = new ArrayList<String>();
	@SuppressWarnings("rawtypes")
	public List getCurrentWrapperList() {
		return currentWrapperList;
	}
	public void setCurrentWrapperList(@SuppressWarnings("rawtypes") List currentWrapperList) {
		this.currentWrapperList = currentWrapperList;
	}
	public String getCurrentTemplateFilenameWithFullPath() {
		return currentTemplateFilenameWithFullPath;
	}
	public void setCurrentTemplateFilenameWithFullPath(
			String currentTemplateFilenameWithFullPath) {
		this.currentTemplateFilenameWithFullPath = currentTemplateFilenameWithFullPath;
	}
	public Map<String, Object> getCurrentParameters() {
		return currentParameters;
	}
	public void setCurrentParameters(Map<String, Object> currentParameters) {
		this.currentParameters = currentParameters;
	}

	@SuppressWarnings("rawtypes")
	private List currentWrapperList;
	private String currentTemplateFilenameWithFullPath;
	private Map<String, Object> currentParameters; 
	
	private JasperReportHelper(){
		reports = new ArrayList<JasperPrint>();
	}
	
	/**
	 * @return
	 * Return an JasperReportHelper Object
	 */
	public static JasperReportHelper get(){return new JasperReportHelper();}

	/**
	 * 
	 * @param print
	 * @return
	 * Add a new jasper report to the helper, all jasper report will exported until export 
	 */
	public JasperReportHelper addReport(JasperPrint print){
		reports.add(print);
		return this;
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws JRException
	 * compile the current jasper report and add to the helper
	 */
	public JasperReportHelper compileAndAddReport() throws IOException, JRException{
		JasperPrint print = JasperReportHelper.callFillReport(this.getCurrentWrapperList(), this.getCurrentTemplateFilenameWithFullPath(), this.getCurrentParameters());
		addReport(print);
		return this;
	}
	
	/**
	 * 
	 * @param wrapperList
	 * @param templateFilenameWithFullPath
	 * @param parameters
	 * @return
	 * Add a report as current processing report. The report param, wrapperList template path can be modify through getter and setter.
	 * The report is not yet added to the report, until it is add to compiled and added to the report, it still in process status.
	 * Call compileAndAddToReport() to add it.
	 */
	public JasperReportHelper setCurrentReport(@SuppressWarnings("rawtypes") List wrapperList,
			String templateFilenameWithFullPath, Map<String, Object> parameters) {
		this.setCurrentWrapperList(wrapperList);
		this.setCurrentTemplateFilenameWithFullPath(templateFilenameWithFullPath);
		this.setCurrentParameters(parameters);
		return this;
	}
	
	public ByteArrayOutputStream exportAsExcel() throws JRException, IOException {
		return exportAsExcel(null);
	}

	/**
	 * 
	 * @return
	 * @throws JRException
	 * @throws IOException
	 * 
	 * Export the reports as Excel
	 */
	public ByteArrayOutputStream exportAsExcel(SimpleXlsxReportConfiguration xlsxReportConfig) throws JRException, IOException {
		if (sheetNames.size() < reports.size()) {
			int sizeDiff = reports.size() - sheetNames.size();
			int initSheet = sheetNames.size();
			for (int i = 0; i < sizeDiff; i++)
				this.sheetNames.add("Sheet " + (++initSheet));
		}

		return exportAsExcel(this.sheetNames.toArray(new String[] {}), xlsxReportConfig);
	}

	
	public ByteArrayOutputStream exportAsExcel(String[] sheetNames, SimpleXlsxReportConfiguration xlsxReportConfig) throws JRException, IOException {
		return callJasperReportsToExcel(this.reports, sheetNames, xlsxReportConfig);
	}
	
	/**
	 * Export the report as PDF
	 * @return
	 * @throws FileNotFoundException
	 * @throws JRException
	 */
	public ByteArrayOutputStream exportAsPDF() throws FileNotFoundException, JRException{
		JasperPrint tempPrint = null;
		for(JasperPrint report : this.reports){
			tempPrint = combineReport(tempPrint, report);
		}
		return callJasperReportToStream(tempPrint);
	}
	
	/**
	 * @author xethhung
	 * Jun 25, 2015
	 * Return JasperPrint in order to combine multiple report into single file
	 */
	public static JasperPrint callFillReport(@SuppressWarnings("rawtypes") List wrapperList, String templateFilenameWithFullPath, Map<String, Object> parameters ) throws IOException, JRException {
        JRBeanCollectionDataSource beanDataSource   = new JRBeanCollectionDataSource(wrapperList);
        FileInputStream jrInputStream = new FileInputStream(templateFilenameWithFullPath+".jasper");
        
        //filling data to Jasper Report
       JasperPrint jasperReport = JasperFillManager.fillReport(jrInputStream, parameters, beanDataSource);
       jrInputStream.close();
       return jasperReport;
	}
	
	/**
	 * @author xethhung
	 * Jun 25, 2015
	 * Combine multiple report into single file
	 */
	private static JasperPrint combineReport(JasperPrint printSource, JasperPrint printDest){
		if(printSource == null)
			return printDest;
		else{
			List<JRPrintPage> pages = printDest.getPages();
			for(JRPrintPage page : pages){
				printSource.addPage(page);
			}
		}
		return printSource;
	}

	/**
	 * @author xethhung
	 * Jun 25, 2015
	 * Convert jasperPrint into byteArrayOutputStream 
	 */
	public static ByteArrayOutputStream callJasperReportToStream(JasperPrint jasperReport) throws JRException, FileNotFoundException{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperReport, outputStream);
		return outputStream;
	}

	@SuppressWarnings("unchecked")
	private static ByteArrayOutputStream callJasperReportsToExcel(@SuppressWarnings("rawtypes") List jasperReports, String[] sheetNames, SimpleXlsxReportConfiguration xlsxReportConfig)throws JRException, IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	
	
		//Convert Jasper Report to Excel
		JRXlsxExporter excelFile = new JRXlsxExporter();

		excelFile.setExporterInput(SimpleExporterInput.getInstance(jasperReports));
		excelFile.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
		
		if (xlsxReportConfig == null) {
			xlsxReportConfig = new SimpleXlsxReportConfiguration();
			xlsxReportConfig.setOnePagePerSheet(false);
			xlsxReportConfig.setDetectCellType(true);
			xlsxReportConfig.setWhitePageBackground(false);
			xlsxReportConfig.setRemoveEmptySpaceBetweenRows(true);
			xlsxReportConfig.setFontSizeFixEnabled(true);
			xlsxReportConfig.setIgnorePageMargins(true);
			xlsxReportConfig.setCollapseRowSpan(true);
			xlsxReportConfig.setIgnoreGraphics(true);
			xlsxReportConfig.setRemoveEmptySpaceBetweenColumns(true);
		}
		
		if(sheetNames.length>0){
			xlsxReportConfig.setSheetNames(sheetNames);
		}
		excelFile.setConfiguration(xlsxReportConfig);
		excelFile.exportReport();
	
		return outputStream;
	}
}
