package com.gammon.pcms.helper;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {
	public static <T> T getRequestParam(T param, Class<T> valueType, ObjectMapper mapper, HttpServletRequest request){
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
}
