package com.ww.performancechore.classloader;


import android.util.Log;

import com.example.mylibrary.TestLog2;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * DexClassLoader demo
 *
 * https://developer.aliyun.com/article/682888
 *
 * ****************
 *
 * view source   ???  below does not work
 * https://www.androidos.net.cn/sourcecode
 *
 *
 * --------------------
 *
 * DexClassLoader:
 *
 * /libcore/dalvik/src/main/java/dalvik/system/DexClassLoader.java
 *
 * DexClassLoader
 * http://androidxref.com/8.1.0_r33/xref/libcore/dalvik/src/main/java/dalvik/system/DexClassLoader.java
 *
 * BaseDexClassLoader
 * http://androidxref.com/8.1.0_r33/xref/libcore/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java
 *
 *
 * ------
 * source arrangement ....
 *
 *
 * http://androidxref.com/8.1.0_r33/xref/
 *
 *
 *
 */

/**
 * https://stackoverflow.com/questions/1735714/copy-java-object-class-from-one-classloader-to-another-classloader
 *
 *
 */

/**
 *
 * class-forname-vs-classloader-loadclass
 *
 *
 * https://stackoverflow.com/questions/8100376/class-forname-vs-classloader-loadclass-which-to-use-for-dynamic-loading#:~:text=Class.forName%28%29uses%20the%20caller%27s%20classloader%20and%20initializes%20the%20class,that%20there%27s%20a%20Class.forName%28%29that%20also%20takes%20a%20ClassLoader.
 *
 *
 * The first one (Class.forName("SomeClass");) will:
 *     use the class loader that loaded the class which calls this code
 *     initialize the class (that is, all static initializers will be run)
 *
 * The other (ClassLoader.getSystemClassLoader().loadClass("SomeClass");) will:
 *     use the "system" class loader (which is overridable)
 *     not initialize the class (say, if you use it to load a JDBC driver, it won't get registered, and you won't be able to use JDBC!)
 */
public class ClassLoaderTest {

    static List<Class> list = new ArrayList();
    private static void addClass(Class cls){
        int ind = 0;
        for(Class t:list){
            if(t.equals(cls)){
                Log.d("aaa","ind:"+ind+"  t:"+t+" cls:"+cls+"   equal");
            } else {
                Log.d("aaa","ind:"+ind+"  t:"+t+" cls:"+cls+"   not equal");
            }
            ind++;
        }
        list.add(cls);
    }


    public static void pluginLoad2(){
        Object o= null;
        try {
//            Class<?> cls = Class.forName("com.example.mylibrary.TestLog2");
//            if(cls==null){
//                Log.d("aaa","TestLog2 loaded:");
//            } else {
//                Log.d("aaa","TestLog2 not loaded:");
//            }

            String pluginPath = "/sdcard/Download/aaa/pluginapk1-debug.apk" ;
            String targetPath = "/sdcard/Download/bbb";
            DexClassLoader dexClassLoader = new DexClassLoader(pluginPath,targetPath,null, ClassLoaderTest.class.getClassLoader());
            Class<?> cls2 = dexClassLoader.loadClass("com.example.mylibrary.TestLog2");
            if(cls2!=null){
                Log.d("aaa","TestLog2 class:"+(cls2));
                addClass(cls2);
                o = cls2.newInstance();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            Log.d("aaa","TestLog2 instance:"+(o==null));
            if(o!=null){
                // error caught:java.lang.NoClassDefFoundError: Failed resolution of: Lcom/example/mylibrary/TestLog2;
                Log.d("aaa","TestLog2 is TestLog2:"+(o instanceof TestLog2));
            }

        }
    }


    public static void pluginLoad(){


        //hash code collision examples
//        String str1 = "Aa";
//        String str2 = "BB";
//        Log.d("aaa","str1:"+str1.hashCode()+"  str2:"+str2.hashCode());

        //
//        String str1 = "Bb";
//        String str2 = "CC";
//        Log.d("aaa","str1:"+str1.hashCode()+"  str2:"+str2.hashCode());
//  ----------end of hash code collision examples

        String pluginPath = "/sdcard/Download/aaa/pluginapk1-debug.apk" ;
        String targetPath = "/sdcard/Download/bbb";

        // if you create a new classloader to load class,  this class is visible only to this class loader .... 
        DexClassLoader dexClassLoader = new DexClassLoader(pluginPath,targetPath,null, ClassLoaderTest.class.getClassLoader());
        Log.d("aaa","cl:"+dexClassLoader);
        try {
            Class<?> cls = dexClassLoader.loadClass("com.hehe.pluginapk1.TestLog");
            if(cls!=null){
                addClass(cls);
            }
            Log.d("aaa","cls"+(cls==null?"null":cls));
            Object o = cls.newInstance();
            Log.d("aaa","o"+(o==null?"null":o));
            cls.getDeclaredMethod("say",String.class).invoke(cls.newInstance(),"AAAAAAAAAAAAA");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        try {
            Class<?> cls2 = Class.forName("com.hehe.pluginapk1.TestLog",true,dexClassLoader);
            Log.d("aaa","cls2"+(cls2==null?"null":cls2));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



        // not accessible by this classloader
        ClassLoader currentCl = ClassLoaderTest.class.getClassLoader();
        Log.d("aaa","current Cl"+(currentCl==null?"null":currentCl)); // dalvik.system.PathClassLoader
        Class<?> cls3=null;
        try {
            cls3 = Class.forName("com.hehe.pluginapk1.TestLog");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            Log.d("aaa","cls3"+(cls3==null?"null":cls3));
        }

    }

}