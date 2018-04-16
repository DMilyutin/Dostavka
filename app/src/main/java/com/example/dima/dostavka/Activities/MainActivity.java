package com.example.dima.dostavka.Activities;


import android.content.Context;
import android.content.Intent;

import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import android.widget.Toast;


import com.example.dima.dostavka.Helper.OrderAdapter;
import com.example.dima.dostavka.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.example.dima.dostavka.Helper.*;
import ru.profit_group.scorocode_sdk.Callbacks.CallbackFindDocument;
import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;
import ru.profit_group.scorocode_sdk.scorocode_objects.Query;

public class MainActivity extends AppCompatActivity {

    private ListView listMainActivity;
    private OrderAdapter adapter;
    private ArrayList<Map<String, String>> data;

    public static final String COLLECTION_NAME = "work";

    private String nameCast = "NameCustomer";
    private String townCast = "TownCustomer";
    private String coastCast ="CoastOrder";

    private DocumentFields fields;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fields = new DocumentFields(this);


        listMainActivity = findViewById(R.id.listMainActivity);


        //startWork();

   }



   public void startWork() {
       Log.i("Loog", "Вход startWork");
        Query query = new Query(COLLECTION_NAME);
       // query.setFieldsForSearch(list);
       Log.i("Loog", "Запуск запроса");
        query.findDocuments(new CallbackFindDocument() {
            @Override
            public void onDocumentFound(List<DocumentInfo> documentInfos) {
                // Вылетает приложение
                if (documentInfos != null){

                    setAdapter(documentInfos);
                    }
            }

            @Override
            public void onDocumentNotFound(String errorCode, String errorMessage) {
                Log.i("Loog", errorCode);
                }});
        }


   private void setAdapter(List<DocumentInfo> documentInfos) {
       Log.i("Loog", "Получен лист, создание адаптера");
        adapter = new OrderAdapter(MainActivity.this, documentInfos);
        listMainActivity.setAdapter(adapter);
            }

            public static void display(Context context) {  // Метод активации главного экрана
                context.startActivity(new Intent(context, MainActivity.class));
            }

    @Override
    protected void onResume() {
        super.onResume();
        startWork();
    }


    public void starttt(View view) {
        Log.i("Loog", "Кнопка отработала1");

    }
}