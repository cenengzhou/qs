package com.gammon.pcms.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.gammon.pcms.dto.RocDetailJasperWrapper;
import com.gammon.pcms.dto.RocJasperWrapper;
import com.gammon.pcms.dto.IRocDetailJasperWrapper;
import com.gammon.pcms.dto.RocAmountWrapper;
import com.gammon.pcms.dto.RocCaseWrapper;

public class bean {

	private static int count;
	public static final String TENDER_RISK = "Tender Risk";
	public static final String TENDER_OPPS = "Tender Opps";
	public static final String CONTINGENCY = "Contingency";
	public static final String RISK = "Risk";
	public static final String OPPS = "Opps";
	
	public static final String INCREASED_TURNOVER= "Increased Turnover";
	public static final String INCREASED_COST= "Increased Cost";
	public static final String REDUCED_TURNOVER= "Reduced Turnover";
	public static final String REDUCED_COST = "Reduced Cost";
	
	public static String[] turnoverImplacts = new String[] {REDUCED_TURNOVER, INCREASED_TURNOVER};
	public static String[] costImplacts = new String[] {REDUCED_COST, INCREASED_COST};
	public static String[] implacts = new String[] { INCREASED_TURNOVER, INCREASED_COST, REDUCED_TURNOVER, REDUCED_COST};
	
	public static Collection<RocJasperWrapper> createBeanCollection() {
		List<RocJasperWrapper> list = new ArrayList<>();

		RocJasperWrapper wrapper = getCommonWrapper();
		
		wrapper.setDetailsTenderRisks(generateRocDetailWrapperList(TENDER_RISK));
		wrapper.setDetailsTenderOpps(generateRocDetailWrapperList(TENDER_OPPS));
		wrapper.setDetailsTenderOther(generateRocDetailWrapperList(CONTINGENCY));
		wrapper.setDetailsRisks(generateRocDetailWrapperList(RISK));
		wrapper.setDetailsOpps(generateRocDetailWrapperList(OPPS));

		list.add(wrapper);
		return list;
	}
	
	public static Collection<RocJasperWrapper> createCombineDetailBeanCollection() {
		List<RocJasperWrapper> list = new ArrayList<>();
		
		RocJasperWrapper wrapper = getCommonWrapper();
		
		wrapper.getDetailsContingencies().addAll(generateRocDetailWrapperList(bean.TENDER_RISK));
		wrapper.getDetailsContingencies().addAll(generateRocDetailWrapperList(bean.TENDER_OPPS));
		wrapper.getDetailsContingencies().addAll(generateRocDetailWrapperList(bean.CONTINGENCY));
		wrapper.getDetailsRisksOpps().addAll(generateRocDetailWrapperList(bean.RISK));
		wrapper.getDetailsRisksOpps().addAll(generateRocDetailWrapperList(bean.OPPS));
		
		list.add(wrapper);
		return list;
	}
	
	public static RocJasperWrapper getCommonWrapper(){
		RocJasperWrapper wrapper = new RocJasperWrapper();
		wrapper.setSumCaseTenderRisks(generateRocCaseWrapper());
		wrapper.setSumCaseTenderOpps(generateRocCaseWrapper());
		wrapper.setSumCaseTenderOther(generateRocCaseWrapper());
		wrapper.setSumCaseRisks(generateRocCaseWrapper());
		wrapper.setSumCaseOpps(generateRocCaseWrapper());
		
		wrapper.setAsAtDate("Sep-2021");
		wrapper.setProjectName("projectName1");
		wrapper.setProjectNumber("1234");
		return wrapper;
	}
	
	static RocCaseWrapper generateRocCaseWrapper() {
		return new RocCaseWrapper(generateRocAmountWrapper(), generateRocAmountWrapper(), generateRocAmountWrapper());
	}
	
	static RocAmountWrapper generateRocAmountWrapper() {
		return new RocAmountWrapper(getRandomNumber(0,1000),getRandomNumber(0,1000));
	}
	
	static List<IRocDetailJasperWrapper> generateRocDetailWrapperList(String desc){
		return generateRocDetailWrapperList(desc, getRandomNumber(3, 100));
	}
	
	static List<IRocDetailJasperWrapper> generateRocDetailWrapperList(String desc, int size) {
		List<IRocDetailJasperWrapper> list = new ArrayList<>();
		for(int i = 0; i < size; i++) {
			list.add(generateRocDetailWrapper(desc));
		}
		return list;
	}

	static RocDetailJasperWrapper generateRocDetailWrapper(String desc) {
		count++;
		String implact = implacts[getRandomNumber(0,3)];
		return new RocDetailJasperWrapper(
			getRandomNumber(0, 1000), 
			getRandomNumber(0, 1000), 
			getRandomNumber(0, 1000), 
			getRandomNumber(0, 1000), 
			getRandomNumber(0, 1000), 
			getRandomNumber(0, 1000), 
				desc,
				desc + " " + implact + " "+ count,
				implact,
				"projectRef " + count,
				"remark" + count,
				"id" + count
		);
	}
	
	static int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}

}
