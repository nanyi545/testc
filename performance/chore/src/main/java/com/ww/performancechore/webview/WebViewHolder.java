package com.ww.performancechore.webview;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewHolder {

    /**
     * 结合手Q、网易严选、美团等其他方面优化方案，大致如下：
     *
     * WebView在Application中提前初始化 ------------
     * 实现WebView复用
     * 另开WebView进程
     * DNS解析优化（接口与网页主域名一致）------------
     * 线上资源压缩、CDN加速
     * 静态直出，直接下发首屏html
     * 离线预推，下发离线包，并增量更新
     * WebView创建与网络请求并行
     * 网页按节点局部刷新
     * 自定义实现图片资源缓存
     * 重新定义图片加载格式，shareP
     * 本地资源拦截替换 ------------
     *
     */
    static WebView web;

    public static WebView getWeb() {
        if(web.getParent()!=null){
            ViewGroup p = (ViewGroup) web.getParent();
            p.removeView(web);
        }
        return web;
    }

    public static void initWeb(Application app){
        long t1 = System.currentTimeMillis();
        web = new WebView(app);
        Log.d("wwww","webview create:"+( System.currentTimeMillis() - t1) );
        WebSettings setting = web.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setDatabaseEnabled(true);
        setting.setDomStorageEnabled(true);

        web.setWebChromeClient(new WebChromeClient(){
        });
        web.setWebViewClient(new WebViewClient(){
            long startTime;
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                startTime = System.currentTimeMillis();
                Log.d("wwww","onPageStarted");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                long elapse=System.currentTimeMillis() - startTime;
                Log.d("wwww","onPageFinished:"+elapse);
            }
        });
        web.loadUrl("https://main.m.taobao.com/");

    }



}
