package com.byeongseop.nt900.daeguebusapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StationInfoAdapter extends BaseAdapter {
    Context con;
    ArrayList<StationInfo> data;

    public StationInfoAdapter(Context context, ArrayList<StationInfo>data) {
        this.con=context;
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StationInfoView sv;
        if(convertView!=null) { //기존의 view객체를 재활용할것임
            sv=(StationInfoView)convertView;
            sv.data=data.get(position);
            sv.staNum.setText(sv.data.staNum);
            sv.staName.setText(sv.data.staName);
        } else {
            sv=new StationInfoView(con,data.get(position));
        }
        return sv;
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
