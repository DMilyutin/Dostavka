package com.example.dima.dostavka.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dima.dostavka.Helper.Helper;
import com.example.dima.dostavka.Helper.Order;
import com.example.dima.dostavka.R;

import ru.profit_group.scorocode_sdk.Callbacks.CallbackDocumentSaved;
import ru.profit_group.scorocode_sdk.Callbacks.CallbackGetDocumentById;
import ru.profit_group.scorocode_sdk.Callbacks.CallbackRemoveDocument;
import ru.profit_group.scorocode_sdk.Responses.data.ResponseRemove;
import ru.profit_group.scorocode_sdk.scorocode_objects.Document;
import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;

public class DetailOrder extends AppCompatActivity {

    private TextView name;
    private TextView town;
    private TextView coast;
    private TextView address;
    private Button btTakeOrder;
    private final Document document = new Document("work");
    Order order;
    String num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        name = findViewById(R.id.nameCustomerDetail);
        town = findViewById(R.id.townCustomerDetail);
        coast = findViewById(R.id.tvCoastOrderCustomerDetail);
        address = findViewById(R.id.tvCountOrderCustomerDetail);
        btTakeOrder = findViewById(R.id.btTakeOrderDetail);

        Intent intent = getIntent();

        String[] orderS = intent.getStringArrayExtra("Order");
        order = new Order(orderS[0], orderS[1],orderS[2],orderS[3], orderS[4]);

        getOrderFromIntent(order);


        btTakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeOrderFromActivWork(document);
                addOrderInHistory(order);
                onBackPressed();

                // TODO Продолжение?

            }
        });

    }

    private void addOrderInHistory(Order order) {
            Toast.makeText(DetailOrder.this, num, Toast.LENGTH_SHORT).show();
            Document newDocument = new Document("history_work_balashiha");
            newDocument.setField("historyNameCustomer", order.getNameCustomer());
            newDocument.setField("historyAddressCustomer", order.getAddressCustomer());
            newDocument.setField("historyCoastOrder", order.getCoastOrder());
            // TODO Номер водителя
            newDocument.setField("historyDriver",num );
            newDocument.saveDocument(new CallbackDocumentSaved() {
                @Override
                public void onDocumentSaved() {

                }

                @Override
                public void onDocumentSaveFailed(String errorCode, String errorMessage) {
                    Helper.showToast(DetailOrder.this, errorMessage);
                }
            });

    }

    private void removeOrderFromActivWork(final Document document) {

        document.getDocumentById(order.getIdOrder(), new CallbackGetDocumentById() {
        @Override
        public void onDocumentFound(DocumentInfo documentInfo) {
            document.removeDocument(new CallbackRemoveDocument() {
                @Override
                public void onRemoveSucceed(ResponseRemove responseRemove) {
                   
                }

                @Override
                public void onRemoveFailed(String errorCode, String errorMessage) {
                    Toast.makeText(DetailOrder.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onDocumentNotFound(String errorCode, String errorMessage) {
            Toast.makeText(DetailOrder.this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    });
    }

    private void getOrderFromIntent(Order order){
        name.setText(order.getNameCustomer());
        town.setText(order.getAddressCustomer());
        coast.setText(order.getCoastOrder());
        address.setText( order.getNumberOfAddress());
    }
}
