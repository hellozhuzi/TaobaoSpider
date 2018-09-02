package com.jk.test.chain;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.http.HttpTask;
import com.jiema.Jiema;
import com.jk.test.BaseCase;
import com.jk.test.util.Util;

public class Beautychain extends BaseCase{
	public void test() {
		init();
		String code = getCode();
		System.out.println("get code:"+code);
		regTelegram();
		doTask(code);
		qiutTelegram();
		
	}
	private void init() {
		closeTelegram();
		SetInputMethod();
		HttpTask.setProid(HttpTask.PROID_TELEGRAM);
		openTelegram();
		
	}
	private void closeTelegram() {
		String cmd = "am force-stop org.telegram.btcchat";
		 Util.ExecCmd(cmd);
		 sleep(1500);
	}
	private void  openTelegram() {
		String packageAppString = "org.telegram.btcchat/org.telegram.ui.LaunchActivity";
		Util.ExecCmd("am start -n " + packageAppString);
	}
	private void openLantern() {
		String packageAppString = "org.getlantern.lantern/org.lantern.app.activity.LanternMainActivity_";
		Util.ExecCmd("am start -n " + packageAppString);	
	}
	private boolean regTelegram()  {
		
		try {
			String phone = new Jiema().getphone();
			System.out.println("now enter phone:"+phone);
			if(phone==null || phone.length()==0){
				return false;
			}
			UiObject start = new UiObject(new UiSelector().text("开始聊天"));
			if(start.exists()){
				start.click();
			}
			sleep(1000);
			UiObject input1 = new UiObject(new UiSelector().className("android.widget.EditText").index(1));
			input1.click();
			input1.setText(encodeToModifiedUtf7("86"));// input keywords
			sleep(5000);
			UiObject input2 = new UiObject(new UiSelector().className("android.widget.EditText").index(2));
			input2.click();
			input2.setText(encodeToModifiedUtf7(phone));// input keywords
			
			UiObject ok = new UiObject(new UiSelector().className("android.widget.ImageView"));
			ok.clickAndWaitForNewWindow();
			Thread.sleep(5000);
			
			UiObject verify = null;
			while (true) {
				verify = new UiObject(new UiSelector().text("验证码"));
				if(verify.exists()){
					break;
				}
				sleep(1000);
			}
			verify.click();
			String veryfycode = new Jiema().getVerifyCode(phone);
			if(veryfycode==null){
				return false; 
			}
			input2.setText(encodeToModifiedUtf7(veryfycode));// input keywords
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return true;
	}
	private void doTask(String code) {
		try {
			UiObject tmp = new UiObject(new UiSelector().className("android.widget.ImageView").index(0));
			UiObject more = tmp.getFromParent(new UiSelector().index(2)).getChild(new UiSelector().className("android.widget.ImageView"));
			more.click();
			UiObject edit = new UiObject(new UiSelector().className("android.widget.EditText"));
			edit.click();
			edit.setText(encodeToModifiedUtf7("https://0.plus/beautychain"));
			
			UiObject add =  new UiObject(new UiSelector().text("加入"));
			add.click();
			
			edit = new UiObject(new UiSelector().className("android.widget.EditText"));
			edit.click();
			edit.setText(encodeToModifiedUtf7("code"));	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	
	}
	private void qiutTelegram() {
		try {
			UiObject more = new UiObject(new UiSelector().className("android.widget.ImageView"));
			more.clickAndWaitForNewWindow();
			UiObject set = new UiObject(new UiSelector().text("设置"));
			set.clickAndWaitForNewWindow();
			UiObject tmp = new UiObject(new UiSelector().className("android.widget.ImageView").index(0));
			more = tmp.getFromParent(new UiSelector().index(1)).getChild(new UiSelector().className("android.widget.ImageView"));
			more.clickAndWaitForNewWindow();
			
			UiObject logout = new UiObject(new UiSelector().text("注销"));
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
}
