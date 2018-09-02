package com.zfz.android.taobaospider;


import com.zfz.android.taobaospider.util.AssetsHelper;
import com.zfz.android.taobaospider.util.LogHelper;
import com.zfz.android.taobaospider.util.ShellUtil;

public class TaobaoTask extends Thread{
    private static boolean bwork = false;
    private static int count = 0;
    private MainActivity activity;
    public TaobaoTask(MainActivity activity){
        this.activity = activity;
    }
    @Override
    public void run() {
        try {
            bwork = true;
            while (bwork) {
                activity.setLog("脚本已经开始执行");
                doTask();
                Thread.sleep(10000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void doTask(){
        LogHelper.i("start taobao autocase");
//        String jarpath = AssetsHelper.jarPath;
        String jarpath = "UiTest.jar";
        String cmd = "uiautomator runtest "+jarpath
                +" -c com.jk.test.TaobaoTklGetStoreInfo#test ";
        ShellUtil.CommandResult ret = ShellUtil.execCommand(cmd,true);
        String sucMsg = ret.successMsg;
        if (sucMsg == null){
            activity.setLog("这个设备不能正确运行脚本");
            bwork = false;
    }
        else if(sucMsg.contains("get info shoptel")){
                activity.setLog("成功获取电话");
                count++;
                activity.setCount(count+"");
        }
        else if(!sucMsg.contains("start taobao task")){
            activity.setLog("脚本运行出错:"+sucMsg);
//            bwork = false;
        }

    }
    public void kill(){
        try {
            bwork = false;
            ShellUtil.CommandResult rst = ShellUtil.execCommand(" ps | grep uiautomator",true);
            String result = rst.successMsg;
            String[] array = result.split("\n");
            for(String s : array){
                String Pid = null;
                String[] paramList = s.split("\\s+");
                if (paramList != null && paramList.length > 1) {
                    Pid = paramList[1];
                    String cmd = "kill -9 "+Pid;
                    ShellUtil.execCommand(cmd,true);
                }
            }
            Thread.sleep(200);
            activity.setLog("手动杀死脚本进程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
