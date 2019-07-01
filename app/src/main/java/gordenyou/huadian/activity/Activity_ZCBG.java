package gordenyou.huadian.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

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

    @BindView(R.id.zcbg_newleixing)
    EdittextView newleixing;
    @BindView(R.id.zcbg_newbianma)
    EdittextView newbianma;
    @BindView(R.id.zcbg_newbeizhu)
    EdittextView newbeizhu;
    @BindView(R.id.zcbg_newbumen)
    EdittextView newbumen;
    @BindView(R.id.zcbg_newquyu)
    EdittextView newquyu;

    MySQLiteOpenHelper dbhelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    public void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_zcbg);
        ButterKnife.bind(this);
    }


    @Override
    public void LogicMethod() {

    }

    @Override
    public void initValues() {
        dbhelper = new MySQLiteOpenHelper(getBaseContext(), "temp_data.db", null, 1);
        sqLiteDatabase = dbhelper.getWritableDatabase();
    }

    @Override
    public void GetRequestResult(String data, int m) {

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
        }
        cursor.close();
    }
}
