package com.example.dima.dostavka.Helper;

import android.content.Context;
import android.widget.Toast;

public class Helper {


   public static void showToast(Context context, String textRes) {
        Toast.makeText(context, (textRes), Toast.LENGTH_SHORT).show();
    }

}
