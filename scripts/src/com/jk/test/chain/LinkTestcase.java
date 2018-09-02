package com.jk.test.chain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.jk.test.BaseCase;
import com.jk.test.util.Util;

public class LinkTestcase extends BaseCase{
	private String account;
	private String pwd;
	private String pay;
	private JSONArray feed ;
	public void test(){
		try {
			SetInputMethod();
			getparams();
			openWallet();
			sleep(2000);
		} catch (Exception e) {
			// TODO: handle exception
		}	
	}
	
	private void openWallet() {
		try {
		    String packageAppString = "net.onethingtech.wallet/net.onethingtech.wallet.activity.SplashActivity";
			String resultString = Util.ExecCmd("am start -n " + packageAppString);
		    sleep(1000);
		    UiObject object = new UiObject(new UiSelector().text("我知道了"));
		    object.clickAndWaitForNewWindow();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	private JSONObject getparams() {
		String param = getParams().getString("param");
		
		JSONObject paramObj = null;
		try {
			paramObj = new JSONObject(Util.Base64Decode(param));
			System.out.println("get param:"+paramObj.toString());
			account = paramObj.getString("account");
			pwd = paramObj.getString("pwd");
			pay = paramObj.getString("pay");
			JSONArray feedArray = paramObj.getJSONArray("feed");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();			
		}
		return paramObj;
	}
	private void feed() {
		try {
	
			
			String accoutShort = account.substring(account.length()-4);
			UiObject object = new UiObject(new UiSelector().textContains(accoutShort));
			object.clickAndWaitForNewWindow();
			
			for(int i=0;i<feed.length();i++){
				Double feedone = feed.getJSONObject(i).getDouble("f");
				double remain = getRemain();
				if(remain<feedone){
					System.out.println("余额不足,停止喂养");
					break;
				}
				UiObject btn =  new UiObject(new UiSelector().text("发起转账"));
				btn.clickAndWaitForNewWindow();
				UiObject payAccount = new UiObject(new UiSelector().text("请输入收款账户"));
				String text = encodeToModifiedUtf7(pay);
				payAccount.setText(text);// input keywords
				UiObject mont = new UiObject(new UiSelector().text("请输入转账金额"));
				mont.setText(encodeToModifiedUtf7(feedone+""));
				UiObject trans =  new UiObject(new UiSelector().text("转账"));
				trans.clickAndWaitForNewWindow();
				UiObject sure =  new UiObject(new UiSelector().text("确认转账"));
				sure.clickAndWaitForNewWindow();
				UiObject pwdenter = new UiObject(new UiSelector().text("请输入交易密码"));
				pwdenter.setText(encodeToModifiedUtf7(pwd));				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	
		
	}
	private Double getRemain() {
		Double last = 0.0;
		try {
			UiObject yue = new UiObject(new UiSelector().text("余额"));
			UiObject yueTrue = yue.getFromParent(new UiSelector().index(1));
			last = Double.parseDouble(yueTrue.getText());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return last;
		
	}


}
