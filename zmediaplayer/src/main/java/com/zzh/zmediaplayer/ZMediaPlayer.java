package com.zzh.zmediaplayer;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

/**
 * 创建人:zzh ; 时间: 2017/6/15.
 * 描述:ZMediaPlayer
 */

public class ZMediaPlayer extends FrameLayout {
    private Context context;

    private ZMediaPlayerController playerController;
    private View view;
    private ZMediaPlayerService mediaPlayerService;

    public ZMediaPlayer(@NonNull Context context) {
        this(context, null);
    }

    public ZMediaPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZMediaPlayer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        playerController = ZMediaPlayerController.instance(context);
        mediaPlayerService = ZMediaPlayerManager.getMediaPlayerService();
        init();
    }

    private void init() {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(getLayoutId(), null);
        }
        playerController.setView(view);
        addView(view);
    }

    public int getLayoutId() {
        return R.layout.item_player;
    }


    public void setUp(String url) {
        mediaPlayerService.setUp(url);
    }

    public void setUp(List<String> url) {
        mediaPlayerService.setUp(url);
    }
}
