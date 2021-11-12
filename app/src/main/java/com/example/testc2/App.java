package com.example.testc2;

import android.app.Application;
import android.app.Instrumentation;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.util.Printer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tvtb.jlib.MyClass;

import org.apache.commons.collections4.set.UnmodifiableSet;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicLong;

import xcrash.XCrash;


/**
 * App启动
 *
 *
 * 1.  入口 --> ActivityThread.main()
 *
 * http://androidxref.com/8.1.0_r33/xref/frameworks/base/core/java/android/app/ActivityThread.java
 * 6459    public static void main(String[] args) {
 * ...
 * 6478        Looper.prepareMainLooper();
 * 6479
 * 6480        ActivityThread thread = new ActivityThread();
 * 6481        thread.attach(false);
 * 6483        if (sMainThreadHandler == null) {
 * 6484            sMainThreadHandler = thread.getHandler();
 * 6485        }
 * 6486
 *
 *
 *
 * 2.  ActivityThread.attach()
 *
 * 6315    private void attach(boolean system) {
 * ...
 * 6328            final IActivityManager mgr = ActivityManager.getService();
 * 6329            try {
 * 6330                mgr.attachApplication(mAppThread);
 * 6331            } catch (RemoteException ex) {
 * 6332                throw ex.rethrowFromSystemServer();
 * 6333            }
 *
 *
 *
 * 3.  IPC ---> AMS.attachApplication()
 *
 * http://androidxref.com/8.1.0_r33/xref/frameworks/base/services/core/java/com/android/server/am/ActivityManagerService.java
 *
 * 7214    @Override
 * 7215    public final void attachApplication(IApplicationThread thread) {
 * 7216        synchronized (this) {
 * 7217            int callingPid = Binder.getCallingPid();
 * 7218            final long origId = Binder.clearCallingIdentity();
 * 7219            attachApplicationLocked(thread, callingPid);
 * 7220            Binder.restoreCallingIdentity(origId);
 * 7221        }
 * 7222    }
 *
 *
 *
 * ----> attachApplicationLocked
 * 6914        // Find the application record that is being attached...  either via
 * 6915        // the pid if we are running in multiple processes, or just pull the
 * 6916        // next app record if we are emulating process with anonymous threads.
 *
 *                 http://androidxref.com/8.1.0_r33/xref/frameworks/base/services/core/java/com/android/server/am/ProcessRecord.java
 * 6917        ProcessRecord app;
 *
 *
 *
 * IApplicationThread thread:  AMS 中 ApplicationThread的代理
 *
 * -----> IApplicationThread.bindApplication()
 *
 *
 *
 *
 * 4.   ApplicationThread
 *
 * http://androidxref.com/8.1.0_r33/xref/frameworks/base/core/java/android/app/ActivityThread.java
 * -----> ApplicationThread.bindApplication()
 *        public final void bindApplication(..)
 *
 * 916            AppBindData data = new AppBindData();
 * 917            data.processName = processName;
 * 918            data.appInfo = appInfo;
 * 919            data.providers = providers;
 * 920            data.instrumentationName = instrumentationName;       // 后面用来生成Instrument
 * 921            data.instrumentationArgs = instrumentationArgs;
 * 922            data.instrumentationWatcher = instrumentationWatcher;
 * 923            data.instrumentationUiAutomationConnection = instrumentationUiConnection;
 * 924            data.debugMode = debugMode;
 * 925            data.enableBinderTracking = enableBinderTracking;
 * 926            data.trackAllocation = trackAllocation;
 * 927            data.restrictedBackupMode = isRestrictedBackupMode;
 * 928            data.persistent = persistent;
 * 929            data.config = config;
 * 930            data.compatInfo = compatInfo;
 * 931            data.initProfilerInfo = profilerInfo;
 * 932            data.buildSerial = buildSerial;
 * 933            sendMessage(H.BIND_APPLICATION, data);
 *
 *
 * ----->  ActivityThread.H
 *
 * 1462    private class H extends Handler {
 * ...
 * ...
 * 1653                case BIND_APPLICATION:
 * 1654                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "bindApplication");
 * 1655                    AppBindData data = (AppBindData)msg.obj;
 * 1656                    handleBindApplication(data);
 * 1657                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
 * 1658                    break;
 *
 *
 * ----->  ApplicationThread.handleBindApplication()
 *
 * 5429    private void handleBindApplication(AppBindData data) {
 *
 *
 * 生成instrument
 * 5673            try {
 * 5674                final ClassLoader cl = instrContext.getClassLoader();
 * 5675                mInstrumentation = (Instrumentation)cl.loadClass(data.instrumentationName.getClassName()).newInstance();
 *
 *
 * 生成App
 * 5709        Application app;
 * ...
 * 5715            app = data.info.makeApplication(data.restrictedBackupMode, null);
 *
 *
 *
 *
 *
 */

//  androidx.camera.camera2.impl.Camera2Initializer  ??

public class  App extends Application {

    Instrumentation ins;

    public static App getInstance(){
        return instance;
    }

    static App instance;

    HandlerThread t;
    class CheckHandler extends Handler {
        public CheckHandler(@NonNull Looper looper) {
            super(looper);
        }
        public CheckHandler(@NonNull Looper looper, @Nullable Callback callback) {
            super(looper, callback);
        }
        public void restPeriod(){
            float workLoad = (work.get()+0.0f)/3000l;
            Log.d("watcher","workload:"+workLoad);
            work.set(0);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    restPeriod();
                }
            },3000);
        }
        AtomicLong work = new AtomicLong(0);
        public void addWorkTime(long workTime){
            work.addAndGet(workTime);
        }
    }
    CheckHandler checkHandler;



    class WorkHandler extends Handler {
        public WorkHandler(@NonNull Looper looper) {
            super(looper);
        }

        public WorkHandler(@NonNull Looper looper, @Nullable Callback callback) {
            super(looper, callback);
        }

        public void startPeriodWork(){
            long start = System.currentTimeMillis();
            int j = 0;
            for (int i=0;i<200000000;i++){
                j+=i;
            }
            long end = System.currentTimeMillis();
            Log.d("watcher","heavy work:"+(end-start));
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    startPeriodWork();
                }
            },4000);
        }
    }



    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        t = new HandlerThread("checker");
        t.start();
        checkHandler = new CheckHandler(t.getLooper());
        checkHandler.restPeriod();

        Looper.getMainLooper().setMessageLogging(new Printer() {
            Stack<Long> s = new Stack();
            @Override
            public void println(String x) {
                if(x.startsWith(">>>")){
                    s.push(System.currentTimeMillis());
                } else {
                    long preTime = s.pop();
                    long diff = System.currentTimeMillis() - preTime;
                    checkHandler.addWorkTime(diff);
                }
            }
        });

//        WorkHandler workHandler = new WorkHandler(Looper.getMainLooper());
//        workHandler.startPeriodWork();




//        File f = new File("/sdcard/Download/textc_x");

        //   /storage/self/primary/Android/data/com.example.testc2/cache
        File f = new File(getExternalCacheDir().getAbsolutePath());
        if(!f.exists()){
            f.mkdirs();
        }
        Log.d("hehe","location:"+f.getAbsolutePath()+"  exist:"+f.exists()+"   direct:"+f.isDirectory());

        XCrash.init(this, new XCrash.InitParameters()
                .setJavaRethrow(true)
                .setJavaLogCountMax(10)
                .setJavaDumpAllThreadsWhiteList(new String[]{"^main$", "^Binder:.*", ".*Finalizer.*"})
                .setJavaDumpAllThreadsCountMax(10)
//                .setJavaCallback(callback)
                .setNativeRethrow(true)
                .setNativeLogCountMax(10)
                .setNativeDumpAllThreadsWhiteList(new String[]{"^xcrash\\.sample$", "^Signal Catcher$", "^Jit thread pool$", ".*(R|r)ender.*", ".*Chrome.*"})
                .setNativeDumpAllThreadsCountMax(10)
//                .setNativeCallback(callback)
                .setAnrRethrow(true)
                .setAnrLogCountMax(10)
//                .setAnrCallback(callback)
                .setPlaceholderCountMax(3)
                .setPlaceholderSizeKb(512)
                .setLogDir(f.getAbsolutePath())
                .setLogFileMaintainDelayMs(1000));


        Set set =  UnmodifiableSet.unmodifiableSet( new HashSet<>());

    }

}
