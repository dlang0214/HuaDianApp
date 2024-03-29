package gordenyou.huadian.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.barcode.Scanner;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import gordenyou.huadian.R;
import gordenyou.huadian.common.CommonMethod;
import gordenyou.huadian.common.MySQLiteOpenHelper;
import gordenyou.huadian.util.UserInfo;
import gordenyou.huadian.view.EdittextView;
import gordenyou.huadian.view.HeaderTitle;
import gordenyou.huadian.view.NumberView;
import gordenyou.huadian.view.ScannerView;
import gordenyou.huadian.view.SpinnerView;
import gordenyou.huadian.view.TextshowView;
import tableLayout.TableLayout;

public class Activity_ZCJY extends BaseActivity {
    @BindView(R.id.zcjy_header)
    HeaderTitle headerTitle;
    @BindView(R.id.zcjy_bumen)
    SpinnerView bumen;
    @BindView(R.id.zcjy_tiaoma)
    ScannerView tiaoma;
    @BindView(R.id.zcjy_mingchen)
    TextshowView mingchen;
    @BindView(R.id.zcjy_leixing)
    TextshowView leixing;
    @BindView(R.id.zcjy_shuliang)
    NumberView shuliang;
    //    @BindView(R.id.zcjy_chuwei)
//    EdittextView chuwei;
    @BindView(R.id.zcjy_yuji)
    EdittextView yujishijian;
    @BindView(R.id.zcjy_jieyongren)
    ScannerView jieyongren;
    @BindView(R.id.zcjy_table)
    TableLayout tableLayout;
    @BindView(R.id.zcjy_linearlayout)
    LinearLayout linearLayout;

    MySQLiteOpenHelper dbhelper;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<String> temp_list = new ArrayList<>();
    Set<String> BorrowListDetail;
    Set<String> ListDetail;
    boolean change = false;
    Set<String> checklist = new HashSet<>();

    String str_bumen, str_tiaoma, str_mingcheng, str_leixing, str_jieyongren, str_time, str_jiluren, str_jieyongleixing = "", str_yujishijian;
    int int_shuliang;
    String BorrowID;

    @Override
    public void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_zcjy);
        ButterKnife.bind(this);
        String[] header = {"借用人", "借用部门", "资产条码", "资产名称", "资产类型", "借用数量", "借用类型", "记录人", "借用时间", "归还时间"};
        SetLayout(linearLayout);
        SetTableLayout(tableLayout);
        SetHeaderTitle(headerTitle);
        SetHeader(header);
    }

    @Override
    public void LogicMethod() {
        bumen.setSpinnerList(this, UserInfo.deptInfo);
        yujishijian.getEditText().setInputType(InputType.TYPE_CLASS_DATETIME);
        jieyongren.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_bumen = bumen.getName();
                if (str_bumen.equals("")) {
                    ShowErrMsgDialog("请选择部门！");
                } else {
                    str_tiaoma = tiaoma.getText();
                    if (checklist.contains(str_tiaoma)) {
                        ShowWarmMsgDialog("表格中已存在此资产借用记录!");
                    } else {
                        str_jieyongren = jieyongren.getText();
                        if(str_jieyongren.equals("")){
                            ShowWarmMsgDialog("请输入借用人！");
                            CommonMethod.SetFocus(jieyongren.getEditTextView());
                        }else {
                            str_yujishijian = yujishijian.getText();
                            if(str_yujishijian.equals("")){
                                ShowWarmMsgDialog("请输入预计借用时间！");
                                CommonMethod.SetFocus(yujishijian.getEditText());
                            }else{
                                str_mingcheng = mingchen.getText();
                                str_leixing = leixing.getText();
                                int_shuliang = shuliang.getNumber();
                                str_jiluren = UserInfo.USERNAME;
                                str_time = CommonMethod.getTime();
                                String temp = str_jieyongren + "○" + str_bumen + "○" + str_tiaoma + "○" + str_mingcheng + "○" +
                                        str_leixing + "○" + int_shuliang + "○" + str_jieyongleixing + "○" + str_jiluren + "○" + str_time + "○" + str_yujishijian;
                                temp_list.add(temp);
                                checklist.add(str_tiaoma);
                                firstRowAsTitle(temp_list);
                                CommonMethod.CloseKeyboard(view);
                            }

                        }

                    }
                }
            }
        });

        headerTitle.getRightbutton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp_list.isEmpty()){
                    ShowErrMsgDialog("请先添加借用资产！");
                }else {
                    InsertBorrowList();
                    InsertBorrowListDetail();
                }
            }
        });
    }

    @Override
    public void ScannerEvent() {
        Cursor cursor = sqLiteDatabase.rawQuery("select MaterielName, MaterielKind from rMaterielInfo where mcode = '" + tiaoma.getText() + "'", null);
        while (cursor.moveToNext()) {
            mingchen.SetText(cursor.getString(cursor.getColumnIndex("MaterielName")));
            leixing.SetText(cursor.getString(cursor.getColumnIndex("MaterielKind")));
        }
        cursor.close();
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
                        SetValues("AssetBorrowListDetail", BorrowListDetail);
                        addList("AssetBorrowListDetail");
                        SetValues("AssetBorrowList", ListDetail);
                        addList("AssetBorrowList");
                    }
                    finish();
                    break;
            }
        }
        return true;
    }

    private void InsertBorrowListDetail() {
        sqLiteDatabase.beginTransaction();
        try {
            for (String i : temp_list) {
                String[] temp = i.split("○");
                ContentValues contentValues = new ContentValues();
                contentValues.put("BorrowListID", BorrowID);
                contentValues.put("Borrowman", temp[0]);
                contentValues.put("DeptName", temp[1]);
                contentValues.put("AssetID", temp[2]);
                contentValues.put("MaterielName", temp[3]);
                contentValues.put("MaterielKind", temp[4]);
                contentValues.put("BorrowNum", temp[5]);
                contentValues.put("BorrowType", temp[6]);
                contentValues.put("UserName", temp[7]);
                contentValues.put("EditDate", temp[8]);
                contentValues.put("PlanDate", temp[9]);
                contentValues.put("State", 0); //0为被借用，1位已归还。
                if (sqLiteDatabase.insertOrThrow("AssetBorrowListDetail", null, contentValues) != -1) {
                    BorrowListDetail.add(BorrowID);
                    change = true;
                }
            }
            sqLiteDatabase.setTransactionSuccessful();

            ClearData();
            tiaoma.SetText("");
            mingchen.SetText("");
            leixing.SetText("");
            jieyongren.SetText("");
            ShowWarmMsgDialog("借用成功！");
        } catch (Exception e) {
            ShowErrMsgDialog(e.getMessage());
        } finally {
            sqLiteDatabase.endTransaction();

        }
    }

    private void InsertBorrowList() {
        BorrowID = "ABL" + CommonMethod.getListTime();
        ContentValues contentValues = new ContentValues();
        contentValues.put("BorrowListID", BorrowID);
        contentValues.put("UserName", UserInfo.USERNAME);
        contentValues.put("EditDate", CommonMethod.getTime());
        if (sqLiteDatabase.insertOrThrow("AssetBorrowList", null, contentValues) != -1) {
            ListDetail.add(BorrowID);
            change = true;
        }
    }

    @Override
    public void initValues() {
        dbhelper = new MySQLiteOpenHelper(getBaseContext(), "temp_data.db", null, 1);
        sqLiteDatabase = dbhelper.getWritableDatabase();
        ListDetail = getSetValues("AssetBorrowList");
        ListDetail = new HashSet<>(ListDetail);
        BorrowListDetail = getSetValues("AssetBorrowListDetail");
        BorrowListDetail = new HashSet<>(BorrowListDetail);
    }

    @Override
    public void GetRequestResult(String data, int m) {

    }

}
