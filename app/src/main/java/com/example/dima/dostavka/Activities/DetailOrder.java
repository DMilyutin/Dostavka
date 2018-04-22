package com.example.dima.dostavka.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dima.dostavka.Helper.Helper;
import com.example.dima.dostavka.Helper.Order;
import com.example.dima.dostavka.Helper.myEnum;
import com.example.dima.dostavka.R;

import ru.profit_group.scorocode_sdk.Callbacks.CallbackDocumentSaved;
import ru.profit_group.scorocode_sdk.Callbacks.CallbackGetDocumentById;
import ru.profit_group.scorocode_sdk.Callbacks.CallbackRemoveDocument;
import ru.profit_group.scorocode_sdk.Callbacks.CallbackUpdateDocument;
import ru.profit_group.scorocode_sdk.Responses.data.ResponseRemove;
import ru.profit_group.scorocode_sdk.Responses.data.ResponseUpdate;
import ru.profit_group.scorocode_sdk.scorocode_objects.Document;
import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;
import ru.profit_group.scorocode_sdk.scorocode_objects.Query;
import ru.profit_group.scorocode_sdk.scorocode_objects.Update;

public class DetailOrder extends AppCompatActivity {

    private TextView name;
    private TextView town;
    private TextView coast;
    private TextView address;
    private Button btTakeOrder;

    private String COLLECTION_HISTORY_WORK_BALASHIH = "history_work_balashiha";
    private String COLLECTION_WORK_BALASHIHA = "work_balashiha";


    private final Document document = new Document(COLLECTION_WORK_BALASHIHA);
    Document newDocument = new Document(COLLECTION_HISTORY_WORK_BALASHIH);
    Order order;
    String num;
    String idDriver;


    private String latForMap = "55.7979";
    private String lonForMap = "37.9375";


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
        idDriver = intent.getStringExtra("IdDriver");

        getOrderFromIntent(order);


        btTakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickButton(btTakeOrder.getText().toString());
            }
        });

    }

    private void clickButton(String textButton){
        Query query = new Query(COLLECTION_HISTORY_WORK_BALASHIH);
        query.equalTo("historyIdWork", order.getIdOrder());
        if(textButton == null){return;}

        switch (textButton){
            case "Принять заказ" :
                removeOrderFromActivWork(document);
                addOrderInHistory(order);
                btTakeOrder.setText("Забрал заказ");
                // Через сколько буду
                // navigatorStart();
                break;

            case "Забрал заказ" :
                btTakeOrder.setText("Доставил заказ");
                upDataHistoriStatusOrder(false, query);
                // navigatorStart();
                break;
            case "Доставил заказ" :

                btTakeOrder.setText("Взять заказ");
                upDataHistoriStatusOrder(true, query);
                onBackPressed();
                break;

            default: Helper.showToast(DetailOrder.this, "Не известно");
            break;
        }

    }

    private void upDataHistoriStatusOrder(Boolean b, Query query){
        Update update = new Update().set("historyStatusOrder", b);
        query.updateDocument(update, new CallbackUpdateDocument() {
            @Override
            public void onUpdateSucceed(ResponseUpdate responseUpdate) {

            }

            @Override
            public void onUpdateFailed(String errorCode, String errorMessage) {
                Helper.showToast(DetailOrder.this, errorMessage);
            }
        });
    }



    private void navigatorStart() {
        Uri uri = Uri.parse("yandexnavi://show_point_on_map").buildUpon()
                .appendQueryParameter("lat", latForMap)
                .appendQueryParameter("lon", lonForMap).build();


        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("ru.yandex.yandexnavi");
        startActivity(intent);

    }

    private void addOrderInHistory(Order order) {
            //Toast.makeText(DetailOrder.this, num, Toast.LENGTH_SHORT).show();
            newDocument.setField("historyNameCustomer", order.getNameCustomer());
            newDocument.setField("historyAddressCustomer", order.getAddressCustomer());
            newDocument.setField("historyCoastOrder", order.getCoastOrder());
            newDocument.setField("historyIdWork", order.getIdOrder());
            // TODO id Водителя
            newDocument.setField("historyIdDriver",idDriver );
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
