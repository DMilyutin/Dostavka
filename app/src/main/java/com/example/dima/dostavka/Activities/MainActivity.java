package com.example.dima.dostavka.Activities;

import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.example.dima.dostavka.Helper.OrderAdapter;
import com.example.dima.dostavka.R;

import java.util.List;

import com.example.dima.dostavka.Helper.*;

import ru.profit_group.scorocode_sdk.Callbacks.CallbackFindDocument;
import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;
import ru.profit_group.scorocode_sdk.scorocode_objects.Query;

public class MainActivity extends AppCompatActivity {

    private ListView listMainActivity;
    private OrderAdapter adapter;

    private static final String COLLECTION_NAME = "work";

   // private DocumentFields fields;

    private Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aSwitch = findViewById(R.id.swOnline);
        listMainActivity = findViewById(R.id.listMainActivity);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // TODO Автообновление
                } else{}

            }
        });


        //fields = new DocumentFields(this);



        listMainActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Order order = adapter.getOrder(i);
                Intent intent = new Intent(MainActivity.this, DetailOrder.class);

                String id = order.getIdOrder();
                String name = order.getNameCustomer();
                String town = order.getAddressCustomer();
                String coast = order.getCoastOrder();
                String adr = order.getNumberOfAddress();

                String[] orderS = {name, town, coast, adr, id};

                intent.putExtra("Order", orderS);
                startActivity(intent);

            }
        });

    }


    public void startWork() {

        Query query = new Query(COLLECTION_NAME);
        query.findDocuments(new CallbackFindDocument() {
            @Override
            public void onDocumentFound(List<DocumentInfo> documentInfos) {
                setAdapter(documentInfos);

            }

            @Override
            public void onDocumentNotFound(String errorCode, String errorMessage) {
                Helper.showToast(MainActivity.this, errorMessage);
            }
        });
    }


    private void setAdapter(List<DocumentInfo> documentInfos) {
        adapter = new OrderAdapter(this, documentInfos);
        listMainActivity.setAdapter(adapter);
    }


    public static void display(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }


    @Override
    protected void onStart() {
        super.onStart();
        startWork();
    }
}