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


    public static final String COLLECTION_NAME = "work";



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
                setAdapter(documentInfos);

            }

            @Override
            public void onDocumentNotFound(String errorCode, String errorMessage) {
                Log.i("Loog", errorCode);
                }});
        }


   private void setAdapter(List<DocumentInfo> documentInfos) {
       Log.i("Loog", "Получен лист, создание адаптера");
        adapter = new OrderAdapter(this, documentInfos);
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



}