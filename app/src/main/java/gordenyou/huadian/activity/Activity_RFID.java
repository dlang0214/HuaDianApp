package gordenyou.huadian.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import gordenyou.huadian.R;
import gordenyou.huadian.common.MySQLiteOpenHelper;
import gordenyou.huadian.view.HeaderTitle;
import gordenyou.huadian.view.ScannerView;
import gordenyou.huadian.view.TextshowView;

public class Activity_RFID extends BaseActivity {
    @BindView(R.id.rfid_header)
    HeaderTitle headerTitle;
    @BindView(R.id.rfid_bianma)
    ScannerView bianma;
    @BindView(R.id.rfid_mingchen)
    TextshowView mingchen;
    @BindView(R.id.rfid_leixin)
    TextshowView leixin;
    @BindView(R.id.rfid_quyu)
    TextshowView quyu;
    @BindView(R.id.rfid_chengben)
    TextshowView chengben;
//    @BindView(R.id.rfid_zerenren)
//    TextshowView zerenren;
//    @BindView(R.id.rfid_suoyouren)
//    TextshowView shiyongren;
//    @BindView(R.id.rfid_chuchang)
//    TextshowView chuchang;
//    @BindView(R.id.rfid_beizhu)
//    TextshowView beizhu;
    @BindView(R.id.rfid_EPC)
    ScannerView epc;

    MySQLiteOpenHelper dbhelper;
    SQLiteDatabase sqLiteDatabase;
    Set<String> list_change = new HashSet<>();

    String stgongsi, stbianma, stmingchen, stleixin, stquyu, stchengben, stzerenren, stshiyongren, stchuchang, stbeizhu, stepc;

    @Override
    public void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_rfid);
        ButterKnife.bind(this);
    }

    @Override
    public void LogicMethod() {
        headerTitle.getRightbutton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getScreenData();
                if(stbianma.isEmpty()){
                    ShowWarmMsgDialog("请扫描条码！");
                }else if(stepc.isEmpty()){
                    ShowWarmMsgDialog("请读取RFID！");
                }else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("rMaterielInfo", stbianma);
                }
            }
        });
    }

    @Override
    public void initValues() {
        dbhelper = new MySQLiteOpenHelper(getBaseContext(), "temp_data.db", null, 1);
        sqLiteDatabase = dbhelper.getWritableDatabase();
        list_change = getSetValues("rMaterielInfo");
    }

    private void getScreenData() {
        stbianma = bianma.getText();
        stepc = epc.getText();
    }

    private void SetScreenData() {
        mingchen.SetText(stmingchen);
        leixin.SetText(stleixin);
        chengben.SetText(stchengben);
        quyu.SetText(stquyu);
//        zerenren.SetText(stzerenren);
//        shiyongren.SetText(stshiyongren);
//        chuchang.SetText(stchuchang);
//        beizhu.SetText(stbeizhu);
    }

    @Override
    public void GetRequestResult(String data, int m) {
    }


    @Override
    public void ScannerEvent() {
        Cursor cursor = sqLiteDatabase.rawQuery("select * from rMaterielInfo where mcode = '" + bianma.getText() + "'", null);
        while(cursor.moveToNext()){
            stmingchen = cursor.getString(cursor.getColumnIndex("MaterielName"));
            stleixin = cursor.getString(cursor.getColumnIndex("MaterielKind"));
            stchengben = cursor.getString(cursor.getColumnIndex("CostCenter"));
            stquyu = cursor.getString(cursor.getColumnIndex("areaname"));
//            stzerenren = cursor.getString(cursor.getColumnIndex("resman"));
//            stshiyongren = cursor.getString(cursor.getColumnIndex("ownername"));
//            stchuchang = cursor.getString(cursor.getColumnIndex("productsn"));
//            stbeizhu = cursor.getString(cursor.getColumnIndex("newremark"));
        }
        cursor.close();
        SetScreenData();
    }
}
