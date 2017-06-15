package com.zzh.zmediaplayer;

/**
 * 创建人:zzh ; 时间: 2017/6/1.
 * 描述:
 */

public interface ZMediaPlayerInterface {

    void start();
    void restart();
    void up();
    void next();
    void pause();
    void release();
    void seekTo(int progress);

    int getDuration();
    int getCurrentPosition();
    int getBufferPercentage();

    boolean isIdle();
    boolean isPreparing();
    boolean isPrepared();
    boolean isPlaying();
    boolean isPaused();
    boolean isError();
    boolean isCompleted();

    boolean isPlayerPaused();
}
