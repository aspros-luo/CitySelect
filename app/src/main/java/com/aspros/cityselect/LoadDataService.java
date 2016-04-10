package com.aspros.cityselect;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Aspros on 16/4/10.
 */
public class LoadDataService extends IntentService {
    private StringBuilder sb;

    private DatabaseHelper dbHelp;
    private SQLiteDatabase db;
    private String countryName;
    private String countryCode;
    private String provinceName;
    private String provinceCode;
    private String cityName;
    private String cityCode;
    private String areaName;
    private String areaCode;


    public LoadDataService() {
        super("LoadDataService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        dbHelp = new DatabaseHelper(this, "countryList.db", null, 1);
        db = dbHelp.getWritableDatabase();
        try {
            //实例化StringBuilder
//            sb = new StringBuilder("");

            /*XML解析数据*/

            //得到Resources资源
            Resources r = getResources();
            //通过Resources，获得XmlResourceParser实例
            XmlResourceParser xrp = r.getXml(R.xml.loclist);
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String name = xrp.getName();
                    if (name.equals("CountryRegion")) {
                        countryName = xrp.getAttributeValue(null, "Name");
                        countryCode = xrp.getAttributeValue(null, "Code");
                        db.execSQL("insert into country values(?,?)", new String[]{countryCode, countryName});
                    }
                    if (name.equals("State")) {
                        provinceCode = xrp.getAttributeValue(null, "Code");
                        provinceName = xrp.getAttributeValue(null, "Name");
                        db.execSQL("insert into province values(?,?,?)", new String[]{provinceCode, countryCode, provinceName});

                    }
                    if (name.equals("City")) {
                        cityCode = xrp.getAttributeValue(null, "Code");
                        cityName = xrp.getAttributeValue(null, "Name");
                        db.execSQL("insert into city values(?,?,?)", new String[]{cityCode, provinceCode, cityName});
                    }
                    if (name.equals("Region")) {
                        areaCode = xrp.getAttributeValue(null, "Code");
                        areaName = xrp.getAttributeValue(null, "Name");
                        db.execSQL("insert into area values(?,?,?,?)", new String[]{areaCode, provinceCode, cityCode, areaName});
                    }
//                    sb.append(countryName + countryCode + "\n" + provinceName + provinceCode + "\n" + cityName + cityCode + "\n" + areaName + areaCode + "\n");
                }
                xrp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //数据加载完成后，弹窗提示
        Toast.makeText(LoadDataService.this, "LoadData Finished", Toast.LENGTH_SHORT).show();

    }
}
