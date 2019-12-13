package com.byeongseop.nt900.daeguebusapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

//즐겨찾기한 정류장을 보여주는 화면

public class BookMarkActivity extends AppCompatActivity {
    public static Context CONTEXT;//다른 화면에서 현재의 콘텍스트에 접근할수있게 해주는 변수

    ListView bookmarkList;
    AdView bookMarkAd;
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    StationInfoAdapter adapter;
    ArrayList<StationInfo> arr=new ArrayList<StationInfo>();//즐겨찾기 데이터베이스에 추가된 정류장들 저장할 arraylist

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("즐겨찾는 정류장");
        setContentView(R.layout.bookmarkactivity);
        bookmarkList=(ListView)findViewById(R.id.bookMarkList);

        myHelper=new MyDBHelper(this);
        sqlDB=myHelper.getReadableDatabase();
        Cursor cursor;
        cursor=sqlDB.rawQuery("SELECT * FROM bookMarkDB;",null);

        while(cursor.moveToNext()) {    //즐겨찾기한 정류장들을 arraylist에 저장하는 과정
            StationInfo temp=new StationInfo();
            temp.staNum=cursor.getString(0);
            temp.staName=cursor.getString(1);
            arr.add(temp);
        }
        cursor.close();
        sqlDB.close();

        adapter=new StationInfoAdapter(getApplicationContext(),arr);
        bookmarkList.setAdapter(adapter);
        bookmarkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                Intent intent=new Intent(BookMarkActivity.this,BusInfoActivity.class);
                intent.putExtra("number",number);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });

        //광고 나타나게 하기
        bookMarkAd=findViewById(R.id.bookMarkAd);
        AdRequest adRequest=new AdRequest.Builder().build();
        bookMarkAd.loadAd(adRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        arr.clear();
        myHelper=new MyDBHelper(this);
        sqlDB=myHelper.getReadableDatabase();
        Cursor cursor;
        cursor=sqlDB.rawQuery("SELECT * FROM bookMarkDB;",null);

        while(cursor.moveToNext()) {    //즐겨찾기한 정류장들을 arraylist에 저장하는 과정
            StationInfo temp=new StationInfo();
            temp.staNum=cursor.getString(0);
            temp.staName=cursor.getString(1);
            arr.add(temp);
        }
        cursor.close();
        sqlDB.close();

        adapter.notifyDataSetChanged();
    }
}
