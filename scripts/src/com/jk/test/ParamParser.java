package com.jk.test;

import java.util.HashMap;
import java.util.Map;

import android.util.Base64;

public class ParamParser {
	
	private static String parseParam(String param) {
		if (param == null) {
			return null;
		}
		
		byte[] paramTotal = Base64.decode(param, Base64.DEFAULT);
		String paramTotalStr = new String(paramTotal);
		String[] paramList = paramTotalStr.split("&");
		if (paramList == null || paramList.length !=2) {
			return null;
		}
		
		String verString = paramList[0];
		String paramString = paramList[1];
		int index1 = verString.indexOf("ver=");
		int index2 = paramString.indexOf("param=");
		if (index1 == -1 || index2 == -1) {
			return null;
		}
		
		String version = verString.substring(index1+4);
		if (!version.equals("1.0")) {
			return null;
		}
		
		String paramValue = paramString.substring(index2+6);
		return new String(Base64.decode(paramValue, Base64.DEFAULT));
	}
	
	public static Map<String, String> ParseParamKeyValueMap(String param){
		String paramValue = parseParam(param);
		
		String[] paramList = paramValue.split("&");
		if (paramList == null || paramList.length <= 0) {
			return null;
		}
		
		Map<String, String> resMap = new HashMap<String, String>();
		for (String pm : paramList) {
			String[] paramPair = pm.split("=");
			if((paramPair.length > 1) && (!paramPair[0].isEmpty())&& (!paramPair[1].isEmpty()))
			{
				resMap.put(paramPair[0], paramPair[1]);
			}
		}
		return resMap;
	}
}
