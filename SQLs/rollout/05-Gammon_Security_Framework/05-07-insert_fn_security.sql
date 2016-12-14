--insert 1 record into AC_ACCESSRIGHT
--insert 253 records into AC_FUNCTION and AC_FUNCTIONSECURITY
SET SERVEROUTPUT ON
DECLARE
	TYPE FN IS VARRAY(6) OF VARCHAR2(128);
	TYPE FnTable IS TABLE OF FN INDEX BY VARCHAR2(16);
	fn_list	FnTable;
	DEBUGON                         	    NUMBER:=0;
	EXEC_INSERT_FNSEC						NUMBER:=1;
	EXEC_INSERT_ACCESSRIGHT					NUMBER:=1;
  	EXEC_DELETE_FNSEC           			NUMBER:=0;
	ACDATA_SCHEMA		                    VARCHAR2(16):='ACDATAPROD';
	access_right		                    VARCHAR2(32):='ENABLE';
	count_insert_fn                 	    NUMBER:=0;
	count_insert_sec                	    NUMBER:=0;
	count_delete_fn                 	    NUMBER:=0;
	count_delete_sec                	    NUMBER:=0;
	fn_id				                    VARCHAR(16);
	SQL_INSERT_FN		                    VARCHAR2(512);
	SQL_INSERT_SEC							VARCHAR2(512);
	SQL_INSERT_ACCESSRIGHT_DISABLE			VARCHAR2(512):= 
      'insert into ' || ACDATA_SCHEMA ||'.AC_ACCESSRIGHT
      (FUNCTIONTYPE, ACCESSRIGHT, DESCRIPTION, LASTMODIFYDATE,LASTMODIFYUSER)
      values(''FUNCTION'', ''DISABLE'', ''DISABLE'', sysdate,	''SYSTEM'')';
  SQL_DELETE_FN                       VARCHAR2(512):= 'DELETE from ' || ACDATA_SCHEMA || '.AC_FUNCTION where APPLICATIONCODE = ''QS''';
  SQL_DELETE_SEC                       VARCHAR2(512):= 'DELETE from ' || ACDATA_SCHEMA || '.AC_FUNCTIONSECURITY where APPLICATIONCODE = ''QS''';
BEGIN
	fn_list('1') := fn('F02010101', 'ROLE_QS_QS', 'AddendumController', 'createAddendum', 'POST', 'service/addendum/createAddendum');
	fn_list('2') := fn('F02010102', 'ROLE_QS_QS', 'AddendumController', 'addAddendumDetail', 'POST', 'service/addendum/addAddendumDetail');
	fn_list('3') := fn('F02010103', 'ROLE_QS_QS', 'AddendumController', 'addAddendumFromResourceSummaries', 'POST', 'service/addendum/addAddendumFromResourceSummaries');
	fn_list('4') := fn('F02010201', 'ROLE_QS_ENQ', 'AddendumController', 'getLatestAddendum', 'GET', 'service/addendum/getLatestAddendum');
	fn_list('5') := fn('F02010202', 'ROLE_QS_ENQ', 'AddendumController', 'getAddendum', 'GET', 'service/addendum/getAddendum');
	fn_list('6') := fn('F02010203', 'ROLE_QS_ENQ', 'AddendumController', 'getAddendumList', 'GET', 'service/addendum/getAddendumList');
	fn_list('7') := fn('F02010204', 'ROLE_QS_ENQ', 'AddendumController', 'getTotalApprovedAddendumAmount', 'GET', 'service/addendum/getTotalApprovedAddendumAmount');
	fn_list('8') := fn('F02010205', 'ROLE_QS_ENQ', 'AddendumController', 'getAddendumDetailHeader', 'GET', 'service/addendum/getAddendumDetailHeader');
	fn_list('9') := fn('F02010206', 'ROLE_QS_ENQ', 'AddendumController', 'getAddendumDetailsByHeaderRef', 'GET', 'service/addendum/getAddendumDetailsByHeaderRef');
	fn_list('10') := fn('F02010207', 'ROLE_QS_ENQ', 'AddendumController', 'getAddendumDetailsWithoutHeaderRef', 'GET', 'service/addendum/getAddendumDetailsWithoutHeaderRef');
	fn_list('11') := fn('F02010208', 'ROLE_QS_ENQ', 'AddendumController', 'getAllAddendumDetails', 'GET', 'service/addendum/getAllAddendumDetails');
	fn_list('12') := fn('F02010209', 'ROLE_QS_ENQ', 'AddendumController', 'getDefaultValuesForAddendumDetails', 'GET', 'service/addendum/getDefaultValuesForAddendumDetails');
	fn_list('13') := fn('F02010301', 'ROLE_QS_QS', 'AddendumController', 'updateAddendum', 'POST', 'service/addendum/updateAddendum');
	fn_list('14') := fn('F02010302', 'ROLE_QS_QS', 'AddendumController', 'updateAddendumDetailHeader', 'POST', 'service/addendum/updateAddendumDetailHeader');
	fn_list('15') := fn('F02010303', 'ROLE_QS_QS', 'AddendumController', 'updateAddendumDetail', 'POST', 'service/addendum/updateAddendumDetail');
	fn_list('16') := fn('F02010401', 'ROLE_QS_QS', 'AddendumController', 'deleteAddendumDetailHeader', 'POST', 'service/addendum/deleteAddendumDetailHeader');
	fn_list('17') := fn('F02010402', 'ROLE_QS_QS', 'AddendumController', 'deleteAddendumDetail', 'POST', 'service/addendum/deleteAddendumDetail');
	fn_list('18') := fn('F02010403', 'ROLE_QS_QS', 'AddendumController', 'deleteAddendumFromSCDetails', 'POST', 'service/addendum/deleteAddendumFromSCDetails');
	fn_list('19') := fn('F02010501', 'ROLE_QS_QS', 'AddendumController', 'submitAddendumApproval', 'POST', 'service/addendum/submitAddendumApproval');
	fn_list('20') := fn('F02020201', 'ROLE_QS_ENQ', 'ADLController', 'getMonthlyJobCostList', 'GET', 'service/adl/getMonthlyJobCostList');
	fn_list('21') := fn('F02020202', 'ROLE_QS_ENQ', 'ADLController', 'getMonthlyJobCostListByPeroidRange', 'GET', 'service/adl/getMonthlyJobCostListByPeroidRange');
	fn_list('22') := fn('F02020203', 'ROLE_QS_ENQ', 'ADLController', 'getAAJILedgerList', 'GET', 'service/adl/getAAJILedgerList');
	fn_list('23') := fn('F02020204', 'ROLE_QS_ENQ', 'ADLController', 'getJobDashboardData', 'GET', 'service/adl/getJobDashboardData');
	fn_list('24') := fn('F02020205', 'ROLE_QS_ENQ', 'ADLController', 'getAccountBalanceList', 'GET', 'service/adl/getAccountBalanceList');
	fn_list('25') := fn('F02020206', 'ROLE_QS_ENQ', 'ADLController', 'getAccountLedgerList', 'GET', 'service/adl/getAccountLedgerList');
	fn_list('26') := fn('F02020207', 'ROLE_QS_ENQ', 'ADLController', 'getAccountLedgerListByGlDate', 'POST', 'service/adl/getAccountLedgerListByGlDate');
	fn_list('27') := fn('F02020208', 'ROLE_QS_ENQ', 'ADLController', 'getAccountMasterList', 'GET', 'service/adl/getAccountMasterList');
	fn_list('28') := fn('F02020209', 'ROLE_QS_ENQ', 'ADLController', 'getAccountMaster', 'GET', 'service/adl/getAccountMaster');
	fn_list('29') := fn('F02020210', 'ROLE_QS_ENQ', 'ADLController', 'getAddressBookListOfSubcontractorAndClient', 'GET', 'service/adl/getAddressBookListOfSubcontractorAndClient');
	fn_list('30') := fn('F02020211', 'ROLE_QS_ENQ', 'ADLController', 'getAddressBook', 'GET', 'service/adl/getAddressBook');
	fn_list('31') := fn('F02020212', 'ROLE_QS_ENQ', 'ADLController', 'getBusinessUnitList', 'GET', 'service/adl/getBusinessUnit');
	fn_list('32') := fn('F02020213', 'ROLE_QS_ENQ', 'ADLController', 'getApprovalHeaderList', 'GET', 'service/adl/getApprovalHeaderList');
	fn_list('33') := fn('F02020214', 'ROLE_QS_ENQ', 'ADLController', 'getApprovalDetailList', 'GET', 'service/adl/getApprovalDetailList');
	fn_list('34') := fn('F02020215', 'ROLE_QS_ENQ', 'ADLController', 'obtainCompanyCodeAndName', 'POST', 'service/adl/obtainCompanyCodeAndName');
	fn_list('35') := fn('F02030101', 'ROLE_QS_QS', 'AttachmentController', 'uploadRepackingAttachment', 'POST', 'service/attachment/uploadRepackingAttachment');
	fn_list('36') := fn('F02030102', 'ROLE_QS_QS', 'AttachmentController', 'addRepackagingTextAttachment', 'POST', 'service/attachment/addRepackagingTextAttachment');
	fn_list('37') := fn('F02030103', 'ROLE_QS_QS', 'AttachmentController', 'uploadSCAttachment', 'POST', 'service/attachment/uploadSCAttachment');
	fn_list('38') := fn('F02030104', 'ROLE_QS_QS', 'AttachmentController', 'uploadAddendumAttachment', 'POST', 'service/attachment/uploadAddendumAttachment');
	fn_list('39') := fn('F02030105', 'ROLE_QS_QS', 'AttachmentController', 'uploadAddendumTextAttachment', 'POST', 'service/attachment/uploadAddendumTextAttachment');
	fn_list('40') := fn('F02030106', 'ROLE_QS_QS', 'AttachmentController', 'uploadTextAttachment', 'POST', 'service/attachment/uploadTextAttachment');
	fn_list('41') := fn('F02030107', 'ROLE_QS_QS', 'AttachmentController', 'addMainCertFileAttachment', 'POST', 'service/attachment/addMainCertFileAttachment');
	fn_list('42') := fn('F02030108', 'ROLE_QS_QS', 'AttachmentController', 'uploadTransit', 'POST', 'service/attachment/transitUpload');
	fn_list('43') := fn('F02030201', 'ROLE_QS_ENQ', 'AttachmentController', 'getRepackagingAttachments', 'POST', 'service/attachment/getRepackagingAttachments');
	fn_list('44') := fn('F02030202', 'ROLE_QS_ENQ', 'AttachmentController', 'downloadRepackagingAttachment', 'GET', 'service/attachment/downloadRepackagingAttachment');
	fn_list('45') := fn('F02030203', 'ROLE_QS_ENQ', 'AttachmentController', 'getAttachmentListForPCMS', 'POST', 'service/attachment/getAttachmentListForPCMS');
	fn_list('46') := fn('F02030204', 'ROLE_QS_ENQ', 'AttachmentController', 'obtainAddendumFileAttachment', 'GET', 'service/attachment/obtainAddendumFileAttachment');
	fn_list('47') := fn('F02030205', 'ROLE_QS_ENQ', 'AttachmentController', 'generateSCAttachment', 'GET', 'service/attachment/downloadScAttachment');
	fn_list('48') := fn('F02030206', 'ROLE_QS_ENQ', 'AttachmentController', 'getMainCertFileAttachment', 'POST', 'service/attachment/getMainCertFileAttachment');
	fn_list('49') := fn('F02030207', 'ROLE_QS_ENQ', 'AttachmentController', 'obtainAttachmentList', 'POST', 'service/attachment/obtainAttachmentList');
	fn_list('50') := fn('F02030208', 'ROLE_QS_ENQ', 'AttachmentController', 'generateSubcontractLiabilityReport', 'GET', 'service/attachment/subcontractLiabilityReportExport');
	fn_list('51') := fn('F02030209', 'ROLE_QS_ENQ', 'AttachmentController', 'generatePrintBQMasterReconciliationReport', 'GET', 'service/attachment/printBQMasterReconciliationReport');
	fn_list('52') := fn('F02030210', 'ROLE_QS_ENQ', 'AttachmentController', 'generatePrintBQResourceRecociliationReport', 'GET', 'service/attachment/printBQRecourseReconciliationReport');
	fn_list('53') := fn('F02030211', 'ROLE_QS_ENQ', 'AttachmentController', 'generatePaymentCertificateEnquiryExcel', 'GET', 'service/attachment/paymentCertificateEnquiryExcelExport');
	fn_list('54') := fn('F02030212', 'ROLE_QS_ENQ', 'AttachmentController', 'printPaymentCertificateReportPdf', 'GET', 'service/attachment/printPaymentCertificateReportPdf');
	fn_list('55') := fn('F02030213', 'ROLE_QS_ENQ', 'AttachmentController', 'generateFinanceSubcontractListExcel', 'GET', 'service/attachment/financeSubcontractListDownload');
	fn_list('56') := fn('F02030214', 'ROLE_QS_ENQ', 'AttachmentController', 'generateSubcontractReport', 'GET', 'service/attachment/subcontractReportExport');
	fn_list('57') := fn('F02030215', 'ROLE_QS_ENQ', 'AttachmentController', 'generateSubcontractLiabilityExcel', 'GET', 'service/attachment/subcontractLiabilityExcelExport');
	fn_list('58') := fn('F02030216', 'ROLE_QS_ENQ', 'AttachmentController', 'generateSubcontractorAnalysisExcel', 'GET', 'service/attachment/subcontractorAnalysisExcelExport');
	fn_list('59') := fn('F02030217', 'ROLE_QS_ENQ', 'AttachmentController', 'generateSubcontractorAnalysisReport', 'GET', 'service/attachment/subcontractorAnalysisReportExport');
	fn_list('60') := fn('F02030301', 'ROLE_QS_QS', 'AttachmentController', 'saveRepackagingTextAttachment', 'POST', 'service/attachment/saveRepackagingTextAttachment');
	fn_list('61') := fn('F02030401', 'ROLE_QS_QS', 'AttachmentController', 'deleteRepackagingAttachment', 'POST', 'service/attachment/deleteRepackagingAttachment');
	fn_list('62') := fn('F02030402', 'ROLE_QS_QS', 'AttachmentController', 'deleteAddendumAttachment', 'POST', 'service/attachment/deleteAddendumAttachment');
	fn_list('63') := fn('F02030403', 'ROLE_QS_QS', 'AttachmentController', 'deleteAttachment', 'POST', 'service/attachment/deleteAttachment');
	fn_list('64') := fn('F02030404', 'ROLE_QS_QS', 'AttachmentController', 'deleteMainCertAttachment', 'POST', 'service/attachment/deleteMainCertAttachment');
	fn_list('65') := fn('F02040201', 'ROLE_QS_ENQ', 'JobController', 'getJobList', 'POST', 'service/job/getJobList');
	fn_list('66') := fn('F02040202', 'ROLE_QS_ENQ', 'JobController', 'getJobDetailList', 'POST', 'service/job/getJobDetailList');
	fn_list('67') := fn('F02040203', 'ROLE_QS_ENQ', 'JobController', 'getCompanyName', 'GET', 'service/job/getCompanyName');
	fn_list('68') := fn('F02040204', 'ROLE_QS_ENQ', 'JobController', 'getJob', 'GET', 'service/job/getJob');
	fn_list('69') := fn('F02040205', 'ROLE_QS_ENQ', 'JobController', 'getJobDates', 'GET', 'service/job/getJobDates');
	fn_list('70') := fn('F02040206', 'ROLE_QS_ENQ', 'JobController', 'obtainAllJobCompany', 'GET', 'service/job/obtainAllJobCompany');
	fn_list('71') := fn('F02040207', 'ROLE_QS_ENQ', 'JobController', 'obtainAllJobDivision', 'GET', 'service/job/obtainAllJobDivision');
	fn_list('72') := fn('F02040301', 'ROLE_QS_QS', 'JobController', 'updateJobInfo', 'POST', 'service/job/updateJobInfo');
	fn_list('73') := fn('F02040302', 'ROLE_QS_QS', 'JobController', 'updateJobDates', 'POST', 'service/job/updateJobDates');
	fn_list('74') := fn('F02040303', 'ROLE_QS_QS', 'JobController', 'updateJobInfoAndDates', 'POST', 'service/job/updateJobInfoAndDates');
	fn_list('75') := fn('F02050101', 'ROLE_QS_QS', 'MainCertController', 'createMainCert', 'POST', 'service/mainCert/createMainCert');
	fn_list('76') := fn('F02050102', 'ROLE_QS_QS', 'MainCertController', 'insertIPA', 'POST', 'service/mainCert/insertIPA');
	fn_list('77') := fn('F02050201', 'ROLE_QS_ENQ', 'MainCertController', 'getCertificateList', 'GET', 'service/mainCert/getCertificateList');
	fn_list('78') := fn('F02050202', 'ROLE_QS_ENQ', 'MainCertController', 'getPaidMainCertList', 'GET', 'service/mainCert/getPaidMainCertList');
	fn_list('79') := fn('F02050203', 'ROLE_QS_ENQ', 'MainCertController', 'getMainCertNoList', 'GET', 'service/mainCert/getMainCertNoList');
	fn_list('80') := fn('F02050204', 'ROLE_QS_ENQ', 'MainCertController', 'getRetentionReleaseList', 'GET', 'service/mainCert/getRetentionReleaseList');
	fn_list('81') := fn('F02050205', 'ROLE_QS_ENQ', 'MainCertController', 'getMainCertContraChargeList', 'GET', 'service/mainCert/getMainCertContraChargeList');
	fn_list('82') := fn('F02050206', 'ROLE_QS_ENQ', 'MainCertController', 'getCertificate', 'GET', 'service/mainCert/getCertificate');
	fn_list('83') := fn('F02050207', 'ROLE_QS_ENQ', 'MainCertController', 'getCertificateDashboardData', 'GET', 'service/mainCert/getCertificateDashboardData');
	fn_list('84') := fn('F02050208', 'ROLE_QS_ENQ', 'MainCertController', 'getLatestMainCert', 'GET', 'service/mainCert/getLatestMainCert');
	fn_list('85') := fn('F02050209', 'ROLE_QS_ENQ', 'MainCertController', 'getCumulativeRetentionReleaseByJob', 'GET', 'service/mainCert/getCumulativeRetentionReleaseByJob');
	fn_list('86') := fn('F02050210', 'ROLE_QS_ENQ', 'MainCertController', 'getMainCertReceiveDateAndAmount', 'POST', 'service/mainCert/getMainCertReceiveDateAndAmount');
	fn_list('87') := fn('F02050211', 'ROLE_QS_ENQ', 'MainCertController', 'getCalculatedRetentionRelease', 'POST', 'service/mainCert/getCalculatedRetentionRelease');
	fn_list('88') := fn('F02050301', 'ROLE_QS_QS', 'MainCertController', 'updateRetentionRelease', 'POST', 'service/mainCert/updateRetentionRelease');
	fn_list('89') := fn('F02050302', 'ROLE_QS_QS', 'MainCertController', 'updateCertificate', 'POST', 'service/mainCert/updateCertificate');
	fn_list('90') := fn('F02050303', 'ROLE_QS_QS', 'MainCertController', 'confirmIPC', 'POST', 'service/mainCert/confirmIPC');
	fn_list('91') := fn('F02050304', 'ROLE_QS_QS', 'MainCertController', 'resetIPC', 'POST', 'service/mainCert/resetIPC');
	fn_list('92') := fn('F02050305', 'ROLE_QS_QS', 'MainCertController', 'postIPC', 'POST', 'service/mainCert/postIPC');
	fn_list('93') := fn('F02050306', 'ROLE_QS_QS', 'MainCertController', 'updateMainCertContraChargeList', 'POST', 'service/mainCert/updateMainCertContraChargeList');
	fn_list('94') := fn('F02050307', 'ROLE_QS_QS', 'MainCertController', 'deleteMainCertContraCharge', 'POST', 'service/mainCert/deleteMainCertContraCharge');
	fn_list('95') := fn('F02050308', 'ROLE_QS_QS_ADM', 'MainCertController', 'updateMainCertFromF03B14Manually', 'POST', 'service/mainCert/updateMainCertFromF03B14Manually');
	fn_list('96') := fn('F02050309', 'ROLE_QS_QS_ADM', 'MainCertController', 'updateCertificateByAdmin', 'POST', 'service/mainCert/updateCertificateByAdmin');
	fn_list('97') := fn('F02050501', 'ROLE_QS_QS', 'MainCertController', 'submitNegativeMainCertForApproval', 'POST', 'service/mainCert/submitNegativeMainCertForApproval');
	fn_list('98') := fn('F02060101', 'ROLE_QS_QS', 'PaymentController', 'createPayment', 'POST', 'service/payment/createPayment');
	fn_list('99') := fn('F02060201', 'ROLE_QS_ENQ', 'PaymentController', 'getLatestPaymentCert', 'GET', 'service/payment/getLatestPaymentCert');
	fn_list('100') := fn('F02060202', 'ROLE_QS_ENQ', 'PaymentController', 'getPaymentCertList', 'GET', 'service/payment/getPaymentCertList');
	fn_list('101') := fn('F02060203', 'ROLE_QS_ENQ', 'PaymentController', 'getPaymentCert', 'GET', 'service/payment/getPaymentCert');
	fn_list('102') := fn('F02060204', 'ROLE_QS_ENQ', 'PaymentController', 'getPaymentDetailList', 'GET', 'service/payment/getPaymentDetailList');
	fn_list('103') := fn('F02060205', 'ROLE_QS_ENQ', 'PaymentController', 'getTotalPostedCertAmount', 'GET', 'service/payment/getTotalPostedCertAmount');
	fn_list('104') := fn('F02060206', 'ROLE_QS_ENQ', 'PaymentController', 'getSCPaymentCertSummary', 'GET', 'service/payment/getPaymentCertSummary');
	fn_list('105') := fn('F02060207', 'ROLE_QS_ENQ', 'PaymentController', 'getGSTAmount', 'GET', 'service/payment/getGSTAmount');
	fn_list('106') := fn('F02060208', 'ROLE_QS_ENQ', 'PaymentController', 'getPaymentResourceDistribution', 'GET', 'service/payment/getPaymentResourceDistribution');
	fn_list('107') := fn('F02060209', 'ROLE_QS_ENQ', 'PaymentController', 'obtainPaymentCertificateList', 'POST', 'service/payment/obtainPaymentCertificateList');
	fn_list('108') := fn('F02060210', 'ROLE_QS_ENQ', 'PaymentController', 'calculatePaymentDueDate', 'GET', 'service/payment/calculatePaymentDueDate');
	fn_list('109') := fn('F02060301', 'ROLE_QS_QS', 'PaymentController', 'updatePaymentCertificate', 'POST', 'service/payment/updatePaymentCertificate');
	fn_list('110') := fn('F02060302', 'ROLE_QS_QS', 'PaymentController', 'updatePaymentDetails', 'POST', 'service/payment/updatePaymentDetails');
	fn_list('111') := fn('F02060303', 'ROLE_QS_QS_ADM', 'PaymentController', 'updateF58011FromSCPaymentCertManually', 'POST', 'service/payment/updateF58011FromSCPaymentCertManually');
	fn_list('112') := fn('F02060304', 'ROLE_QS_QS_ADM', 'PaymentController', 'updatePaymentCert', 'POST', 'service/payment/updatePaymentCert');
	fn_list('113') := fn('F02060305', 'ROLE_QS_QS_ADM', 'PaymentController', 'runPaymentPosting', 'POST', 'service/payment/runPaymentPosting');
	fn_list('114') := fn('F02060501', 'ROLE_QS_QS', 'PaymentController', 'submitPayment', 'POST', 'service/payment/submitPayment');
	fn_list('115') := fn('F02070101', 'ROLE_QS_QS', 'RepackagingController', 'addRepackaging', 'POST', 'service/repackaging/addRepackaging');
	fn_list('116') := fn('F02070102', 'ROLE_QS_QS', 'RepackagingController', 'generateSnapshot', 'POST', 'service/repackaging/generateSnapshot');
	fn_list('117') := fn('F02070103', 'ROLE_QS_QS', 'RepackagingController', 'sendEmailToReviewer', 'POST', 'service/repackaging/sendEmailToReviewer');
	fn_list('118') := fn('F02070201', 'ROLE_QS_ENQ', 'RepackagingController', 'getRepackagingEntry', 'GET', 'service/repackaging/getRepackagingEntry');
	fn_list('119') := fn('F02070202', 'ROLE_QS_ENQ', 'RepackagingController', 'getRepackagingListByJobNo', 'GET', 'service/repackaging/getRepackagingListByJobNo');
	fn_list('120') := fn('F02070203', 'ROLE_QS_ENQ', 'RepackagingController', 'getLatestRepackaging', 'GET', 'service/repackaging/getLatestRepackaging');
	fn_list('121') := fn('F02070204', 'ROLE_QS_ENQ', 'RepackagingController', 'getRepackagingDetails', 'GET', 'service/repackaging/getRepackagingDetails');
	fn_list('122') := fn('F02070205', 'ROLE_QS_ENQ', 'RepackagingController', 'getReviewerList', 'GET', 'service/repackaging/getReviewerList');
	fn_list('123') := fn('F02070206', 'ROLE_QS_ENQ', 'RepackagingController', 'getReviewerListFromGSF', 'POST', 'service/repackaging/getReviewerListFromGSF');
	fn_list('124') := fn('F02070301', 'ROLE_QS_QS', 'RepackagingController', 'updateRepackaging', 'POST', 'service/repackaging/updateRepackaging');
	fn_list('125') := fn('F02070401', 'ROLE_QS_QS', 'RepackagingController', 'deleteRepackaging', 'DELETE', 'service/repackaging/deleteRepackaging');
	fn_list('126') := fn('F02070501', 'ROLE_QS_REVIEWER', 'RepackagingController', 'confirmAndPostRepackaingDetails', 'POST', 'service/repackaging/confirmAndPostRepackaingDetails');
	fn_list('127') := fn('F02080101', 'ROLE_QS_QS', 'ResourceSummaryController', 'addResourceSummary', 'POST', 'service/resourceSummary/addResourceSummary');
	fn_list('128') := fn('F02080102', 'ROLE_QS_QS', 'ResourceSummaryController', 'generateResourceSummaries', 'POST', 'service/resourceSummary/generateResourceSummaries');
	fn_list('129') := fn('F02080201', 'ROLE_QS_ENQ', 'ResourceSummaryController', 'getResourceSummaries', 'POST', 'service/resourceSummary/getResourceSummaries');
	fn_list('130') := fn('F02080202', 'ROLE_QS_ENQ', 'ResourceSummaryController', 'getResourceSummariesForAddendum', 'GET', 'service/resourceSummary/getResourceSummariesForAddendum');
	fn_list('131') := fn('F02080203', 'ROLE_QS_ENQ', 'ResourceSummaryController', 'getResourceSummariesBySC', 'GET', 'service/resourceSummary/getResourceSummariesBySC');
	fn_list('132') := fn('F02080204', 'ROLE_QS_ENQ', 'ResourceSummaryController', 'getResourceSummariesByAccountCode', 'GET', 'service/resourceSummary/getResourceSummariesByAccountCode');
	fn_list('133') := fn('F02080205', 'ROLE_QS_ENQ', 'ResourceSummaryController', 'getResourceSummariesByLineType', 'GET', 'service/resourceSummary/getResourceSummariesByLineType');
	fn_list('134') := fn('F02080206', 'ROLE_QS_ENQ', 'ResourceSummaryController', 'getResourceSummariesForIV', 'GET', 'service/resourceSummary/getResourceSummariesForIV');
	fn_list('135') := fn('F02080207', 'ROLE_QS_ENQ', 'ResourceSummaryController', 'getResourceSummariesGroupByObjectCode', 'GET', 'service/resourceSummary/getResourceSummariesGroupByObjectCode');
	fn_list('136') := fn('F02080208', 'ROLE_QS_ENQ', 'ResourceSummaryController', 'getUneditableResourceSummaryID', 'GET', 'service/resourceSummary/getUneditableResourceSummaryID');
	fn_list('137') := fn('F02080209', 'ROLE_QS_QS_ADM', 'ResourceSummaryController', 'obtainResourceSummariesByJobNumberForAdmin', 'GET', 'service/resourceSummary/obtainResourceSummariesByJobNumberForAdmin');
	fn_list('138') := fn('F02080301', 'ROLE_QS_QS', 'ResourceSummaryController', 'updateResourceSummaries', 'POST', 'service/resourceSummary/updateResourceSummaries');
	fn_list('139') := fn('F02080302', 'ROLE_QS_QS', 'ResourceSummaryController', 'splitOrMergeResources', 'POST', 'service/resourceSummary/splitOrMergeResources');
	fn_list('140') := fn('F02080303', 'ROLE_QS_QS', 'ResourceSummaryController', 'updateIVAmount', 'POST', 'service/resourceSummary/updateIVAmount');
	fn_list('141') := fn('F02080304', 'ROLE_QS_QS', 'ResourceSummaryController', 'postIVAmounts', 'POST', 'service/resourceSummary/postIVAmounts');
	fn_list('142') := fn('F02080305', 'ROLE_QS_QS', 'ResourceSummaryController', 'updateIVForSubcontract', 'POST', 'service/resourceSummary/updateIVForSubcontract');
	fn_list('143') := fn('F02080306', 'ROLE_QS_QS_ADM', 'ResourceSummaryController', 'updateResourceSummariesForAdmin', 'POST', 'service/resourceSummary/updateResourceSummariesForAdmin');
	fn_list('144') := fn('F02080401', 'ROLE_QS_QS', 'ResourceSummaryController', 'deleteResources', 'POST', 'service/resourceSummary/deleteResources');
	fn_list('145') := fn('F02090101', 'ROLE_QS_QS', 'SubcontractController', 'addAddendumToSubcontractDetail', 'POST', 'service/subcontract/addAddendumToSubcontractDetail');
	fn_list('146') := fn('F02090102', 'ROLE_QS_QS_ADM', 'SubcontractController', 'createSystemConstant', 'POST', 'service/subcontract/createSystemConstant');
	fn_list('147') := fn('F02090103', 'ROLE_QS_QS', 'SubcontractController', 'generateSCDetailsForPaymentRequisition', 'POST', 'service/subcontract/generateSCDetailsForPaymentRequisition');
	fn_list('148') := fn('F02090104', 'ROLE_QS_QS_ADM', 'SubcontractController', 'generateSCPackageSnapshotManually', 'POST', 'service/subcontract/generateSCPackageSnapshotManually');
	fn_list('149') := fn('F02090201', 'ROLE_QS_ENQ', 'SubcontractController', 'getSubcontractDetailByID', 'GET', 'service/subcontract/getSubcontractDetailByID');
	fn_list('150') := fn('F02090202', 'ROLE_QS_ENQ', 'SubcontractController', 'getSubcontractList', 'GET', 'service/subcontract/getSubcontractList');
	fn_list('151') := fn('F02090203', 'ROLE_QS_ENQ', 'SubcontractController', 'getSubcontractSnapshotList', 'GET', 'service/subcontract/getSubcontractSnapshotList');
	fn_list('152') := fn('F02090204', 'ROLE_QS_ENQ', 'SubcontractController', 'getWorkScope', 'GET', 'service/subcontract/getWorkScope');
	fn_list('153') := fn('F02090205', 'ROLE_QS_ENQ', 'SubcontractController', 'getSubcontract', 'GET', 'service/subcontract/getSubcontract');
	fn_list('154') := fn('F02090206', 'ROLE_QS_ENQ', 'SubcontractController', 'getSCDetails', 'GET', 'service/subcontract/getSCDetails');
	fn_list('155') := fn('F02090207', 'ROLE_QS_ENQ', 'SubcontractController', 'getSubcontractDetailForWD', 'GET', 'service/subcontract/getSubcontractDetailForWD');
	fn_list('156') := fn('F02090208', 'ROLE_QS_ENQ', 'SubcontractController', 'getOtherSubcontractDetails', 'GET', 'service/subcontract/getOtherSubcontractDetails');
	fn_list('157') := fn('F02090209', 'ROLE_QS_ENQ', 'SubcontractController', 'getSubcontractDetailsWithBudget', 'GET', 'service/subcontract/getSubcontractDetailsWithBudget');
	fn_list('158') := fn('F02090210', 'ROLE_QS_ENQ', 'SubcontractController', 'getSCDetailForAddendumUpdate', 'GET', 'service/subcontract/getSCDetailForAddendumUpdate');
	fn_list('159') := fn('F02090211', 'ROLE_QS_ENQ', 'SubcontractController', 'getSCDetailList', 'GET', 'service/subcontract/getSCDetailList');
	fn_list('160') := fn('F02090212', 'ROLE_QS_ENQ', 'SubcontractController', 'getSubcontractDashboardData', 'GET', 'service/subcontract/getSubcontractDashboardData');
	fn_list('161') := fn('F02090213', 'ROLE_QS_ENQ', 'SubcontractController', 'getSubcontractDetailsDashboardData', 'GET', 'service/subcontract/getSubcontractDetailsDashboardData');
	fn_list('162') := fn('F02090214', 'ROLE_QS_ENQ', 'SubcontractController', 'getAwardedSubcontractNos', 'GET', 'service/subcontract/getAwardedSubcontractNos');
	fn_list('163') := fn('F02090215', 'ROLE_QS_ENQ', 'SubcontractController', 'getUnawardedSubcontractNosUnderPaymentRequisition', 'GET', 'service/subcontract/getUnawardedSubcontractNosUnderPaymentRequisition');
	fn_list('164') := fn('F02090216', 'ROLE_QS_ENQ', 'SubcontractController', 'getDefaultValuesForSubcontractDetails', 'GET', 'service/subcontract/getDefaultValuesForSubcontractDetails');
	fn_list('165') := fn('F02090217', 'ROLE_QS_ENQ', 'SubcontractController', 'getSubcontractDetailTotalNewAmount', 'GET', 'service/subcontract/getSubcontractDetailTotalNewAmount');
	fn_list('166') := fn('F02090218', 'ROLE_QS_ENQ', 'SubcontractController', 'getFinalizedSubcontractNos', 'GET', 'service/subcontract/getFinalizedSubcontractNos');
	fn_list('167') := fn('F02090219', 'ROLE_QS_ENQ', 'SubcontractController', 'getCompanyBaseCurrency', 'GET', 'service/subcontract/getCompanyBaseCurrency');
	fn_list('168') := fn('F02090220', 'ROLE_QS_ENQ', 'SubcontractController', 'searchSystemConstants', 'GET', 'service/subcontract/searchSystemConstants');
	fn_list('169') := fn('F02090221', 'ROLE_QS_ENQ', 'SubcontractController', 'getPerforamceAppraisalsList', 'POST', 'service/subcontract/getPerforamceAppraisalsList');
	fn_list('170') := fn('F02090222', 'ROLE_QS_ENQ', 'SubcontractController', 'getProvisionPostingHistList', 'GET', 'service/subcontract/getProvisionPostingHistList');
	fn_list('171') := fn('F02090301', 'ROLE_QS_QS', 'SubcontractController', 'updateSubcontractDetailAddendum', 'POST', 'service/subcontract/updateSubcontractDetailAddendum');
	fn_list('172') := fn('F02090302', 'ROLE_QS_QS', 'SubcontractController', 'upateSubcontract', 'POST', 'service/subcontract/upateSubcontract');
	fn_list('173') := fn('F02090303', 'ROLE_QS_QS', 'SubcontractController', 'updateWDandIV', 'POST', 'service/subcontract/updateWDandIV');
	fn_list('174') := fn('F02090304', 'ROLE_QS_QS', 'SubcontractController', 'updateWDandIVList', 'POST', 'service/subcontract/updateWDandIVList');
	fn_list('175') := fn('F02090305', 'ROLE_QS_QS', 'SubcontractController', 'updateFilteredWDandIVByPercent', 'POST', 'service/subcontract/updateFilteredWDandIVByPercent');
	fn_list('176') := fn('F02090306', 'ROLE_QS_QS', 'SubcontractController', 'upateSubcontractDates', 'POST', 'service/subcontract/upateSubcontractDates');
	fn_list('177') := fn('F02090307', 'ROLE_QS_QS', 'SubcontractController', 'recalculateResourceSummaryIV', 'POST', 'service/subcontract/recalculateResourceSummaryIV');
	fn_list('178') := fn('F02090308', 'ROLE_QS_QS', 'SubcontractController', 'calculateTotalWDandCertAmount', 'POST', 'service/subcontract/calculateTotalWDandCertAmount');
	fn_list('179') := fn('F02090309', 'ROLE_QS_QS', 'SubcontractController', 'updateSCDetailsNewQuantity', 'POST', 'service/subcontract/updateSCDetailsNewQuantity');
	fn_list('180') := fn('F02090310', 'ROLE_QS_QS_ADM', 'SubcontractController', 'runProvisionPostingManually', 'POST', 'service/subcontract/runProvisionPostingManually');
	fn_list('181') := fn('F02090311', 'ROLE_QS_QS_ADM', 'SubcontractController', 'updateF58001FromSCPackageManually', 'POST', 'service/subcontract/updateF58001FromSCPackageManually');
	fn_list('182') := fn('F02090312', 'ROLE_QS_QS_ADM', 'SubcontractController', 'updateMultipleSystemConstants', 'POST', 'service/subcontract/updateMultipleSystemConstants');
	fn_list('183') := fn('F02090313', 'ROLE_QS_QS_ADM', 'SubcontractController', 'inactivateSystemConstant', 'POST', 'service/subcontract/inactivateSystemConstant');
	fn_list('184') := fn('F02090314', 'ROLE_QS_QS_ADM', 'SubcontractController', 'updateSubcontractAdmin', 'POST', 'service/subcontract/updateSubcontractAdmin');
	fn_list('185') := fn('F02090401', 'ROLE_QS_QS', 'SubcontractController', 'deleteSubcontractDetailAddendum', 'POST', 'service/subcontract/deleteSubcontractDetailAddendum');
	fn_list('186') := fn('F02090501', 'ROLE_QS_QS', 'SubcontractController', 'submitAwardApproval', 'POST', 'service/subcontract/submitAwardApproval');
	fn_list('187') := fn('F02090502', 'ROLE_QS_QS', 'SubcontractController', 'submitSplitTerminateSC', 'POST', 'service/subcontract/submitSplitTerminateSC');
	fn_list('188') := fn('F02100101', 'ROLE_QS_QS', 'TenderController', 'createTender', 'POST', 'service/tender/createTender');
	fn_list('189') := fn('F02100102', 'ROLE_QS_QS', 'TenderController', 'createTenderVariance', 'POST', 'service/tender/createTenderVariance');
	fn_list('190') := fn('F02100201', 'ROLE_QS_ENQ', 'TenderController', 'getTenderDetailList', 'GET', 'service/tender/getTenderDetailList');
	fn_list('191') := fn('F02100202', 'ROLE_QS_ENQ', 'TenderController', 'getTender', 'GET', 'service/tender/getTender');
	fn_list('192') := fn('F02100203', 'ROLE_QS_ENQ', 'TenderController', 'getRecommendedTender', 'GET', 'service/tender/getRecommendedTender');
	fn_list('193') := fn('F02100204', 'ROLE_QS_ENQ', 'TenderController', 'getTenderList', 'GET', 'service/tender/getTenderList');
	fn_list('194') := fn('F02100205', 'ROLE_QS_ENQ', 'TenderController', 'getTenderComparisonList', 'GET', 'service/tender/getTenderComparisonList');
	fn_list('195') := fn('F02100206', 'ROLE_QS_ENQ', 'TenderController', 'getUneditableTADetailIDs', 'GET', 'service/tender/getUneditableTADetailIDs');
	fn_list('196') := fn('F02100207', 'ROLE_QS_ENQ', 'TenderController', 'getTenderVarianceList', 'GET', 'service/tender/getTenderVarianceList');
	fn_list('197') := fn('F02100301', 'ROLE_QS_QS', 'TenderController', 'updateRecommendedTender', 'POST', 'service/tender/updateRecommendedTender');
	fn_list('198') := fn('F02100302', 'ROLE_QS_QS', 'TenderController', 'updateTenderDetails', 'POST', 'service/tender/updateTenderDetails');
	fn_list('199') := fn('F02100401', 'ROLE_QS_QS', 'TenderController', 'deleteTender', 'POST', 'service/tender/deleteTender');
	fn_list('200') := fn('F02110101', 'ROLE_QS_QS', 'TransitController', 'confirmResourcesAndCreatePackages', 'POST', 'service/transit/confirmResourcesAndCreatePackages');
	fn_list('201') := fn('F02110102', 'ROLE_QS_QS', 'TransitController', 'saveTransitResourcesList', 'POST', 'service/transit/saveTransitResourcesList');
	fn_list('202') := fn('F02110103', 'ROLE_QS_QS', 'TransitController', 'saveTransitResources', 'POST', 'service/transit/saveTransitResources');
	fn_list('203') := fn('F02110104', 'ROLE_QS_QS', 'TransitController', 'createOrUpdateTransitHeader', 'POST', 'service/transit/createOrUpdateTransitHeader');
	fn_list('204') := fn('F02110201', 'ROLE_QS_ENQ', 'TransitController', 'getIncompleteTransitList', 'GET', 'service/transit/getIncompleteTransitList');
	fn_list('205') := fn('F02110202', 'ROLE_QS_ENQ', 'TransitController', 'obtainTransitCodeMatcheList', 'GET', 'service/transit/obtainTransitCodeMatcheList');
	fn_list('206') := fn('F02110203', 'ROLE_QS_ENQ', 'TransitController', 'obtainTransitUomMatcheList', 'GET', 'service/transit/obtainTransitUomMatcheList');
	fn_list('207') := fn('F02110204', 'ROLE_QS_ENQ', 'TransitController', 'getTransit', 'GET', 'service/transit/getTransit');
	fn_list('208') := fn('F02110205', 'ROLE_QS_ENQ', 'TransitController', 'getTransitBQItems', 'GET', 'service/transit/getTransitBQItems');
	fn_list('209') := fn('F02110206', 'ROLE_QS_ENQ', 'TransitController', 'getTransitResources', 'GET', 'service/transit/getTransitResources');
	fn_list('210') := fn('F02110301', 'ROLE_QS_QS', 'TransitController', 'completeTransit', 'POST', 'service/transit/completeTransit');
	fn_list('211') := fn('F02120201', 'ROLE_QS_ENQ', 'UnitController', 'getAllWorkScopes', 'GET', 'service/unit/getAllWorkScopes');
	fn_list('212') := fn('F02120202', 'ROLE_QS_ENQ', 'UnitController', 'getUnitOfMeasurementList', 'GET', 'service/unit/getUnitOfMeasurementList');
	fn_list('213') := fn('F02120203', 'ROLE_QS_ENQ', 'UnitController', 'getAppraisalPerformanceGroupMap', 'GET', 'service/unit/getAppraisalPerformanceGroupMap');
	fn_list('214') := fn('F02120204', 'ROLE_QS_ENQ', 'UnitController', 'getSCStatusCodeMap', 'GET', 'service/unit/getSCStatusCodeMap');
	fn_list('215') := fn('F02130101', 'ROLE_QS_QS', 'JdeController', 'createAccountMasterByGroup', 'POST', 'service/jde/createAccountMasterByGroup');
	fn_list('216') := fn('F02130201', 'ROLE_QS_ENQ', 'JdeController', 'getPORecordList', 'POST', 'service/jde/getPORecordList');
	fn_list('217') := fn('F02130202', 'ROLE_QS_ENQ', 'JdeController', 'getARRecordList', 'POST', 'service/jde/getARRecordList');
	fn_list('218') := fn('F02130203', 'ROLE_QS_ENQ', 'JdeController', 'obtainAPRecordList', 'POST', 'service/jde/obtainAPRecordList');
	fn_list('219') := fn('F02130204', 'ROLE_QS_ENQ', 'JdeController', 'getAPPaymentHistories', 'POST', 'service/jde/getAPPaymentHistories');
	fn_list('220') := fn('F02130205', 'ROLE_QS_ENQ', 'JdeController', 'getSubcontractorWorkScope', 'POST', 'service/jde/getSubcontractorWorkScope');
	fn_list('221') := fn('F02130206', 'ROLE_QS_ENQ', 'JdeController', 'searchVendorAddressDetails', 'POST', 'service/jde/searchVendorAddressDetails');
	fn_list('222') := fn('F02130207', 'ROLE_QS_ENQ', 'JdeController', 'getSubcontractor', 'GET', 'service/jde/getSubcontractor');
	fn_list('223') := fn('F02130208', 'ROLE_QS_ENQ', 'JdeController', 'getSubcontractorList', 'GET', 'service/jde/getSubcontractorList');
	fn_list('224') := fn('F02130209', 'ROLE_QS_ENQ', 'JdeController', 'searchObjectList', 'POST', 'service/jde/searchObjectList');
	fn_list('225') := fn('F02130210', 'ROLE_QS_ENQ', 'JdeController', 'searchSubsidiaryList', 'POST', 'service/jde/searchSubsidiaryList');
	fn_list('226') := fn('F02130211', 'ROLE_QS_QS', 'JdeController', 'validateAndCreateAccountCode', 'POST', 'service/jde/validateAndCreateAccountCode');
	fn_list('227') := fn('F02130501', 'ROLE_QS_QS', 'JdeController', 'postBudget', 'POST', 'service/jde/postBudget');
	fn_list('228') := fn('F02140201', 'ROLE_QS_ENQ', 'SystemController', 'GetSessionList', 'POST', 'service/system/GetSessionList');
	fn_list('229') := fn('F02140202', 'ROLE_QS_ENQ', 'SystemController', 'getCurrentSessionId', 'POST', 'service/system/GetCurrentSessionId');
	fn_list('230') := fn('F02140203', 'ROLE_QS_IMS_ADM', 'SystemController', 'findEntityByIdRevision', 'POST', 'service/system/findEntityByIdRevision');
	fn_list('231') := fn('F02140204', 'ROLE_QS_IMS_ADM', 'SystemController', 'findRevisionsByEntity', 'POST', 'service/system/findRevisionsByEntity');
	fn_list('232') := fn('F02140205', 'ROLE_QS_IMS_ADM', 'SystemController', 'findByRevision', 'POST', 'service/system/findByRevision');
	fn_list('233') := fn('F02140206', 'ROLE_QS_IMS_ADM', 'SystemController', 'getSystemProperties', 'POST', 'service/system/getSystemProperties');
	fn_list('234') := fn('F02140207', 'ROLE_QS_IMS_ENQ', 'SystemController', 'getAuditTableMap', 'POST', 'service/system/getAuditTableMap');
	fn_list('235') := fn('F02140208', 'ROLE_QS_ENQ', 'SystemController', 'obtainCacheKey', 'POST', 'service/system/obtainCacheKey');
	fn_list('236') := fn('F02140209', 'ROLE_QS_ENQ', 'SystemController', 'getProperties', 'POST', 'service/system/getProperties');
	fn_list('237') := fn('F02140210', 'ROLE_QS_IMS_ENQ', 'SystemController', 'getAllTriggers', 'POST', 'service/system/getAllTriggers');
	fn_list('238') := fn('F02140211', 'ROLE_QS_ENQ', 'SystemController', 'logToBackend', 'POST', 'service/system/logToBackend');
	fn_list('239') := fn('F02140301', 'ROLE_QS_IMS_ADM', 'SystemController', 'testModifySubcontractAndDetail', 'POST', 'service/system/testModifySubcontractAndDetail');
	fn_list('240') := fn('F02140302', 'ROLE_QS_IMS_ADM', 'SystemController', 'testModifyPaymentCertAndDetail', 'POST', 'service/system/testModifyPaymentCertAndDetail');
	fn_list('241') := fn('F02140303', 'ROLE_QS_IMS_ADM', 'SystemController', 'updateQrtzTriggerList', 'POST', 'service/system/updateQrtzTriggerList');
	fn_list('242') := fn('F02140304', 'ROLE_QS_IMS_ADM', 'SystemController', 'invalidateSessionList', 'POST', 'service/system/InvalidateSessionList');
	fn_list('243') := fn('F02140401', 'ROLE_QS_QS_ADM', 'SystemController', 'housekeepAuditTable', 'POST', 'service/system/housekeepAuditTable');
	fn_list('244') := fn('F02150201', 'ROLE_QS_ENQ', 'SecurityController', 'getCurrentUser', 'GET', 'service/security/getCurrentUser');
	fn_list('245') := fn('F02150202', 'ROLE_QS_ENQ', 'SecurityController', 'getUserByUsername', 'GET', 'service/security/getUserByUsername');
	fn_list('246') := fn('F02160201', 'ROLE_QS_ENQ', 'UserPreferenceController', 'obtainUserPreferenceByCurrentUser', 'POST', 'service/userPreference/obtainUserPreferenceByCurrentUser');
	fn_list('247') := fn('F02160301', 'ROLE_QS_ENQ', 'UserPreferenceController', 'setDefaultJobNo', 'POST', 'service/userPreference/setDefaultJobNo');
	fn_list('248') := fn('F02170201', 'ROLE_QS_ENQ', 'IVPostingHistController', 'obtainIVPostingHistoryList', 'POST', 'service/ivpostinghist/obtainIVPostingHistoryList');
	fn_list('249') := fn('F02180201', 'ROLE_QS_ENQ', 'SubcontractorController', 'obtainSubcontractorWrappers', 'POST', 'service/subcontractor/obtainSubcontractorWrappers');
	fn_list('250') := fn('F02180202', 'ROLE_QS_ENQ', 'SubcontractorController', 'obtainClientWrappers', 'POST', 'service/subcontractor/obtainClientWrappers');
	fn_list('251') := fn('F02180203', 'ROLE_QS_ENQ', 'SubcontractorController', 'obtainSubconctractorStatistics', 'POST', 'service/subcontractor/obtainSubconctractorStatistics');
	fn_list('252') := fn('F02180204', 'ROLE_QS_ENQ', 'SubcontractorController', 'obtainPackagesByVendorNo', 'POST', 'service/subcontractor/obtainPackagesByVendorNo');
	fn_list('253') := fn('F02180205', 'ROLE_QS_ENQ', 'SubcontractorController', 'obtainTenderAnalysisWrapperByVendorNo', 'POST', 'service/subcontractor/obtainTenderAnalysisWrapperByVendorNo');
  	fn_list('254') := fn('F02070207', 'ROLE_QS_ENQ', 'RepackagingController', 'getRepackaging', 'GET', 'service/repackaging/getRepackaging');

	IF(EXEC_INSERT_ACCESSRIGHT = 1) THEN 
		execute immediate SQL_INSERT_ACCESSRIGHT_DISABLE;
	END IF;
  
  if(EXEC_DELETE_FNSEC = 1) THEN
    execute immediate SQL_DELETE_FN;
    IF(SQL%ROWCOUNT>0) THEN
      count_delete_fn:=count_delete_fn + SQL%ROWCOUNT;
    END IF;
    
    execute immediate SQL_DELETE_SEC;
    IF(SQL%ROWCOUNT>0) THEN
      count_delete_sec:=count_delete_sec + SQL%ROWCOUNT;
    END IF;
  END IF;
  
	fn_id := fn_list.FIRST;
	WHILE fn_id IS NOT NULL LOOP

		SQL_INSERT_FN := 
						'insert into ' || ACDATA_SCHEMA || '.AC_FUNCTION
						(
						APPLICATIONCODE, 
						FUNCTIONNAME,
						FUNCTIONDESCRIPTION,
						FUNCTIONTYPE,
						LASTMODIFYDATE,
						LASTMODIFYUSER
						)
						values 
						(
						''QS'',
						''' || fn_list(fn_id)(1) || ''',
						''' || fn_list(fn_id)(2) || ' | ' || fn_list(fn_id)(5) || ' | ' || fn_list(fn_id)(6) || ''',
						''FUNCTION'',
						sysdate,
						''SYSTEM''
						)';
	IF(DEBUGON = 1) THEN DBMS_Output.PUT_LINE(SQL_INSERT_FN); END IF;
    IF(EXEC_INSERT_FNSEC = 1) THEN 
      execute immediate SQL_INSERT_FN;
      IF(SQL%ROWCOUNT>0) THEN
        count_insert_fn:=count_insert_fn + SQL%ROWCOUNT;
      END IF;
    END IF;  
    
		SQL_INSERT_SEC :=
						'insert into ' || ACDATA_SCHEMA || '.AC_FUNCTIONSECURITY
						(
						APPLICATIONCODE,
						ROLENAME,
						FUNCTIONNAME,
						ACCESSRIGHT,
						STATUS,
						LASTMODIFYDATE,
						LASTMODIFYUSER
						)
						values
						(
						''QS'',
						''' || fn_list(fn_id)(2) || ''',
						''' || fn_list(fn_id)(1) || ''',
						''' || access_right || ''',
						''A'',
						sysdate,
						''SYSTEM''
						)';
    IF(DEBUGON = 1) THEN DBMS_Output.PUT_LINE(SQL_INSERT_SEC); END IF;
    IF(EXEC_INSERT_FNSEC = 1) THEN 
      execute immediate SQL_INSERT_SEC;
      IF(SQL%ROWCOUNT>0) THEN
        count_insert_sec:=count_insert_sec + SQL%ROWCOUNT;
      END IF;
    END IF;  
    
		fn_id:= fn_list.NEXT(fn_id);
	END LOOP;
  DBMS_Output.PUT_LINE('delete_fn:' || count_delete_fn);
  DBMS_Output.PUT_LINE('delete_sec:' || count_delete_sec);  
  DBMS_Output.PUT_LINE('insert_fn:' || count_insert_fn);
  DBMS_Output.PUT_LINE('insert_sec:' || count_insert_sec);
END;
/