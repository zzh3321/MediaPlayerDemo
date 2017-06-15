package com.zzh.mediaplayerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zzh.zmediaplayer.ZMediaPlayer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private String url = "http://so1.111ttt.com:8282/2016/1/12m/10/205100120468.m4a?tflag=1496396510&pin=3a362" +
            "9ec22202b2c17521393b9ef5e98&ip=113.247.23.217#.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ZMediaPlayer mediaPlayer = (ZMediaPlayer) findViewById(R.id.mediaplayer);
        mediaPlayer.setUp(url,"");
    }
}
