package com.jk.test.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Base64;


public class Util {
			
	public static String ExecCmd(String paramString) {
		BufferedReader bufferedReader;
		StringBuffer stringBuffer;
		
		try {
			Process process = Runtime.getRuntime().exec(paramString);
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			char[] arrayOfChar = new char[1024];
			stringBuffer = new StringBuffer();
			int i = bufferedReader.read(arrayOfChar);
			if (i > 0) {
				stringBuffer.append(arrayOfChar, 0, i);
			}
			bufferedReader.close();
			process.waitFor();
			String resultString = stringBuffer.toString();
			return resultString;
		} catch (Exception e) {
			return "Exception";
		}		
	}
	public static String execCommand(String command) 
	{
	    // start the ls command running
	    //String[] args =  new String[]{"sh", "-c", command};
	    try 
	    {
		    Process proc = Runtime.getRuntime().exec(command);        //杩欏彞璇濆氨鏄痵hell涓庨珮绾ц瑷�闂寸殑璋冪敤
		        //濡傛灉鏈夊弬鏁扮殑璇濆彲浠ョ敤鍙﹀涓�涓閲嶈浇鐨別xec鏂规硶
		        //瀹為檯涓婅繖鏍锋墽琛屾椂鍚姩浜嗕竴涓瓙杩涚▼,瀹冩病鏈夌埗杩涚▼鐨勬帶鍒跺彴
		        //涔熷氨鐪嬩笉鍒拌緭鍑�,鎵�浠ラ渶瑕佺敤杈撳嚭娴佹潵寰楀埌shell鎵ц鍚庣殑杈撳嚭
	        InputStreamReader inputstreamreader = new InputStreamReader(proc.getInputStream());
	        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
	        // read the ls output
	        String line = "";
	        StringBuilder sb = new StringBuilder(line);
	        while ((line = bufferedreader.readLine()) != null) 
	        {
	            //System.out.println(line);
	            sb.append(line);
	            sb.append('\n');
	        }
	        //tv.setText(sb.toString());
	        //浣跨敤exec鎵ц涓嶄細绛夋墽琛屾垚鍔熶互鍚庢墠杩斿洖,瀹冧細绔嬪嵆杩斿洖
	        //鎵�浠ュ湪鏌愪簺鎯呭喌涓嬫槸寰堣鍛界殑(姣斿澶嶅埗鏂囦欢鐨勬椂鍊�)
	        //浣跨敤wairFor()鍙互绛夊緟鍛戒护鎵ц瀹屾垚浠ュ悗鎵嶈繑鍥�
	        if (proc.waitFor() != 0) 
	        {
                System.err.println("exit value = " + proc.exitValue());
            }
	        return sb.toString();
        }
        catch (Exception e) 
	    {  
            System.err.println(e);
            return e.toString();
        }
    }
	
	public static String toUnicode(String s) {
        String s1 = "";
        for (int i = 0; i < s.length(); i++) {
            s1 +="\\u" +  Integer.toHexString(s.charAt(i));
 
        }
        return s1;
    }
	
	public static void TakeRest(long millsecs) {
		try {
			Thread.sleep(millsecs);
		} catch (InterruptedException e) {

		}
	}
	
	public static String Base64Decode(String data) {
		byte[] decoded_data = Base64.decode(data.getBytes(), Base64.DEFAULT);
		String ssString = new String(decoded_data);
		return ssString;
	}
	
	public static String Base64Encode(String data) {
		byte[] encoded_data = Base64.encode(data.getBytes(), Base64.DEFAULT);
		String ssString = new String(encoded_data);
		return ssString;
	}
	public static String getDatetime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date=simpleDateFormat.format(new Date(System.currentTimeMillis()));
		return date;
	}
	public static void PrintExecLog(int paramInt, String paramString) {
		
	    if (paramInt == 1)
	    {
	      System.out.println(getDatetime()+"[Success]:" + paramString);
	      return;
	    }
	    if (paramInt == 0)
	    {
	      System.out.println(getDatetime()+"[Fail]:" + paramString);
	      return;
	    }
	    if (paramInt == 2)
	    {
	      System.out.println(getDatetime()+"[Warn]:" + paramString);
	      return;
	    }
	    if (paramInt == 3) // retry
	    {
	      System.out.println(getDatetime()+"[Error]:" + paramString);
	      return;
	    }
	    System.out.println(paramString);
	}
}
