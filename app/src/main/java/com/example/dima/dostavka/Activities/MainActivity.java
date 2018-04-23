package com.example.dima.dostavka.Activities;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import android.widget.Switch;
import android.widget.TextView;

import com.example.dima.dostavka.Helper.OrderAdapter;
import com.example.dima.dostavka.R;

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
    private TextView tvBalance;


    private Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aSwitch = findViewById(R.id.swOnline);
        tvBalance = findViewById(R.id.tvBalanceMainActivity);
        listMainActivity = findViewById(R.id.listMainActivity);

        Intent intent = getIntent();
        String login = intent.getStringExtra("login");

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                   new MyRunnable();
                } else{
                    //myRunnable.stop();
                     }

            }
        });


       identification(login);

        listMainActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Order order = adapter.getOrder(i);
                    startDetailOrder(order); }
        });

    }

    private void startDetailOrder(Order order){
        Intent intent = new Intent(MainActivity.this, DetailOrder.class);

        String[] orderS = {order.getNameCustomer(), order.getAddressCustomer(),
                order.getCoastOrder(), order.getNumberOfAddress(),
                order.getIdOrder(), order.getAddressForDriver()};

        intent.putExtra("Order", orderS);
        intent.putExtra("IdDriver", driver.getId());
        intent.putExtra("BalanceDriver", driver.getBalanceDriver());

        //Helper.showToast(MainActivity.this, (String.valueOf(driver.getBalanceDriver())));
        startActivity(intent);
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
                if (errorCode.equals("0")) {
                    listMainActivity.setAdapter((ListAdapter) adapterT);

                    //Helper.showToast(MainActivity.this, "Заказов нет");}
                }
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
        //driver.setNameDriver(    documentInfo.getFields().get("nameDriver").toString());
        //driver.setLastNameDriver(documentInfo.getFields().get("lastNameDriver").toString());
        //driver.setCarDriver(     documentInfo.getFields().get("telephonNamber").toString());
        //driver.setCarNumber(     documentInfo.getFields().get("carDriver").toString());
        //driver.setTelephonNamber(documentInfo.getFields().get("carNumber").toString());
        //driver.setBalanceDriver((NumberdocumentInfo.getFields().get("balanceDriver")));
        driver.setBalanceDriver(documentInfo.getString("balanceDriver"));
       // orderAcсesS(Integer.getInteger(driver.getBalanceDriver()));
        tvBalance.setText(driver.getBalanceDriver());
    }


    @Override
    protected void onStart() {
        super.onStart();
        startWork();
    }


    class MyRunnable implements Runnable{
    Thread thread;

    MyRunnable(){
        thread = new Thread(this, "Поток обновления");
        Log.i("Loog", "Второй поток");
        thread.start();
    }

    public void stop(){
        thread.stop();
    }
        @Override
        public void run() {

            for (int i = 5; i > 0; i++) {
                Log.i("Loog", "Второй поток: " + i);
                startWork();
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    Log.i("Loog", "Второй поток прерван " + i);
                }
            }
    }

}

}