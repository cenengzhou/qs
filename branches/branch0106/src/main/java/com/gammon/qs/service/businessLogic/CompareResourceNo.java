package com.gammon.qs.service.businessLogic;

import java.io.Serializable;
import java.util.Comparator;

import com.gammon.qs.domain.BpiItemResource;

public class CompareResourceNo implements Comparator<BpiItemResource>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3318238872239189275L;

	public int compare(BpiItemResource resource1, BpiItemResource resource2) {
		if(resource1==null || resource2 == null)
			return 0;
		
		return resource1.getResourceNo().compareTo(resource2.getResourceNo());				

	}

	
}