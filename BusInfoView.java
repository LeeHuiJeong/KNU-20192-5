package com.byeongseop.nt900.daeguebusapp;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BusInfoView extends LinearLayout {
    LinearLayout image;
    TextView route_no, arr_state, cur_pos;
    BusInfo data;

    public BusInfoView(Context context, BusInfo data) {//data:현재 줄에 보여줄 데이터
        super(context);
        this.data=data;

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.busline,this,true);

        route_no=(TextView)findViewById(R.id.route_no);
        arr_state=(TextView)findViewById(R.id.arr_state);
        cur_pos=(TextView)findViewById(R.id.cur_pos);
        image=(LinearLayout)findViewById(R.id.busImage);

        route_no.setText(data.route_no);
        arr_state.setText(data.arr_state);
        cur_pos.setText(data.cur_pos);
        //image.setBackgroundResource(data.res_id);//배경 사진 설정
    }
}
