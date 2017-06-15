package com.zzh.zmediaplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * 创建人:zzh ; 时间: 2017/6/15.
 * 描述:
 */

public class ZMediaPlayerManager {
    private static final String TAG = "ZMediaPlayerManager";

    private static Context mApplicationContext;
    private static ZMediaPlayerService mediaPlayerService;

    public static ZMediaPlayerService getMediaPlayerService(){
        return mediaPlayerService;
    }

    public static void bind(Context context) {
        mApplicationContext = context;
        startMusicService();
        bindMusicService();
    }

    /**
     * 绑定service
     */
    private static void bindMusicService() {
        Intent intent = new Intent(mApplicationContext, ZMediaPlayerService.class);
        mApplicationContext.bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    /**
     * 回调onServiceConnected 函数，通过IBinder 获取 Service对象，实现Activity与 Service的绑定
     */
    private static ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                Toast.makeText(mApplicationContext, "bind success", Toast.LENGTH_SHORT).show();
                mediaPlayerService = ((ZMediaPlayerService.MyBinder) (service)).getService();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bindMusicService();
        }
    };

    /**
     * 启动MusicService服务
     */
    private static void startMusicService() {
        Intent intent = new Intent(mApplicationContext, ZMediaPlayerService.class);
        mApplicationContext.startService(intent);
    }
}
