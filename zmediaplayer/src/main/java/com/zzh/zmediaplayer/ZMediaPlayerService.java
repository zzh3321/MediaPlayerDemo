package com.zzh.zmediaplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;


/**
 * 创建人:zzh ; 时间: 2017/6/1.
 * 描述:
 */

public class ZMediaPlayerService extends Service implements ZMediaPlayerInterface{
    public static final int STATE_ERROR = -1;          // 播放错误
    public static final int STATE_IDLE = 0;            // 播放未开始
    public static final int STATE_PREPARING = 1;       // 播放准备中
    public static final int STATE_PREPARED = 2;        // 播放准备就绪
    public static final int STATE_PLAYING = 3;         // 正在播放
    public static final int STATE_PAUSED = 4;          // 暂停播放
    public static final int STATE_COMPLETED = 6;       // 播放完成

    private static final String TAG = "ZMediaPlayerService";

    private static MediaPlayer mediaPlayer;
    private ZMediaPlayerController mController;
    private int mCurrentState = STATE_IDLE;

    public static final int PLAYER_NORMAL = 7;
    public static final int PLAYER_PAUSED = 8;//手动暂停

    private int mPlayerState = PLAYER_NORMAL;

    private int mBufferPercentage;

    private String url;//播放的地址
    private String id;//播放的id

    @Override
    public void onCreate() {
        super.onCreate();
        mController = ZMediaPlayerController.instance(this);
        setController();
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        }
    }

    private MyBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        public ZMediaPlayerService getService() {
            return ZMediaPlayerService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }



    @Override
    public void start() {
        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        mCurrentState = STATE_PREPARING;
        mController.setControllerState(mPlayerState, mCurrentState);
    }

    @Override
    public void restart() {
        mediaPlayer.start();
        mCurrentState = STATE_PLAYING;
        mPlayerState = PLAYER_NORMAL;
        mController.setControllerState(mPlayerState, mCurrentState);
    }

    @Override
    public void up() {

    }

    @Override
    public void next() {

    }

    @Override
    public void pause() {
        if (mCurrentState == STATE_PLAYING) {
            mediaPlayer.pause();
        }
        if (mCurrentState == STATE_PREPARING) {
            mPlayerState = PLAYER_PAUSED;
        }
        mCurrentState = STATE_PAUSED;
        mController.setControllerState(mPlayerState, mCurrentState);
    }

    @Override
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    @Override
    public int getDuration() {
        return mediaPlayer != null ? mediaPlayer.getDuration() : 0;
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0;
    }

    @Override
    public int getBufferPercentage() {
        return mBufferPercentage;
    }

    @Override
    public boolean isIdle() {
        return mCurrentState == STATE_IDLE;
    }

    @Override
    public boolean isPreparing() {
        return mCurrentState == STATE_PREPARING;
    }

    @Override
    public boolean isPrepared() {
        return mCurrentState == STATE_PREPARED;
    }

    @Override
    public boolean isPlaying() {
        return mCurrentState == STATE_PLAYING;
    }

    @Override
    public boolean isPaused() {
        return mCurrentState == STATE_PAUSED;
    }

    @Override
    public boolean isError() {
        return mCurrentState == STATE_ERROR;
    }

    @Override
    public boolean isCompleted() {
        return mCurrentState == STATE_COMPLETED;
    }

    @Override
    public boolean isPlayerPaused() {
        return mPlayerState == PLAYER_PAUSED;
    }


    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {

        }
    };

    private MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            mBufferPercentage = percent;
        }
    };

    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mCurrentState = STATE_PREPARED;
            if (mPlayerState != PLAYER_PAUSED) {
                mp.start();
                mCurrentState = STATE_PLAYING;
            }
            mController.setControllerState(mPlayerState, mCurrentState);
        }
    };


    public void setController() {
        ZMediaPlayerController mController = ZMediaPlayerController.instance(this);
        mController.setMediaPlayerInterface(this);
    }


    public void setUp(String url,String id){
        this.url = url;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }
}
