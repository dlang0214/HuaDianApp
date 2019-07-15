package gordenyou.huadian.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.barcode.Scanner;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import gordenyou.huadian.R;
import gordenyou.huadian.common.MySQLiteOpenHelper;
import gordenyou.huadian.view.EdittextView;
import gordenyou.huadian.view.HeaderTitle;
import gordenyou.huadian.view.ScannerView;
import gordenyou.huadian.view.TextshowView;

public class Activity_ZCBG extends BaseActivity {
    @BindView(R.id.zcbg_header)
    HeaderTitle headerTitle;
    @BindView(R.id.zcbg_tiaoma)
    ScannerView tiaoma;
    @BindView(R.id.zcbg_EPC)
    TextshowView epc;
    @BindView(R.id.zcbg_mingchen)
    TextshowView mingchen;
    @BindView(R.id.zcbg_leixing)
    TextshowView leixing;
    @BindView(R.id.zcbg_bianma)
    TextshowView bianma;
    @BindView(R.id.zcbg_beizhu)
    TextshowView beizhu;
    @BindView(R.id.zcbg_bumen)
    TextshowView bumen;
    @BindView(R.id.zcbg_quyu)
    TextshowView quyu;
    @BindView(R.id.zcbg_zerenren)
    TextshowView zerenren;
    @BindView(R.id.zcbg_shiyongren)
    TextshowView shiyongren;

    @BindView(R.id.zcbg_newzerenren)
    EdittextView newzerenren;
    @BindView(R.id.zcbg_newshiyongren)
    EdittextView newshiyongren;
    @BindView(R.id.zcbg_newbianma)
    EdittextView newbianma;
    @BindView(R.id.zcbg_newbeizhu)
    EdittextView newbeizhu;
    @BindView(R.id.zcbg_newfangjian)
    EdittextView newfangjian;
    @BindView(R.id.zcbg_newquyu)
    EdittextView newquyu;

    MySQLiteOpenHelper dbhelper;
    SQLiteDatabase sqLiteDatabase;
    Set<String> list_change;
    boolean change = false;

    @Override
    public void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_zcbg);
        ButterKnife.bind(this);
    }

    @Override
    public void LogicMethod() {
        headerTitle.getRightbutton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        ContentValues con = new ContentValues();
                        con.put("newres",newzerenren.getText());
                        con.put("newowner",newshiyongren.getText());
                        con.put("newsn",newbianma.getText());
                        con.put("newremark",newbeizhu.getText());
                        con.put("newroom",newfangjian.getText());
                        con.put("newarea",newquyu.getText());
                        if(sqLiteDatabase.update("rMaterielInfo", con,"mcode = ?" , new String[]{tiaoma.getText()}) == 1){
                            ShowWarmMsgDialog("资产变更成功！");
                            list_change.add(tiaoma.getText());
                            change = true;
                            newzerenren.getEditText().setText("");
                            newshiyongren.getEditText().setText("");
                            newbianma.getEditText().setText("");
                            newbeizhu.getEditText().setText("");
                            newfangjian.getEditText().setText("");
                            newquyu.getEditText().setText("");
                        }
                    }
        });

    }

    @Override
    public void initValues() {
        dbhelper = new MySQLiteOpenHelper(getBaseContext(), "temp_data.db", null, 1);
        sqLiteDatabase = dbhelper.getWritableDatabase();
        list_change = getSetValues("ZCBG");
        list_change = new HashSet<>(list_change);
    }

    @Override
    public void GetRequestResult(String data, int m) {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getRepeatCount() == 0) {
            switch (keyCode) {
                case 221:
                case 220:
                    Scanner.Read();
                    break;
                case 4:
                    if(change){
                        SetValues("ZCBG", list_change);
                        addList("ZCBG");
                    }
                    finish();
                    break;
            }
        }
        return true;
    }

    @Override
    public void ScannerEvent() {
        Cursor cursor = sqLiteDatabase.rawQuery("select * from rMaterielInfo where mcode = '" + tiaoma.getText() + "'", null);
        while(cursor.moveToNext()){
            epc.SetText(cursor.getString(cursor.getColumnIndex("EPC")));
            mingchen.SetText(cursor.getString(cursor.getColumnIndex("MaterielName")));
            leixing.SetText(cursor.getString(cursor.getColumnIndex("MaterielKind")));
            bianma.SetText(cursor.getString(cursor.getColumnIndex("productsn")));
            beizhu.SetText(cursor.getString(cursor.getColumnIndex("newremark")));
            bumen.SetText(cursor.getString(cursor.getColumnIndex("CostCenter")));
            quyu.SetText(cursor.getString(cursor.getColumnIndex("areaname")));
            zerenren.SetText(cursor.getString(cursor.getColumnIndex("resman")));
            shiyongren.SetText(cursor.getString(cursor.getColumnIndex("ownername")));
        }
        cursor.close();
    }
}
