package com.zzh.zmediaplayer;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 创建人:zzh ; 时间: 2017/6/15.
 * 描述:
 */

public class ZMediaPlayerController implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private TextView playerTime;
    private TextView playerTotalTime;
    private SeekBar seekBar;
    private ImageButton playNext;
    private ImageButton playBegin;
    private ImageButton playUp;

    private Timer mUpdateProgressTimer;
    private TimerTask mUpdateProgressTimerTask;
    private CountDownTimer mDismissTopBottomCountDownTimer;

    private Context mContext;

    private static ZMediaPlayerInterface mPlayerInterface;

    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");

    private static ZMediaPlayerController playerController;

    public static ZMediaPlayerController instance(Context mContext) {
        if (playerController == null) {
            playerController = new ZMediaPlayerController(mContext);
        }
        return playerController;
    }

    public ZMediaPlayerController(Context mContext){
        this.mContext = mContext;
    }

    public void setView(View view) {
        playerTime = (TextView) view.findViewById(R.id.player_time);
        playerTotalTime = (TextView) view.findViewById(R.id.player_total_time);
        seekBar = (SeekBar) view.findViewById(R.id.player_seekBar);
        playNext = (ImageButton) view.findViewById(R.id.player_next);
        playBegin = (ImageButton) view.findViewById(R.id.player_begin);
        playUp = (ImageButton) view.findViewById(R.id.player_up);
        seekBar.setOnSeekBarChangeListener(this);
        playUp.setOnClickListener(this);
        playBegin.setOnClickListener(this);
        playNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == playBegin) {//开始播放或者暂停
            if (mPlayerInterface.isPlaying() || mPlayerInterface.isPreparing()) {
                mPlayerInterface.pause();
            } else if (mPlayerInterface.isPaused() ||
                    (mPlayerInterface.isPrepared() && mPlayerInterface.isPlayerPaused())) {
                mPlayerInterface.restart();
            }
            if (mPlayerInterface.isIdle()) {
                mPlayerInterface.start();
            }
        } else if (v == playNext) {//下一首
            mPlayerInterface.next();
        } else if (v == playUp) {//上一首
            mPlayerInterface.up();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {//不判断会导致持续走下面的方法,音乐卡顿
            mPlayerInterface.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void setControllerState(int playerState, int playState) {
        switch (playState) {
            case ZMediaPlayerService.STATE_IDLE:
                break;
            case ZMediaPlayerService.STATE_PREPARING:
                playBegin.setImageResource(R.drawable.player_play_big);
                break;
            case ZMediaPlayerService.STATE_PREPARED:
                if (playerState == ZMediaPlayerService.PLAYER_PAUSED) {
                    playBegin.setImageResource(R.drawable.player_stop_big);
                } else {
                    playBegin.setImageResource(R.drawable.player_play_big);
                    startUpdateProgressTimer();
                }
                break;
            case ZMediaPlayerService.STATE_PAUSED:
                playBegin.setImageResource(R.drawable.player_stop_big);
                cancelUpdateProgressTimer();
                break;
            case ZMediaPlayerService.STATE_PLAYING:
                playBegin.setImageResource(R.drawable.player_play_big);
                startUpdateProgressTimer();
                break;
        }
    }

    private void startUpdateProgressTimer() {
        cancelUpdateProgressTimer();
        if (mUpdateProgressTimer == null) {
            mUpdateProgressTimer = new Timer();
        }
        if (mUpdateProgressTimerTask == null) {
            mUpdateProgressTimerTask = new TimerTask() {
                @Override
                public void run() {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            updateProgress();
                        }
                    });
                }
            };
        }
        mUpdateProgressTimer.schedule(mUpdateProgressTimerTask, 0, 300);
    }


    private void cancelUpdateProgressTimer() {
        if (mUpdateProgressTimer != null) {
            mUpdateProgressTimer.cancel();
            mUpdateProgressTimer = null;
        }
        if (mUpdateProgressTimerTask != null) {
            mUpdateProgressTimerTask.cancel();
            mUpdateProgressTimerTask = null;
        }
    }

    private void updateProgress() {
        int position = mPlayerInterface.getCurrentPosition();
        int duration = mPlayerInterface.getDuration();
        int bufferPercentage = mPlayerInterface.getBufferPercentage();
        seekBar.setMax(duration);
        seekBar.setProgress(position);
        seekBar.setSecondaryProgress(bufferPercentage);
        playerTime.setText(time.format(position));
        playerTotalTime.setText(time.format(duration));
    }

    public void setMediaPlayerInterface(ZMediaPlayerInterface zMediaPlayerInterface) {
        mPlayerInterface = zMediaPlayerInterface;
    }
}
