package com.zzh.mediaplayerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zzh.zmediaplayer.ZMediaPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

//    private String url = "http://so1.111ttt.com:8282/2016/1/12m/10/205100120468.m4a?tflag=1496396510&pin=3a362" +
//            "9ec22202b2c17521393b9ef5e98&ip=113.247.23.217#.mp3";

    private String[] urls = {"http://up.mcyt.net/md5/53/OTQyMzk=_Qq4329912.mp3",
            "http://up.mcyt.net/md5/53/MjY4NzAy_Qq4329912.mp3",
            "http://up.mcyt.net/md5/53/NzE0OTU4Mw_Qq4329912.mp3",
            "http://up.mcyt.net/md5/17/MTg0MzYyMQ_Qq4329912.mp3",
            "http://up.mcyt.net/md5/17/MTMzODQxNA_Qq4329912.mp3",
            "http://up.mcyt.net/md5/17/NDkwNzg5NA_Qq4329912.mp3"};

    private List<String> listUrl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ZMediaPlayer mediaPlayer = (ZMediaPlayer) findViewById(R.id.mediaplayer);
        Collections.addAll(listUrl, urls);
        mediaPlayer.setUp(listUrl);
    }
}
