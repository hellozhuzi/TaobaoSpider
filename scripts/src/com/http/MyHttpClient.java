package com.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;



public class MyHttpClient {

    ClientInfo info;
    public MyHttpClient(ClientInfo info) {
        this.info = info;
    }

    public void start() {
        try {
            if(info.type.equals(ClientInfo.GET)){
                 get(info);
            }
            else {
               post(info);
            }
        }
        catch (Exception e){
        }
    }

    private void get(ClientInfo info) {
    	HttpClient client = new DefaultHttpClient();
		// TODO Auto-generated method stub
    	HttpGet request = new HttpGet(info.url);
		HttpResponse response;
		try {
			response = client.execute(request);
			info.callback.completed(response);
		}catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
	}
    private void post(ClientInfo info) throws IOException {
        try {
        	HttpClient client = new DefaultHttpClient();
         	HttpPost request = new HttpPost(info.url);
    		HttpResponse response;
    		try {
    			response = client.execute(request);
    			info.callback.completed(response);
    		}catch (ClientProtocolException e) {
    		} catch (IOException e) {
    		}

        }
        catch (Exception e){
            e.printStackTrace();;
        }

    }
}

