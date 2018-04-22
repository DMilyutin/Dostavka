package com.example.dima.dostavka.Activities;

import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;

import com.example.dima.dostavka.Helper.OrderAdapter;
import com.example.dima.dostavka.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.example.dima.dostavka.Helper.*;

import ru.profit_group.scorocode_sdk.Callbacks.CallbackFindDocument;
import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;
import ru.profit_group.scorocode_sdk.scorocode_objects.Query;

public class MainActivity extends AppCompatActivity {


    public static Driver driver = new Driver("","","","","",
            "","");

    private static final String COLLECTION_WORK_BALASHIHA = "work_balashiha";
    private static final String COLLECTION_DRIVERS_BALASHIHA = "drivers_balashiha";

    private ListView listMainActivity;
    private OrderAdapter adapter;
    private Adapter adapterT;


    private Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aSwitch = findViewById(R.id.swOnline);
        listMainActivity = findViewById(R.id.listMainActivity);

        Intent intent = getIntent();
        String login = intent.getStringExtra("login");

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // TODO Автообновление
                } else{}

            }
        });


       identification(login);
       //driver = new Driver(identification(login)[0],identification(login)[1],identification(login)[2],
       //        identification(login)[3],
       //        identification(login)[4],identification(login)[5],identification(login)[6]);
       //Helper.showToast(MainActivity.this, driver.getNameDriver());




        listMainActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Helper.showToast(MainActivity.this, driver.getNameDriver());
                Order order = adapter.getOrder(i);
                Intent intent = new Intent(MainActivity.this, DetailOrder.class);

                String[] orderS = {order.getNameCustomer(), order.getAddressCustomer(),
                        order.getCoastOrder(), order.getNumberOfAddress(), order.getIdOrder()};

                intent.putExtra("Order", orderS);
                intent.putExtra("IdDriver", driver.getId());
                startActivity(intent);

            }
        });

    }


    public void startWork() {

        Query query = new Query(COLLECTION_WORK_BALASHIHA);
        query.findDocuments(new CallbackFindDocument() {
            @Override
            public void onDocumentFound(List<DocumentInfo> documentInfos) {
                setAdapter(documentInfos);

            }

            @Override
            public void onDocumentNotFound(String errorCode, String errorMessage) {
                if (errorCode.equals("0")){
                    listMainActivity.setAdapter((ListAdapter) adapterT);

                    Helper.showToast(MainActivity.this, "Заказов нет");}
                else Helper.showToast(MainActivity.this, errorMessage);
            }
        });
    }


    private void setAdapter(List<DocumentInfo> documentInfos) {
        adapter = new OrderAdapter(this, documentInfos);
        listMainActivity.setAdapter(adapter);
    }




    private void identification(String login){


        Query query = new Query(COLLECTION_DRIVERS_BALASHIHA);
        query.equalTo("telephonNamber", login);
        query.findDocuments(new CallbackFindDocument() {
            @Override
            public void onDocumentFound(List<DocumentInfo> documentInfos) {
                initDriver(documentInfos);

            }

            @Override
            public void onDocumentNotFound(String errorCode, String errorMessage) {
                Helper.showToast(MainActivity.this, errorMessage);
            }
        });
    }

    private void initDriver(List<DocumentInfo> documentInfos){
        DocumentInfo documentInfo = new DocumentInfo();
        documentInfo = documentInfos.get(0);
        driver.setId(            documentInfo.getId());
        driver.setNameDriver(    documentInfo.getFields().get("nameDriver").toString());
        driver.setLastNameDriver(documentInfo.getFields().get("lastNameDriver").toString());
        driver.setCarDriver(     documentInfo.getFields().get("telephonNamber").toString());
        driver.setCarNumber(     documentInfo.getFields().get("carDriver").toString());
        driver.setTelephonNamber(documentInfo.getFields().get("carNumber").toString());
        driver.setBalanceDriver( documentInfo.getFields().get("balanceDriver").toString());

        //Helper.showToast(MainActivity.this, driver.getNameDriver());
    }


    @Override
    protected void onStart() {
        super.onStart();
        startWork();
    }
}