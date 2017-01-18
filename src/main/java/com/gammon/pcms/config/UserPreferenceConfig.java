package com.gammon.pcms.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:${userPreference.properties}")
public class UserPreferenceConfig {
	
	@Value("#{${UserPreference.GridSetting}}")
	private Map<String, String> gridSetting;
	@Value("#{${UserPreference.GridNameMap}}")
	private Map<String, String> gridNameMap;
	
	/**
	 * @return the gridNameMap
	 */
	public Map<String, String> getGridNameMap() {
		return gridNameMap;
	}
	
	public String getGridName(String gridKey){
		return getGridName(gridKey, false);
	}
	
	public String getGridName(String gridKey, boolean withPrefix){
		String gridName;
		if(withPrefix){
			String[] key = gridKey.split(getGridSetting("prefix"));
			if(key.length < 2) throw new IllegalArgumentException(gridKey + " does not contain " + getGridSetting("prefix"));
			gridName = getGridNameMap().get(key[1]);
		} else {
			gridName = getGridNameMap().get(gridKey);
		}
		return gridName;
	}
	
	public String getGridKey(String gridName){
		for(String key : gridNameMap.keySet()){
			if(gridNameMap.get(key).toLowerCase().indexOf(gridName.toLowerCase()) >= 0){
				return key;
			}
		}
		throw new IllegalArgumentException(gridName + " not found in GridNameMap");
	}

	/**
	 * @return the gridSetting
	 */
	public Map<String, String> getGridSetting() {
		return gridSetting;
	}
	
	public String getGridSetting(String key){
		return getGridSetting().get(key);
	}
}