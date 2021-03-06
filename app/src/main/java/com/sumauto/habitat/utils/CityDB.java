package com.sumauto.habitat.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.sumauto.util.PListHandler;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/05/02 5.6.6 
 */
public class CityDB extends SQLiteOpenHelper {
    private Context context;

    private static final String CREATE_DB = "CREATE TABLE city("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "pname TEXT,"//省名
            + "pid INT,"
            + "cname TEXT,"//城市名
            + "cid INT ,"
            + "area TEXT"//区
            + ")";

    public CityDB(Context context) {
        super(context, "city", null, 3);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(context.getAssets().open("city.sql")));

            String line;
            String buffer = "";
            while ((line = in.readLine()) != null) {
                buffer += line;
                if (line.trim().endsWith(";")) {
                    db.execSQL(buffer.replace(";", ""));
                    buffer = "";
                }
            }
        } catch (IOException e) {
            Log.e("db-error", e.toString());
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                Log.e("db-error", e.toString());
            }
        }
//
//        try {
//            db.execSQL(CREATE_DB);
//            PListHandler plistHandler = new PListHandler();
//            SAXParserFactory.newInstance()
//                    .newSAXParser()
//                    .parse(context.getAssets().open("city.plist"), plistHandler);
//            HashMap<String, Object> mapResult = plistHandler.getMapResult();
//            processInit(db, mapResult, "");
//        } catch (ParserConfigurationException | SAXException | IOException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 读取数据库文件（.sql），并执行sql语句
     * */
    private void executeAssetsSQL(SQLiteDatabase db, String schemaName) {

    }
    @SuppressWarnings("unchecked")
    void processInit(SQLiteDatabase db, HashMap<String, Object> result, String title) {
        Set<String> keySet = result.keySet();
        for (String key : keySet) {
            Object value = result.get(key);
            if (value instanceof HashMap) {
                String nextTitle = TextUtils.isEmpty(title) ? "" : title + ",";
                nextTitle += key;
                processInit(db, (HashMap<String, Object>) value, nextTitle);
            } else if (value instanceof ArrayList) {
                ArrayList<String> array = (ArrayList<String>) value;
                String[] split = title.split(",");
                String pid = split[0];
                String pname = split[1];
                String cid = split[2];
                if (array.size() == 0) {
                    db.execSQL("INSERT INTO city (pname,pid,cname,cid,area) VALUES(?,?,?,?,?)",
                            new String[]{pname, pid, key, cid, ""});
                } else {
                    for (String area : array) {
                        db.execSQL("INSERT INTO city (pname,pid,cname,cid,area) VALUES(?,?,?,?,?)",
                                new String[]{pname, pid, key, cid, area});
                    }
                }
            }else{
                Log.e("","--->unknow type"+ value);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS city");
        onCreate(db);
    }
}
