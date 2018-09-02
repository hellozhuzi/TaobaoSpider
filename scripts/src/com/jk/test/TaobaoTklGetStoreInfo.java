package com.jk.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.jk.test.util.Util;

import android.util.Base64;


public class TaobaoTklGetStoreInfo extends BaseCase{
	int finishcount = 0;
	public void test() {
		System.out.println("start taobao task");
//		while (true) {				
			try {
				initTaobao();
				JSONObject shoptask = getShoptask();
				String tkl = shoptask.getString("tkl");
				int shopId = shoptask.getInt("shopid");
				opentkl(tkl);	
				openShop();
				JSONObject shop = getStoreInfo();
				if(shop!=null){
					shop.put("shopid", shopId);
					pushToDB(shop);
					pushToDB2(shop);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finishcount++;
			Util.PrintExecLog(1, "finishedcount:"+finishcount);
			sleepRandom(0, 1);
		}
//	}
	private void initTaobao() {
		String cmd = "am force-stop com.taobao.taobao";
		 Util.ExecCmd(cmd);
		 sleep(1500);
	}
	private JSONObject getShoptask() {
		String url = "http://120.132.26.247/spider/getshoptask.php";
		HttpClient client = new DefaultHttpClient();		
		HttpGet request = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(request);
		    HttpEntity httpEntity = response.getEntity();
			String result = EntityUtils.toString(httpEntity);
			JSONObject jsonObject = new JSONObject(result);
			Util.PrintExecLog(1, "get shop task:"+jsonObject.toString());
			return jsonObject;	
		} 
		catch (Exception e) {
	      e.printStackTrace();   
	      return null;     
		}	
	}
	private void opentkl(String tkl) {
		String tklBase64;
		try {
			tklBase64 = new String(Base64.encode(tkl.getBytes("utf-8"), Base64.NO_WRAP), "utf-8");
			String cmd = "am start -n com.wb.wbapp/.ClipboardActivity -e tkl " + tklBase64;
			Util.ExecCmd(cmd);
			Util.PrintExecLog(1, "open "+tkl);;
			sleep(1000);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void openShop() {
		String packageAppString = "com.taobao.taobao/com.taobao.tao.welcome.Welcome";
		Util.ExecCmd("am start -n " + packageAppString);
		sleep(10000);
		int count = 0;
		try {
			while (count < 5) {
				UiObject open =  new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/tpd_common_action"));
				if(open.exists()){
					open.clickAndWaitForNewWindow();
					System.out.println("open shop page");
					break;
				}
				count++;
				sleep(1500);
			}
			System.out.println("can not open shop page");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private JSONObject getStoreInfo() {
		JSONObject shop = new JSONObject();
		try {	
			String name = null;
			String tel = null;
			//find logo	
			UiObject shopNamaObject = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/tv_shop_name"));
			name = shopNamaObject.getText();
			UiObject shopLogoObject =  new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/iv_logo"));
			if (shopLogoObject == null || !shopLogoObject.exists()) {
				Util.PrintExecLog(0, "cannot position shop logo");
				return null;
			}	
			//click logo
			shopLogoObject.clickAndWaitForNewWindow(10000L);
			Util.TakeRest(5000L);
			UiObject telTitleObject = new UiObject(new UiSelector().description("服务电话"));
		
			if (telTitleObject == null || !telTitleObject.exists()) {
				telTitleObject = new UiObject(new UiSelector().text("服务电话"));
				if(!telTitleObject.exists()){
					Util.PrintExecLog(0, "this store has no tel");
					tel = "";
				}
				else{
					UiObject telObject = telTitleObject.getFromParent(new UiSelector().index(1));
					tel = telObject.getText();
				}
			}
			else {
				UiObject telParentObject = telTitleObject.getFromParent(new UiSelector().className("android.widget.FrameLayout"));
				if (telParentObject == null || !telParentObject.exists()) {
					Util.PrintExecLog(0, "cannot position tel");
					tel = "";
				}
				else {
					UiObject telObject = telParentObject.getChild(new UiSelector().className("android.view.View"));
					tel = telObject.getContentDescription();
				}
				
			}
			Util.PrintExecLog(1, "tel=" + tel+" name:"+name);
			//save to db
		
			shop.put("shoptel", tel);
			shop.put("shopname", name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return shop;
	}
	
	private void pushToDB(JSONObject shopItem) {
		HttpClient client = new DefaultHttpClient();
		String url = "";
		try {
			int shopid = shopItem.getInt("shopid");
			String shoptel = shopItem.getString("shoptel");
			String shopname = shopItem.getString("shopname");
			Util.PrintExecLog(1,"get info shoptel:"+shoptel+" name:"+shopname);
			String encodename = java.net.URLEncoder.encode(shopname, "UTF-8");
			url = "http://120.132.26.247/spider/putshopinfo.php?shopid="+shopid
					+"&shoptel="+shoptel+"&shopname="+encodename;
			Util.PrintExecLog(1,"url:"+url);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpGet request = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(request);		
		    HttpEntity httpEntity = response.getEntity();
			String result = EntityUtils.toString(httpEntity);					
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void pushToDB2(JSONObject shopItem) {
		HttpClient client = new DefaultHttpClient();
		String url = "";
		try {
			int shopid = shopItem.getInt("shopid");
			String shoptel = shopItem.getString("shoptel");
			String shopname = shopItem.getString("shopname");
			Util.PrintExecLog(1,"get info shoptel:"+shoptel+" name:"+shopname);
			String encodename = java.net.URLEncoder.encode(shopname, "UTF-8");			
			url = "https://www.ubibi.cn/api/v1/ubibi/spider_shop_info?shopid="+shopid
					+"&shoptel="+shoptel+"&shopname="+encodename;
			Util.PrintExecLog(1,"ddd");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpGet request = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(request);		
		    HttpEntity httpEntity = response.getEntity();
			String result = EntityUtils.toString(httpEntity);					
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
