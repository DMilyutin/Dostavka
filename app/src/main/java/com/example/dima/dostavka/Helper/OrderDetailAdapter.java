package com.example.dima.dostavka.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import com.example.dima.dostavka.R;



public class OrderDetailAdapter extends BaseAdapter {

    ArrayList<InfoForDetail> list;


    LayoutInflater inflater;

    public OrderDetailAdapter(Context context1, ArrayList<InfoForDetail> list1){
        list = list1;
        inflater = (LayoutInflater) context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view1, ViewGroup viewGroup) {
        View view = view1;
        if(view == null){view = inflater.inflate(R.layout.for_list_detail_order, viewGroup, false);}

        InfoForDetail info = getIndoDetail(i);

        ((TextView) view.findViewById(R.id.tvAddressForListDetail)).setText(info.getAddress());
        //view.findViewById(R.id.imageView);
        view.findViewById(R.id.imageView2);


        return view;
    }

    public InfoForDetail getIndoDetail(int poss){
        InfoForDetail infoForDetail = new InfoForDetail(list.get(poss).getName(), list.get(poss).getAddress(),
                list.get(poss).getPhone());

        return infoForDetail;
    }
}
