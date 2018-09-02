package com.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

public class HttpCallback {
    public String content = null;
    public void completed(HttpResponse response){
    	 HttpEntity entity = response.getEntity();
    	 try {
		    if(entity!=null){
	             String results = EntityUtils.toString(entity, "UTF-8");
	             System.out.println("接口返回值="+results);
	             content = results;
	         }
		} catch (Exception e) {
			// TODO: handle exception
		}        
    }

    public String getContent() {
        return content;
    }
}
