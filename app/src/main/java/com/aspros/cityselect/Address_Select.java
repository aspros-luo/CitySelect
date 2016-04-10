package com.aspros.cityselect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aspros on 16/4/9.
 */
public class Address_Select extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private DatabaseHelper dbHelp;
    private SQLiteDatabase db;
    private List<provinceInfo> provinceList = new ArrayList<provinceInfo>();
    private List<cityInfo> cityList = new ArrayList<cityInfo>();
    private List<areaInfo> areaList = new ArrayList<areaInfo>();

    private Spinner spinnerProvince;
    private Spinner spinnerCity;
    private Spinner spinnerArea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_select);

        dbHelp = new DatabaseHelper(this, "countryList.db", null, 1);
        db = dbHelp.getWritableDatabase();

        spinnerProvince = (Spinner) findViewById(R.id.provinces);
        spinnerCity = (Spinner) findViewById(R.id.city);
        spinnerArea = (Spinner) findViewById(R.id.area);



        ArrayAdapter<provinceInfo> adapter = new ArrayAdapter<provinceInfo>(this, R.layout.support_simple_spinner_dropdown_item, getProvinceList());
        spinnerProvince.setAdapter(adapter);
//        spinnerProvince.setSelection(0, true);
        spinnerProvince.setOnItemSelectedListener(this);

        spinnerArea.setVisibility(View.INVISIBLE);

        findViewById(R.id.back_address).setOnClickListener(this);
    }

    private String provinceId = null;
    private String cityId = null;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.provinces:
                provinceId = provinceList.get(position).getId();

                ArrayAdapter<cityInfo> cityAdapter = new ArrayAdapter<cityInfo>(this, R.layout.support_simple_spinner_dropdown_item, getCityList(provinceId));
                spinnerCity.setAdapter(cityAdapter);
                spinnerCity.setOnItemSelectedListener(this);
                break;
            case R.id.city:
                cityId = cityList.get(position).getId();
                Toast.makeText(Address_Select.this, getAreaList(provinceId, cityId).size()+"", Toast.LENGTH_SHORT).show();
                if(getAreaList(provinceId, cityId).size()>0) {

                    spinnerArea.setVisibility(View.VISIBLE);

                    ArrayAdapter<areaInfo> areaAdapter = new ArrayAdapter<areaInfo>(this, R.layout.support_simple_spinner_dropdown_item, getAreaList(provinceId, cityId));
                    spinnerArea.setAdapter(areaAdapter);
                }else
                {
                    spinnerArea.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.area:
                break;
            default:
                break;
        }

    }


    private List<provinceInfo> getProvinceList() {
        Cursor cursor = db.rawQuery("select * from province", null);
        while (cursor.moveToNext()) {
            String provinceName = cursor.getString(cursor.getColumnIndex("provinceName"));
            String provinceId = cursor.getString(cursor.getColumnIndex("id"));
            provinceInfo pi = new provinceInfo();
            pi.setId(provinceId);
            pi.setProvinceName(provinceName);
            provinceList.add(pi);
        }
        cursor.close();
        return provinceList;
    }

    private List<cityInfo> getCityList(String provinceId) {
        cityList = new ArrayList<cityInfo>();
        Cursor cursor = db.rawQuery("select * from city where provinceId=?", new String[]{provinceId});
        while (cursor.moveToNext()) {
            String cityName = cursor.getString(cursor.getColumnIndex("cityName"));
            String cityId = cursor.getString(cursor.getColumnIndex("id"));
            cityInfo ci = new cityInfo();
            ci.setId(cityId);
            ci.setCityName(cityName);
            cityList.add(ci);
        }
        cursor.close();
        return cityList;
    }

    private List<areaInfo> getAreaList(String provinceId, String cityId) {
        areaList = new ArrayList<areaInfo>();
        if (provinceId != null && cityId != null) {

            Cursor cursor = db.rawQuery("select * from area where provinceId=? and cityId=?", new String[]{provinceId, cityId});
            while (cursor.moveToNext()) {
                String areaName = cursor.getString(cursor.getColumnIndex("areaName"));
                String areaId = cursor.getString(cursor.getColumnIndex("id"));
                areaInfo ai = new areaInfo();
                ai.setId(areaId);
                ai.setAreaName(areaName);
                areaList.add(ai);
            }
            cursor.close();
        } else {
        }
        return areaList;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_address:
                Intent i = new Intent();
                i.putExtra("provinceStr", spinnerProvince.getSelectedItem().toString());
                i.putExtra("cityStr", spinnerCity.getSelectedItem().toString());
                i.putExtra("areaStr", spinnerArea.getSelectedItem().toString());
                setResult(RESULT_OK, i);
                finish();
                break;
            default:
                break;
        }
    }


    private class provinceInfo {
        String id;
        String provinceName;

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return provinceName;
        }

    }

    private class cityInfo {
        String id;
        String cityName;

        public void setId(String id) {
            this.id = id;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getId() {
            return id;
        }

        public String getCityName() {
            return cityName;
        }

        @Override
        public String toString() {
            return cityName;
        }
    }

    private class areaInfo {
        String id;
        String areaName;

        public void setId(String id) {
            this.id = id;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getId() {
            return id;
        }

        public String getAreaName() {
            return areaName;
        }

        @Override
        public String toString() {
            return areaName;
        }
    }
}
