package com.milyutin.dima.dostavka.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.milyutin.dima.dostavka.R;

import java.util.List;

import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;

public class OrderMainAdapter extends BaseAdapter {


    LayoutInflater inflater;
    List<DocumentInfo> list;

    public OrderMainAdapter(Context context1, List<DocumentInfo> list1){
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
        if(view == null){view = inflater.inflate(R.layout.for_adapter_main_activity, viewGroup, false);}

        Order orgerForList = getOrder(position);

        ((TextView) view.findViewById(R.id.tvCustomerList)).setText(orgerForList.getNameCustomer());
        ((TextView) view.findViewById(R.id.tvAddressList)).setText(orgerForList.getAddressCustomer());
        ((TextView) view.findViewById(R.id.tvCoastList)).setText(orgerForList.getCoastOrder());
        ((TextView) view.findViewById(R.id.tvNumberOfAddressList)).setText(orgerForList.getNumberOfAddress());
        ((TextView) view.findViewById(R.id.tvTimeFilingCustomerForMainList))
                .setText(orgerForList.getTimeFilingCustomer());
        return view;
    }

    public Order getOrder(int position){
        Order order = new Order(list.get(position).getFields().get("nameCustomer").toString(),
                list.get(position).getFields().get("addressCustomer").toString(),
                list.get(position).getFields().get("coastOrder").toString(),
                list.get(position).getFields().get("numberOfAddresses").toString(),
                list.get(position).getId(),
                list.get(position).getFields().get("idForWorkBalashiha").toString());
        order.setTimeFilingCustomer(list.get(position).getFields().get("timeFilingCustomer").toString());
        return order;
    }

}
