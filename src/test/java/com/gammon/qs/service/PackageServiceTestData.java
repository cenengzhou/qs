/**
 * 
 */
package com.gammon.qs.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import com.gammon.junit.testcase.TransactionTestCase;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.BQResourceSummary;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.wrapper.addAddendum.AddAddendumWrapper;

/**
 * @author paulyiu
 *
 */
@Configuration
public class PackageServiceTestData extends TransactionTestCase.TestDataBase {
	
	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#PackageService()}.
	 */
	public TransactionTestCase.TestDataMethod testPackageService() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getPackages(java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testGetPackages() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getCachedSCDetailsList()}.
	 */
	public TransactionTestCase.TestDataMethod testGetCachedSCDetailsList() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#setCachedSCDetailsList(java.util.List)}.
	 */
	public TransactionTestCase.TestDataMethod testSetCachedSCDetailsList() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getCachedWorkScopeList()}.
	 */
	public TransactionTestCase.TestDataMethod testGetCachedWorkScopeList() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#setCachedWorkScopeList(java.util.List)}.
	 */
	public TransactionTestCase.TestDataMethod testSetCachedWorkScopeList() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getSCListView(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testGetSCListView() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainAwardedPackage(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainAwardedPackage() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainNotAwardedPackage(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainNotAwardedPackage() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainSubcontractList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.lang.Integer)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainSubcontractListStringStringStringStringStringStringStringStringStringStringListOfStringInteger() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getSCDetailsOfPackageForExcel(java.lang.String, java.lang.String, java.lang.String, boolean)}.
	 */
	public TransactionTestCase.TestDataMethod testGetSCDetailsOfPackageForExcel() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getSCDetailListAtPageForExcel(int)}.
	 */
	public TransactionTestCase.TestDataMethod testGetSCDetailListAtPageForExcel() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getSCDetailsOfPackage(java.lang.String, java.lang.String, java.lang.String, boolean)}.
	 */
	public TransactionTestCase.TestDataMethod testGetSCDetailsOfPackage() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainSCDetailListAtPage(int)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainSCDetailListAtPage() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#updateWDandCertQuantity(java.util.List)}.
	 */
	public TransactionTestCase.TestDataMethod testUpdateWDandCertQuantity() {
		return new TransactionTestCase.TestDataMethod() {

			private PackageService service = applicationContext.getBean(PackageService.class);
			@SuppressWarnings("unused")
			private SCDetails testCaseObject;

			private String jobNumber = "13389";
			private String packageNo = "2010";
			private String billItem = null;
			private String description = null;
			private String lineType = null;
			private int pageNum = 0;

			@Override
			public Object obtainObjectFromDB() {
				try {
					return service.getScDetailsByPage(jobNumber, packageNo, billItem, description, lineType, pageNum)
							.getCurrentPageContentList().get(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
//				testCaseObject = (SCDetails) obtainObjectFromDB();
//				testCaseObject.setCumCertifiedQuantity(148.0);
//				UpdateSCPackageSaveWrapper testWrapper = UpdateSCPackageSaveWrapper
//						.updateWDandCertQtyWindowRecordToWrapper(testCaseObject, 0.0,
//								UpdateSCPackageSaveWrapper.CUM_CERTIFIED_QTY, SecurityContextHolder.getContext().getAuthentication().getName());
//				List<UpdateSCPackageSaveWrapper> pendingSaveWrapperList = new ArrayList<UpdateSCPackageSaveWrapper>();
//				pendingSaveWrapperList.add(testWrapper);
//
//				service.updateWDandCertQuantity(pendingSaveWrapperList);
			}
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#triggerUpdateSCPaymentDetail(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testTriggerUpdateSCPaymentDetail() {
		return new TransactionTestCase.TestDataMethod() {
			private PackageService packageService = applicationContext.getBean(PackageService.class);
			private PaymentService paymentService = applicationContext.getBean(PaymentService.class);
			private List<SCPaymentCert> scPaymentCertList = null;
			
			private String jobNumber = "13389"; 
			private String packageNo = "2010";
			private String ifPayment = null;
			private String createUser = "paulnpyiu"; 
			private String directPaymentIndicator = "N";

			@Override
			public Object obtainObjectFromDB() {
				try {
					scPaymentCertList = paymentService.obtainSCPaymentCertListByPackageNo(jobNumber, Integer.valueOf(packageNo));

				} catch (DatabaseOperationException e) {
					e.printStackTrace();
				}
				return scPaymentCertList;
			}

			@SuppressWarnings("unchecked")
			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
				List<SCPaymentCert> tempObjList = (List<SCPaymentCert>) obtainObjectFromDB();
				for(SCPaymentCert tempObj: tempObjList){
					data.add(tempObj.getId());
				}
				return data;
			}

			@Override
			public void executeTestMethod() {
				try {
					packageService.triggerUpdateSCPaymentDetail(jobNumber, packageNo, ifPayment, createUser, directPaymentIndicator);
				} catch (DatabaseOperationException e) {
					e.printStackTrace();
				}
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getScPaymentCertHBDao()}.
	 */
	public TransactionTestCase.TestDataMethod testGetScPaymentCertHBDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#setScPaymentCertHBDao(com.gammon.qs.dao.SCPaymentCertHBDao)}.
	 */
	public TransactionTestCase.TestDataMethod testSetScPaymentCertHBDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getScPaymentDetailHBDao()}.
	 */
	public TransactionTestCase.TestDataMethod testGetScPaymentDetailHBDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#setScPaymentDetailHBDao(com.gammon.qs.dao.SCPaymentDetailHBDao)}.
	 */
	public TransactionTestCase.TestDataMethod testSetScPaymentDetailHBDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getSCDetailsExcelFileByJobPackageNoPackageType(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testGetSCDetailsExcelFileByJobPackageNoPackageType() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#addAddendumByWrapperListStr(java.util.List)}.
	 */
	public TransactionTestCase.TestDataMethod testAddAddendumByWrapperListStr() {
		return new TransactionTestCase.TestDataMethod() {
			private BQResourceSummaryService bqResourceSummaryService = applicationContext.getBean(BQResourceSummaryService.class);
			private JobService jobService = applicationContext.getBean(JobService.class);
			@SuppressWarnings("unused")
			private List<AddAddendumWrapper> wrapperList;
			
			private String jobNumber = "13389";
			private String packageNo = "2010";
			private String objectCode = "140299";
			private String subsidiaryCode = "29999999";
			private String description = null;
			private String type = null;
			private String levyExcluded = "";
			private String defectExcluded = "";
			@SuppressWarnings("unused")
			private List<BQResourceSummary> bqResourceSummaries;
			
			@Override
			public Object obtainObjectFromDB() {
				try{
					Job job = jobService.obtainJob(jobNumber);
					bqResourceSummaries = bqResourceSummaryService.obtainResourceSummariesSearchByPage(job, 
							packageNo, objectCode, subsidiaryCode, description, type, levyExcluded, defectExcluded, 0)
							.getCurrentPageContentList();
					
				}catch(Exception e){
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#addAddendumByWrapperStrFromRepackaging(com.gammon.qs.wrapper.addAddendum.AddAddendumWrapper, java.util.List)}.
	 */
	public TransactionTestCase.TestDataMethod testAddAddendumByWrapperStrFromRepackaging() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#addAddendumByWrapperStr(com.gammon.qs.wrapper.addAddendum.AddAddendumWrapper)}.
	 */
	public TransactionTestCase.TestDataMethod testAddAddendumByWrapperStr() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getAddendumEnquiry(java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testGetAddendumEnquiry() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getSCLine(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testGetSCLine() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#updateAddendumByWrapper(com.gammon.qs.wrapper.updateAddendum.UpdateAddendumWrapper)}.
	 */
	public TransactionTestCase.TestDataMethod testUpdateAddendumByWrapper() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#submitAddendumApproval(java.lang.String, java.lang.Integer, java.lang.Double, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testSubmitAddendumApproval() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#defaultValuesForAddingSCDetailLines(java.lang.String, java.lang.Integer, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testDefaultValuesForAddingSCDetailLines() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#uploadSCDetailByExcel(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, byte[])}.
	 */
	public TransactionTestCase.TestDataMethod testUploadSCDetailByExcel() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getScDetailsByPage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)}.
	 */
	public TransactionTestCase.TestDataMethod testGetScDetailsByPage() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainSCDetails(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainSCDetails() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainSubcontractList(com.gammon.qs.wrapper.finance.SubcontractListWrapper)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainSubcontractListSubcontractListWrapper() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainSubcontractListPaginationWrapper(com.gammon.qs.wrapper.finance.SubcontractListWrapper)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainSubcontractListPaginationWrapper() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainSubcontractListByPage(int)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainSubcontractListByPage() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getPackageHBDao()}.
	 */
	public TransactionTestCase.TestDataMethod testGetPackageHBDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#setPackageHBDao(com.gammon.qs.dao.SCPackageHBDao)}.
	 */
	public TransactionTestCase.TestDataMethod testSetPackageHBDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#suspendAddendum(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testSuspendAddendum() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#setMasterListRepository(com.gammon.qs.service.MasterListService)}.
	 */
	public TransactionTestCase.TestDataMethod testSetMasterListRepository() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getMasterListRepository()}.
	 */
	public TransactionTestCase.TestDataMethod testGetMasterListRepository() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getUnitRepository()}.
	 */
	public TransactionTestCase.TestDataMethod testGetUnitRepository() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#setUnitRepository(com.gammon.qs.service.UnitService)}.
	 */
	public TransactionTestCase.TestDataMethod testSetUnitRepository() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getAccountCodeWSDao()}.
	 */
	public TransactionTestCase.TestDataMethod testGetAccountCodeWSDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#setAccountCodeWSDao(com.gammon.qs.dao.AccountCodeWSDao)}.
	 */
	public TransactionTestCase.TestDataMethod testSetAccountCodeWSDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#setJobCostDao(com.gammon.qs.dao.JobCostWSDao)}.
	 */
	public TransactionTestCase.TestDataMethod testSetJobCostDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getJobCostDao()}.
	 */
	public TransactionTestCase.TestDataMethod testGetJobCostDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#setTenderAnalysisHBDao(com.gammon.qs.dao.TenderAnalysisHBDao)}.
	 */
	public TransactionTestCase.TestDataMethod testSetTenderAnalysisHBDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getTenderAnalysisHBDao()}.
	 */
	public TransactionTestCase.TestDataMethod testGetTenderAnalysisHBDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getCreateGLDao()}.
	 */
	public TransactionTestCase.TestDataMethod testGetCreateGLDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#setCreateGLDao(com.gammon.qs.dao.CreateGLWSDao)}.
	 */
	public TransactionTestCase.TestDataMethod testSetCreateGLDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#deleteAddendum(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testDeleteAddendum() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#toCompleteAddendumApproval(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testToCompleteAddendumApproval() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#nonAwardedSCApproval(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Double, java.lang.Double, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testNonAwardedSCApproval() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#retrieveNonAwardedSCPackageList(java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testRetrieveNonAwardedSCPackageList() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getUnawardedSCPackages(com.gammon.qs.domain.Job)}.
	 */
	public TransactionTestCase.TestDataMethod testGetUnawardedSCPackages() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getUnawardedPackageNos(com.gammon.qs.domain.Job)}.
	 */
	public TransactionTestCase.TestDataMethod testGetUnawardedPackageNos() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getAwardedPackageStore(com.gammon.qs.domain.Job)}.
	 */
	public TransactionTestCase.TestDataMethod testGetAwardedPackageStore() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getUneditablePackageNos(com.gammon.qs.domain.Job)}.
	 */
	public TransactionTestCase.TestDataMethod testGetUneditablePackageNos() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainUneditableUnawardedPackageNos(com.gammon.qs.domain.Job)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainUneditableUnawardedPackageNos() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainUnawardedPackageNosUnderPaymentRequisition(com.gammon.qs.domain.Job)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainUnawardedPackageNosUnderPaymentRequisition() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getAwardedPackageNos(com.gammon.qs.domain.Job)}.
	 */
	public TransactionTestCase.TestDataMethod testGetAwardedPackageNos() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#retrieveTenderAnalysisList(java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testRetrieveTenderAnalysisList() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#toCompleteSCAwardApproval(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testToCompleteSCAwardApproval() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getPackagesFromList(com.gammon.qs.domain.Job, java.util.List)}.
	 */
	public TransactionTestCase.TestDataMethod testGetPackagesFromList() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getSCPackages(com.gammon.qs.domain.Job)}.
	 */
	public TransactionTestCase.TestDataMethod testGetSCPackages() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainSCPackage(com.gammon.qs.domain.Job, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainSCPackageJobString() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getSCPackageForPackagePanel(com.gammon.qs.domain.Job, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testGetSCPackageForPackagePanel() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#saveOrUpdateSCPackage(com.gammon.qs.domain.SCPackage)}.
	 */
	public TransactionTestCase.TestDataMethod testSaveOrUpdateSCPackage() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#setBqResourceSummaryDao(com.gammon.qs.dao.BQResourceSummaryHBDao)}.
	 */
	public TransactionTestCase.TestDataMethod testSetBqResourceSummaryDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getBqResourceSummaryDao()}.
	 */
	public TransactionTestCase.TestDataMethod testGetBqResourceSummaryDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#updateNewQuantity(java.lang.Long, java.lang.Double)}.
	 */
	public TransactionTestCase.TestDataMethod testUpdateNewQuantity() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#submitSplitTerminateSC(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testSubmitSplitTerminateSC() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#recalculateResourceSummaryIV(com.gammon.qs.domain.Job, java.lang.String, boolean)}.
	 */
	public TransactionTestCase.TestDataMethod testRecalculateResourceSummaryIV() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#recalculateResourceSummaryIVbyJob(com.gammon.qs.domain.Job)}.
	 */
	public TransactionTestCase.TestDataMethod testRecalculateResourceSummaryIVbyJob() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#submitAwardApproval(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testSubmitAwardApproval() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getSystemConstantHBDaoImpl()}.
	 */
	public TransactionTestCase.TestDataMethod testGetSystemConstantHBDaoImpl() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#toCompleteSplitTerminate(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testToCompleteSplitTerminate() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainSystemConstant(java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainSystemConstant() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#searchSystemConstants(java.lang.String, java.lang.String, java.lang.String, java.lang.Double, java.lang.Double, java.lang.Double, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testSearchSystemConstants() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#inactivateSystemConstant(com.gammon.qs.domain.SystemConstant, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testInactivateSystemConstant() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#updateMultipleSystemConstants(java.util.List, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testUpdateMultipleSystemConstants() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#createSystemConstant(com.gammon.qs.domain.SystemConstant, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testCreateSystemConstant() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainWorkScopeList()}.
	 */
	public TransactionTestCase.TestDataMethod testObtainWorkScopeList() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainWorkScopeList(java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainWorkScopeListString() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#downloadWorkScopeExcelFile(java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testDownloadWorkScopeExcelFile() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainWorkScope(java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainWorkScope() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#currencyCodeValidation(java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testCurrencyCodeValidation() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#runProvisionPostingManually(java.lang.String, java.util.Date, java.lang.Boolean, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testRunProvisionPostingManually() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#calculateTotalWDandCertAmount(java.lang.String, java.lang.String, boolean)}.
	 */
	public TransactionTestCase.TestDataMethod testCalculateTotalWDandCertAmount() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#recalculateTotalWDAmount(java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testRecalculateTotalWDAmount() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#downloadSubcontractEnquiryReportExcelFile(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Boolean, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testDownloadSubcontractEnquiryReportExcelFile() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#downloadSubcontractEnquiryReportPDFFile(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testDownloadSubcontractEnquiryReportPDFFile() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#updateScDetailFromResource(com.gammon.qs.domain.Resource, com.gammon.qs.domain.SCPackage)}.
	 */
	public TransactionTestCase.TestDataMethod testUpdateScDetailFromResource() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getSCDetail(com.gammon.qs.domain.SCPackage, java.lang.String, java.lang.Integer)}.
	 */
	public TransactionTestCase.TestDataMethod testGetSCDetailSCPackageStringInteger() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#updateSCDetail(com.gammon.qs.domain.SCDetails)}.
	 */
	public TransactionTestCase.TestDataMethod testUpdateSCDetail() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#inactivateSCDetail(com.gammon.qs.domain.SCDetails)}.
	 */
	public TransactionTestCase.TestDataMethod testInactivateSCDetail() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getNextSequenceNo(com.gammon.qs.domain.SCPackage)}.
	 */
	public TransactionTestCase.TestDataMethod testGetNextSequenceNo() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainSCPackageNosUnderPaymentRequisition(java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainSCPackageNosUnderPaymentRequisition() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainSCPackage(java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainSCPackageStringString() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#searchSCDetailProvision(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testSearchSCDetailProvision() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getPerforamceAppraisalsList(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testGetPerforamceAppraisalsList() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getSearchAppraisalResult(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, int)}.
	 */
	public TransactionTestCase.TestDataMethod testGetSearchAppraisalResult() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#downloadPerformanceAppraisalExcelFile(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testDownloadPerformanceAppraisalExcelFile() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getAppraisalResultByPage(java.lang.String, int)}.
	 */
	public TransactionTestCase.TestDataMethod testGetAppraisalResultByPage() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#canAccess(java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testCanAccess() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getCompanyBaseCurrency(java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testGetCompanyBaseCurrency() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getCurrencyCodeList(java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testGetCurrencyCodeList() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#searchProvisionHistory(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testSearchProvisionHistory() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#downloadScProvisionHistoryExcelFile(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testDownloadScProvisionHistoryExcelFile() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#populateGridByPage(int)}.
	 */
	public TransactionTestCase.TestDataMethod testPopulateGridByPage() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#deleteProvisionHistories(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)}.
	 */
	public TransactionTestCase.TestDataMethod testDeleteProvisionHistories() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#updateSCDetailsWorkdoneQuantityByResource(java.util.List)}.
	 */
	public TransactionTestCase.TestDataMethod testUpdateSCDetailsWorkdoneQuantityByResource() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getReadyToUpdateWorkdoneQuantityPackageNos(java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testGetReadyToUpdateWorkdoneQuantityPackageNos() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getSCDetail(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)}.
	 */
	public TransactionTestCase.TestDataMethod testGetSCDetailStringStringStringStringStringInteger() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#saveSCPackage(com.gammon.qs.domain.SCPackage)}.
	 */
	public TransactionTestCase.TestDataMethod testSaveSCPackage() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#updateSCDetailWorkdoneQtybyHQL(com.gammon.qs.domain.SCDetails, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testUpdateSCDetailWorkdoneQtybyHQL() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#updateSCStatus(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testUpdateSCStatus() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#updateAddendumStatus(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testUpdateAddendumStatus() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#updateSCSplitTerminateStatus(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testUpdateSCSplitTerminateStatus() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#updateSCFinalPaymentStatus(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testUpdateSCFinalPaymentStatus() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#saveOrUpdateSCPackageAdmin(com.gammon.qs.domain.SCPackage)}.
	 */
	public TransactionTestCase.TestDataMethod testSaveOrUpdateSCPackageAdmin() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getScPackageControlDao()}.
	 */
	public TransactionTestCase.TestDataMethod testGetScPackageControlDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#setScPackageControlDao(com.gammon.qs.dao.SCPackageControlHBDao)}.
	 */
	public TransactionTestCase.TestDataMethod testSetScPackageControlDao() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getSCDetailsExcelFileByJob(java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testGetSCDetailsExcelFileByJob() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#updateSCDetailsNewQuantity(java.util.List)}.
	 */
	public TransactionTestCase.TestDataMethod testUpdateSCDetailsNewQuantity() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainPackageAwardedType(com.gammon.qs.domain.SCPackage)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainPackageAwardedType() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#validateAndInsertSCAwardHedgingNotification(com.gammon.qs.domain.SCPackage)}.
	 */
	public TransactionTestCase.TestDataMethod testValidateAndInsertSCAwardHedgingNotification() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainSubcontractHoldMessage()}.
	 */
	public TransactionTestCase.TestDataMethod testObtainSubcontractHoldMessage() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#generateSCPackageSnapshotManually()}.
	 */
	public TransactionTestCase.TestDataMethod testGenerateSCPackageSnapshotManually() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#generateSCDetailsForPaymentRequisition(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testGenerateSCDetailsForPaymentRequisition() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#recalculateSCPackageSubcontractSum(com.gammon.qs.domain.SCPackage, java.util.List)}.
	 */
	public TransactionTestCase.TestDataMethod testRecalculateSCPackageSubcontractSum() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#recalculateSCDetailPostedCertQty(java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testRecalculateSCDetailPostedCertQty() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#updateF58001FromSCPackageManually()}.
	 */
	public TransactionTestCase.TestDataMethod testUpdateF58001FromSCPackageManually() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainSystemConstantSearchOption(java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainSystemConstantSearchOption() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#getContraChargeEnquiryWrapper(com.gammon.qs.wrapper.contraChargeEnquiry.ContraChargeSearchingCriteriaWrapper, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testGetContraChargeEnquiryWrapper() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainContraCharge(com.gammon.qs.wrapper.contraChargeEnquiry.ContraChargeSearchingCriteriaWrapper)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainContraCharge() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainContraChargeListPaginationWrapper(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainContraChargeListPaginationWrapper() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainContraChargeByPage(int)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainContraChargeByPage() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainContraChargeReportPDF(com.gammon.qs.wrapper.contraChargeEnquiry.ContraChargeSearchingCriteriaWrapper)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainContraChargeReportPDF() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.PackageService#obtainContraChargeReportExcel(com.gammon.qs.wrapper.contraChargeEnquiry.ContraChargeSearchingCriteriaWrapper)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainContraChargeReportExcel() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

}
