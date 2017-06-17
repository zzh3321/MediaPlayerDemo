package com.zzh.zmediaplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.List;


/**
 * 创建人:zzh ; 时间: 2017/6/1.
 * 描述:
 */

public class ZMediaPlayerService extends Service implements ZMediaPlayerInterface {
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

    public static final int CONTROL_NORMAL = 9;
    public static final int CONTROL_UP = 10;
    public static final int CONTROL_NEXT = 11;
    private int mControlState = CONTROL_NORMAL;

    private int mBufferPercentage;

    private String url;//播放的地址
    private List<String> listUrl;//播放地址
    private String id;//播放的id
    private int position = 0;//用来记录当前播放的url的索引

    @Override
    public void onCreate() {
        super.onCreate();
        mController = ZMediaPlayerController.instance(this);
        setController();
        initMediaPlayer();
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
        setDataAndStartMedia();
    }

    private void setDataAndStartMedia() {
        initializePlayerState();
        String s = getPlayUrl();
        try {
            mediaPlayer.setDataSource(s);
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
        mControlState = CONTROL_UP;
        setDataAndStartMedia();
    }

    @Override
    public void next() {
        mControlState = CONTROL_NEXT;
        setDataAndStartMedia();
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
        return (mediaPlayer != null && mCurrentState != STATE_PREPARING) ? mediaPlayer.getDuration() : 0;
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

    private MyBinder binder = new MyBinder();


    public class MyBinder extends Binder {
        public ZMediaPlayerService getService() {
            return ZMediaPlayerService.this;
        }
    }


    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    };

    private boolean onBufferingFirstChange = true;

    private MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            //有时候第一次进来直接会变成100然后又重头开始,暂时这么处理
            if (onBufferingFirstChange && percent == 100){
                onBufferingFirstChange = false;
                return;
            }
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

    private void initializePlayerState() {
        if (mediaPlayer != null) {
            //mediaPlayer.stop();
            mediaPlayer.reset();
            onBufferingFirstChange = true;
        }
    }

    private void initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setLooping(true);
            mediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        }
    }


    public void setController() {
        mController.setMediaPlayerInterface(this);
    }

    public void setLooping(boolean looping){
        mediaPlayer.setLooping(looping);
    }


    public void setUp(String url) {
        listUrl = null;
        this.url = url;
    }

    public void setUp(List<String> listUrl) {
        url = null;
        this.listUrl = listUrl;
    }

    public String getUrl() {
        return url;
    }


    private String getPlayUrl() {
        if (url != null) {
            return url;
        }

        if (listUrl != null) {
            updatePosition();
            return listUrl.get(position);
        }

        return null;
    }

    private void updatePosition() {
        switch (mControlState) {
            case CONTROL_NORMAL:
                break;
            case CONTROL_UP:
                if (position > 0) {
                    position--;
                } else {
                    position = listUrl.size() - 1;
                }
                mControlState = CONTROL_NORMAL;
                break;
            case CONTROL_NEXT:
                if (position < listUrl.size() - 1) {
                    position++;
                } else {
                    position = 0;
                }
                mControlState = CONTROL_NORMAL;
                break;
        }
    }
}
