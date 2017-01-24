package com.gammon.qs.service.jobCost;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gammon.qs.domain.ProvisionPostingHist;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.io.ExcelWorkbook;

/**
 * modified by matthewlam, 2015-01-19
 * Bug Fix #92: rearrange column names and order for SC Provision History Panel 
 */
public class ScProvisionHistoryExcelGenerator {
	private static SimpleDateFormat dateFormatter;

	// setup a generic class to hold all generic formats(and change to enum if possible)
	private static final String FORMAT_AMOUNT = "#,##0.00";
	private static final String FORMAT_QUANTITY = "#,##0.0000";
	private static final String FORMAT_RATE = "#,##0.0000";

	/**
	 * created by matthewlam, 2015-01-19
	 * Please note that the enum must be setup in order since
	 * the column number is handled by ordinal of the enum
	 */
	private static enum Column {
		JOB_NO("Job No.", 10, null),
		PACKAGE_NO("Package No.", 12, null),
		POSTED_MONTH("Posted Month", 14, null),
		POSTED_YEAR("Posted Year", 14, null),
		OBJECT_CODE("Object Code", 12, null),
		SUBSIDIARY_CODE("Subsidiary Code", 15, null),
		SC_RATE("SC Rate", 15, FORMAT_RATE),
		CUMULATIVE_WORKDONE_QTY("Cumulative Workdone Qty", 25, FORMAT_QUANTITY),
		CUMULATIVE_CERTIFIED_QTY("Cumulative Certified Qty", 24, FORMAT_QUANTITY),
		CUMULATIVE_WORKDONE_AMOUNT("Cumulative Workdone Amount", 29, FORMAT_AMOUNT),
		POSTED_CERTIFIED_AMOUNT("Posted Certified Amount", 23, FORMAT_AMOUNT),
		PROVISION("Provision", 12, FORMAT_AMOUNT),
		USERNAME("Username", 10, null);

		private final String title;
		private final int width;
		private final String format;

		Column(String title, int width, String format) {
			this.title = title; 
			this.width = width;
			this.format = format;
		}

		private String title() {
			return title;
		}

		private int width() {
			return width;
		}

		private String format() {
			return format;
		}

	}

	private ExcelFile excelFile;
	private ExcelWorkbook excelDoc;
	private List<ProvisionPostingHist> provisionHistoryList;
	private String jobNumber;

	public ScProvisionHistoryExcelGenerator(List<ProvisionPostingHist> provisionHistoriesList,
			String jobNumber) {
		this.provisionHistoryList	= provisionHistoriesList;
		this.jobNumber				= jobNumber;
		dateFormatter				= new SimpleDateFormat("dd/MM/yyyy");
	}

	public ExcelFile generate() {
		excelFile	= new ExcelFile();
		excelDoc	= excelFile.getDocument();

		excelFile.setFileName("J" + jobNumber + " SC Provision History " + dateFormatter.format(new Date()) + ExcelFile.EXTENSION);

		if (!provisionHistoryList.isEmpty()) {
			excelFile.setEmpty(false);
			excelDoc.setCurrentSheetName("job(" + this.jobNumber + ")");

			generateSheet();
		} else {
			excelFile.setEmpty(true);
		}

		return excelFile;
	}

	private void generateSheet() {
		//Set Title Row
		excelDoc.insertRow(createTitleRow());
		excelDoc.setCellFontBold(0, 0, 0, Column.values().length - 1);
		excelDoc.setCellAlignment(ExcelWorkbook.ALIGN_H_CENTER, 0, 0, 0, Column.values().length - 1);

		int lastRow = provisionHistoryList.size(); // title row engages one row

		//insert content rows
		for (int i = 0; i < lastRow; i++) {
			excelDoc.insertRow(createContentRow(provisionHistoryList.get(i)));
		}

		for (Column column : Column.values()) {
			excelDoc.setColumnWidth(column.ordinal(), column.width());
			if (column.format != null)
				excelDoc.setCellDataFormat(column.format(), 1, column.ordinal(), lastRow, column.ordinal());
		}

		excelDoc.setCellAlignment(ExcelWorkbook.ALIGN_H_RIGHT, 1, Column.SC_RATE.ordinal(), lastRow, Column.PROVISION.ordinal());
		excelDoc.setCellAlignment(ExcelWorkbook.ALIGN_H_CENTER, 1, Column.USERNAME.ordinal(), lastRow, Column.USERNAME.ordinal());

	}

	private String[] createContentRow(ProvisionPostingHist hist) {
		List<String> contents = new ArrayList<String>();

		contents.add(jobNumber != null ? jobNumber : "");
		contents.add(hist.getPackageNo() != null ? hist.getPackageNo() : "");
		contents.add(hist.getPostedMonth() != null ? hist.getPostedMonth().toString() : "");
		contents.add(hist.getPostedYr() != null ? hist.getPostedYr().toString() : "");
		contents.add(hist.getObjectCode() != null ? hist.getObjectCode() : "");
		contents.add(hist.getSubsidiaryCode() != null ? hist.getSubsidiaryCode() : "");
		contents.add(hist.getScRate() != null ? hist.getScRate().toString() : "");
		contents.add(hist.getCumLiabilitiesAmount() != null ? hist.getCumLiabilitiesAmount().toString() : "");
		contents.add(hist.getCumulativeCertifiedQuantity() != null ? hist.getCumulativeCertifiedQuantity().toString() : "");
		contents.add(hist.getCumLiabilitiesAmount() != null ? hist.getCumLiabilitiesAmount().toString() : "");
		contents.add(hist.getPostedCertAmount() != null ? hist.getPostedCertAmount().toString() : "");
		contents.add(hist.getProvision() != null ? hist.getProvision().toString() : "");
		contents.add(hist.getCreatedUser() != null ? hist.getCreatedUser().toString() : "");

		return contents.toArray(new String[contents.size()]);
	}

	private String[] createTitleRow() {
		String[] titles = new String[Column.values().length];

		for (Column column : Column.values()) {
			titles[column.ordinal()] = column.title();
		}

		return titles;
	}
}
