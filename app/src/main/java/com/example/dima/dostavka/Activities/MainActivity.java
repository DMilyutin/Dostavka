package com.example.dima.dostavka.Activities;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import android.widget.ListAdapter;
import android.widget.ListView;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvBalance = findViewById(R.id.tvBalanceMainActivity);
        listMainActivity = findViewById(R.id.listMainActivity);

        Intent intent = getIntent();
        String login = intent.getStringExtra("login");

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
        query.equalTo("phoneNumber", login);
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
        DocumentInfo documentInfo;
        documentInfo = documentInfos.get(0);
        driver.setId(documentInfo.getId());
        driver.setBalanceDriver(documentInfo.getString("balanceDriver"));
        tvBalance.setText(driver.getBalanceDriver());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.historiItem : {
                Intent intent = new Intent(MainActivity.this, historyActivity.class);
                intent.putExtra("IdDriver", driver.getId());
                startActivity(intent);
            }
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        new MyRunnable();
    }

    class MyRunnable implements Runnable{
    Thread thread;


    MyRunnable(){
        thread = new Thread(this, "Поток обновления");
        Log.i("Loog", "Второй поток");
        thread.start();

    }

    private void stop() throws InterruptedException {
    thread.join();
    }
        @Override
        public void run() {

            for (int i = 5; i < 9; i++) {

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