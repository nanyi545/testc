package com.ww.performancechore;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.WeakHashMap;

/**
 * 容灾处理
 */
public class DisasterTolerance {
    private static final String TAG = DisasterTolerance.class.getSimpleName();

    private static DisasterTolerance ourInstance;

    private boolean enable = true;

    private WeakHashMap<Looper,ExceptionFilter> leWeakHashMap = new WeakHashMap<>();

    public static DisasterTolerance getInstance() {
        if (ourInstance == null){
            synchronized (DisasterTolerance.class){
                if (ourInstance == null){
                    ourInstance = new DisasterTolerance();
                }
            }
        }
        return ourInstance;
    }

    private DisasterTolerance() {
    }

    public boolean isEnable() {
        return enable;
    }

    public static void destroy(){
        synchronized (DisasterTolerance.class){
            if (ourInstance!=null){
                ourInstance.leWeakHashMap.clear();
            }
            ourInstance = null;
        }
    }

    /**
     * 捕获Looper异常
     * @param looper
     */
    public void catchLooperException(Looper looper, ExceptionFilter exceptionFilter) {
        if (!isEnable()){
            return;
        }
        if (looper!=null){
            if (!leWeakHashMap.containsKey(looper)){
                new Handler(looper).post(new Runnable() {

                    private void doLoopWithErrorCatch() {
                        try {
                            Log.d("gggg","pre call loop:"+Looper.myLooper().isCurrentThread() );
                            Looper.myLooper().loop();
                            Log.d("gggg","after call loop:" );  // this will not get called ...
                        } catch (Throwable e) {
                            ExceptionFilter exceptionFilter1 = leWeakHashMap.get(Looper.myLooper());
                            if (exceptionFilter1 !=null && exceptionFilter1.filterException(e)) {
                                doLoopWithErrorCatch();
                            } else {
                                throw e;
                            }
                        }
                    }

                    @Override
                    public void run() {
                        try {
                            doLoopWithErrorCatch();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            leWeakHashMap.put(looper,exceptionFilter);
        }
    }


    /**
     * 异常过滤，方便进行某个类型的异常处理
     */
    public interface ExceptionFilter {
        boolean filterException(Throwable throwable);
    }
}
