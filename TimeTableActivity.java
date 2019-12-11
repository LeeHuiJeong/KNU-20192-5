package com.byeongseop.nt900.daeguebusapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//막차 도착정보 보여주는 화면

public class TimeTableActivity extends AppCompatActivity {
    private WebView webTimeTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);  //액션바(화면 상단 제목) 화면에 안 보이게 하기, 밑에 줄의 코드보다 먼저 작성해줘야함
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_time_table);

        webTimeTable=(WebView)findViewById(R.id.webTimeTable);
        WebSettings webSettings=webTimeTable.getSettings();

        webSettings.setJavaScriptEnabled(true);//자바스크립트허용
        webSettings.setLoadWithOverviewMode(true);//컨텐츠가 웹뷰보다 클경우 스크린 크기에 맞게 조정

        webTimeTable.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webTimeTable.loadUrl("http://m.businfo.go.kr/bp/m/timeTbl.do?act=main");
    }
    //   추가전에 뒤로가기 이벤트 호출시 홈으로 돌아갔으나, 이젠 일반적인 뒤로가기 기능 활성화
    public void onBackPressed() {
        if(webTimeTable.canGoBack())webTimeTable.goBack();
        else finish();
    }
}
