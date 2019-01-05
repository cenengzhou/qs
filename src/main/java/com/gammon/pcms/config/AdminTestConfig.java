package com.gammon.pcms.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("${admin_test.properties}")
public class AdminTestConfig {
	
	@Value("#{${admin.test.items}}")
	private Map<String, Map<String, Map<String, Map<String, String>>>> itemMap;

	/**
	 * @return the itemMap
	 */
	public Map<String, Map<String, Map<String, Map<String, String>>>> getItemMap() {
		return itemMap;
	}

	public Map<String, Map<String, Map<String, String>>> getCategory(String category){
		return getItemMap().get(category);
	}
	
	public Map<String, Map<String, String>> getCategoryMethod(String category, String method){
		return getCategory(category).get(method);
	}
	
	public Map<String, String> getCategoryMethodItem(String category, String method, String item){
		return getCategoryMethod(category, method).get(item);
	}

	public String getCategoryMethodItemDetail(String category, String method, String item, String detail){
		return getCategoryMethodItem(category, method, item).get(detail);
	}
	
	public List<String> getCategoryList(){
		List<String> categoryList = new ArrayList<>();
		getItemMap().forEach((k,v) -> categoryList.add(k));
		return categoryList;
	}
	
	public List<String> getCategoryMethodList(String category){
		List<String> categoryMethodList = new ArrayList<>();
		getCategory(category).forEach((k,v) -> categoryMethodList.add(k));
		return categoryMethodList;
	}
	
	public List<String> getCategoryMethodItemList(String category, String method){
		List<String> categoryMethodItemList = new ArrayList<>();
		getCategoryMethod(category, method).forEach((k,v) -> categoryMethodItemList.add(k));
		return categoryMethodItemList;
	}
	
	public List<String> getCategoryMethodItemDetailList(String category, String method, String item) {
		List<String> categoryMethodItemDetailList = new ArrayList<>();
		getCategoryMethodItem(category, method, item).forEach((k, v) -> categoryMethodItemDetailList.add(k));
		return categoryMethodItemDetailList;
	}
}
