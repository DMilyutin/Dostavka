package com.milyutin.dima.dostavka.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.milyutin.dima.dostavka.Helper.Helper;
import com.milyutin.dima.dostavka.Helper.HistoryOrderAdapter;
import com.milyutin.dima.dostavka.R;

import java.util.List;

import ru.profit_group.scorocode_sdk.Callbacks.CallbackFindDocument;
import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;
import ru.profit_group.scorocode_sdk.scorocode_objects.Query;

public class HistoryActivity extends AppCompatActivity {

    private String COLLECTION_FOR_WORK_BALASHIHA = "for_work_balashiha";


    private ListView historyList;
    private HistoryOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyList = findViewById(R.id.historyListL);

        Intent intent = getIntent();
        String idDriver = intent.getStringExtra("IdDriver");



        findHistoryById(idDriver);
    }

    private void findHistoryById(String idDriver) {
        Query query = new Query(COLLECTION_FOR_WORK_BALASHIHA);
        query.equalTo("idDriver", idDriver);
        query.findDocuments(new CallbackFindDocument() {
            @Override
            public void onDocumentFound(List<DocumentInfo> documentInfos) {
                addListHistory(documentInfos);
            }

            @Override
            public void onDocumentNotFound(String errorCode, String errorMessage) {
                Helper.showToast(HistoryActivity.this, errorMessage);
            }
        });
    }

    private void addListHistory(List<DocumentInfo> documentInfos) {
        adapter = new HistoryOrderAdapter(this, documentInfos);
        historyList.setAdapter(adapter);
    }




}
