package com.zfz.android.taobaospider.util;

import java.util.LinkedList;
import java.util.List;

/*
* 此类显示最多log*/
public class ViewLogHelper {
    //最多显示50条
    private static int MAX_LOG_COUNT = 3;
    private static ViewLogHelper instance = null;
    private List<String> logList = new LinkedList<>();
    public static ViewLogHelper getInstance(){
        if (instance == null) {
            instance = new ViewLogHelper();
        }
        return instance;
    }
    public void pushLog(String log){
        if(logList.size()<MAX_LOG_COUNT){
            logList.add(Tool.getDatetime()+":"+log);
        }
        else {
            logList.remove(0);
            logList.add(Tool.getDatetime()+":"+log);
        }
    }
    public String getLog(){
        StringBuilder b = new StringBuilder();
        for (int i=0;i<logList.size();i++) {
            b.append(logList.get(i)+"\n");
        }
        return b.toString();
    }
}
