package com.example.dima.dostavka.Helper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dima.dostavka.R;

import java.util.ArrayList;
import java.util.List;

import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;

public class OrderAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<DocumentInfo> list;


    public OrderAdapter(Context context1, List<DocumentInfo> list1){
        //context = context1;
        list = list1;
        inflater = (LayoutInflater) context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view1, ViewGroup viewGroup) {
        View view = view1;
        if(view == null){view = inflater.inflate(R.layout.for_list_main_activity, viewGroup, false);}

        //list.get(position).getFields();
        //Order order = getOrder(position);

        ((TextView) view.findViewById(R.id.tvCustomerList)).setText((String) list.get(position).getFields().get("nameCustomer") );
        ((TextView) view.findViewById(R.id.tvTownList)).setText((String) list.get(position).getFields().get("townCustomer") );
        ((TextView) view.findViewById(R.id.tvCoastList)).setText((String) list.get(position).getFields().get("coastOrder") );



        return view;
    }

    Order getOrder(int position){
        return ((Order) getItem(position));
    }
}
