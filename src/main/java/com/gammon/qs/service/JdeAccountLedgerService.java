/**
 * GammonQS-PH3
 * AccountLedgerRepositoryHBImpl.java
 * @author koeyyeung
 * Created on May 9, 2013 11:32:09 AM
 */
package com.gammon.qs.service;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.JdeAccountLedgerHBDao;
import com.gammon.qs.domain.JdeAccountLedger;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.qs.io.ExcelWorkbookProcessor;
import com.gammon.qs.util.DateUtil;
import com.gammon.qs.wrapper.BudgetForecastExcelWrapper;
import com.gammon.qs.wrapper.BudgetForecastWrapper;
@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class)
public class JdeAccountLedgerService {
	private Logger						logger	= Logger.getLogger(this.getClass().getName());
	@Autowired
	private JdeAccountLedgerHBDao			accountLedgerDao;
	@Autowired
	private ExcelWorkbookProcessor		excelFileProcessor;

	public List<JdeAccountLedger> obtainAccountLedgersByLedgerType(String jobNo, String ledgerType) throws DatabaseOperationException {
		return accountLedgerDao.obtainAccountLedgersByLedgerType(jobNo, ledgerType);
	}

	public List<BudgetForecastWrapper> obtainAccountLedgersByJobNo(String jobNo, Integer month, Integer year) throws DatabaseOperationException {
		logger.info("obtainAccountLedgersByJobNo-STARTED");
		List<BudgetForecastWrapper> budgetAndForecastWrapperList = new ArrayList<BudgetForecastWrapper>();

		/*int day = Integer.valueOf(DateUtil.formatDate(new Date(), "dd"));
		int month = Integer.valueOf(DateUtil.formatDate(new Date(), "MM"));

		if (!"12".equals(month)) {
			if (day > 25) {
				month = month + 1;
			}
		}*/

		List<BudgetForecastWrapper> wrapperList = accountLedgerDao.obtainAccountLedgersByJobNo(jobNo, year);

		for (BudgetForecastWrapper wrapper : wrapperList) {

			List<JdeAccountLedger> accountLedgers = accountLedgerDao.obtainAccountLedgersByBudgetForecastWrapper(wrapper);
			if (accountLedgers.size() > 0) {
				if ("1".equals(accountLedgers.get(0).getObjectCode().substring(0, 1))) {
					BudgetForecastWrapper budgetAndForecastWrapper = new BudgetForecastWrapper();
					budgetAndForecastWrapper.setJobNo(jobNo);
					budgetAndForecastWrapper.setObjectCode(accountLedgers.get(0).getObjectCode());
					budgetAndForecastWrapper.setSubledger(accountLedgers.get(0).getSubledger());
					budgetAndForecastWrapper.setFiscalYear(accountLedgers.get(0).getFiscalYear());

					// obtain description from FC--> OB--> AA
					budgetAndForecastWrapper.setDescription(accountLedgers.get(0).getDescription());

					for (JdeAccountLedger accountLedgerToCheck : accountLedgers) {
						if ("AA".equals(accountLedgerToCheck.getLedgerType())) {
							switch (month) {
							case 1:
								budgetAndForecastWrapper.setActualCost(accountLedgerToCheck.getPeriod01());
								break;
							case 2:
								budgetAndForecastWrapper.setActualCost(accountLedgerToCheck.getPeriod02());
								break;
							case 3:
								budgetAndForecastWrapper.setActualCost(accountLedgerToCheck.getPeriod03());
								break;
							case 4:
								budgetAndForecastWrapper.setActualCost(accountLedgerToCheck.getPeriod04());
								break;
							case 5:
								budgetAndForecastWrapper.setActualCost(accountLedgerToCheck.getPeriod05());
								break;
							case 6:
								budgetAndForecastWrapper.setActualCost(accountLedgerToCheck.getPeriod06());
								break;
							case 7:
								budgetAndForecastWrapper.setActualCost(accountLedgerToCheck.getPeriod07());
								break;
							case 8:
								budgetAndForecastWrapper.setActualCost(accountLedgerToCheck.getPeriod08());
								break;
							case 9:
								budgetAndForecastWrapper.setActualCost(accountLedgerToCheck.getPeriod09());
								break;
							case 10:
								budgetAndForecastWrapper.setActualCost(accountLedgerToCheck.getPeriod10());
								break;
							case 11:
								budgetAndForecastWrapper.setActualCost(accountLedgerToCheck.getPeriod11());
								break;
							case 12:
								budgetAndForecastWrapper.setActualCost(accountLedgerToCheck.getPeriod12());
								break;
							}

						} else if ("FC".equals(accountLedgerToCheck.getLedgerType())) {

							switch (month) {
							case 1:
								budgetAndForecastWrapper.setLatestForecast(accountLedgerToCheck.getPeriod01());
								break;
							case 2:
								budgetAndForecastWrapper.setLatestForecast(accountLedgerToCheck.getPeriod02());
								break;
							case 3:
								budgetAndForecastWrapper.setLatestForecast(accountLedgerToCheck.getPeriod03());
								break;
							case 4:
								budgetAndForecastWrapper.setLatestForecast(accountLedgerToCheck.getPeriod04());
								break;
							case 5:
								budgetAndForecastWrapper.setLatestForecast(accountLedgerToCheck.getPeriod05());
								break;
							case 6:
								budgetAndForecastWrapper.setLatestForecast(accountLedgerToCheck.getPeriod06());
								break;
							case 7:
								budgetAndForecastWrapper.setLatestForecast(accountLedgerToCheck.getPeriod07());
								break;
							case 8:
								budgetAndForecastWrapper.setLatestForecast(accountLedgerToCheck.getPeriod08());
								break;
							case 9:
								budgetAndForecastWrapper.setLatestForecast(accountLedgerToCheck.getPeriod09());
								break;
							case 10:
								budgetAndForecastWrapper.setLatestForecast(accountLedgerToCheck.getPeriod10());
								break;
							case 11:
								budgetAndForecastWrapper.setLatestForecast(accountLedgerToCheck.getPeriod11());
								break;
							case 12:
								budgetAndForecastWrapper.setLatestForecast(accountLedgerToCheck.getPeriod12());
								break;
							}

						} else if ("OB".equals(accountLedgerToCheck.getLedgerType())) {
							budgetAndForecastWrapper.setOriginalBudget(accountLedgerToCheck.getBeginBalance());
						}
					}

					budgetAndForecastWrapperList.add(budgetAndForecastWrapper);
				}
			}
		}

		logger.info("budgetAndForecastWrapperList: " + budgetAndForecastWrapperList.size());

		return budgetAndForecastWrapperList;
	}

	public String uploadBudgetForecastExcel(String jobNo, String ledgerType, byte[] file) throws DatabaseOperationException {
		logger.info("STARTED -> uploadBudgetForecastExcel()");

		String SUBCON_NO_STRING_FORMAT = "0000";
		String readingRow = "";

		List<JdeAccountLedger> accountLedgerToUpdateList;
		List<JdeAccountLedger> accountLedgerToInsertList;
		try {
			Set<String> budgetForecastHashMap = new HashSet<String>();
			HashMap<Long, JdeAccountLedger> accountLedgerHashMap = new HashMap<Long, JdeAccountLedger>();

			accountLedgerToUpdateList = new ArrayList<JdeAccountLedger>();
			accountLedgerToInsertList = new ArrayList<JdeAccountLedger>();
			// Open Excel File
			excelFileProcessor.openFile(file);

			// Read Headers of Row 0
			excelFileProcessor.readLine(0);
			logger.info("Total row: " + excelFileProcessor.getNumRow());

			int day = Integer.valueOf(DateUtil.formatDate(new Date(), "dd"));
			int month = Integer.valueOf(DateUtil.formatDate(new Date(), "MM"));

			if (!"12".equals(month)) {
				if (day > 25) {
					++month;
				}
			}

			List<BudgetForecastExcelWrapper> budgetForecastExcelWrapperList = new ArrayList<BudgetForecastExcelWrapper>();

			for (int i = 1; i < excelFileProcessor.getNumRow(); i++) {
				String[] line = excelFileProcessor.readLine(6);

				String objectCodeTemp = GenericValidator.isBlankOrNull((String) line[0]) ? "" : ((String) line[0]).trim();
				String descriptionTemp = GenericValidator.isBlankOrNull((String) line[1]) ? "" : ((String) line[1]).trim();
				String subcontractNoTemp = GenericValidator.isBlankOrNull((String) line[2]) ? null : ((String) line[2]).trim();// in database, the data is set to be null instead of "" for "AA"
				double contractTotalTemp = objectToDouble(line[3]);

				// this message will be shown when there is error occured
				int actualRowNo = i + 1;
				if (subcontractNoTemp == null)
					readingRow = "Row: " + actualRowNo + " - " + "ObjectCode: " + objectCodeTemp + " - " + "SubcontractNo.: ";
				else
					readingRow = "Row: " + actualRowNo + " - " + "ObjectCode: " + objectCodeTemp + " - " + "SubcontractNo.: " + subcontractNoTemp;

				if ((line[0] == null || "".equals(line[0])) && (line[1] == null || "".equals(line[1])) && (line[2] == null || "".equals(line[2])) && (line[3] == null || "".equals(line[3]))) {// check when to end the file
					break;
				}
				if ((line[0] == null || "".equals(line[0])) && (line[1] == null || "".equals(line[1])) && ("TOTAL:".equals(line[2]))) {// check when to end the file
					break;
				}

				// check whether the object code is valid or not
				if (!"".equals(objectCodeTemp)) {
					if (objectCodeTemp.length() <= 6) {
						if (objectCodeTemp.startsWith("1")) {
							if (!objectCodeTemp.startsWith("14") && subcontractNoTemp != null) {
								return "Only Object Code starts with '14' can have subcontract number <br> " + readingRow;
							}
							BudgetForecastExcelWrapper budgetForecastExcelWrapperTemp = new BudgetForecastExcelWrapper();
							budgetForecastExcelWrapperTemp.setObjectCode(objectCodeTemp);
							budgetForecastExcelWrapperTemp.setDescription(descriptionTemp);
							budgetForecastExcelWrapperTemp.setSubledger(subcontractNoTemp);
							budgetForecastExcelWrapperTemp.setContractTotal(contractTotalTemp);

							budgetForecastExcelWrapperList.add(budgetForecastExcelWrapperTemp);

						} else
							return "Object Code should start with 1 <br> " + readingRow;
					} else
						return "The length of Object Code should be less than or equal to 6 digits <br> " + readingRow;
				} else
					return "Object Code cannot be blank <br> " + readingRow;

				if (descriptionTemp.length() > 60) {
					return "Description should be less than 60 characters. <br> " + readingRow;
				}

				// check whether the subcontract no. is valid or not
				if (subcontractNoTemp != null) {
					try {
						Integer.valueOf(subcontractNoTemp);// check to make sure subcon no is an integer
					} catch (Exception e) {
						return "Please input integer only for subcontract number.<br>" + readingRow;
					}

					if (subcontractNoTemp.length() > 4)
						return "Subcontract number cannot be longer than 4 digits.<br>" + readingRow;
					else if (subcontractNoTemp.length() > 0 && subcontractNoTemp.length() < 4) {
						subcontractNoTemp = numericToString(Integer.valueOf(subcontractNoTemp), SUBCON_NO_STRING_FORMAT);
					}
				} else
					subcontractNoTemp = "";

				String recordId = objectCodeTemp.concat(subcontractNoTemp);
				if (budgetForecastHashMap.contains(recordId)) {
					return "Duplicated records found.<br>Please make sure that the object code and subcontract number are unique.<br>" + readingRow;
				}

				budgetForecastHashMap.add(recordId);
			}

			logger.info("budgetForecastExcelWrapperList: " + budgetForecastExcelWrapperList.size());

			List<JdeAccountLedger> existingRecords = accountLedgerDao.obtainAccountLedgersByJobNo(jobNo, ledgerType);
			for (JdeAccountLedger existingRecord : existingRecords) {
				accountLedgerHashMap.put(existingRecord.getId(), existingRecord);
			}

			for (BudgetForecastExcelWrapper budgetForecastExcelWrapper : budgetForecastExcelWrapperList) {
				String objectCode = budgetForecastExcelWrapper.getObjectCode();
				if (objectCode.length() < 6) {
					for (int j = objectCode.length(); j <= 6; j++) {
						objectCode = objectCode.concat("0");
						j++;
					}
				}
				String description = budgetForecastExcelWrapper.getDescription();
				String subcontractNo = budgetForecastExcelWrapper.getSubledger();
				if (subcontractNo != null) {
					int subconNo = Integer.valueOf(subcontractNo);
					if (subconNo == 0)
						subcontractNo = null;
				}
				Double contractTotal = budgetForecastExcelWrapper.getContractTotal();

				JdeAccountLedger accountLedger = new JdeAccountLedger();
				accountLedger.setJobNo(jobNo);
				accountLedger.setLedgerType(ledgerType);
				accountLedger.setObjectCode(objectCode);
				accountLedger.setSubledger(subcontractNo);

				String year = DateUtil.formatDate(new Date(), "yyyy");
				accountLedger.setFiscalYear(Integer.parseInt(year));

				if (subcontractNo != null && !"".equals(subcontractNo)) {
					accountLedger.setSubledgerType("X");
				}

				JdeAccountLedger existedAccountLedger = accountLedgerDao.obtainAccountLedgerByAccountLedger(accountLedger);
				if (existedAccountLedger != null) {
					existedAccountLedger.setDescription(description);

					if ("FC".equals(ledgerType)) {
						existedAccountLedger = setContractTotal(existedAccountLedger, month, contractTotal);
					} else if ("OB".equals(ledgerType)) {
						existedAccountLedger.setBeginBalance(contractTotal);
					}

					accountLedgerToUpdateList.add(existedAccountLedger);

				} else if (existedAccountLedger == null) {
					accountLedger.setDescription(description);

					if ("FC".equals(ledgerType)) {
						accountLedger = setContractTotal(accountLedger, month, contractTotal);

					} else if ("OB".equals(ledgerType)) {
						accountLedger.setBeginBalance(contractTotal);
					}

					accountLedgerToInsertList.add(accountLedger);
				}
			}

			if (accountLedgerToUpdateList.size() == 0 && accountLedgerToInsertList.size() == 0) {
				return "No record was imported.";
			}

			// Update records and remove it from hash map
			for (JdeAccountLedger accountLedgerToUpdateTemp : accountLedgerToUpdateList) {
				accountLedgerHashMap.remove(accountLedgerToUpdateTemp.getId());
				accountLedgerDao.update(accountLedgerToUpdateTemp);
			}

			ArrayList<JdeAccountLedger> accountLedgerToUpdateArrayList = new ArrayList<JdeAccountLedger>(accountLedgerHashMap.values());

			// Update records: reset the rest to zero
			for (JdeAccountLedger accountLedgerToUpdate : accountLedgerToUpdateArrayList) {
				if ("FC".equals(ledgerType)) {
					accountLedgerToUpdate = setContractTotal(accountLedgerToUpdate, month, 0.0);
				} else if ("OB".equals(ledgerType)) {
					accountLedgerToUpdate.setBeginBalance(0.00);
				}
				accountLedgerDao.update(accountLedgerToUpdate);
			}

			for (JdeAccountLedger accountLedgerToInsert : accountLedgerToInsertList) {
				accountLedgerDao.insert(accountLedgerToInsert);
			}

			logger.info("numOfupdated: " + accountLedgerToUpdateList.size());
			logger.info("numOfInserted: " + accountLedgerToInsertList.size());
			logger.info("END -> uploadBudgetForecastExcel");
		} catch (Exception e) {
			logger.info("Error reading excel: " + e.getMessage());
			e.printStackTrace();
			return "Error found. Please contact helpdesk.";
		}

		return null;
	}

	private JdeAccountLedger setContractTotal(JdeAccountLedger accountLedger, int month, double contractTotal) {
		switch (month) {
		case 1:
			accountLedger.setPeriod01(contractTotal);
			accountLedger.setPeriod02(contractTotal);
			accountLedger.setPeriod03(contractTotal);
			accountLedger.setPeriod04(contractTotal);
			accountLedger.setPeriod05(contractTotal);
			accountLedger.setPeriod06(contractTotal);
			accountLedger.setPeriod07(contractTotal);
			accountLedger.setPeriod08(contractTotal);
			accountLedger.setPeriod09(contractTotal);
			accountLedger.setPeriod10(contractTotal);
			accountLedger.setPeriod11(contractTotal);
			accountLedger.setPeriod12(contractTotal);
			break;
		case 2:
			accountLedger.setPeriod02(contractTotal);
			accountLedger.setPeriod03(contractTotal);
			accountLedger.setPeriod04(contractTotal);
			accountLedger.setPeriod05(contractTotal);
			accountLedger.setPeriod06(contractTotal);
			accountLedger.setPeriod07(contractTotal);
			accountLedger.setPeriod08(contractTotal);
			accountLedger.setPeriod09(contractTotal);
			accountLedger.setPeriod10(contractTotal);
			accountLedger.setPeriod11(contractTotal);
			accountLedger.setPeriod12(contractTotal);
			break;
		case 3:
			accountLedger.setPeriod03(contractTotal);
			accountLedger.setPeriod04(contractTotal);
			accountLedger.setPeriod05(contractTotal);
			accountLedger.setPeriod06(contractTotal);
			accountLedger.setPeriod07(contractTotal);
			accountLedger.setPeriod08(contractTotal);
			accountLedger.setPeriod09(contractTotal);
			accountLedger.setPeriod10(contractTotal);
			accountLedger.setPeriod11(contractTotal);
			accountLedger.setPeriod12(contractTotal);
			break;
		case 4:
			accountLedger.setPeriod04(contractTotal);
			accountLedger.setPeriod05(contractTotal);
			accountLedger.setPeriod06(contractTotal);
			accountLedger.setPeriod07(contractTotal);
			accountLedger.setPeriod08(contractTotal);
			accountLedger.setPeriod09(contractTotal);
			accountLedger.setPeriod10(contractTotal);
			accountLedger.setPeriod11(contractTotal);
			accountLedger.setPeriod12(contractTotal);
			break;
		case 5:
			accountLedger.setPeriod05(contractTotal);
			accountLedger.setPeriod06(contractTotal);
			accountLedger.setPeriod07(contractTotal);
			accountLedger.setPeriod08(contractTotal);
			accountLedger.setPeriod09(contractTotal);
			accountLedger.setPeriod10(contractTotal);
			accountLedger.setPeriod11(contractTotal);
			accountLedger.setPeriod12(contractTotal);
			break;
		case 6:
			accountLedger.setPeriod06(contractTotal);
			accountLedger.setPeriod07(contractTotal);
			accountLedger.setPeriod08(contractTotal);
			accountLedger.setPeriod09(contractTotal);
			accountLedger.setPeriod10(contractTotal);
			accountLedger.setPeriod11(contractTotal);
			accountLedger.setPeriod12(contractTotal);
			break;

		case 7:
			accountLedger.setPeriod07(contractTotal);
			accountLedger.setPeriod08(contractTotal);
			accountLedger.setPeriod09(contractTotal);
			accountLedger.setPeriod10(contractTotal);
			accountLedger.setPeriod11(contractTotal);
			accountLedger.setPeriod12(contractTotal);
			break;
		case 8:
			accountLedger.setPeriod08(contractTotal);
			accountLedger.setPeriod09(contractTotal);
			accountLedger.setPeriod10(contractTotal);
			accountLedger.setPeriod11(contractTotal);
			accountLedger.setPeriod12(contractTotal);
			break;

		case 9:
			accountLedger.setPeriod09(contractTotal);
			accountLedger.setPeriod10(contractTotal);
			accountLedger.setPeriod11(contractTotal);
			accountLedger.setPeriod12(contractTotal);
			break;
		case 10:
			accountLedger.setPeriod10(contractTotal);
			accountLedger.setPeriod11(contractTotal);
			accountLedger.setPeriod12(contractTotal);
			break;
		case 11:
			accountLedger.setPeriod11(contractTotal);
			accountLedger.setPeriod12(contractTotal);
			break;
		case 12:
			accountLedger.setPeriod12(contractTotal);
			break;
		}

		return accountLedger;
	}

	public ExcelFile downloadBudgetForecastExcel(String jobNo, String excelType, Integer month, Integer year) throws DatabaseOperationException {
		logger.info("STARTED -> downloadBudgetForecastExcel");
		List<BudgetForecastWrapper> budgetForecastWrapperList = obtainAccountLedgersByJobNo(jobNo, month, year);

		ExcelFile excelFile = null;
		if ("ALL".equals(excelType))
			excelFile = budgetForecastExcelFile(jobNo, String.valueOf(month), String.valueOf(year), budgetForecastWrapperList);
		else if ("FC".equals(excelType))
			excelFile = latestForecastExcelFile(jobNo, String.valueOf(month), String.valueOf(year), budgetForecastWrapperList);

		logger.info("END -> downloadBudgetForecastExcel");
		return excelFile;
	}

	private ExcelFile latestForecastExcelFile(String jobNo, String month, String year,  List<BudgetForecastWrapper> budgetForecastWrapperList) {
		ExcelFile excelFile = new ExcelFile();
		ExcelWorkbook doc = excelFile.getDocument();
		String filename = "";
		filename = jobNo + "-LatestForecastExcel-" + DateUtil.formatDate(new Date()) + ExcelFile.EXTENSION;
		excelFile.setFileName(filename);
		logger.info("J#" + jobNo + " Creating Excel file: " + filename);

		// Column Headers
		String[] colHeaders = new String[] {"Object Code",
											"Description",
											"Subcontract No",
											"Contract total(Forecast as of " + month + "/" + year + ")" };
		doc.insertRow(colHeaders);
		doc.setCellFontBold(0, 0, 0, colHeaders.length);

		// Insert rows
		logger.info("Inserting rows of Excel file: " + filename);
		int row = 1;
		Double latestForecast = 0.0;
		for (BudgetForecastWrapper budgetForecastWrapper : budgetForecastWrapperList) {
			latestForecast += budgetForecastWrapper.getLatestForecast() != null ? budgetForecastWrapper.getLatestForecast() : 0.0;

			doc.insertRow(new String[4]);

			doc.setCellValue(row, 0, budgetForecastWrapper.getObjectCode(), true);
			doc.setCellValue(row, 1, budgetForecastWrapper.getDescription(), true);
			doc.setCellValue(row, 2, budgetForecastWrapper.getSubledger(), true);
			doc.setCellValue(row, 3, budgetForecastWrapper.getLatestForecast() != null ? budgetForecastWrapper.getLatestForecast().toString() : "0.00", false);
			row++;
		}

		String[] rowTotal = new String[] { "", "", "TOTAL:", latestForecast.toString() };
		doc.insertRow(rowTotal);

		doc.setColumnWidth(0, 15);
		doc.setColumnWidth(1, 50);
		doc.setColumnWidth(2, 15);
		doc.setColumnWidth(3, 20);
		doc.setCellFontBold(row, 2, row, 3);

		return excelFile;
	}


	private ExcelFile budgetForecastExcelFile(String jobNo, String month, String year, List<BudgetForecastWrapper> budgetForecastWrapperList) {
		ExcelFile excelFile = new ExcelFile();
		ExcelWorkbook doc = excelFile.getDocument();
		String filename = "";
		filename = jobNo + " BudgetForecastExcel " + DateUtil.formatDate(new Date()) + ExcelFile.EXTENSION;
		excelFile.setFileName(filename);
		logger.info("J#" + jobNo + " Creating Excel file: " + filename);

		// Column Headers
		String[] colHeaders = new String[] {"Object Code",
											"Description",
											"Subcontract No",
											"Contract total(as of " + month + "/" + year + ") Actual Cost",
											"Original Budget",
											"Forecast"
											};
		doc.insertRow(colHeaders);
		doc.setCellFontBold(0, 0, 0, colHeaders.length);

		// Insert rows
		logger.info("Inserting rows of Excel file: " + filename);
		int row = 1;
		Double actualCost = 0.0;
		Double originalBudget = 0.0;
		Double latestForecast = 0.0;
		for (BudgetForecastWrapper budgetForecastWrapper : budgetForecastWrapperList) {
			actualCost += budgetForecastWrapper.getActualCost() != null ? budgetForecastWrapper.getActualCost() : 0.0;
			originalBudget += budgetForecastWrapper.getOriginalBudget() != null ? budgetForecastWrapper.getOriginalBudget() : 0.0;
			latestForecast += budgetForecastWrapper.getLatestForecast() != null ? budgetForecastWrapper.getLatestForecast() : 0.0;

			doc.insertRow(new String[colHeaders.length]);
			
			doc.setCellValue(row, 0, budgetForecastWrapper.getObjectCode(), true);
			doc.setCellValue(row, 1, budgetForecastWrapper.getDescription(), true);
			doc.setCellValue(row, 2, budgetForecastWrapper.getSubledger(), true);
			doc.setCellValue(row, 3, budgetForecastWrapper.getActualCost() != null ? budgetForecastWrapper.getActualCost().toString() : "0.00", false);
			doc.setCellValue(row, 4, budgetForecastWrapper.getOriginalBudget() != null ? budgetForecastWrapper.getOriginalBudget().toString() : "0.00", false);
			doc.setCellValue(row, 5, budgetForecastWrapper.getLatestForecast() != null ? budgetForecastWrapper.getLatestForecast().toString() : "0.00", false);
			// doc.setCellStyle(row, 5, "0.00");

			row++;
		}

		String[] rowTotal = new String[] { 	"",
											"",
											"TOTAL:",
											actualCost.toString(),
											originalBudget.toString(),
											latestForecast.toString() };
		doc.insertRow(rowTotal);

		doc.setColumnWidth(0, 15);
		doc.setColumnWidth(1, 50);
		doc.setColumnWidth(2, 15);
		doc.setColumnWidth(3, 40);
		doc.setColumnWidth(4, 20);
		doc.setColumnWidth(5, 20);
		doc.setCellFontBold(row, 2, row, 5);

		return excelFile;
	}

	private Double objectToDouble(Object value) {
		if (value != null)
			if (value instanceof Double)
				return (Double) value;
			else if (GenericValidator.isDouble(value.toString().trim()))
				return Double.valueOf(value.toString().trim());
		return Double.valueOf(0);
	}

	private String numericToString(Object value, String formattingString) {
		if (value == null)
			return "";
		if (value instanceof Integer) {
			try {
				DecimalFormat formatter = new DecimalFormat(formattingString);
				StringBuffer buffer = new StringBuffer();
				FieldPosition pos = new FieldPosition(0);
				formatter.format(value, buffer, pos);
				return buffer.toString();
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
		return value.toString();
	}
	
}
