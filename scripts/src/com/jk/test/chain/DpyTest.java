package com.jk.test.chain;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.http.HttpTask;
import com.jk.test.BaseCase;
import com.jk.test.util.Util;

public class DpyTest extends BaseCase{

	public void test() {
		SetInputMethod();
		inputUrl();
		new HttpTask().login();
		reg();	
	}
	private void reg() {
		try {
//			String  tel = "17326024390";
			String tel = new HttpTask().getphone();
			System.out.println("get phone:"+tel);
			String pwd = tel+"1";
			String text;
			UiObject telInput = new UiObject(new UiSelector().resourceId("phone"));
			if(telInput.exists()){
				telInput.click();
				text = encodeToModifiedUtf7(tel);
				telInput.setText(text);// input keywords
				sleep(1000);
			}
			sleepRandomSeconds();
			UiObject pwdInput = new UiObject(new UiSelector().resourceId("password"));
			pwdInput.click();
			text = encodeToModifiedUtf7(pwd);
			pwdInput.setText(text);// input keywords
			sleepRandomSeconds();
			UiObject pwdInput1 = new UiObject(new UiSelector().resourceId("passwordConfirm"));
			pwdInput1.click();
			text = encodeToModifiedUtf7(pwd);
			pwdInput1.setText(text);// input keywords
			sleepRandomSeconds();
			UiObject sureBtn = new UiObject(new UiSelector().resourceId("sendVerifyCodeBtn"));
			sureBtn.click();
			sleepRandomSeconds();
			sleep(20*1000);
			UiObject very = new UiObject(new UiSelector().resourceId("verifyCode"));
			very.click();
			String vercode = new HttpTask().getVifyMsg();
			text = encodeToModifiedUtf7(vercode);
			very.setText(text);// input keywords
			new HttpTask().releasePhone();
			UiObject reg = new UiObject(new UiSelector().resourceId("register"));
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
			String url = "http://delphy.org/register.html?invitationCode=CtP564/Z+yg=";
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
			getUiDevice().swipe(100, 550, 100, 200, 20);
//			sleep(60*1000);
			waitforHuman();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	private void waitforHuman() {
		long init = System.currentTimeMillis();
		
		while (true) {
			UiObject varify = new UiObject( new UiSelector().descriptionContains("恭喜"));
			if(varify.exists()){
				System.out.println("human pass");
				break;
			}
			long cur = System.currentTimeMillis();
			if(cur-init>60*1000){
				System.out.println("wait too long");
				break;
			}
			sleep(3000);
			System.out.println("wait ...human....");
		}		
	}
}
