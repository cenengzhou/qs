package com.gammon.qs.service.businessLogic;

import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.SubcontractDetailAP;
import com.gammon.qs.domain.SubcontractDetailBQ;
import com.gammon.qs.domain.SubcontractDetailCC;
import com.gammon.qs.domain.SubcontractDetailOA;
import com.gammon.qs.domain.SubcontractDetailRT;
import com.gammon.qs.domain.SubcontractDetailVO;

public class SCDetailsLogic {
		
	public static String generateBillItem(String scLineType, Integer maxSeqNo) {
		String maxSeqNoStr="";
		String billItemMid="/ /"+scLineType.trim()+"/";
		for (int i=0;i<4-maxSeqNo.toString().length();i++)
			maxSeqNoStr+="0";
		maxSeqNoStr+=maxSeqNo;
		if ("D1".equals(scLineType)||"D2".equals(scLineType))
			return "94"+billItemMid+maxSeqNoStr;
		if ("V1".equals(scLineType)||"V2".equals(scLineType)||"V3".equals(scLineType))
			return "95"+billItemMid+maxSeqNoStr;
		if ("BS".equals(scLineType)||"CF".equals(scLineType)||"L1".equals(scLineType)||"L2".equals(scLineType))
			return "96"+billItemMid+maxSeqNoStr;
		if ("MS".equals(scLineType)||"RR".equals(scLineType)||"RA".equals(scLineType))
			return "97"+billItemMid+maxSeqNoStr;
		if ("AP".equals(scLineType)||"OA".equals(scLineType))
			return "98"+billItemMid+maxSeqNoStr;
		if ("C1".equals(scLineType)||"C2".equals(scLineType))
			return "99"+billItemMid+maxSeqNoStr;
		return null;
	}

	/*public static Integer generateSequenceNo(List<SCDetails> scDetails) {
		Integer maxSeqNo=0;
		
		for (SCDetails scDetail:scDetails){
			if (maxSeqNo<scDetail.getSequenceNo())
				maxSeqNo=scDetail.getSequenceNo();
		}
		maxSeqNo++;
		return maxSeqNo;
	}*/

	public static SubcontractDetail createSCDetailByLineType(String scLineType) {
		if ("V1".equals(scLineType)||"V2".equals(scLineType)|| "V3".equals(scLineType)||
			"D1".equals(scLineType)||"D2".equals(scLineType)||
			"L1".equals(scLineType)||"L2".equals(scLineType)||
			"CF".equals(scLineType) )
			return new SubcontractDetailVO();
		if ("C1".equals(scLineType)||"C2".equals(scLineType))
			return new SubcontractDetailCC();
		if ("RA".equals(scLineType)||"RR".equals(scLineType))
			return new SubcontractDetailRT();
		if ("OA".equals(scLineType))
			return new SubcontractDetailOA();
		if ("AP".equals(scLineType)||"MS".equals(scLineType))
			return new SubcontractDetailAP();
		if ("BQ".equals(scLineType)||"B1".equals(scLineType))
			return new SubcontractDetailBQ();
		return null;
	}	

}
