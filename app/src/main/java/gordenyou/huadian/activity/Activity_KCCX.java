package gordenyou.huadian.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import gordenyou.huadian.R;
import gordenyou.huadian.common.MySQLiteOpenHelper;
import gordenyou.huadian.view.HeaderTitle;
import gordenyou.huadian.view.ScannerView;
import gordenyou.huadian.view.TextshowView;
import tableLayout.TableLayout;

public class Activity_KCCX extends BaseActivity {
    @BindView(R.id.kccx_header)
    HeaderTitle headerTitle;
    @BindView(R.id.kccx_bianma)
    ScannerView bianma;
    @BindView(R.id.kccx_mingchen)
    TextshowView mingchen;
    @BindView(R.id.kccx_leixing)
    TextshowView leixing;
    @BindView(R.id.kccx_chengben)
    TextshowView chengben;
    @BindView(R.id.kccx_quyu)
    TextshowView quyu;
    @BindView(R.id.kccx_beizhu)
    TextshowView beizhu;
    @BindView(R.id.kccx_zhuangtai)
    TextshowView zhuangtai;
    @BindView(R.id.kccx_table)
    TableLayout table;

    ArrayList<String> list_temp = new ArrayList<>();
    String[] header = new String[]{ "资产名称", "借用人", "借用时间", "预计归还时间"};

    MySQLiteOpenHelper dbhelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    public void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_kccx);
        ButterKnife.bind(this);
        SetTableLayout(table);
        SetHeader(header);
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
        Cursor cursor1 = sqLiteDatabase.rawQuery("select * from rMaterielInfo where mcode = '" + bianma.getText() + "'", null);
        while(cursor1.moveToNext()){
            mingchen.SetText(cursor1.getString(cursor1.getColumnIndex("MaterielName")));
            leixing.SetText(cursor1.getString(cursor1.getColumnIndex("MaterielKind")));
            chengben.SetText(cursor1.getString(cursor1.getColumnIndex("CostCenter")));
            quyu.SetText(cursor1.getString(cursor1.getColumnIndex("areaname")));
            beizhu.SetText(cursor1.getString(cursor1.getColumnIndex("newremark")));
        }
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from AssetBorrowListDetail where State = 0 and AssetID = '" + bianma.getText() + "'", null);
        if(cursor.getCount() == 1){
            while (cursor.moveToNext()){
                String stmingchen = cursor.getString(cursor.getColumnIndex("MaterielName"));
                String stjieyongren = cursor.getString(cursor.getColumnIndex("Borrowman"));
                String stjieyongshijian = cursor.getString(cursor.getColumnIndex("EditDate"));
                String stguihuanshijian = cursor.getString(cursor.getColumnIndex("PlanDate"));

                String temp = stmingchen  + "○" + stjieyongren  + "○" + stjieyongshijian  + "○" + stguihuanshijian;
                list_temp.add(temp);
                zhuangtai.SetText("当前资产已被借用");
                zhuangtai.getTextview().setTextColor(getResources().getColor(R.color.busy));
            }
        }else{
            zhuangtai.SetText("资产在库，可以使用");
            zhuangtai.getTextview().setTextColor(getResources().getColor(R.color.pandianzhong));
        }
        cursor.close();
        firstRowAsTitle(list_temp);
    }
}
