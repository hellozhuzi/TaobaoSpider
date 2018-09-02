package com.jk.test.chain;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.http.HttpTask;
import com.jk.test.BaseCase;
import com.jk.test.util.Util;

public class CandyTest extends BaseCase{
	public void test() {
		SetInputMethod();
		inputUrl();
		new HttpTask().login();
		reg();	
	}
	private void reg() {
		try {
			String tel = new HttpTask().getphone();
			String pwd = tel+"!";
			String text;
			UiObject telInput = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("phone"));
			text = encodeToModifiedUtf7(tel);
			telInput.setText(text);// input keywords
	
			sleepRandomSeconds();
			
			UiObject verybtn = new UiObject(new UiSelector().description("Login"));
			verybtn.click();
		
			sleep(5000);
			UiObject very = new UiObject(new UiSelector().className("android.widget.ListView"));
			very.click();
			String vercode = new HttpTask().getVifyMsg();
			text = encodeToModifiedUtf7(vercode);
			very.setText(text);
		
			new HttpTask().releasePhone();
			UiObject reg = new UiObject(new UiSelector().description("Next"));
			reg.click();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	public void inputUrl() {
		try {
			//stop
			String packagename = "com.oupeng.mini.android";
			String resultString = Util.ExecCmd("am force-stop " + packagename);
			sleep(10000);
			String packageAppString = "com.oupeng.mini.android/com.opera.android.OperaStartActivity";
		    resultString = Util.ExecCmd("am start " + packageAppString);
			sleep(5000);
			String url = "https://candy.one/i/523856";
			UiObject input = new UiObject(new UiSelector().resourceId("com.oupeng.mini.android:id/search_engine_title"));
			input.click();
			sleep(1000);
			input = new UiObject(new UiSelector().resourceId("com.oupeng.mini.android:id/url_field"));
			input.click();
			String text = encodeToModifiedUtf7(url);
			input.setText(text);// input keywords
			sleepRandomSeconds();
			UiObject btn =  new UiObject(new UiSelector().resourceId("com.oupeng.mini.android:id/action_button"));
			btn.click();
			sleepRandomSeconds();
			sleep(10*1000);
//			getUiDevice().swipe(100, 550, 100, 200, 20);
//			waitforHuman();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
