# MediaPlayerDemo

一个简单的音频播放器demo

This is a simple MediaPlayer Demo

* `注意 : 项目中添加了一个MediaPlayerService , 使用的时候建议增加一个SplashActivity延迟2秒进入主页面 , 让服务进行初始化 , 否则会导致bindservice还没有获取到service对象导致项目空指针`

## Usage:

1.Import library

    compile 'com.zmediaplayer:zmediaplayer:1.1.0'
    
2.initialise in your application

    ZMediaPlayerManager.bind(Application context);
    
3.Add MediaPlayer in your layout

    <com.zzh.zmediaplayer.ZMediaPlayer
        android:id="@+id/mediaplayer"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
    </com.zzh.zmediaplayer.ZMediaPlayer>
    
4.set the media List urls or string url

    ZMediaPlayer mediaPlayer = (ZMediaPlayer) findViewById(R.id.mediaplayer);
    mediaPlayer.setUp(urls);
    //or
    //mediaPlayer.setUp(url)

本项目更多在于学习交流,其中可能存在一些bug,目前并不完善

![image](https://github.com/zzh3321/MediaPlayerDemo/raw/master/images/screenshot.png)
