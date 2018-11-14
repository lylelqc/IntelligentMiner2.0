package com.sly.app.utils;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import com.sly.app.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 01 on 2017/6/9.
 */
public class TimeUtils {
    private int time=60;

    private Timer timer;

    private Button btnSure;

    private String btnText;

    public TimeUtils(Button btnSure, String btnText) {
        super();
        this.btnSure = btnSure;
        this.btnText = btnText;
    }

    public  void StopTimer(){
        timer.cancel();
        btnSure.setText(btnText);
        btnSure.setEnabled(true);
        btnSure.setBackgroundColor(Color.WHITE);
        btnSure.setTextColor(Color.parseColor("#0aaadc"));
    }



    public void RunTimer(){
        timer=new Timer();
        TimerTask task=new TimerTask() {

            @Override
            public void run(){
                time--;
                Message msg=handler.obtainMessage();
                msg.what=1;
                handler.sendMessage(msg);

            }
        };


        timer.schedule(task, 100, 1000);
    }


    private Handler handler =new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:

                    if(time>0){
                        btnSure.setEnabled(false);
                        btnSure.setText(time+"秒后重发");
                        btnSure.setTextColor(Color.WHITE);
                        btnSure.setBackgroundResource(R.drawable.buttontwostyle);
                    }else{
                        timer.cancel();
                        btnSure.setText("重发");
                        btnSure.setEnabled(true);
                        btnSure.setBackgroundColor(Color.WHITE);
                        btnSure.setTextColor(Color.parseColor("#0aaadc"));
                    }
                    break;
                default:
                    break;
            }

        }
    };
}
