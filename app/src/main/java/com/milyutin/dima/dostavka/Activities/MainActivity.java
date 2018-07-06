package com.milyutin.dima.dostavka.Activities;

import android.content.Intent;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;


import android.widget.Switch;
import android.widget.TextView;

import com.milyutin.dima.dostavka.Helper.OrderMainAdapter;
import com.milyutin.dima.dostavka.R;

import java.util.List;

import com.milyutin.dima.dostavka.Helper.*;

import ru.profit_group.scorocode_sdk.Callbacks.CallbackFindDocument;
import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;
import ru.profit_group.scorocode_sdk.scorocode_objects.Query;

public class MainActivity extends AppCompatActivity {



    Driver driver = new Driver("","","","","",
            "","");

    private static final String COLLECTION_WORK_BALASHIHA = "work_balashiha";
    private static final String COLLECTION_DRIVERS_BALASHIHA = "drivers_balashiha";

    private ListView listMainActivity;
    private OrderMainAdapter adapter;
    private Adapter adapterT;
    private TextView tvBalance;
    private ImageView infoBalance;
    private Switch isWork;

    private String login;
    private MyRunnable mRun;
    AlertDialog dlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvBalance = findViewById(R.id.tvBalanceMainActivity);
        listMainActivity = findViewById(R.id.listMainActivity);
        infoBalance = findViewById(R.id.ivInfoBalance);
        isWork = findViewById(R.id.swIsWork);

        Intent intent = getIntent();
         login = intent.getStringExtra("login");

       identification(login);

        listMainActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Order order = adapter.getOrder(i);
                try {
                    mRun.stopp();
                    isWork.setChecked(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startDetailOrder(order); }
        });

        infoBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBalanceDialog();
            }
        });

        isWork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isWork.setText("Работа");

                    mRun = new MyRunnable();
                }
                if(!b){
                    //isWork.setText("Отдых");

                    try {
                        mRun.stopp();
                        listMainActivity.setAdapter((ListAdapter) adapterT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showBalanceDialog() {
        String s ="Если на вашем балансе менее 10% от стоимости заказа,"
                +" то вы не сможете приянть его. "
                +"Для пополнения баланса переведите сумму на Qiwi кошелек 89251459197"
                +" с указанным номером телефона в комментариях";

        final View dialog = getLayoutInflater().inflate(R.layout.dialog_info_balance, null);
        TextView tvInfo = dialog.findViewById(R.id.tvInfoBalanceOnDialog);
        tvInfo.setText(s);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialog);

        dlg = builder.create();
        dlg.show();



        //dlg.getWindow().set
        //dlg.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);

    }

    private void startDetailOrder(Order order){
        Intent intent = new Intent(MainActivity.this, DetailOrder.class);



        String[] orderS = {order.getNameCustomer(), order.getAddressCustomer(),
                order.getCoastOrder(), order.getNumberOfAddress(),
                order.getIdOrder(), order.getIdForWorkBalashiha(), order.getTimeFilingCustomer()};

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
        adapter = new OrderMainAdapter(this, documentInfos);
        listMainActivity.setAdapter(adapter);
    }




    private void identification(String login){ // Запрос в бд данных водителя
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
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                intent.putExtra("IdDriver", driver.getId());
                startActivity(intent);
            }
        }

        return true;
    }



    @Override
    protected void onStart() {
        super.onStart();
        identification(login);
        //new MyRunnable();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        isWork.setChecked(true);
    }

    class MyRunnable implements Runnable{
    Thread thread;


    MyRunnable(){
        thread = new Thread(this, "Поток обновления");
        Log.i("Loog", "Второй поток");
        thread.start();
    }

    private void stopp() throws InterruptedException {
    thread.interrupt();
    }
        @Override
        public void run() {
           // int i;
            try {
            for (; ; ) {
                if(!thread.isInterrupted()){
                Log.i("Loog", "Второй поток: ");
                startWork();
                thread.sleep(30000);
                }else{
                    throw new InterruptedException();
                }
            }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

}

}