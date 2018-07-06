package com.milyutin.dima.dostavka.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.milyutin.dima.dostavka.R;

import java.text.SimpleDateFormat;
import java.util.List;

import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;

public class HistoryOrderAdapter  extends BaseAdapter{
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    LayoutInflater inflater;
    List<DocumentInfo> list;

    public HistoryOrderAdapter(Context context1, List<DocumentInfo> list1){
        list = list1;
        inflater = (LayoutInflater) context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return  list.size();
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
    public View getView(int i, View view1, ViewGroup viewGroup) {
        String status = list.get(i).getFields().get("statusOrder").toString();
        int res = R.drawable.ic_check_green_24dp;
        if (status.equals("Отменен"))
            res = R.drawable.ic_cancel_red_24dp;
        if (status.equals("Выполняется"))
            res = R.drawable.ic_leave_green_24dp;

        View view = view1;
        if(view == null){view = inflater.inflate(R.layout.for_adapter_history, viewGroup, false);}

        String dat = format.format(list.get(i).getDate("createdAt"));

        ((TextView) view.findViewById(R.id.tvNameForListHistory))
                .setText(list.get(i).getFields().get("nameCustomer").toString());
        ((TextView) view.findViewById(R.id.tvDataForListHistory))
                .setText(dat);
        ((TextView) view.findViewById(R.id.tvCoastForListHistory))
                .setText(list.get(i).getFields().get("coastOrder").toString());
        ((ImageView) view.findViewById(R.id.ivStatusForListHistory)).setImageResource(res);

        return view;
    }

    Order getHistoryOrder(int position){
        Order order = new Order(list.get(position).getFields().get("nameCustomer").toString(),
                list.get(position).getFields().get("addressCustomer").toString(),
                list.get(position).getFields().get("coastOrder").toString(),
                list.get(position).getFields().get("numberOfAddresses").toString(),
                list.get(position).getId(),
                list.get(position).getFields().get("idForWorkBalashiha").toString());

        return order;
    }
}
