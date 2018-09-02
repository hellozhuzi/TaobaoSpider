package com.zfz.android.taobaospider.util;



import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Tool {
    public static String getDatetime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date=simpleDateFormat.format(new Date(System.currentTimeMillis()));
        return date;
    }
    public static String ConvertToSizeString(long size)
    {
        long GB = size / 1024 / 1024 / 1024;
        if (GB < 1)
        {
            long MB = size / 1024 / 1024;
            if (MB < 1)
            {
                long KB = size / 1024;
                if (KB < 1)
                {
                    return size + "B";
                }
                else
                {
                    return KB + "KB";
                }
            }
            else
            {
                return MB + "MB";
            }
        }
        else
        {
            return GB + "GB";
        }
    }
    public static String ConvertToSpeedString(int speed)
    {
        double GB = speed / 1024d / 1024d / 1024d;
        if (GB < 1)
        {
            double MB = speed / 1024d / 1024d;
            if (MB < 1)
            {
                double KB = speed / 1024d;
                if (KB < 1)
                {
                    return speed + "B/s";
                }
                else
                {
                    return KB + "KB/s";
                }
            }
            else
            {
                return MB + "MB/s";
            }
        }
        else
        {
            return GB + "GB/s";
        }
    }
    public static String RandomCode(int length)
    {
        String[] numberList = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
        StringBuilder num = new StringBuilder();

        for (int i = 0; i < length; i++)
        {
            num.append(numberList[(int)(Math.random()*numberList.length)]);
        }
        return num.toString();
    }
    public static String SignPassword(String paramString)
    {
        if (paramString == null)
        {
            return "";
        }
        String md5Str = GetMD5(paramString);
        byte[] b = md5Str.getBytes();
        StringBuilder sb = new StringBuilder(md5Str);
        byte c1 = b[2];
        byte c2 = b[8];
        byte c3 = b[17];
        byte c4 = b[27];
        sb.deleteCharAt(27);
        sb.deleteCharAt(17);
        sb.deleteCharAt(8);
        sb.deleteCharAt(2);

        sb.insert(2,(char)c2);
        sb.insert(8, (char)c1);
        sb.insert(17, (char)c4);
        sb.insert(27, (char)c3);
        return GetMD5(sb.toString());
    }
    public static String GetMD5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try {
                md.update(plainText.getBytes("UTF8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer(200);
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset] & 0xff;
                if (i < 16) buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {

            return null;
        }
    }


    public static String getShortFloat(String f,int length) {
        if(f.contains(".")) {
            String frac = f.substring(f.indexOf(".") + 1);
            String s = f.substring(0,f.indexOf(".")+1);
            for(int i=0;i<length;i++){
                s+= frac.substring(i,i+1);
            }
            return s;
        }
        else {
            return f;
        }


    }
}
