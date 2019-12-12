package com.byeongseop.nt900.daeguebusapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//정류장 검색화면(StationInfo, StationInfoAdapter,StationInfoView와 이 파일은 세트임)
public class StationInfoActivity extends AppCompatActivity {
    EditText edtStaName;
    Button btnStaSearch;
    ListView staListView;
    AdView stationAd;

    ArrayList<StationInfo> data=new ArrayList<StationInfo>();
    StationInfoAdapter adapter;

    Handler myHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter=new StationInfoAdapter(StationInfoActivity.this,data);
            staListView.setAdapter(adapter);
            btnStaSearch.setEnabled(true);
            btnStaSearch.setText("검색");
        }
    };

    class MyThread extends Thread {
        @Override
        public void run() {
            try {
                URL url=new URL("http://businfo.daegu.go.kr/ba/arrbus/arrbus.do");
                HttpURLConnection con= (HttpURLConnection)url.openConnection();
                con.setDoOutput(true);
                String parameter="act=findByBusStopNo&bsNm="+edtStaName.getText();
                OutputStream wr=con.getOutputStream();
                wr.write(parameter.getBytes("euc-kr"));//파라미터를 바이트코드로 작성
                wr.flush();//버퍼를 비워서 일을 처리함

                //결과로 넘어온 버스 정류장 정보 읽어오기
                BufferedReader rd=new BufferedReader(new InputStreamReader(con.getInputStream(),"euc-kr")); //InputStreamReader :바이트 단위로 읽는 데이터를 문자 단위로 읽을수 있도록 해줌, "euc-kr"타입으로 변환함->한글 읽기 위해서

                Source src=new Source(rd);//결과로 넘어온 정보 소스를 가지는 소스 객체
                src.fullSequentialParse();//소스 안의 태그들을 모두 element타입으로 객체화
               // src.getAllElements();//객체화된 모든 태그 가져옴
                List<Element>list=src.getAllElements();//모든 태그가 리스트에 나열되어있음

                StationInfo stationInfo=new StationInfo();
                int count=0;
                for(Element element:list) {
                    if(element.getName().equals("td")) {    //태그명이 td일때
                        count++;
                        if(count%2==0) {
                            stationInfo.staNum=element.getTextExtractor().toString();
                            data.add(stationInfo);
                            count=0;
                        }else {
                            stationInfo=new StationInfo();  //이 코드는 꼭 있어야 함. -> 매 번 새로운 객체를 만들어줘야 모든 검색된 정보를 표현할 수 있음, 안그러면 마지막에 검색된 객체로 모든 리스트뷰가 표현됨
                            stationInfo.staName=element.getTextExtractor().toString();
                        }
                    }
                }

                myHandler.sendEmptyMessage(0);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_info);
        setTitle("버스 정류장 검색하기");
        edtStaName=(EditText)findViewById(R.id.edt_staName);
        btnStaSearch=(Button)findViewById(R.id.btn_sta_search);
        staListView=(ListView)findViewById(R.id.sta_ListView);

        btnStaSearch.setOnClickListener(new View.OnClickListener() {    //검색 버튼 클릭시 이벤트처리
            @Override
            public void onClick(View v) {
                data.clear();//이전에 검색했던 것을 없애기 위해 arraylist비워줌
                btnStaSearch.setEnabled(false);
                btnStaSearch.setText("요청중");
                new MyThread().start();

                //(특정 View로부터 생성된 키보드 숨기기) EditText클릭시 생긴 키보드를 검색 버튼 클릭시 키보드 보이는거 없애주는 작업
                InputMethodManager im=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(edtStaName.getWindowToken(),0);
            }
        });

        staListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout layout=(LinearLayout)view;//클릭된 한줄

                LinearLayout layout2=(LinearLayout)layout.getChildAt(0);
                LinearLayout layout3=(LinearLayout)layout2.getChildAt(0);
                TextView sta_name=(TextView)layout3.getChildAt(0);
                TextView sta_number=(TextView)layout3.getChildAt(1);
                String number=sta_number.getText().toString();
                String name=sta_name.getText().toString();

                //버스 도착정보 보여주는 화면으로 전환
                Intent intent=new Intent(StationInfoActivity.this,BusInfoActivity.class);
                intent.putExtra("number",number);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });

        //광고 나타나게하기
        stationAd=findViewById(R.id.stationAd);
        AdRequest adRequest=new AdRequest.Builder().build();
        stationAd.loadAd(adRequest);
    }
}
