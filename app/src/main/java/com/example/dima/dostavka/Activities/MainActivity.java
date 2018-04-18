package com.example.dima.dostavka.Activities;

import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.dima.dostavka.Helper.OrderAdapter;
import com.example.dima.dostavka.R;

import java.util.List;

import com.example.dima.dostavka.Helper.*;
import ru.profit_group.scorocode_sdk.Callbacks.CallbackFindDocument;
import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;
import ru.profit_group.scorocode_sdk.scorocode_objects.Query;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    private ListView listMainActivity;
    private OrderAdapter adapter;

    private static final String COLLECTION_NAME = "work";

    private DocumentFields fields;

    private Switch aSwitch;

    void online(){
        Toast.makeText(this, "onLine", LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aSwitch = findViewById(R.id.swOnline);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    Toast.makeText(MainActivity.this, "onLine", LENGTH_SHORT).show();

                }
                else
                    Toast.makeText(MainActivity.this, "offLine", LENGTH_SHORT).show();
            }
        });



        fields = new DocumentFields(this);

        listMainActivity = findViewById(R.id.listMainActivity);

        listMainActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              //  Intent intent = new Intent(MainActivity.this, DetailOrder.class);
               // startActivity(intent);
                // Передача данных заказа в DetailOrder
                // TODO
            }
        });

   }



   public void startWork() {
       Log.i("Loog", "Вход startWork");
        Query query = new Query(COLLECTION_NAME);
       // query.setFieldsForSearch(list);
       Log.i("Loog", "Запуск запроса");
        query.findDocuments(new CallbackFindDocument() {
            @Override
            public void onDocumentFound(List<DocumentInfo> documentInfos) {
                Log.i("Loog", "Документ получен!");
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

            public static void display(Context context) {
                context.startActivity(new Intent(context, MainActivity.class));
            }

    @Override
    protected void onResume() {
        super.onResume();

        startWork();
    }



}