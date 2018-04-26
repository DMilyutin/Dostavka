package com.example.dima.dostavka.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dima.dostavka.Helper.Helper;
import com.example.dima.dostavka.Helper.Order;
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

    private ListView listDetailOrder;

    private TextView name;
    private TextView town;
    private TextView coast;
    private TextView address;
    private Button btTakeOrder;

    private String COLLECTION_HISTORY_WORK_BALASHIH = "history_work_balashiha";
    private String COLLECTION_WORK_BALASHIHA = "work_balashiha";
    private String COLLECTION_DRIVER_BALASHIHA = "drivers_balashiha";



    private final Document document = new Document(COLLECTION_WORK_BALASHIHA);
    Document newDocument = new Document(COLLECTION_HISTORY_WORK_BALASHIH);

    String idDriver;
    String balanceDriverS;
    Double balanceDriver;
    String[] addrees;
    String time;
    AlertDialog dlg;
    Order orderO;

    ArrayAdapter<String> adapter;

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
        listDetailOrder = findViewById(R.id.listDetailOrder);

        Intent intent = getIntent();
        orderO = getIntent().getParcelableExtra("Order");

        String[] orderS = intent.getStringArrayExtra("Order");
        orderO = new Order(orderS[0], orderS[1],orderS[2],orderS[3], orderS[4], orderS[5]);
        idDriver = intent.getStringExtra("IdDriver");
        balanceDriverS = intent.getStringExtra("BalanceDriver");
        balanceDriver = Double.parseDouble(balanceDriverS);




        getOrderFromIntent(orderO);


        btTakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickButton(btTakeOrder.getText().toString());
            }
        });

    }

    private Boolean provBalance() {
        Double coast = Double.parseDouble(orderO.getCoastOrder());
        if(Math.round(balanceDriver - (coast*0.13)) <0){
            Helper.showToast(DetailOrder.this, "Недостаточно средств");
            return false;
        }
        else return true;
    }

    private void clickButton(String textButton){

        Query query = new Query(COLLECTION_HISTORY_WORK_BALASHIH);

        query.equalTo("historyIdWork", orderO.getIdOrder());
        if(textButton == null){return;}
        if(!provBalance()){return;}

        switch (textButton){
            case "Принять заказ" :
                removeOrderFromActivWork(document);

                break;

            case "Забрал заказ" :
                btTakeOrder.setText("Доставил заказ");
                upDataHistoriStatusOrder(false, query, time);
                // navigatorStart();
                break;
            case "Доставил заказ" :
                address = null;
                btTakeOrder.setText("Взять заказ");
                upDataHistoriStatusOrder(true, query, null);
                onBackPressed();
                break;

            default: Helper.showToast(DetailOrder.this, "Не известно");
            break;
        }

    }

    private void howMachTime() {
        final View dialog = getLayoutInflater().inflate(R.layout.time_dialog_detail_order, null);
        Button bt10Min = dialog.findViewById(R.id.bt10Min);
        Button bt15Min = dialog.findViewById(R.id.bt15Min);
        Button bt20Min = dialog.findViewById(R.id.bt20Min);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialog).setTitle("Буду на месте через");

            dlg = builder.create();
            dlg.show();

        bt10Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrderInHistory("Курьер будет через 10 минут");
                dlg.dismiss();
            }
        });
        bt15Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrderInHistory("Курьер будет через 15 минут");
                dlg.dismiss();
            }
        });
        bt20Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrderInHistory("Курьер будет через 20 минут");
                dlg.dismiss();
            }
        });
    }

    private void addOrderInHistory(String time){
        addOrderInHistory(orderO, time);
        Helper.showToast(DetailOrder.this, time);
        initList(orderO.getAddressForDriver());
        btTakeOrder.setText("Забрал заказ");
        //navigatorStart();
    }

    private void initList(String addresForDriver) {
        int collAdress = Integer.parseInt(orderO.getNumberOfAddress());
        addrees = new String[collAdress];
        addrees = addresForDriver.split(";");

        adapter = new  ArrayAdapter<>(
                this, android.R.layout.simple_expandable_list_item_1, addrees);
        listDetailOrder.setAdapter(adapter);
    }

    private void upDataHistoriStatusOrder(Boolean b, Query query, String time){
        Update update = new Update().set("historyStatusOrder", b).set("histori_address_for_driver", orderO.getAddressForDriver())
                .set("match_time", time);

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

    private void addOrderInHistory(Order order, String time) {
            //Toast.makeText(DetailOrder.this, num, Toast.LENGTH_SHORT).show();
            newDocument.setField("historyNameCustomer", order.getNameCustomer());
            newDocument.setField("historyAddressCustomer", order.getAddressCustomer());
            newDocument.setField("historyCoastOrder", order.getCoastOrder());
            newDocument.setField("historyIdWork", order.getIdOrder());
            newDocument.setField("historyIdDriver",idDriver );
            newDocument.setField("match_time", time);
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
        document.getDocumentById(orderO.getIdOrder(), new CallbackGetDocumentById() {
        @Override
        public void onDocumentFound(DocumentInfo documentInfo) {
            document.removeDocument(new CallbackRemoveDocument() {
                @Override
                public void onRemoveSucceed(ResponseRemove responseRemove) {
                    howMachTime();

                }

                @Override
                public void onRemoveFailed(String errorCode, String errorMessage) {
                    Toast.makeText(DetailOrder.this, "Заказ умыкнули", Toast.LENGTH_SHORT).show();
                    onBackPressed();

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
