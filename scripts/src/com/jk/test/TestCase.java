package com.jk.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.beetstra.jutf7.ModifiedUTF7Charset;
import com.jk.test.util.Util;

import android.os.SystemClock;

public class TestCase extends UiAutomatorTestCase {
	private Charset charsetEncoder  = new ModifiedUTF7Charset("X-MODIFIED-UTF-7", new String[] {});
	private TaskStatus m_TaskStatus = TaskStatus.STATUS_INIT;
	
	private UiSelector getUiSelectorById(String resourceId) {
		if (resourceId.startsWith("com.tmall.wireless")) {
			int i = resourceId.indexOf(":id/");
			if (i != -1) {
				resourceId = resourceId.substring(i + 1);
				resourceId = "^com.tmall.wireless.*" + resourceId + "$";
				return new UiSelector().resourceIdMatches(resourceId);
			}
			return new UiSelector().resourceId(resourceId);
		}
		return new UiSelector().resourceId(resourceId);
	}
	
	private UiObject findUiObjectByResourceId(String resourceId) {
		UiObject obj = new UiObject(getUiSelectorById(resourceId));
		if (obj != null && obj.exists()) {
			return obj;
		}
		
		abnormalViewProcess();
		return new UiObject(getUiSelectorById(resourceId));
	}
	
	public void SetInputMethod() {
		Util.PrintExecLog(4, "set llh input method");
		String packageAppString = "com.llh.llhinput.llhinput/com.llh.llhinput.Utf7ImeService";
		Util.ExecCmd("ime set " + packageAppString);
	}
	
	private String encodeToModifiedUtf7(String paramString) throws UnsupportedEncodingException {
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

	private void processHomeDisturb() {
		// flow hint
		UiObject flowHintYesObject = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/TBDialog_buttons_OK").textContains("鏄�"));
		if (flowHintYesObject != null && flowHintYesObject.exists()) {
			try {
				flowHintYesObject.clickAndWaitForNewWindow(10000L);
				Util.TakeRest(4000L);
				Util.PrintExecLog(4, "click flow hint view");
			} catch (UiObjectNotFoundException e1) {
			}
		}
					
		// update dialog
		int tryNums = 5;
		do {
			UiObject updateDlgObject = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/contentDialog"));
			if (updateDlgObject != null && updateDlgObject.exists()) {
				UiObject cancelBtnObject = new UiObject(new UiSelector().className("android.widget.TextView").text("鍙栨秷"));
				if (cancelBtnObject != null && cancelBtnObject.exists()) {
					try {
						cancelBtnObject.clickAndWaitForNewWindow(10000L);
						Util.TakeRest(2000L);
						Util.PrintExecLog(4, "click cancel update btn");
					} catch (Exception e) {
					}
				}
			}
			tryNums--;
		} while (tryNums > 0);
					
		// wait delay view disappear
		Util.PrintExecLog(4, "check if there exist delay disappear view");
		long starttime = SystemClock.uptimeMillis();
		while (SystemClock.uptimeMillis() - starttime < 20000L) {
			UiObject delayDisappearObject = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/aab"));
			if (!delayDisappearObject.exists()) {
				break;
			}
			Util.TakeRest(1000L);
		}
		
		// operate guide views
		Util.PrintExecLog(4, "swipe right guide view");
		int nGuideForwardCount = 0;
		for (int i=1; i<=10; i++) {
			UiScrollable guideScroll = new UiScrollable(new UiSelector().resourceId("com.taobao.taobao:id/vp_guide_photos")).setAsHorizontalList();
			if (!guideScroll.exists()) {
				break;
			}
			try {
				guideScroll.flingForward();
			} catch (UiObjectNotFoundException e1) {
			}
			
			if (i > 4) {
				try {
					guideScroll.clickAndWaitForNewWindow(10000L);
				} catch (UiObjectNotFoundException e1) {
				}
				Util.TakeRest(1000L);
			}
			nGuideForwardCount++;
			Util.TakeRest(500L);
		}
		
		// try the reverse method
		if (nGuideForwardCount > 10) {
			Util.PrintExecLog(4, "swipe left guide view");
			for (int j=1; j<=10; j++) {
				UiObject guideObject = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/vp_guide_photos"));
				if (!guideObject.exists()) {
					break;
				}
				try {
					guideObject.swipeLeft(20);
				} catch (UiObjectNotFoundException e1) {
				}
				if (j > 4) {
					try {
						guideObject.clickAndWaitForNewWindow(10000L);
					} catch (UiObjectNotFoundException e1) {
					}
					Util.TakeRest(1000L);
				}
				Util.TakeRest(500L);
			}
		}
	}
	
	private void abnormalViewProcess() {

		try {
			UiObject localObject = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/TBDialog_buttons_Cancel").textContains("绋嶅悗鍐嶈"));
			if (localObject == null || !localObject.exists()) {
				localObject = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/TBDialog_buttons_Cancel").textContains("涓嶅啀鎻愰啋"));
				if (localObject != null && localObject.exists()) {
					localObject.clickAndWaitForNewWindow(10000L);
					Util.PrintExecLog(4, "detect abnormal view TBDialog");
					return;
				}
			}
	
			// while input keyword
			if (this.m_TaskStatus.compareTo(TaskStatus.STATUS_INPUT_KEYWORD) == 0) {
				UiObject localObject2 = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/up"));
				if (localObject2 != null && !localObject2.exists()) {
					localObject2 = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/kakalib_button_nav_left"));
				}
				if (localObject2 != null && localObject2.exists()) {
					localObject2.clickAndWaitForNewWindow(10000L);
					Util.PrintExecLog(4, "remove kakalib_button_nav_left");
					return;
				}
				
				localObject2 = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/tv_title").text("棣栭〉"));
				if (localObject2 != null && localObject2.exists()) {
					localObject2.clickAndWaitForNewWindow(10000L);
					Util.PrintExecLog(4, "click main page");
					return;
				}
			}
		
			// while check filter order or check filter condition
			if (this.m_TaskStatus.compareTo(TaskStatus.STATUS_CHECK_FILTER_ORDER) == 0 ||
					this.m_TaskStatus.compareTo(TaskStatus.STATUS_CHECK_FILTER_CONDITION) == 0) {
				UiObject localObject2 = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/searchbtn"));
				if (localObject2 != null && localObject2.exists()) {
					localObject2.clickAndWaitForNewWindow(10000L);
					Util.TakeRest(1000L);
					return;
				}
			}
	
			if (!getUiDevice().isScreenOn()) {
				getUiDevice().wakeUp();
			}
		
			UiObject localObject3 = new UiObject(new UiSelector().resourceId("android:id/message").textContains("鏃犲搷搴斻�傝灏嗗叾鍏抽棴鍚楋紵"));
			if (localObject3 != null && localObject3.exists()) {
				UiObject localObject4 = new UiObject(new UiSelector().text("纭畾"));
				if (localObject4 != null && localObject4.exists()) {
					localObject4.clickAndWaitForNewWindow(10000L);
					Util.PrintExecLog(4, "remove unresponse view");
				}
			}
		} catch (Exception e) {
			
		}
	}

	private UiScrollable findUiScrollableById(String Id) {
		UiScrollable localScrollable = new UiScrollable(getUiSelectorById(Id));
		if (localScrollable != null && localScrollable.exists()) {
			return localScrollable;
		}
		
		if (this.m_TaskStatus.compareTo(TaskStatus.STATUS_SEARCH_CLICK_GOODS) == 0) {
			UiObject searchBtnObject = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/searchbtn"));
			if (searchBtnObject != null && searchBtnObject.exists()) {
				try {
					searchBtnObject.clickAndWaitForNewWindow(10000L);
				} catch (UiObjectNotFoundException e) {

				}
				Util.PrintExecLog(4, "cannot find UiScrollable but find searchbtn, click it");
				Util.TakeRest(1000L);
				handleSearchStyleMode();
			}
		}
		
		Util.TakeRest(1000L);
		localScrollable = new UiScrollable(new UiSelector().resourceId(Id));
		if (localScrollable != null && localScrollable.exists()) {
			return localScrollable;
		}
		
		localScrollable = new UiScrollable(new UiSelector().scrollable(true));
		return localScrollable;
	}

	private void handleSearchStyleMode() {
		try {
			UiObject localObject = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/style_img"));
			if (localObject != null && localObject.exists() && localObject.getText().indexOf("?") == -1) {
				localObject.clickAndWaitForNewWindow(10000L);
				Util.TakeRest(1000L);
			}
		} catch (Exception e) {
			
		}
	}

	/***************************************************/
	
	public void testDemo() throws UiObjectNotFoundException {
        getUiDevice().pressHome();
	}	
	
	public void StartTaobao() {
		String packageAppString = "com.taobao.taobao/com.taobao.tao.welcome.Welcome";
		String resultString = Util.ExecCmd("am start -n " + packageAppString);

		if (resultString.toLowerCase().indexOf("cmp=" + packageAppString.toLowerCase()) == -1) {
			Util.PrintExecLog(0, "start taobao failed");
		} else {
			int tryNums = 3;
			UiObject promptBoxObject = new UiObject(new UiSelector().resourceId("com.mediatek.security:id/checkbox"));
			try {
				do {				
					promptBoxObject = new UiObject(new UiSelector().resourceId("com.mediatek.security:id/checkbox"));
					if (promptBoxObject != null && promptBoxObject.exists()) {
						promptBoxObject.clickAndWaitForNewWindow(10000L);
						Util.TakeRest(1000);
						UiObject allowBtnObject = new UiObject(new UiSelector().resourceId("android:id/button1").text("鍏佽"));
						if (allowBtnObject != null && allowBtnObject.exists()) {
							allowBtnObject.clickAndWaitForNewWindow(10000L);
							Util.TakeRest(1000);
						}
					}
					tryNums--;
					Util.TakeRest(1000L);
				} while (tryNums > 0);	
			} catch (Exception e) {

			}
			Util.PrintExecLog(1, "start taobao success");
		}
	}
	
	public void test_inputKeyWords() {
		String param = getParams().getString("param");
		JSONObject paramObj;
		String keyWord;
		try {
			paramObj = new JSONObject(Util.Base64Decode(param));
			keyWord = paramObj.getString("keyword");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		this.m_TaskStatus = TaskStatus.STATUS_INPUT_KEYWORD;	
		if (keyWord == null || keyWord.equals("")) {
			Util.PrintExecLog(0, "search keyword is empty");
			return;
		}
		Util.PrintExecLog(4, "keyWord is " + keyWord);
		
		//clsoe taobao
		getUiDevice().pressBack();
		Util.TakeRest(1000L);
		getUiDevice().pressBack();
		Util.TakeRest(1000L);
		getUiDevice().pressBack();
		Util.TakeRest(1000L);
		getUiDevice().pressBack();
		Util.TakeRest(1000L);
		getUiDevice().pressBack();
		Util.TakeRest(1000L);
		
		//check taobao running
		UiObject tbObject = new UiObject(new UiSelector().packageName("com.taobao.taobao"));
		if (!tbObject.exists()) {
			Util.PrintExecLog(4, "taobao is not running, restart taobao");
			StartTaobao();
			Util.TakeRest(4000L);
		} 
		
		try {
			UiObject homeSearchObject = findUiObjectByResourceId("com.taobao.taobao:id/home_searchedit");
			if (homeSearchObject != null && homeSearchObject.exists()) {
				homeSearchObject.clickAndWaitForNewWindow(10000L);
				Util.TakeRest(1500L);
				Util.PrintExecLog(4, "home search edit clicked");
				
				homeSearchObject = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/home_searchedit"));
				if (homeSearchObject != null && homeSearchObject.exists()) {
					homeSearchObject.clickTopLeft();
					Util.TakeRest(2000L);
					Util.PrintExecLog(4, "home search edit topleft clicked");
				}
			} else {
				processHomeDisturb();
				Util.TakeRest(200L);
				
				// retry check home search edit, and click again
				Util.PrintExecLog(4, "recheck home search edit");
				homeSearchObject = findUiObjectByResourceId("com.taobao.taobao:id/home_searchedit");
				if (homeSearchObject != null && homeSearchObject.exists()) {
					homeSearchObject.clickAndWaitForNewWindow(10000L);
					Util.TakeRest(1500L);
					homeSearchObject = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/home_searchedit"));
					if (homeSearchObject != null && homeSearchObject.exists()) {
						homeSearchObject.clickTopLeft();
						Util.TakeRest(2000L);
					}
				} else {
					// if home search edit is EditText
					UiObject homeEditTextObject = new UiObject(new UiSelector().className("android.widget.EditText"));
					if (homeEditTextObject != null && homeEditTextObject.exists()) {
						homeEditTextObject.clickAndWaitForNewWindow(10000L);
						Util.TakeRest(1500L);
					} else {
						Util.PrintExecLog(0, "click search edit fail");
						return;
					}
				}
			}
						
			// now at search page
			Util.PrintExecLog(4, "check search edit");
			UiObject searchEditObject = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/searchEdit"));
			if (searchEditObject == null || !searchEditObject.exists()) {
				Util.TakeRest(1000L);
				searchEditObject = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/searchedit"));
				if (searchEditObject == null || !searchEditObject.exists()) {
					Util.PrintExecLog(0, "search edit cannot find");
					return;
				}
			}
			
			Util.PrintExecLog(4, "find search edit");
			String oldKeyWords = searchEditObject.getText();
			if (oldKeyWords.equals("")) {
				Util.TakeRest(1000L);
				oldKeyWords = searchEditObject.getText();
			}
			
			if (!oldKeyWords.equals("")) {
				UiObject editDelObject = findUiObjectByResourceId("com.taobao.taobao:id/edit_del_btn");
				if (editDelObject != null && editDelObject.exists()) {
					editDelObject.clickAndWaitForNewWindow(10000L);
					Util.TakeRest(1000L);
				}
			}
			
			SetInputMethod();
			Util.TakeRest(1000L);
			
			String text = encodeToModifiedUtf7(keyWord);
			searchEditObject.setText(text);// input keywords
			Util.PrintExecLog(4, "input keyword complete");
			UiObject searchBtnObject = findUiObjectByResourceId("com.taobao.taobao:id/searchbtn");
			searchBtnObject.clickAndWaitForNewWindow(10000L);
			Util.TakeRest(4000L);
			
			UiObject disturbUiObject = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/closeBtn"));
			if (disturbUiObject != null && disturbUiObject.exists()) {
				disturbUiObject.clickAndWaitForNewWindow(10000L);
			}
			Util.PrintExecLog(1, "input keyword success");
			
			Util.TakeRest(3000L);
			UiObject sortBarObject = new UiObject(new UiSelector().text("店铺"));
			if (sortBarObject == null || !sortBarObject.exists()) {
					Util.PrintExecLog(0, "cannot position shop bar");
					return;
			}

			sortBarObject.clickAndWaitForNewWindow(10000L);
			Util.PrintExecLog(1, "shop tab clicked");
			Util.TakeRest(20000L);
			int count = 0;
			while (!isStoreListPage()) {
				count++;
				Util.TakeRest(2000L);
				Util.PrintExecLog(1, "wait the shop list");
				if(count>150){
					Util.PrintExecLog(0, "wait too long for shoplist");
					break;
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public void test_findStoreInfo() {	
		try{
			String param = getParams().getString("param");
			JSONObject paramObj;
			int  searchcount;
			try {
				paramObj = new JSONObject(Util.Base64Decode(param));
				searchcount = paramObj.getInt("count");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			//
			if(!backToStorelist()){
				Util.PrintExecLog(0, "TASK_FAIL");
				return;
			}
			Map<String,Boolean>doneMap = new HashMap<String,Boolean>();
			int instance = 1;
			int count = 0;
		
			while(count < searchcount ) {
				if(!backToStorelist()){
					Util.PrintExecLog(0, "TASK_FAIL");
					return;
				}
				UiObject notfind = new UiObject(new UiSelector().text("没有找到相关的店铺"));
				if(notfind.exists()){
					Util.PrintExecLog(0, "NO STORE maybe key err");
					return;
				}
				UiObject finishObj = new UiObject(new UiSelector().text("没有更多宝贝了"));
				if(finishObj.exists()){
					Util.PrintExecLog(1, "TASK_FINISH no store left to get info ");
					return;
				}
				UiScrollable listViewScrollable = findUiScrollableById("com.taobao.taobao:id/search_listview");
				if (listViewScrollable == null || !listViewScrollable.exists()) {
					Util.PrintExecLog(0, "cannot position search list view");
					return;
				}
				listViewScrollable.setSwipeDeadZonePercentage(0.2D);
				UiObject goodsObject = listViewScrollable.getChildByInstance(new UiSelector().resourceId("com.taobao.taobao:id/shopTitle"), instance);
				if (goodsObject == null || !goodsObject.exists()) {
					listViewScrollable.scrollForward(35);
					Util.PrintExecLog(1, "scroll forward");
					instance = 1;
					count++;
					continue;
				}
				
				String storeName = goodsObject.getText();
				if(!doneMap.containsKey(storeName)){
					doneMap.put(storeName, true);
					Util.PrintExecLog(1, "find instance " + instance + "; count=" + count);
					findStoreTel(goodsObject);
				}
				else {
					Util.PrintExecLog(1, "this store alreay searched");
				}
				count++;
				instance++;
			}
			return;
			
		} catch (Exception e) {
			
		}
		
		Util.PrintExecLog(1, "Task Done.");
	}
	
	private void findStoreTel(UiObject goodsObject) {
		
		String title = null;
		try {
			title = goodsObject.getText();
			goodsObject.clickAndWaitForNewWindow(10000L);
			
			Util.PrintExecLog(1, "shop title: " + title);
			Util.TakeRest(5000L);
			//find logo
	
			UiObject shopLogoObject =  new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/iv_logo"));
			if (shopLogoObject == null || !shopLogoObject.exists()) {
				Util.PrintExecLog(0, "cannot position shop logo");
//				getUiDevice().pressBack();
				return;
			}
			
			//click logo
			shopLogoObject.clickAndWaitForNewWindow(10000L);
			Util.TakeRest(5000L);
			
			String tel = null;
			UiObject telTitleObject = new UiObject(new UiSelector().description("服务电话"));
		
			if (telTitleObject == null || !telTitleObject.exists()) {
				telTitleObject = new UiObject(new UiSelector().text("服务电话"));
				if(!telTitleObject.exists()){
					Util.PrintExecLog(0, "this store has no tel");
					return;
				}
				UiObject telObject = telTitleObject.getFromParent(new UiSelector().index(1));
				tel = telObject.getText();
			}
			else {
				UiObject telParentObject = telTitleObject.getFromParent(new UiSelector().className("android.widget.FrameLayout"));
				if (telParentObject == null || !telParentObject.exists()) {
					Util.PrintExecLog(0, "cannot position tel");
					return;
				}
				UiObject telObject = telParentObject.getChild(new UiSelector().className("android.view.View"));
				tel = telObject.getContentDescription();
			}
			Util.PrintExecLog(1, "tel=" + tel);
			//save to db
			postToDb(title, tel);
		} catch (UiObjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}				
	}
	private boolean backToStorelist() {
		int count = 0;
		
		while (!isStoreListPage()) {
			Util.TakeRest(2000L);
			getUiDevice().pressBack();
			count++;
			if(count>15){
				Util.PrintExecLog(0,"in prehandle,return to storelist err");
				return false;
			}
		}
		Util.PrintExecLog(1,"now in storelist page");
		return true;
	}

	private boolean isStoreListPage() {
		UiObject list = new UiObject(new UiSelector().resourceId("com.taobao.taobao:id/search_listview"));
		UiObject xiaoliang = new UiObject(new UiSelector().descriptionContains("销量优先"));
		UiObject xinyong = new UiObject(new UiSelector().descriptionContains("信用"));
		if(list.exists() && xiaoliang.exists() && xinyong.exists()){
			return true;
		}
		return false;
	}

	public void postToDb(String name, String tel) {
		HttpClient client = new DefaultHttpClient();
		String url = "";
		//http://120.132.26.247/spider/putshopinfo.php?shopid=774360&shoptel=12345678&shopname=testshop 
		try {
			String encodename = java.net.URLEncoder.encode(name, "UTF-8");
			url = "http://120.132.26.247/uploadshopinfo.php?shop=" + encodename + "&telephone=" + tel;
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HttpGet request = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(request);

		}catch (ClientProtocolException e) {

		} catch (IOException e) {
		}
	}


}