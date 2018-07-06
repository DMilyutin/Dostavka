package com.milyutin.dima.dostavka;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.milyutin.dima.dostavka.Activities.MainActivity;

public class ServiceForMainAct extends Service{



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Loog", "onCreate");
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i("Loog", "onStartCommand");
        someTask();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Loog", "onDestroy");
    }

    void someTask(){
        final MainActivity m = new MainActivity();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i<5; i++){
                    Log.i("Loog", "Thread is " +i);
                    m.startWork();
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
