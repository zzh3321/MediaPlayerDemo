package com.zzh.mediaplayerdemo;

import android.app.Application;

import com.zzh.zmediaplayer.ZMediaPlayerManager;

/**
 * 创建人:zzh ; 时间: 2017/6/15.
 * 描述:
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ZMediaPlayerManager.bind(this);
    }
}
