package com.jk.test;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.beetstra.jutf7.ModifiedUTF7Charset;
import com.jk.test.util.Util;

import android.util.Base64;

public class BaseCase extends UiAutomatorTestCase{
	private Charset charsetEncoder  = new ModifiedUTF7Charset("X-MODIFIED-UTF-7", new String[] {});
	public void SetInputMethod() {
		Util.PrintExecLog(4, "set llh input method");
		String packageAppString = "com.llh.llhinput.llhinput/com.llh.llhinput.Utf7ImeService";
		Util.ExecCmd("ime set " + packageAppString);
	}
	public String encodeToModifiedUtf7(String paramString) throws UnsupportedEncodingException {
		if (paramString == null || paramString.isEmpty()) {
			return null;
		}
		
		String encodedString = "";
		for (int i=0; i<paramString.length(); i++) {
			char chWord = paramString.charAt(i);
			String word = String.valueOf(chWord);
			if (word != null) {
				ByteBuffer buffer = charsetEncoder.encode(word);
				byte[] bytes = new byte[buffer.limit()];
				buffer.get(bytes);
				String str = new String(bytes, "US-ASCII");
				encodedString += str;
			}
		}
		return encodedString;
	}
	public String getCode() {
		String code = "";
		try {
			JSONObject obj = new JSONObject(parseParams("data"));
			code = obj.getString("code");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return code;
	}
	public String parseParams(String key) {
		String data = getStringParams(key);
		byte[] dataArray = Base64.decode(data, 0);
		data = new String(dataArray);
		return data;
	}
	public String getStringParams(String key) {
		String value = getParams().getString(key);
		if (value == null || value.isEmpty()) {
			value = "";
		}
		return value;
	}
	public void sleepRandom(int minWaitSeconds,int maxWaitSeconds) {
		
		int waitTime = minWaitSeconds + new Random().nextInt(maxWaitSeconds - minWaitSeconds) ;
		System.out.println("now sleep "+waitTime +"seconds");
		sleep(waitTime*1000);	
	}
	public void sleepRandomSeconds() {
		int max = 8;
		int min = 2;
		int v =  min + new Random().nextInt(max - min);
		sleep(v*1000);
	}
}
