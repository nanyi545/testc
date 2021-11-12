package com.ww.performancechore;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import leakcanary.AppWatcher;

//import leakcanary.AppWatcher;


/**
 *
 * use eclipse mat:
 *
 *
 * hprof-conv android.hprof mat.hprof
 * hprof-conv aas.hprof mat.hprof
 *
 */

/**
 *   use LeakCanary:
 *
 * LeakCanary: Watching instance of java.util.ArrayList (hehehe) with key 0f0cc7b4-eeff-46fb-8990-04158b05be07
 * LeakCanary: Found 2 objects retained, not dumping heap yet (app is visible & < 5 threshold)
 *
 *
 *    AppWatcher.INSTANCE.getObjectWatcher().expectWeaklyReachable(objectToWatch,"hehehe");
 *
 *
 */

/**
 *
 * use android studio profiler:
 *
 * upper right : instance view
 * lower right : references
 *
 * view depth(steps to GC root) / shallow heap / retained heap
 *
 */
public class LeakTest {

    public static void log(String content){
        Log.d("ffff","leak:"+content);
    }

    public static class MemObject{
        byte mem[];
        public MemObject() {
            this.mem = new byte[1024*1024/4];
        }
    }





    /**
     * 软引用
     *
     * 当一个对象只有软引用，若虚拟机内存空间足够，垃圾回收器就不会回收该对象；
     *
     * 若内存空间不足，下次GC时这些只有软引用对象将被回收。若垃圾回收器没有回收它，该对象就可以被程序使用。软引用可用来实现内存敏感的高速缓存。
     *
     * 在创建软引用实例时，可以传入一个引用队列（ReferenceQueue）将该软引用与引用队列关联，这样当软引用所引用的对象被垃圾回收器回收前，
     * Jvm虚拟机会把这个软引用加入到与之关联的引用队列中。
     *
     */

    /**
     *
     * 弱引用
     *
     * 弱引用与软引用的区别在于：只具有弱引用的对象拥有更短暂的生命周期。在GC发生时，若一个对象只有弱引用，不管虚拟机内存空间是否足够，都会回收它的内存。
     *
     * 在创建弱引用实例时，可以传入一个引用队列（ReferenceQueue）将该软引用与引用队列关联，这样当软引用所引用的对象被垃圾回收器回收前，
     * Jvm虚拟机就会把这个软引用加入到与之关联的引用队列中。
     *
     *
     *
     * https://juejin.cn/post/6936452012058312740
     *
     *
     *
     */
    public static void testWeak2(){

    }


    public static void testSoft(){
        MemObject o ;
        o = new MemObject();
        ReferenceQueue q= new ReferenceQueue();
        SoftReference s = new SoftReference(o,q);
        o = null;
        System.gc();
        try {
            /**
             * ref queue    not working ?? ....
             *
             * why ?????
             */
            log("queue:"+(q.remove(2000)==null));
        } catch (InterruptedException e) {
            e.printStackTrace();
            log("queue e:"+e );
        } finally {
            log("queue:final" );
        }
//        AppWatcher.INSTANCE.getObjectWatcher().expectWeaklyReachable(o,"cnm");  // ???
    }



    /**
     * 监控activity的  时机  ----- old way ....
     */
    public static void register(Application app){
        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks(){
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                LeakTest.log("onActivityCreated   activity:"+activity);
            }
            @Override
            public void onActivityStarted(@NonNull Activity activity) {
            }
            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                LeakTest.log("onActivityResumed   activity:"+activity);
            }
            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                LeakTest.log("onActivityPaused   activity:"+activity);
            }
            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }
            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }
            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                LeakTest.log("onActivityDestroyed   activity:"+activity);
            }
        });

    }


    /**
     *
     * heap dump
     *
     * HAHA ---->  This repository is DEPRECATED
     *
     * This project was created to provide a heap dump parser for LeakCanary by repackaging heap dump parsers from other repositories.
     * LeakCanary now has its own heap dump parser.
     * That parser is available on Maven Central as com.squareup.leakcanary:leakcanary-haha:2.0-alpha-1.
     *
     */
}
