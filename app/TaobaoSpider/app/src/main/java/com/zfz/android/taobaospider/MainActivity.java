package com.zfz.android.taobaospider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.zfz.android.taobaospider.DaemonService.VMCoreService;
import com.zfz.android.taobaospider.util.AssetsHelper;
import com.zfz.android.taobaospider.util.LogHelper;
import com.zfz.android.taobaospider.util.Tool;
import com.zfz.android.taobaospider.util.ViewLogHelper;


public class MainActivity extends AppCompatActivity {

    private Button act;
    private Boolean isStart = false;
    private TextView status;
    private TextView viewlog;
    private TextView finishedcount;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        try {
//            new AssetsHelper(getApplicationContext()).copy();
//        }
//        catch (Exception e){
//            LogHelper.e("err"+e);
//        }
        final MainActivity activity = this;
        act = (Button) findViewById(R.id.button);
        act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStart){
                    act.setText("开始");
                    new TaobaoTask(activity).kill();
                    status.setText("任务已经结束");
                }
                else {
                    act.setText("结束");
                    new TaobaoTask(activity).start();
                    status.setText("任务已经开始");
                }
                isStart = !isStart;
            }
        });
        finishedcount = (TextView)findViewById(R.id.count);
        finishedcount.setText("0");
        status = (TextView)findViewById(R.id.status);
        viewlog = (TextView)findViewById(R.id.viewlog);
        viewlog.setMovementMethod(ScrollingMovementMethod.getInstance());
        startCoreProcess();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0) {
                    //msg.obj是获取handler发送信息传来的数据
                    @SuppressWarnings("unchecked")
                    String log  =(String) msg.obj;
                    ViewLogHelper.getInstance().pushLog(log);
                    viewlog.setText(ViewLogHelper.getInstance().getLog());
                }
                else if(msg.what == 1){
                    @SuppressWarnings("unchecked")
                    String count  =(String) msg.obj;
                    //给ListView绑定数据
                    finishedcount.setText(count);
                }
            }
        };
    }
    private void startCoreProcess() {
        startService(new Intent(getApplicationContext(), VMCoreService.class));
    }
    public void setCount(String count){
        handler.sendMessage(handler.obtainMessage(1, Tool.getDatetime()+":"+count));
    }
    public void setLog(String log){
        handler.sendMessage(handler.obtainMessage(0,Tool.getDatetime()+":"+log));
    }
}


