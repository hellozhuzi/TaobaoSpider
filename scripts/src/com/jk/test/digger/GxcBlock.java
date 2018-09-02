package com.jk.test.digger;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.jk.test.BaseCase;
import com.jk.test.util.Util;

import android.graphics.Rect;

public class GxcBlock extends BaseCase{
	public void test() {
		startGxc();
		gain();
//		startDigger();
	}
	public void  startGxc() {
		String cmd = "am start -n com.gxb.wallet.app/com.gxb.sdk.showcase.LanuchActivity ";
		Util.ExecCmd(cmd);
		sleep(500);
	}
	private void gain() {
		UiObject web = new UiObject(new UiSelector().className("android.webkit.WebView"));
		try {
			Rect rect = web.getBounds();
			int max_x = 16;
			int max_y = 12;
			int x_begin = rect.left;
			int x_end = rect.right;
			int width_step = (x_end-x_begin)/max_x;
			int y_begin = rect.top;
			int y_end = rect.bottom;		
			//click from y 20%~ 60%
			double y_min = 0.2;
			double y_max = 0.4;
			y_begin = (int)(y_begin+rect.height()*y_min);
			y_end = (int)(y_begin+rect.height()*y_max);
			int height_step = (y_end-y_begin)/max_y;
			System.out.println("y_setp:"+height_step);		
			System.out.println("click form "+y_begin+" to "+y_end);
			for (int i=0;i<max_y;i++){
				for (int j=0;j<max_x;j++){	
					int x = x_begin+width_step*j;
					int y = y_begin+height_step*i;					
					getUiDevice().click(x,y);
					System.out.println("click x:"+x+" y:"+y);
					sleep(100);
				}				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}				
	}
}
