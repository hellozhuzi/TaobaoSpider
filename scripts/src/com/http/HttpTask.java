package com.http;

import org.apache.http.HttpResponse;

public class HttpTask extends Thread{
    public static String token = null;
    public static String phone = null;
    public static String msg = null;
    public static final String PROID_COINMEET = "13044";
    public static final String PROID_CANDY = "13651";
    public static final String PROID_TELEGRAM = "2006";
    public static String proid ="";
    public static String code = "";
    public static void setProid(String setproid) {
		proid = setproid;
	}
    public void login(){
        String name = "17326024390";
        String pwd = "qq541tzyima";
        String url = "http://api.51ym.me/UserInterface.aspx?action=login&username="+name+
                "&password="+pwd;
        ClientInfo info = new ClientInfo();
        info.setUrl(url);
        info.setType(ClientInfo.POST);
        info.setCallback(new HttpCallback() {
            @Override
            public void completed(HttpResponse response) {
            		super.completed(response);
                    String content = getContent();
                    if(content!=null) {
                        if(content.contains("success|")){
                            token = content.substring("success|".length());
                            System.out.println("login OK,get token "+token);
                        }
                    }
                }           
        });
        MyHttpClient client = new MyHttpClient(info);
        client.start();
    }
    public String getphone(){

        String url = "http://api.51ym.me/UserInterface.aspx?action=getmobile&itemid="+proid
        +"&token="+token;
        ClientInfo info = new ClientInfo();
        info.setType(ClientInfo.GET);
        info.setUrl(url);
        info.setCallback(new HttpCallback(){
            @Override
            public void completed(HttpResponse response) {
                super.completed(response);
                String content = getContent();
                if(content.contains("success|")){
                    phone = content.substring("success|".length());
                    System.out.println("get phone OK, "+phone);
                }
            }
        });
        new MyHttpClient(info).start();
        return phone;
    }
    public String getVifyMsg(){

        String url ="http://api.51ym.me/UserInterface.aspx?action=getsms"+
                "&mobile="+phone+
                "&itemid="+proid
                +"&token="+token;
        while (true) {
    	    code = "";
	        ClientInfo info = new ClientInfo();
	        info.setType(ClientInfo.GET);
	        info.setUrl(url);
	        info.setCallback(new HttpCallback(){
	            @Override
	            public void completed(HttpResponse response) {
	                super.completed(response);
	                String content = getContent();
	                if(content.contains("success|")){
	                    msg = content.substring("success|".length());
	                    System.out.println("get msg OK, "+msg);
	                    code = getVercodeFromMsg(msg);
	                    System.out.println("get code OK "+code);
	                }
	            }
	        });
	        new MyHttpClient(info).start();
	        if(code.length()>0){
	        	break;
	        }
       }
  
        return code;
    }
    public void releasePhone(){
        String url = "http://api.51ym.me/UserInterface.aspx?action=release&mobile="+phone
                +"&itemid="+proid+
                "&token="+token;
        ClientInfo info = new ClientInfo();
        info.setType(ClientInfo.GET);
        info.setUrl(url);
        info.setCallback(new HttpCallback(){
            @Override
            public void completed(HttpResponse response) {
                super.completed(response);
                String content = getContent();
                if(content.contains("success|")){
                	
                }
            }
        });
        new MyHttpClient(info).start();
    }
    private String getVercodeFromMsg(String msg) {
    	String vercode = "";
    	if(proid.equals(PROID_COINMEET)){
    		vercode = msg.substring(msg.indexOf("验证码是")+4, msg.indexOf("验证码是")+8);
    	}
    	else if(proid.equals(PROID_CANDY)){
    		vercode = msg.substring(msg.indexOf("code ")+5, msg.indexOf("code ")+11);
		} 	
		return vercode;
	}
}

