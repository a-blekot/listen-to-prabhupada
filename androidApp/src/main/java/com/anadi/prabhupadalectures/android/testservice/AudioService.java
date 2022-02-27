package com.anadi.prabhupadalectures.android.testservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AudioService extends Service {

    private AudioServiceBinder audioServiceBinder = new AudioServiceBinder();

    public AudioService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return audioServiceBinder;
    }
}