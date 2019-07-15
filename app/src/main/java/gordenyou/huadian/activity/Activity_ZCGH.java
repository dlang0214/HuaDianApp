package gordenyou.huadian.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.barcode.Scanner;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import gordenyou.huadian.R;
import gordenyou.huadian.common.CommonMethod;
import gordenyou.huadian.common.MySQLiteOpenHelper;
import gordenyou.huadian.util.UserInfo;
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
    Set<String> zcgh_change;  //记录变化的单据
    Set<String> zcjy_change;
    Set<String> checklist = new HashSet<>();//记录当前表格中的条码
    boolean change = false;

    ArrayList<String> temp_jieyonglist = new ArrayList<>();
    ArrayList<String> temp_guihuanlist = new ArrayList<>();

    String all_bumen, all_mingchen, all_leixing, all_jieyongren;

    String[] list_header = {"借用人", "借用部门", "借用单号", "资产条码", "资产名称", "资产类型", "借用数量", "借用类型", "记录人", "借用时间"};
    String[] list_header1 = {"归还人", "借用单号", "资产条码", "资产名称", "资产类型", "归还数量", "借用部门", "借用人", "归还类型", "记录人", "归还时间"};

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
                            String temp = guihuanren.getText() + "○" + BorrowListID + "○" + tiaoma.getText() + "○" + all_mingchen
                                    + "○" + all_leixing + "○" + num_guihuan.getNumber() + "○" + all_bumen + "○" + all_jieyongren + "○" + " "
                                    + "○" + UserInfo.USERNAME + "○" + CommonMethod.getTime();
                            temp_guihuanlist.add(temp);
                            SetTableLayout(table_guihuan);
                            SetHeader(list_header1);
                            firstRowAsTitle(temp_guihuanlist);
                            checklist.add(tiaoma.getText());
                        }
                    }
                }
                CommonMethod.CloseKeyboard(view);
            }
        });

        header.getRightbutton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp_guihuanlist.isEmpty()){
                    ShowErrMsgDialog("请先添加归还资产！");
                }else {
                    SaveAssetReturnDetail();
                }
            }
        });
    }

    private void SaveAssetReturnDetail() {
        try {
            sqLiteDatabase.beginTransaction();
            for (String i : temp_guihuanlist) {
                String[] temp = i.split("○");
                ContentValues contentValues = new ContentValues();
                contentValues.put("Returnman", temp[0]);
                contentValues.put("BorrowListID", temp[1]);
                contentValues.put("AssetID", temp[2]);
                contentValues.put("MaterielName", temp[3]);
                contentValues.put("MaterielKind", temp[4]);
                contentValues.put("ReturnNum", temp[5]);
                contentValues.put("DeptName", temp[6]);
                contentValues.put("Borrowman", temp[7]);
                contentValues.put("UserName", temp[9]);
                contentValues.put("EditDate", temp[10]);

                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("State", 1);

                if (sqLiteDatabase.insertOrThrow("AssetReturnDetail", null, contentValues) != -1 &&
                        sqLiteDatabase.update("AssetBorrowListDetail",contentValues1, "BorrowListID = ? and AssetID = ?" , new String[]{temp[1], temp[2]}) == 1) {
                    zcgh_change.add(temp[1]);
                    zcjy_change.add(temp[1]);
                    change = true;
                }
            }
            sqLiteDatabase.setTransactionSuccessful();
            SetTableLayout(table_jieyong);
            ClearData();
            SetTableLayout(table_guihuan);
            ClearData();
            tiaoma.SetText("");
            bumen.SetText("");
            num_jieyong.SetText("");
            num_guihuan.getEdittext().setText("");
            guihuanren.SetText("");
            jieyongren.SetText("");
            ShowWarmMsgDialog("归还成功！");
        } catch (Exception e) {
            ShowErrMsgDialog(e.getMessage());
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    @Override
    public void initValues() {
        dbhelper = new MySQLiteOpenHelper(getBaseContext(), "temp_data.db", null, 1);
        sqLiteDatabase = dbhelper.getWritableDatabase();
        zcgh_change = getSetValues("ZCGH");
        zcgh_change = new HashSet<>(zcgh_change);
        zcjy_change = getSetValues("AssetBorrowListDetail");
        zcjy_change = new HashSet<>(zcjy_change);
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
                        SetValues("ZCGH", zcgh_change);
                        addList("ZCGH");
                        SetValues("AssetBorrowListDetail", zcjy_change);
                        addList("AssetBorrowListDetail");
                    }
                    finish();
                    break;
            }
        }
        return true;
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

        Cursor cursor1 = sqLiteDatabase.rawQuery("select * from AssetBorrowListDetail where BorrowListID = '" + BorrowListID + "'", null);
        while (cursor1.moveToNext()) {
            String str_jieyongren = cursor1.getString(cursor1.getColumnIndex("Borrowman"));
            String str_bumen = cursor1.getString(cursor1.getColumnIndex("DeptName"));
            String str_tiaoma = cursor1.getString(cursor1.getColumnIndex("AssetID"));
            String mingchen = cursor1.getString(cursor1.getColumnIndex("MaterielName"));
            String leixing = cursor1.getString(cursor1.getColumnIndex("MaterielKind"));
            String shuliang = cursor1.getString(cursor1.getColumnIndex("BorrowNum"));
            String jieyongleixing = cursor1.getString(cursor1.getColumnIndex("BorrowType"));
            String jiluren = cursor1.getString(cursor1.getColumnIndex("UserName"));
            String shijian = cursor1.getString(cursor1.getColumnIndex("EditDate"));
            String temp = str_jieyongren + "○" + str_bumen + "○" + BorrowListID + "○" + str_tiaoma + "○" + mingchen + "○" + leixing
                    + "○" + shuliang + "○" + jieyongleixing + "○" + jiluren + "○" + shijian;
            temp_jieyonglist.add(temp);
            if (tiaoma.getText().equals(str_tiaoma)) {
                bumen.SetText(str_bumen);
                jieyongren.SetText(str_jieyongren);
                num_jieyong.SetText(shuliang);
                all_bumen = str_bumen;
                all_jieyongren = str_jieyongren;
                all_leixing = leixing;
                all_mingchen = mingchen;
            }
        }
        SetTableLayout(table_jieyong);
        SetHeader(list_header);
        firstRowAsTitle(temp_jieyonglist);
        cursor1.close();
    }
}
