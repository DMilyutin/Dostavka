package com.example.dima.dostavka.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.QwertyKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dima.dostavka.Helper.Helper;
import com.example.dima.dostavka.Helper.InfoForDetail;
import com.example.dima.dostavka.Helper.Order;
import com.example.dima.dostavka.Helper.OrderDetailAdapter;
import com.example.dima.dostavka.R;

import java.util.ArrayList;
import java.util.List;

import ru.profit_group.scorocode_sdk.Callbacks.CallbackDocumentSaved;
import ru.profit_group.scorocode_sdk.Callbacks.CallbackFindDocument;
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
    private TextView timeFiling;
    private Button btTakeOrder;

    private String COLLECTION_WORK_BALASHIHA = "work_balashiha";
    private String COLLECTION_FOR_WORK_BALASHIHA = "for_work_balashiha";
    private String COLLECTION_DRIVER_BALASHIHA = "drivers_balashiha";

    private final Document document = new Document(COLLECTION_WORK_BALASHIHA);

    String idDriver;
    String balanceDriverS;
    Double balanceDriver;
    AlertDialog dlg;
    Order orderO;

    OrderDetailAdapter adapter;

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
        timeFiling = findViewById(R.id.tvCountTimeFilingCustomerDetail);

        Intent intent = getIntent();

        String[] orderS = intent.getStringArrayExtra("Order");

        orderO = new Order(orderS[0], orderS[1],orderS[2],orderS[3], orderS[4], orderS[5]);
        orderO.setTimeFilingCustomer(orderS[6]);



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

        if(textButton == null){return;}
        if(!provBalance()){return;}

        switch (textButton){
            case "Принять заказ" :
                removeOrderFromActivWork(document);
                btTakeOrder.setText("Забрал заказ");
                break;

            case "Забрал заказ" :
                btTakeOrder.setText("Доставил заказ");
                getOllInfo();
                // navigatorStart();
                break;
            case "Доставил заказ" :
                address = null;
                closeOrder();


                break;

            default: Helper.showToast(DetailOrder.this, "Не известно");
            break;
        }

    }

    private void closeOrder() {
        Query query = new Query(COLLECTION_FOR_WORK_BALASHIHA);
        query.equalTo("_id", orderO.getIdForWorkBalashiha());

        Update update = new Update();
        update.set("statusOrder", "Завершен");

        query.updateDocument(update, new CallbackUpdateDocument() {
            @Override
            public void onUpdateSucceed(ResponseUpdate responseUpdate) {

                correktBalance();
            }

            @Override
            public void onUpdateFailed(String errorCode, String errorMessage) {
                Log.i("Loog", "Ошибка clouseOrder");
            }
        });
    }

    private void correktBalance() {

        Query query = new Query(COLLECTION_DRIVER_BALASHIHA);
        query.equalTo("_id", idDriver);
        query.findDocuments(new CallbackFindDocument() {
            @Override
            public void onDocumentFound(List<DocumentInfo> documentInfos) {
                String st = (documentInfos.get(0).getFields().get("balanceDriver").toString());
                Double v = Double.parseDouble(st);
                updateBalance(v);
            }

            @Override
            public void onDocumentNotFound(String errorCode, String errorMessage) {
                Log.i("Loog", "Ошибка корректа баланса");
            }
        });
    }

    private void updateBalance(Double v) {
        Double coastOrder = Double.parseDouble(orderO.getCoastOrder());
        long finishBalance = (long) (v - coastOrder*0.11);
        finishBalance = Math.round(finishBalance);


        Query query = new Query(COLLECTION_DRIVER_BALASHIHA);
        query.equalTo("_id", idDriver);

        Update update = new Update();
        update.set("balanceDriver", finishBalance);

        query.updateDocument(update, new CallbackUpdateDocument() {
            @Override
            public void onUpdateSucceed(ResponseUpdate responseUpdate) {
                Toast.makeText(DetailOrder.this, "Заказ успешно выполнен", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            @Override
            public void onUpdateFailed(String errorCode, String errorMessage) {
                Toast.makeText(DetailOrder.this, "Ошибка закрытия заказа", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void howMachTime() {
        String timeF = timeFiling.getText().toString();
        final View dialog = getLayoutInflater().inflate(R.layout.time_dialog_detail_order, null);

        Button bt10Min = dialog.findViewById(R.id.bt10Min);
        Button bt15Min = dialog.findViewById(R.id.bt15Min);
        Button bt20Min = dialog.findViewById(R.id.bt20Min);
        Button btFilCustomer = dialog.findViewById(R.id.btTimeFildCustomer);

        if(!timeF.equals("Ближайшее время")){
            bt10Min.setVisibility(View.INVISIBLE);
            bt15Min.setVisibility(View.INVISIBLE);
            bt20Min.setVisibility(View.INVISIBLE);
        }
        else {
            btFilCustomer.setVisibility(View.INVISIBLE);
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialog);

            dlg = builder.create();
            dlg.show();

            //dlg.getWindow().setBackgroundDrawableResource(R.color.colorCofe1);

        bt10Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrderInForWorkBalashiha("Курьер будет через 10 минут");
                dlg.dismiss();
            }
        });
        bt15Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrderInForWorkBalashiha("Курьер будет через 15 минут");
                dlg.dismiss();
            }
        });
        bt20Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrderInForWorkBalashiha("Курьер будет через 20 минут");
                dlg.dismiss();
            }
        });
        btFilCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrderInForWorkBalashiha("Курьер будет к назначенному времени");
                dlg.dismiss();
            }
        });
        dlg.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
    }

    private void addOrderInForWorkBalashiha(String time){
        updataForWork(time);
        //navigatorStart();
       //btTakeOrder.setText("Забрал заказ");
    }

    private void getOllInfo() {
        Query query = new Query(COLLECTION_FOR_WORK_BALASHIHA);
        query.equalTo("_id", orderO.getIdForWorkBalashiha());
        query.findDocuments(new CallbackFindDocument() {
            @Override
            public void onDocumentFound(List<DocumentInfo> documentInfos) {
                initOllInfo(documentInfos.get(0).getFields().get("nameForDriver").toString(),
                      documentInfos.get(0).getFields().get("addressForDriver").toString(),
                      documentInfos.get(0).getFields().get("phoneForDriver").toString());
            }

            @Override
            public void onDocumentNotFound(String errorCode, String errorMessage) {
                Log.i("Loog", "гет олл инфо ошибка");
            }
        });



    }

    private void initOllInfo(String nameForDrive, String addressForDriver, String phoneForDriver) {
        ArrayList<InfoForDetail> listInfo = new ArrayList<>();
        int collAdress = Integer.parseInt(orderO.getNumberOfAddress());
        String[] names    ;
        String[] addresses ;
        String[] phones   ;
        names = nameForDrive.split(";");
        addresses= addressForDriver.split(";");
        phones= phoneForDriver.split(";");
        for(int i = 0; i<collAdress; i++){

            InfoForDetail infoForDetail = new InfoForDetail(names[i], addresses[i], phones[i] );
            Log.i("Loog", infoForDetail.toString());
            listInfo.add(infoForDetail);
        }
        adapter = new OrderDetailAdapter(this, listInfo);
        listDetailOrder.setAdapter(adapter);
    }

    private void updataForWork(String filing){
        Query query1 = new Query(COLLECTION_FOR_WORK_BALASHIHA);
        query1.equalTo("_id", orderO.getIdForWorkBalashiha());

        Update update1 = new Update();
        update1.set("timeFilingDriver", filing).set("idDriver", idDriver)
                .set("statusOrder", "Выполняется");

        query1.updateDocument(update1, new CallbackUpdateDocument() {
            @Override
            public void onUpdateSucceed(ResponseUpdate responseUpdate) {
                    Log.i("Loog", "документ изменен");
            }
            @Override
            public void onUpdateFailed(String errorCode, String errorMessage) {

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
        timeFiling.setText(order.getTimeFilingCustomer());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.detailItem : {
                Helper.showToast(this, "В разработке");
                //Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                //intent.putExtra("IdDriver", driver.getId());
                //startActivity(intent);
            }
        }

        return true;
    }

}
