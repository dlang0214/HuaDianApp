package gordenyou.huadian.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import gordenyou.huadian.R;
import gordenyou.huadian.common.MySQLiteOpenHelper;
import gordenyou.huadian.view.HeaderTitle;
import gordenyou.huadian.view.NumberView;
import gordenyou.huadian.view.ScannerView;
import gordenyou.huadian.view.TextshowView;
import tableLayout.TableLayout;

public class Activity_ZCGH extends BaseActivity {
    @BindView(R.id.zcgh_header)
    HeaderTitle header;
    @BindView(R.id.zcgh_tiaoma)
    ScannerView tiaoma;
    @BindView(R.id.zcgh_table_jieyong)
    TableLayout table_jieyong;
    @BindView(R.id.zcgh_table_guihuan)
    TableLayout table_guihuan;
    @BindView(R.id.zcgh_bumen)
    TextshowView bumen;
    @BindView(R.id.zcgh_jieyongren)
    TextshowView jieyongren;
    @BindView(R.id.zcgh_numjieyong)
    TextshowView num_jieyong;
    @BindView(R.id.zcgh_numguihuan)
    NumberView num_guihuan;
    @BindView(R.id.zcgh_guihuanren)
    ScannerView guihuanren;

    MySQLiteOpenHelper dbhelper;
    SQLiteDatabase sqLiteDatabase;
    Set<String> listDetail;
    Set<String> checklist = new HashSet<>();

    ArrayList<String> temp_jieyonglist;
    ArrayList<String> temp_guihuanlist;

    String[] list_header = {"借用人", "借用部门", "资产条码", "资产名称", "资产类型", "借用数量", "借用类型", "记录人", "借用时间"};
    String[] list_header1 = {"归还人", "借用单据", "资产条码", "资产名称", "归还数量", "借用部门", "归还时间"};

    String BorrowListID = "";

    @Override
    public void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_zcgh);
        ButterKnife.bind(this);
        SetTableLayout(table_jieyong);
        SetHeader(list_header);
        SetTableLayout(table_guihuan);
        SetHeader(list_header1);
    }

    @Override
    public void LogicMethod() {
        guihuanren.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tiaoma.getText().equals("")) {
                    ShowWarmMsgDialog("请扫描条码！");
                } else {
                    if (guihuanren.getText().equals("")) {
                        ShowWarmMsgDialog("请输入归还人！");
                    } else {
                        if (checklist.contains(tiaoma.getText())) {
                            ShowWarmMsgDialog("归还列表已存在此归还数据！");
                        } else {
                            String temp = tiaoma.getText() + "○";
                        }
                    }
                }
            }
        });
    }

    @Override
    public void initValues() {
        dbhelper = new MySQLiteOpenHelper(getBaseContext(), "temp_data.db", null, 1);
        sqLiteDatabase = dbhelper.getWritableDatabase();
        listDetail = getSetValues("AssetReturnDetail");
    }

    @Override
    public void GetRequestResult(String data, int m) {

    }

    @Override
    public void ScannerEvent() {
        BorrowListID = "";
        Cursor cursor = sqLiteDatabase.rawQuery("select BorrowListID from AssetBorrowListDetail where AssetID= '" + tiaoma.getText() + "'and State = 0", null);
        if (cursor.getCount() == 1) {
            cursor.moveToNext();
            BorrowListID = cursor.getString(cursor.getColumnIndex("BorrowListID"));
        } else {
            ShowErrMsgDialog("此条码不存在借用记录！");
        }
        cursor.close();

        temp_jieyonglist = new ArrayList<>();
        Cursor cursor1 = sqLiteDatabase.rawQuery("select * from AssetBorrowListDetail where BorrowListID = '" + BorrowListID + "'", null);
        while (cursor1.moveToNext()) {
            String str_jieyongren = cursor1.getString(cursor1.getColumnIndex("Borrowman"));
            String str_bumen = cursor1.getString(cursor1.getColumnIndex("DeptID"));
            String str_tiaoma = cursor1.getString(cursor1.getColumnIndex("AssetID"));
            String mingchen = cursor1.getString(cursor1.getColumnIndex("MaterielName"));
            String leixing = cursor1.getString(cursor1.getColumnIndex("MaterielKind"));
            String shuliang = cursor1.getString(cursor1.getColumnIndex("BorrowNum"));
            String jieyongleixing = cursor1.getString(cursor1.getColumnIndex("BorrowType"));
            String jiluren = cursor1.getString(cursor1.getColumnIndex("UserID"));
            String shijian = cursor1.getString(cursor1.getColumnIndex("EditDate"));
            String temp = str_jieyongren + "○" + str_bumen + "○" + str_tiaoma + "○" + mingchen + "○" + leixing
                    + "○" + shuliang + "○" + jieyongleixing + "○" + jiluren + "○" + shijian;
            temp_jieyonglist.add(temp);
            if (tiaoma.getText().equals(str_tiaoma)) {
                bumen.SetText(str_bumen);
                jieyongren.SetText(str_jieyongren);
                num_jieyong.SetText(shuliang);
            }
        }
        SetHeader(list_header);
        SetTableLayout(table_jieyong);
        firstRowAsTitle(temp_jieyonglist);
        cursor1.close();
    }
}
