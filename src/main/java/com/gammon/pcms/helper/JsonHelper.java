package com.gammon.pcms.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {
	public static <T> T getRequestParam(T param, JavaType valueType, ObjectMapper mapper, HttpServletRequest request){
		if(param == null){
			try {
				param = mapper.readValue(request.getInputStream(), valueType);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return param;
	}
	
	public static List<String> splitToList(String str, int size){
		int totalLength = str.length();
		int index = 0;
		List<String> resultList = new ArrayList<>();
		while (index < totalLength){
			resultList.add(str.substring(index, Math.min(index + size, totalLength)));
			index += size;
		}
		return resultList;
	}

}
