package com.jiema;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import com.http.ClientInfo;
import com.http.HttpCallback;
import com.http.MyHttpClient;

public class Jiema {
	String phone = "";
	String code = "";
	String tmpcode = "";
	public String getphone() {
		
		String param = "control=phone&pid=38068&phoneType=&area=&noarea=&mobile=&vno=&city=&uid=luan1057492315_linpeng12&pid2=0";
        String url = "http://app.jmyzm.com/appApi.do?"+param;
        ClientInfo info = new ClientInfo();
        info.setType(ClientInfo.GET);
        info.setUrl(url);
        info.setCallback(new HttpCallback(){
            @Override
            public void completed(HttpResponse response) {
                super.completed(response);
                String content = getContent();
                try {
                	 System.out.println("get back:"+content);
                	 JSONArray array = new JSONArray(content);               	 
                	 JSONObject info = array.getJSONObject(0);
                     phone = info.getString("phone");
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}              
            }
        });
        new MyHttpClient(info).start();
        return phone;       
	}
	public String getCodeOnce(String phone) {
		String param = "control=noteCode&pid2=0&phone="+phone+"&uid=luan1057492315_linpeng12&pid=38068";
        String url = "http://app.jmyzm.com/appApi.do?"+param;
        ClientInfo info = new ClientInfo();
        info.setType(ClientInfo.GET);
        info.setUrl(url);
        info.setCallback(new HttpCallback(){
            @Override
            public void completed(HttpResponse response) {
                super.completed(response);
                String content = getContent();
                try {
                	 System.out.println("get back:"+content);
                	 JSONArray array = new JSONArray(content);               	 
                	 JSONObject info = array.getJSONObject(0);
                     tmpcode = info.getString("code");
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
               
            }
        });
        new MyHttpClient(info).start();
		return tmpcode;
	}
	public String getVerifyCode(String phone) {
		for(int i=0;i<100;i++){
			String mString = getCodeOnce(phone);
			if(mString!=null && mString.length()>0){
				System.out.println("get msg:"+mString);
				//【BiYong】您的验证码为：153750，15分钟内有效
				String code = mString.substring(mString.indexOf("：")+1, mString.indexOf("：")+7);
				System.out.println("get code:"+code);
				return code;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
