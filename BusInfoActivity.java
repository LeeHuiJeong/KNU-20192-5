package com.byeongseop.nt900.daeguebusapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//특정정류장의 현재 버스 도착정보 알려주는 화면(BusInfo, BusInfoAdapter,BusInfoView)와 세트임

public class BusInfoActivity extends AppCompatActivity {            //특정 정류소의 버스 도착정보 보여줌
    ArrayList<BusInfo> busInfos=new ArrayList<BusInfo>();       //홈페이지로부터 파싱해서 뽑아낸 어떤 정류장의 버스 도착정보를 저장할 어레이리스트
    TextView staName;//현재 정류장 이름
    ListView busList;//버스 도착정보 표현할 리스트뷰
    BusInfoAdapter busAdapter;
    String number="";//현재 정류소 번호
    String name="";//현재 정류소 이름
    TextView empty;
    CheckBox chkBookMark;//즐겨찾기 추가 여부 처리위한 체크박스(별모양)
    SwipeRefreshLayout refreshLayout;
    AdView busAd;

    public static final String PREFS_NAME="MyPrefFile";//체크박스 선택여부(UI) 정보 저장할 Preference의 이름

    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;

    class MyThread extends Thread {
        @Override
        public void run() {
            try {
                URL url=new URL("http://businfo.daegu.go.kr/ba/arrbus/arrbus.do");
                HttpURLConnection con=(HttpURLConnection)url.openConnection();
                con.setDoOutput(true);
                String parameter="act=arrbus&winc_id="+number;
                OutputStream wr=con.getOutputStream();//con에 parameter를 쓰기 위해 스트림과 연결
                wr.write(parameter.getBytes());//파라미터 정보를 con에 씀
                wr.flush();//다 쓰고 난 다음 버퍼를 비워줌.

                //버스 도착정보 읽기
                BufferedReader rd=new BufferedReader(new InputStreamReader(con.getInputStream(),"euc-kr"));//한글을 읽으려면 euc-kr타입으로 설정해줘야 함

                Source src=new Source(rd);  //읽어온 정보 소스를 가지는 소스 객체(파싱의 대상이 됨)
                src.fullSequentialParse();//html코드 즉 소스 안의 태그들을 모두 element타입으로 객체화
                List list=src.getAllElements();//객체화된 모든 태그를 가져와서 리스트 형식으로 차래대로 저장해놓음.

                BusInfo busInfo=new BusInfo();//한 줄에 들어갈 데이터 묶음
                for(int i=0;i<list.size();i++) {
                    Element tag=(Element)list.get(i);

                    //소스에서 <span>태그에 내가 필요한 정보가 들어있음, <span>태그 안의 class속성 값들이 내가 필요한 정보임
                    //route_no
                    //arr_state
                    //cur_pos nsbus,cur_pos

                    if(tag.getName().equals("span")) {//span태그 찾고 태그의 속성값(class 속성에 해당하는 값)찾기
                        switch (tag.getAttributeValue("class")) {//클래스 속성값 찾기
                            case "route_no":
                                busInfo=new BusInfo();//한 줄에 들어갈 데이터 묶음
                                busInfo.route_no=tag.getTextExtractor().toString();//태그의 class속성의 값을 추출해서 저장
                                break;
                            case "arr_state":
                                busInfo.arr_state=tag.getTextExtractor().toString();
                                break;
                            case "cur_pos":
                            case "cur_pos busNN":
                                busInfo.cur_pos=tag.getTextExtractor().toString();
                                busInfos.add(busInfo);
                                break;
                        }
                    }

                }
                myHandler.sendEmptyMessage(0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    Handler myHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {

                busAdapter = new BusInfoAdapter(BusInfoActivity.this, busInfos);
                busList.setAdapter(busAdapter);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_info);

        Intent intent=getIntent();
        number=intent.getStringExtra("number");
        name=intent.getStringExtra("name");

        staName=(TextView)findViewById(R.id.staName);
        busList=(ListView)findViewById(R.id.busList);
        empty=(TextView)findViewById(android.R.id.empty);//리스트 비었을 때 보여줄 뷰의 아이드는 android:id:empty이다!!
        busList.setEmptyView(empty);//리스트가 비었을 경우 empty라는 택스트뷰를 리스트뷰 대신에 보여줌(어뎁터에 달기 전에 달아줌)


        chkBookMark=(CheckBox)findViewById(R.id.chkBookMark);

        SharedPreferences settings=getSharedPreferences(PREFS_NAME,0);
        boolean chk=settings.getBoolean("checked"+name,false);
        chkBookMark.setChecked(chk);


        staName.setText(name);
        MyThread myThread=new MyThread();
        myThread.start();


        myHelper=new MyDBHelper(this);//즐겨찾기 추가 삭제 구현
        chkBookMark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chkBookMark.isChecked()==true) { //체크된 경우
                    sqlDB=myHelper.getWritableDatabase();//쓸 수 있는 myHelper에서 생성된 데이터베이스 열기
                    sqlDB.execSQL("INSERT INTO bookMarkDB VALUES('"+number+"','"+name+"');");
                    sqlDB.close();
                    Toast.makeText(getApplicationContext(),"즐겨찾기에 추가되었습니다.",Toast.LENGTH_SHORT).show();
                } else {    //체크를 해재한 경우
                    sqlDB=myHelper.getWritableDatabase();
                    String query = "DELETE FROM bookMarkDB WHERE staNum='"+number+"';";
                    sqlDB.execSQL(query);
                    sqlDB.close();
                    Toast.makeText(getApplicationContext(),"즐겨찾기에서 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                busInfos.clear();
                MyThread myThread2=new MyThread();
                myThread2.start();
                refreshLayout.setRefreshing(false);//새로고침 완료 후 이 코드를 써야 새로고침 아이콘 사라짐
            }
        });

        //광고 달기
        busAd=findViewById(R.id.busAd);
        AdRequest adRequest=new AdRequest.Builder().build();
        busAd.loadAd(adRequest);
    }

    @Override
    protected void onStop() {   //다른 화면으로 이동할 때 로컬에 즐겨찾기버튼 체크됬는지 아닌지 저장해놓음.
        super.onStop();
        SharedPreferences settings=getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor=settings.edit();
        editor.putBoolean("checked"+name,chkBookMark.isChecked());
        editor.commit();
    }
}
