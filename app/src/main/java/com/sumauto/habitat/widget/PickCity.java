package com.sumauto.habitat.widget;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bigkoo.pickerview.OptionsPickerView;
import com.sumauto.habitat.utils.CityDB;
import com.sumauto.util.PListHandler;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class PickCity extends OptionsPickerView<String> {
    private Context context;
    public PickCity(Context context) {
        super(context);
        this.context=context;
        ArrayList<String> province=new ArrayList<>();
        ArrayList<String> city=new ArrayList<>();
        ArrayList<String> area=new ArrayList<>();

        initData("SELECT DISTINCT pname,pid FROM city ORDER BY pid DESC",province,0);

    }

    void initData(String query,ArrayList<String> data,int index){
        SQLiteDatabase db = new CityDB(context).getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        data.clear();
        while (cursor.moveToNext()){
            data.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
    }

}
