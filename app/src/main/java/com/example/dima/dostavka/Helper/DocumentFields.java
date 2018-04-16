package com.example.dima.dostavka.Helper;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.dima.dostavka.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;

 public class DocumentFields {
     public Context context;
     public DocumentInfo documentInfo;

    public DocumentFields(Context context) {
        this(context, null);
    }

    public DocumentFields(Context context, DocumentInfo documentInfo) {
        this.context = context;
        this.documentInfo = documentInfo;
    }

    public void setDocumentInfo(DocumentInfo documentInfo) {
        this.documentInfo = documentInfo;
    }


    public enum Fields{

        CUSTOMER_NAME(R.string.fieldNameCustomer),
        CUSTOMER_TOWN(R.string.fieldTownCustomer),
        COAST_ORDER(R.string.fieldCoastOrder);

        public int fieldNameId;

        Fields(int fieldNameId) {
            this.fieldNameId = fieldNameId;
        }

        public int getNameId() {
            return fieldNameId;
        }

    }


    public String getCustomerNameField() {
        return context.getString(Fields.CUSTOMER_NAME.getNameId());
    }

    public String getCustomerTownField() {
        return context.getString(Fields.CUSTOMER_TOWN.getNameId());
    }

    public String getCoastOrderField() {
        return context.getString(Fields.COAST_ORDER.getNameId());
    }



    private String getFieldValue(DocumentInfo documentInfo, Fields field) {
        return String.valueOf(documentInfo.getFields().get(context.getString(field.getNameId())));
    }


    @NonNull
    public String getCustomerName() {
        if(documentInfo != null) {
            return getFieldValue(documentInfo, Fields.CUSTOMER_NAME);
        }
        return "";
    }

    @NonNull
    public String getTownCustomer() {
        if(documentInfo != null) {
            return getFieldValue(documentInfo, Fields.CUSTOMER_TOWN);
        }
        return "";
    }

    @NonNull
    public String getCoastOrder() {
        if(documentInfo != null) {
            return getFieldValue(documentInfo, Fields.COAST_ORDER);
        }
        return "";
    }

}
