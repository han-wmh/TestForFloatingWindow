package dongzhong.webview;
/*
Copyright (C) 2021 Galaxy Auto Technology


All Rights Reserved by Galaxy Auto Technology Co., Ltd and its affiliates.

You may not use, copy, distribute, modify, transmit in any form this file

except in compliance with Galaxy Auto Technology in writing by applicable law.

*/

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.constraint.ConstraintLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * 处理Javascript的对话框、网站图标、网站标题以及网页加载进度等
 *
 * Created by wmh on 2021/12/26.
 */
public class CustomWebChromeClient extends WebChromeClient {
    /**
     * context
     */
    private Context context;

    /**
     * webView
     */
    private WebView webView;

    /** 视频全屏参数 */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private WindowManager windowManager;
    private FrameLayout fullscreenContainer;
    private CustomViewCallback customViewCallback;

    public CustomWebChromeClient(Context context, WindowManager windowManager, WebView webView) {
        this.context = context;
        this.windowManager = windowManager;
        this.webView = webView;
    }

    /*** 视频播放相关的方法 **/

    @Override
    public View getVideoLoadingProgressView() {
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        return frameLayout;
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        showCustomView(view, callback);
    }

    @Override
    public void onHideCustomView() {
        hideCustomView();
    }

    /** 视频播放全屏 **/
    private void showCustomView(View view, CustomViewCallback callback) {
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }
        fullScreen();

        fullscreenContainer = new FullscreenHolder(context);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        windowManager.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }

    /** 隐藏视频全屏 */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }
        fullScreen();
        setStatusBarVisibility(true);
        windowManager.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        webView.setVisibility(View.VISIBLE);
    }

    /** 全屏容器界面 */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void fullScreen() {
//        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else {
//            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
    }

    /**
     * 设置status bar可见性
     *
     * @param visible
     */
    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        context.getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}