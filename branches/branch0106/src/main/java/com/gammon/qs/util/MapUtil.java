package com.gammon.qs.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gammon.qs.application.BasePersistedObject;

public class MapUtil {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final Map getIdHashMap(List baseObjectList)
	{
		Map<String, Object> lpMap = new LinkedHashMap<String, Object>();
		
		for(Iterator<BasePersistedObject> it = baseObjectList.iterator(); it.hasNext();){
			
			Object obj = it.next();
			BasePersistedObject baseobj = (BasePersistedObject) obj;
			lpMap.put(String.valueOf(baseobj.getId()), obj);			
		}
		
		return lpMap;
		
	}
}
