package com.gammon.qs.util;

import java.io.Serializable;
import java.util.Comparator;

import com.gammon.qs.domain.Resource;

public class ComparatorUtilResource implements Comparator<Resource>, Serializable{
	private static final long serialVersionUID = -3318238872239189275L;
	
	//Compare Resource by Resource No.
	public int compare(Resource resource1, Resource resource2) {
		if(resource1==null || resource2 == null)
			return 0;
		
		return resource1.getResourceNo().compareTo(resource2.getResourceNo());				
	}

	
}