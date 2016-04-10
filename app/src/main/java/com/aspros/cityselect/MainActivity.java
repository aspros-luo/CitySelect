package com.aspros.cityselect;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView cityShow;
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

    private TextView address_show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityShow = (TextView) findViewById(R.id.showCity);
        address_show= (TextView) findViewById(R.id.address_show);

//        findViewById(R.id.select_address).setEnabled(false);

        findViewById(R.id.select_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Address_Select.class);
                startActivityForResult(i,1);
            }
        });

        Intent is=new Intent(this,LoadDataService.class);
        startService(is);

//        dbHelp = new DatabaseHelper(this, "countryList.db", null, 1);
//        db = dbHelp.getWritableDatabase();
//        findViewById(R.id.loadData).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                parseXML();
//                cityShow.setText(sb.toString());
//            }
//        });
    }

    private void parseXML() {
        try {
            //实例化StringBuilder
            sb = new StringBuilder("");
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
                    db.execSQL("insert into area values(?,?,?,?)", new String[]{areaCode,provinceCode,cityCode, areaName});
                    }
                    sb.append(countryName + countryCode + "\n" + provinceName + provinceCode + "\n" + cityName + cityCode + "\n" + areaName + areaCode + "\n");
                }
                xrp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            Toast.makeText(MainActivity.this, "data has be done", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case 1:
                if (resultCode == RESULT_OK) {
                    super.onActivityResult(requestCode, resultCode, data);
                    String provinceStr = data.getStringExtra("provinceStr");
                    String cityStr = data.getStringExtra("cityStr");
                    String areaStr = data.getStringExtra("areaStr");
                    address_show.setText("省：" + provinceStr + "｜城市：" + cityStr + "｜区：" + areaStr);
                }
                break;
            default:
                break;
        }


    }
}
