package com.byeongseop.nt900.daeguebusapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class CallTaxiActivity extends AppCompatActivity {
    AdView taxiAd;
    Button btnWoon, btnHan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_taxi);

        btnWoon=(Button)findViewById(R.id.btnWoon);
        btnHan=(Button)findViewById(R.id.btnHan);

        btnWoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0537667777"));
                startActivity(intent);
            }
        });

        btnHan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0534241111"));
                startActivity(intent);
            }
        });

        //광고 달기
        taxiAd=findViewById(R.id.taxiAd);
        AdRequest adRequest=new AdRequest.Builder().build();
        taxiAd.loadAd(adRequest);
    }
}
