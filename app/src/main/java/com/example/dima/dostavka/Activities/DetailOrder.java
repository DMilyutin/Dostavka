package com.example.dima.dostavka.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dima.dostavka.Helper.Helper;
import com.example.dima.dostavka.Helper.Order;
import com.example.dima.dostavka.R;

public class DetailOrder extends AppCompatActivity {

    private TextView name;
    private TextView town;
    private TextView coast;
    private TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        name = findViewById(R.id.nameCustomerDetail);
        town = findViewById(R.id.townCustomerDetail);
        coast = findViewById(R.id.tvCoastOrderCustomerDetail);
        address = findViewById(R.id.tvCountOrderCustomerDetail);

        Intent intent = getIntent();

        String[] orderS = intent.getStringArrayExtra("Order");
        Order order = new Order(orderS[0], orderS[1],orderS[2],orderS[3], orderS[4]);

        name.setText(order.getNameCastomer());
        town.setText(order.getTownCastomer());
        coast.setText(order.getCoastOrder());
        address.setText( order.getNumberOfAddress());


    }
}
