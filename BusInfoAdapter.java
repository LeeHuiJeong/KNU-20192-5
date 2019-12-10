package com.byeongseop.nt900.daeguebusapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class BusInfoAdapter extends BaseAdapter {
    Context context;
    ArrayList<BusInfo> data;//Adapter가 받아올 데이터

    public BusInfoAdapter(Context con,ArrayList<BusInfo> data) {
        context=con;
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BusInfoView busInfoView=new BusInfoView(context,data.get(position));//보여줄 한줄 만들기
        return busInfoView;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
