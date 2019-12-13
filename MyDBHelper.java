package com.byeongseop.nt900.daeguebusapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// SQLite데이터베이스를 쓰기위해 쓴 코드(신경안써도 됨)

public class MyDBHelper extends SQLiteOpenHelper {
    public MyDBHelper(Context context) {
        super(context, "bookMarkDB", null, 1);//bookMarkDB:만들어질 DB의 이름
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE bookMarkDB (staNum CHAR(10), staName CHAR(30));");//정류소 번호와 이름을 저장하는 bookMarkDB라는 데이터베이스를 만듬
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS bookMarkDB");
        onCreate(db);
    }
}
