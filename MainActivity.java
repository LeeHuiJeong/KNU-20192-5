package com.byeongseop.nt900.daeguebusapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


//가장 처음 화면, 화면간 전환하는 코드
public class MainActivity extends Activity {
    Button btnBookMark, btnStation, btnTimeTable, btnCallTaxi;
    AdView mainAd;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        MobileAds.initialize(this,"ca-app-pub-7785923892668605~6368309832");

        btnBookMark=(Button)findViewById(R.id.btnBookMark);
        btnStation=(Button)findViewById(R.id.btnStation);
        btnTimeTable=(Button)findViewById(R.id.btnTimeTable);
        btnCallTaxi=(Button)findViewById(R.id.btnCallTaxi);

        btnBookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),BookMarkActivity.class);
                startActivity(intent);
            }
        });
        btnStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),StationInfoActivity.class);
                startActivity(intent);
            }
        });

        btnTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),TimeTableActivity.class);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(),"이 버튼을 누르면 노선을 검색할 수 있도록 빠른 시일내에 구현하겠습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        btnCallTaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CallTaxiActivity.class);
                startActivity(intent);
            }
        });

        //광고 달기
        mainAd=findViewById(R.id.mainAd);
        AdRequest adRequest=new AdRequest.Builder().build();
        mainAd.loadAd(adRequest);

    }
}

