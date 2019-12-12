package com.byeongseop.nt900.daeguebusapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StationInfoView extends LinearLayout { //버스 정류장 정보 한줄을 나타냄
    TextView staName,staNum;
    StationInfo data;

    public StationInfoView(Context context,StationInfo data) {
        super(context);
        this.data=data;

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout=new LinearLayout(context);

        inflater.inflate(R.layout.stationline,this,true);//busline 레이아웃 파일을 객체화함
        staName=(TextView)findViewById(R.id.staName);
        staNum=(TextView)findViewById(R.id.staNum);

        staName.setText(data.staName);
        staNum.setText(data.staNum);
    }
}
