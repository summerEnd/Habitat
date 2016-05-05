package com.sumauto.habitat.widget;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sumauto.habitat.R;
import com.sumauto.habitat.utils.CityDB;
import com.sumauto.wheel.BasePickerView;
import com.sumauto.wheel.WheelView;
import com.sumauto.wheel.WheelView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class PickCity extends BasePickerView {
    private static final String CITY_SQL = "SELECT DISTINCT cname,cid,pname FROM city WHERE pname='%s' ORDER BY cid DESC";
    private static final String AREA_SQL = "SELECT DISTINCT area,cname,cid FROM city WHERE cname='%s'";
    private Context mContext;
    private WheelView mProvinceWheel;
    private WheelView mCityWheel;
    private WheelView mAreaWheel;

    private TextView tvTitle;

    ArrayList<String> mProvinceList = new ArrayList<>();
    ArrayList<String> mCityList = new ArrayList<>();
    ArrayList<String> mAreaList = new ArrayList<>();

    private String province = "江苏省", city = "南京市", area = "";

    public PickCity(Context context) {
        super(context);
        this.mContext = context;

        LayoutInflater.from(context).inflate(R.layout.city_picker, contentContainer, true);
        mProvinceWheel = (WheelView) findViewById(R.id.wv_province);
        mCityWheel = (WheelView) findViewById(R.id.wv_city);
        mAreaWheel = (WheelView) findViewById(R.id.wv_area);

        tvTitle = (TextView) findViewById(R.id.tvTitle);

        mProvinceWheel.setAdapter(new StringAdapter(mProvinceList));
        mCityWheel.setAdapter(new StringAdapter(mCityList));
        mAreaWheel.setAdapter(new StringAdapter(mAreaList));

        mProvinceWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                province = mProvinceList.get(index);
                initData(String.format(CITY_SQL, province), mCityList);
                mCityWheel.requestLayout();
                mCityWheel.invalidate();
            }
        });

        mCityWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                city = mCityList.get(index);
                initData(String.format(AREA_SQL, city), mAreaList);
                mAreaWheel.requestLayout();
                mAreaWheel.invalidate();
            }
        });

        mAreaWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                area = mAreaList.get(index);
            }
        });

        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onSelect();
            }
        });
        contentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        initList();
    }

    protected void onSelect() {

    }

    void initList() {
        int pid = 0;
        int cid = 0;

        SQLiteDatabase db = new CityDB(mContext).getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT DISTINCT pname,pid FROM city ORDER BY pid ASC", null);
        while (cursor.moveToNext()) {
            String pname = cursor.getString(0);
            mProvinceList.add(pname);
            if (pname.contains("江苏"))
                pid = cursor.getInt(1);
        }
        cursor.close();


        cursor = db.rawQuery("SELECT DISTINCT cname,pid,pname,cid FROM city WHERE pid=? ORDER BY cid ASC", new String[]{pid + ""});
        while (cursor.moveToNext()) {
            String cname = cursor.getString(0);
            if (cname.contains("南京"))
                cid = cursor.getInt(3);

            mCityList.add(cname);
        }
        cursor.close();

        cursor = db.rawQuery("SELECT  DISTINCT area,pid,pname,cid,cname FROM city WHERE pid=? AND cid=? ", new String[]{pid + "", cid + "",});
        while (cursor.moveToNext()) {
            mAreaList.add(cursor.getString(0));
        }
        db.close();
        mProvinceWheel.setCurrentItem(pid);
        mCityWheel.setCurrentItem(cid);
        mAreaWheel.setCurrentItem(0);

    }

    void initData(String query, ArrayList<String> data) {
        SQLiteDatabase db = new CityDB(mContext).getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        data.clear();
        while (cursor.moveToNext()) {
            data.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
    }

    private static class StringAdapter implements WheelView.WheelAdapter<String> {
        List<String> data;

        public StringAdapter(List<String> data) {
            this.data = data;
        }

        @Override
        public int getItemsCount() {
            return data.size();
        }

        @Override
        public String getItem(int index) {
            return data.get(index);
        }

        @Override
        public int indexOf(String o) {
            int index = 0;
            for (String province : data) {
                if (province.equals(o)) {
                    return index;
                }
                index++;
            }
            return -1;
        }
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public String getProvince() {
        if (TextUtils.isEmpty(province)) {
            province=mProvinceList.get(mProvinceWheel.getCurrentItem());
        }
        return province;
    }

    public String getCity() {
        if (TextUtils.isEmpty(city)) {
            city=mCityList.get(mCityWheel.getCurrentItem());
        }
        return city;
    }

    public String getArea() {
        if (TextUtils.isEmpty(area)) {
            area=mAreaList.get(mAreaWheel.getCurrentItem());
        }
        return area;
    }

    public String getCityString() {
        return getProvince() + getCity() + getArea();
    }
}
