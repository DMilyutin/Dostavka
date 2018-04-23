package com.example.dima.dostavka.Helper;

import android.util.Log;

import com.example.dima.dostavka.Activities.MainActivity;


public class UpdateThread extends Thread {




    public UpdateThread(){
        super("Поток обновления");
        Log.i("Loog", "Создан второй поток " + this);
        //start();
    }


    public void run(){
        MainActivity mainActivity = new MainActivity();
        Log.i("Loog", "Поток запущен" + this);
        try {
            for(int i =0; i<10; i++) {
                Thread.sleep(1000);
                Log.i("Loog", "Поток Работает " + i + this);
                mainActivity.startWork();
            }
        } catch (InterruptedException e) {
            Log.i("Loog", "Поток прерван");
        }

    }


}
