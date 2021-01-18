package com.gammon.pcms.scheduler.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.MainCertWSDao;
import com.gammon.qs.dao.PaymentCertDetailHBDao;
import com.gammon.qs.dao.PaymentCertHBDao;
import com.gammon.qs.dao.PaymentPostingWSDao;
import com.gammon.qs.dao.PaymentWSDao;
import com.gammon.qs.dao.ResourceSummaryHBDao;
import com.gammon.qs.dao.SubcontractDetailHBDao;
import com.gammon.qs.dao.SubcontractHBDao;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.ResourceSummary;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.SubcontractDetailBQ;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.wrapper.ParentJobMainCertReceiveDateWrapper;
import com.gammon.qs.wrapper.scPayment.AccountMovementWrapper;
import com.gammon.qs.wrapper.scPayment.PaymentDueDateAndValidationResponseWrapper;
@Component
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class PaymentPostingService {
	
	private Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	private PaymentCertHBDao scPaymentCertDao;
	@Autowired
	private PaymentCertDetailHBDao scPaymentDetailDao;
	@Autowired
	private PaymentPostingWSDao paymentPostingDao;
	@Autowired
	private SubcontractHBDao scPackageHBDao;
	@Autowired
	private SubcontractDetailHBDao scDetailDao;
	@Autowired
	private ResourceSummaryHBDao bqResourceSummaryDao;
	@Autowired
	private PaymentWSDao paymentWSDao;
	@Autowired
	private MainCertWSDao mainContractCertificateWSDao;
	@Autowired
	private WebServiceConfig webServiceConfig;
	@Autowired
	private SubcontractDetailHBDao scDetailsHBDao;
	/**
	 * To run payment posting
	 *
	 * @author	tikywong
	 * @since	Mar 24, 2016 9:43:05 AM
	 */
	public void runPaymentPosting() {
		logger.info("STARTED: runPaymentPosting");
		try {
			postPayments(webServiceConfig.getWsJde("USERNAME"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("ENDED: runPaymentPosting");
	}
	
	/**
	 * Post Payment to JDE Account Payable F0411Z1
	 * 
	 * @author tikywong
	 * reviewed on February 13, 2014 2:46:07 PM
	 * @author Henry Lai
	 * modified on 17-10-2014
	 */
	public Boolean postPayments(String usernameJDESOA3) throws Exception {
		usernameJDESOA3 = usernameJDESOA3.toUpperCase();
		List<PaymentCert> paymentCerts = scPaymentCertDao.getSCPaymentCertsPCS();
		for (PaymentCert paymentCert : paymentCerts) {
			try {
				//Update invalid Due Date
				if (paymentCert.getDueDate() == null)
					paymentCert = this.calculateAndUpdatePaymentDueDate(paymentCert);
				else if (paymentCert.getDueDate().before(new Date()))
					paymentCert.setDueDate(new Date());
				
				//Only post payment with Due Date
				if (paymentCert.getDueDate() != null) {
					//CF
					Double cpfAmount = scPaymentDetailDao.getCertCpfAmount(paymentCert);
					if (cpfAmount == null)
						cpfAmount = Double.valueOf(0.0);

					//RT+RA
					Double retentionAmount = scPaymentDetailDao.obtainPaymentRetentionAmount(paymentCert);
					if (retentionAmount == null)
						retentionAmount = Double.valueOf(0.0);
					
					//RR
					Double retentionReleasedAmount = scPaymentDetailDao.obtainPaymentRetentionReleasedAmount(paymentCert);
					if (retentionReleasedAmount == null)
						retentionReleasedAmount = Double.valueOf(0.0);
					
					//GP
					Double gstPayable = scPaymentDetailDao.obtainPaymentGstPayable(paymentCert);
					if (gstPayable == null)
						gstPayable = Double.valueOf(0.0);

					//GR
					Double gstReceivable = scPaymentDetailDao.obtainPaymentGstReceivable(paymentCert);
					if (gstReceivable == null)
						gstReceivable = Double.valueOf(0.0);

					//Any accounts NOT IN (CF, RT, RA, RR, GP, GR, MR)
					List<AccountMovementWrapper> accountMovements = scPaymentDetailDao.obtainPaymentAccountMovements(paymentCert);
					
					//MR
					for (AccountMovementWrapper accountMovement : accountMovements) {
						Double materialRetention = scPaymentDetailDao.obtainAccountMaterialRetention(paymentCert, accountMovement.getObjectCode(), accountMovement.getSubsidiaryCode());
						if (materialRetention == null)
							materialRetention = Double.valueOf(0);

						accountMovement.setMovementAmount(accountMovement.getMovementAmount() - materialRetention);
					}
					// Post payments
					boolean posted = false;
					try {
						posted = paymentPostingDao.postPayments(paymentCert, cpfAmount, retentionAmount + retentionReleasedAmount, gstPayable, gstReceivable, accountMovements, usernameJDESOA3);
					} catch (Exception e1) {
						e1.printStackTrace();
						return Boolean.FALSE;
					}
					
					if (posted) {
						// Update 1.Payment Certificate, 2.Package, 3a.Details (normal Payment), 3b. Payment Details (Direct Payment)
						//1. Update Payment Certificate
						logger.info("Payment Posted!");
						paymentCert.setPaymentStatus(PaymentCert.PAYMENTSTATUS_APR_POSTED_TO_FINANCE);
						paymentCert.setCertIssueDate(new Date());
						paymentCert.populate(usernameJDESOA3);
						scPaymentCertDao.saveOrUpdate(paymentCert);
						
						//2. Update Package
						Subcontract scPackage = paymentCert.getSubcontract();
						if (scPackage == null)
							scPackage = scPackageHBDao.obtainSCPackage(paymentCert.getJobNo(), paymentCert.getPackageNo());
						scPackage.setLastPaymentCertIssuedDate(new Date());
						//scPackage.setAccumlatedRetention(scPackage.getAccumlatedRetention() == null ? new BigDecimal(0.0) : scPackage.getAccumlatedRetention().add(new BigDecimal(retentionAmount)));
						scPackage.setAccumlatedRetention(scPackage.getAccumlatedRetention() == null ? new BigDecimal(0.0) : scPackage.getAccumlatedRetention().add(CalculationUtil.roundToBigDecimal(retentionAmount, 2)));
						scPackage.setRetentionReleased(scPackage.getRetentionReleased() == null ? new BigDecimal(0.0) : scPackage.getRetentionReleased().add(CalculationUtil.roundToBigDecimal(retentionReleasedAmount, 2)));
						
						
						if(paymentCert.getPaymentCertNo()==1){
							scPackage.setFirstPaymentCertIssuedDate(new Date());
							
							/**
							 * @author koeyyeung
							 * modified on Feb, 2015
							 * Payment Requisition Revamp
							 * Cum Work Done will be mirrored from Cum IV under payment requisition 
							 * **/
							if(PaymentCert.DIRECT_PAYMENT.equals(paymentCert.getDirectPayment()) && "1".equals(paymentCert.getSubcontract().getJobInfo().getRepackagingType())){
								try {
									List<SubcontractDetailBQ> scDetailList = scDetailDao.obtainSCDetailsBQ(paymentCert.getJobNo(), paymentCert.getPackageNo());
									for(SubcontractDetailBQ scDetails: scDetailList){
										ResourceSummary resourceSummary = bqResourceSummaryDao.getResourceSummary(scPackage.getJobInfo(), scPackage.getPackageNo(), 
												scDetails.getObjectCode(), scDetails.getSubsidiaryCode(), 
												scDetails.getDescription(), scDetails.getUnit(), scDetails.getCostRate(), scDetails.getQuantity());
										if(resourceSummary!=null && resourceSummary.getCurrIVAmount()!=null && resourceSummary.getCurrIVAmount()!= 0.0){
											scDetails.setCumWorkDoneQuantity(resourceSummary.getRate().equals(Double.valueOf(0)) ? resourceSummary.getRate() : CalculationUtil.round((resourceSummary.getCurrIVAmount()/resourceSummary.getRate()), 4));
											scDetails.setAmountCumulativeWD(new BigDecimal(resourceSummary.getCurrIVAmount()/resourceSummary.getRate()*scDetails.getScRate()).setScale(2, RoundingMode.HALF_UP));
											scDetailDao.update(scDetails);
										}
									} 
								}catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						
						//Fix: Should only set SCPackage "Payment Status"='F'  when payment cert status is APR
						if("F".equals(paymentCert.getIntermFinalPayment())){
							scPackage.setFinalPaymentIssuedDate(new Date());
							scPackage.setPaymentStatus(Subcontract.FINAL_PAYMENT);
						}
						//Payment Requisition
						else if(PaymentCert.DIRECT_PAYMENT.equals(paymentCert.getDirectPayment())){
							scPackage.setPaymentStatus(Subcontract.DIRECT_PAYMENT);
						}
						//Payment No.1 - X except Final Payment
						else
							scPackage.setPaymentStatus(Subcontract.INTERIM_PAYMENT);
						
						
						paymentCert.setSubcontract(scPackage);
						
						
						//Update SCPackage Details
						List<SubcontractDetail> scDetails = scDetailDao.getSCDetails(scPackage);
						if(scDetails!=null && scDetails.size()>0){
							for(SubcontractDetail scDetail : scDetails){
								scDetail.setSubcontract(scPackage);;	//Handle Active Detail lines only
							}
							updateSCDetailandPackageAfterPostingToFinance(scPackage, scDetailsHBDao.getSCDetails(scPackage));
						}

						scPackageHBDao.updateSubcontract(scPackage);

						// add a web service to insert it into F58011 for finance department to reference
						int NumOfRowInserted = paymentWSDao.insertSCPaymentHeader(paymentCert);
						logger.info("Number of row insert into F58011: " + NumOfRowInserted);
					}
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Payment " + paymentCert.getJobNo() + "/" + paymentCert.getPackageNo() + "/" + paymentCert.getPaymentCertNo() + " posting Invalid.\n" + e.getLocalizedMessage());
				// log down exception and continue the loop to post
			}
		}
		return Boolean.TRUE;
	}

	
	/**
	 * 
	 * @author tikywong
	 * refactored on Nov 28, 2013 12:11:11 PM
	 */
	public void updateSCDetailandPackageAfterPostingToFinance(Subcontract subcoontract, List<SubcontractDetail> scDetailsList){
		logger.info("Update Package and Detail after successfully posting to Finance (AP) - Job: "+subcoontract.getJobInfo().getJobNumber()+" Package: "+subcoontract.getPackageNo());
		
		BigDecimal totalPostedCert = new BigDecimal(0.0);
		BigDecimal totalCCPostedCert = new BigDecimal(0.0);
		BigDecimal totalMOSPostedCert = new BigDecimal(0.0);
		BigDecimal totalAPPostedCert = new BigDecimal(0.0);
		
		
		for(SubcontractDetail scDetails : scDetailsList){
			if(BasePersistedAuditObject.ACTIVE.equals(scDetails.getSystemStatus())){
				//1. update package detail
				/**
				 * @author koeyyeung
				 * created on 19 Jul, 2016
				 * convert to amount based**/
				scDetails.setAmountPostedCert(scDetails.getAmountCumulativeCert());
				
				// Payment Requisition: Not approved but paid
				if("BQ".equals(scDetails.getLineType()) && SubcontractDetail.NOT_APPROVED.equals(scDetails.getApproved()))
					scDetails.setApproved(SubcontractDetail.NOT_APPROVED_BUT_PAID);

				//calculate total for package
				if (!"C1".equals(scDetails.getLineType()) && !"C2".equals(scDetails.getLineType()) 
					&&!"MS".equals(scDetails.getLineType()) && !"RR".equals(scDetails.getLineType()) 
					&& !"RT".equals(scDetails.getLineType()) && !"RA".equals(scDetails.getLineType())
					&& !"AP".equals(scDetails.getLineType()))
					totalPostedCert = totalPostedCert.add(scDetails.getAmountPostedCert());
				else if("C1".equals(scDetails.getLineType()) || "C2".equals(scDetails.getLineType()))
					totalCCPostedCert = totalCCPostedCert.add(scDetails.getAmountPostedCert());
				else if("MS".equals(scDetails.getLineType()))
					totalMOSPostedCert = totalMOSPostedCert.add(scDetails.getAmountPostedCert());
				else if("AP".equals(scDetails.getLineType()))
					totalAPPostedCert = totalAPPostedCert.add(scDetails.getAmountPostedCert());
			}

		}
		
		//2. update package
		subcoontract.setTotalPostedCertifiedAmount(totalPostedCert);
		subcoontract.setTotalCCPostedCertAmount(totalCCPostedCert);
		subcoontract.setTotalMOSPostedCertAmount(totalMOSPostedCert);
		subcoontract.setTotalAPPostedCertAmount(totalAPPostedCert);
	}
	
	public PaymentCert calculateAndUpdatePaymentDueDate(PaymentCert paymentCert) throws ValidateBusinessLogicException {
		if (paymentCert == null || paymentCert.getSubcontract() == null || paymentCert.getSubcontract().getJobInfo() == null)
			throw new ValidateBusinessLogicException(paymentCert == null ? ("Payment Certificate is null") : (paymentCert.getSubcontract() == null ? ("Package is null") : ("Job is null")));

		logger.info("Job: " + paymentCert.getSubcontract().getJobInfo().getJobNumber() + " " +
					"Package: " + paymentCert.getSubcontract().getPackageNo() + " " +
					"Payment Certificate No.: " + paymentCert.getPaymentCertNo());

		PaymentDueDateAndValidationResponseWrapper wrapper = calculatePaymentDueDate(paymentCert.getSubcontract().getJobInfo().getJobNumber(),
																					paymentCert.getSubcontract().getPackageNo(),
																					paymentCert.getMainContractPaymentCertNo(),
																					paymentCert.getAsAtDate(),
																					paymentCert.getIpaOrInvoiceReceivedDate(),
																					paymentCert.getDueDate(),
																					paymentCert.getBypassPaymentTerms());

		if (wrapper.isvalid())
			paymentCert.setDueDate(wrapper.getDueDate());
		else
			throw new ValidateBusinessLogicException(wrapper.getErrorMsg());

		return paymentCert;
	}
	
	/**
	 * To calculate "Payment Due Date" according to Payment Terms [QS0 - QS7]
	 *
	 * @param jobNumber
	 * @param packageNo
	 * @param mainCertNo
	 * @param asAtDate
	 * @param ipaOrInvoiceDate
	 * @param dueDate
	 * @return
	 * @author	tikywong
	 * @since	Mar 24, 2016 9:35:33 AM
	 */
	public PaymentDueDateAndValidationResponseWrapper calculatePaymentDueDate(String jobNumber, String packageNo, Integer mainCertNo, Date asAtDate, Date ipaOrInvoiceDate, Date dueDate, String bypassPaymentTerms) {
		PaymentDueDateAndValidationResponseWrapper responseWrapper = new PaymentDueDateAndValidationResponseWrapper();
		Subcontract scPackage = null;

		try {
			scPackage = scPackageHBDao.obtainSCPackage(jobNumber, packageNo);
		} catch (DatabaseOperationException dbException) {
			logger.info("Failed to obtain scPackage. Unable to calculate Due Date for Payment.");
		}

		try {
			Calendar calculatedDueDate = Calendar.getInstance(); // based on today
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");     
			/**
			 * Add Payment Terms QS8 Pay when paid + 0 day
			 * @author koeyyeung
			 * modified on 29-08-2019
			 * **/
			// 1. QS1 or QS2 or QS8
			if (("QS1".equals(scPackage.getPaymentTerms()) || "QS2".equals(scPackage.getPaymentTerms()) || "QS8".equals(scPackage.getPaymentTerms())) && PaymentCert.BYPASS_PAYMENT_TERMS.N.toString().equals(bypassPaymentTerms)) {
				logger.info("Calculating Due Date: "+jobNumber + "-SC" + packageNo + "-MainCert " +mainCertNo + "-" + scPackage.getPaymentTerms());
				// 1a. Obtain Main Certificate's As At Date
				ParentJobMainCertReceiveDateWrapper mainCertAsAtRecDateWrapper = null;
				try {
					mainCertAsAtRecDateWrapper = mainContractCertificateWSDao.obtainParentMainContractCertificate(jobNumber, mainCertNo);
				} catch (Exception e) {
					logger.info("Main Contract Certificate cannot be found");
					responseWrapper.setErrorMsg("Main Contract Certificate cannot be found");
					responseWrapper.setIsvalid(false);
					return responseWrapper;
				}

				if (mainCertAsAtRecDateWrapper == null || mainCertAsAtRecDateWrapper.getValueDateOnCert() == null) {
					logger.info("Main Contract Certificate does not exist or certificate has not been posted (Certificate Status is not 300)");
					responseWrapper.setErrorMsg("Main Contract Certificate does not exist or certificate has not been posted (Certificate Status is not 300)");
					responseWrapper.setIsvalid(false);
					return responseWrapper;
				}

				// if user's As At Date == null, then bypass validation of "Payment's As At Date must be ON or BEFORE than Main Contract Cert's As At Date"
				try {
					
					if (asAtDate != null){
						try {
							Date asAtDateWithoutTime = sdf.parse(sdf.format(asAtDate));
							Date mainCertAsAtDateWithoutTime = mainCertAsAtRecDateWrapper.getValueDateOnCert() != null ?sdf.parse(sdf.format(mainCertAsAtRecDateWrapper.getValueDateOnCert())): null;

							if(asAtDateWithoutTime.after(mainCertAsAtDateWithoutTime)) {
								SimpleDateFormat formattedDate = new SimpleDateFormat("dd/MM/yyyy");
								String parentMainCertAsAtDate = formattedDate.format(mainCertAsAtRecDateWrapper.getValueDateOnCert());

								responseWrapper.setErrorMsg("Payment's As At Date must be ON or BEFORE than Main Contract Certificate's As At Date.</br>" +
										"Main Contract Certificate As At Date: " + parentMainCertAsAtDate);
								responseWrapper.setIsvalid(false);
								return responseWrapper;
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				} catch (NullPointerException nException) {
					responseWrapper.setErrorMsg("Parent's Main Contract Certificate's As At Date is null.");
					responseWrapper.setIsvalid(false);
					return responseWrapper;
				} catch (IllegalArgumentException iException) {
					responseWrapper.setErrorMsg("Unable to format Parent's Main Contract Certificate's As At Date.");
					responseWrapper.setIsvalid(false);
					return responseWrapper;
				}

				// pass the null out and handle elsewhere
				if (mainCertAsAtRecDateWrapper.getReceivedDate() == null) {
					responseWrapper.setIsvalid(true);
					responseWrapper.setDueDate(null);
					return responseWrapper;
				}

				calculatedDueDate.setTime(mainCertAsAtRecDateWrapper.getReceivedDate());
				if ("QS1".equals(scPackage.getPaymentTerms()))
					calculatedDueDate.add(Calendar.DATE, PaymentCert.PAYMENT_TERM_DATE_QS1);
				else
					calculatedDueDate.add(Calendar.DATE, PaymentCert.PAYMENT_TERM_DATE_QS2);

			}
			// 2. QS3
			else if ("QS3".equals(scPackage.getPaymentTerms())) {
				logger.info("Calculating Due Date: QS3");
				if (ipaOrInvoiceDate == null) {
					responseWrapper.setErrorMsg("Subcontract's IPA Received Date does not exist");
					responseWrapper.setIsvalid(false);
					return responseWrapper;
				}
				
				if (asAtDate == null) {
					responseWrapper.setErrorMsg("Payment As At Date does not exist");
					responseWrapper.setIsvalid(false);
					return responseWrapper;
				}
				
				try {
					Date asAtDateWithoutTime = sdf.parse(sdf.format(asAtDate));
					Date ipaOrInvoiceDateWithoutTime = sdf.parse(sdf.format(ipaOrInvoiceDate));

					// invoice received date on or after payment as at date
					if (ipaOrInvoiceDateWithoutTime.before(asAtDateWithoutTime)){
						responseWrapper.setErrorMsg("IPA Received Date has to be ON or AFTER Payment As At Date");
						responseWrapper.setIsvalid(false);
						return responseWrapper;
					}

					calculatedDueDate.setTime(ipaOrInvoiceDate);
					calculatedDueDate.add(Calendar.DATE, PaymentCert.PAYMENT_TERM_DATE_QS3);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			// 3. QS4-QS7
			else if ("QS4".equals(scPackage.getPaymentTerms()) ||"QS5".equals(scPackage.getPaymentTerms()) ||"QS6".equals(scPackage.getPaymentTerms()) ||"QS7".equals(scPackage.getPaymentTerms())) {
				logger.info("Calculating Due Date: QS4-QS7");
				if (ipaOrInvoiceDate == null) {
					responseWrapper.setErrorMsg("Payment Invoice Received Date does not exist");
					responseWrapper.setIsvalid(false);
					return responseWrapper;
				}
				if (asAtDate == null) {
					responseWrapper.setErrorMsg("Payment As At Date does not exist");
					responseWrapper.setIsvalid(false);
					return responseWrapper;
				}
				
				try {
					Date asAtDateWithoutTime = sdf.parse(sdf.format(asAtDate));
					Date ipaOrInvoiceDateWithoutTime = sdf.parse(sdf.format(ipaOrInvoiceDate));
					
					// invoice received date on or after payment as at date
					if (ipaOrInvoiceDateWithoutTime.before(asAtDateWithoutTime)){
						responseWrapper.setErrorMsg("Invoice Received Date has to be ON or AFTER Payment As At Date");
						responseWrapper.setIsvalid(false);
						return responseWrapper;
					}
					
					calculatedDueDate.setTime(ipaOrInvoiceDate);
					if ("QS4".equals(scPackage.getPaymentTerms()))
						calculatedDueDate.add(Calendar.DATE, PaymentCert.PAYMENT_TERM_DATE_QS4);
					else if ("QS5".equals(scPackage.getPaymentTerms()))
						calculatedDueDate.add(Calendar.DATE, PaymentCert.PAYMENT_TERM_DATE_QS5);
					else if ("QS6".equals(scPackage.getPaymentTerms()))
						calculatedDueDate.add(Calendar.DATE, PaymentCert.PAYMENT_TERM_DATE_QS6);
					else if ("QS7".equals(scPackage.getPaymentTerms()))
						calculatedDueDate.add(Calendar.DATE, PaymentCert.PAYMENT_TERM_DATE_QS7);
					
					// invoice received date  - payment as at date  < 30 days
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(asAtDate);
					calendar.add(Calendar.DAY_OF_MONTH,+30);	
					Date maxInvoiceReceivedDate =calendar.getTime();
					if(ipaOrInvoiceDate.after(maxInvoiceReceivedDate)){
						responseWrapper.setErrorMsg("Invoice Received Date cannot be later than Payment As At Date for more than 30 days");
						responseWrapper.setIsvalid(true);//warning only, can proceed to save
						responseWrapper.setDueDate(calculatedDueDate.getTime());
						return responseWrapper;
					}
					
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				
			}
			// 4a. QS0 - User entered Due Date has to be before today ; OR byPassPaymentTerms (QS1, 2, 8)
			else if (dueDate != null) {
				// earlier than today
				if (dueDate.before(new Date())) 
					calculatedDueDate.setTime(new Date());
				else 
					calculatedDueDate.setTime(dueDate);
			}
			// 4b. QS0 - No Due Date entered
			else {
				responseWrapper.setIsvalid(true);
				responseWrapper.setDueDate(dueDate); // set it to null
				return responseWrapper;
			}
			
			//For QS0 - QS8
			if (calculatedDueDate.before(Calendar.getInstance())) 
				calculatedDueDate.setTime(new Date());

			responseWrapper.setIsvalid(true);
			responseWrapper.setDueDate(calculatedDueDate.getTime());
			return responseWrapper;

		} catch (NumberFormatException e) {
			logger.info("Invalid input format.");
		}
		return responseWrapper;
	}
	
}
