package com.hehe.smartcamera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hehe.smartcamera.opgl.CameraRender;

public class GlRecordActivity extends AppCompatActivity {


    /**
     *   this demo activity use old cameraX api
     */
    public static boolean USE_OLD_CAMERAX_API = true;


    GLSurfaceView surface;
    CameraRender render;
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gl_record);
        surface = findViewById(R.id.my_surface);
        surface.setEGLContextClientVersion(2);
        render = new CameraRender(surface);
        surface.setRenderer(render);
        info = findViewById(R.id.info);


        timerhandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    timerhandler.sendEmptyMessageDelayed(1,1000);
                }
                if(msg.what==10087){
                    framesWaiting = msg.arg1;
                }
                if(msg.what==10088){
                    stopped=true;
                }

                updateinfo();
            }
        };
        setMainHandler(timerhandler);

    }



    private void updateinfo(){
        long elapse = (System.currentTimeMillis() - start)/1000;
        StringBuilder sb = new StringBuilder();
        int h = (int) (elapse/3600);
        int hremain = (int) (elapse - h*3600);
        int m = hremain/60;
        int sremain = hremain - m * 60;
        sb.append(" h:"+h);
        sb.append(" m:"+m);
        sb.append(" s:"+sremain);

        if(stopped) {
            String pre  = info.getText().toString();
            if(pre.endsWith("stop")){

            } else{
                info.setText(pre +" stop" );
            }
        } else {
            info.setText("frames waiting:"+framesWaiting+"\n time:"+sb.toString());
        }

    }


    boolean stopped = false;
    int framesWaiting;
    long start;
    Handler timerhandler;



    public void record(View view) {
        render.startRecord(1.0f);
        start = System.currentTimeMillis();
        timerhandler.sendEmptyMessageDelayed(1,1000);
        stopped  = false;
    }

    public void stopRecord(View view) {
        Log.d("timer","stop:");
        render.stopRecord();
    }

    public static Handler MainHandler;

    public static Handler getMainHandler() {
        return MainHandler;
    }

    public static void setMainHandler(Handler mainHandler) {
        MainHandler = mainHandler;
    }
}
