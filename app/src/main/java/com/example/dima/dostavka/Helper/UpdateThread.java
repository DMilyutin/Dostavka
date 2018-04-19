package com.example.dima.dostavka.Helper;

import com.example.dima.dostavka.Activities.MainActivity;

public class UpdateThread extends Thread {

    MainActivity mainActivity = new MainActivity();

    public UpdateThread(){
        super("Поток обновления");
        start();
    }


    public void run(){
        try {

            Thread.sleep(100);
            mainActivity.startWork();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void stopThead(){

    }
}
