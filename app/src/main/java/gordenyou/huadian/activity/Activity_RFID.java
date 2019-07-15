package gordenyou.huadian.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.barcode.Scanner;
import android.hardware.uhf.magic.reader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import gordenyou.huadian.view.HeaderTitle;
import gordenyou.huadian.view.ScannerView;
import gordenyou.huadian.view.TextshowView;
import tableLayout.TableLayout;

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
    @BindView(R.id.rfid_table)
    TableLayout tableLayout;

    MySQLiteOpenHelper dbhelper;
    SQLiteDatabase sqLiteDatabase;
    Set<String> list_change;
    boolean change = false;

    Set<String> list_barcode = new HashSet<>();
    Set<String> list_rfid = new HashSet<>();

    String[] header = new String[]{"资产编码", "RFID"};
    ArrayList<String> list_temp = new ArrayList<>();
    private Handler readerHandler = new ReaderHandler();
    String stbianma, stmingchen, stleixin, stquyu, stchengben, stzerenren, stshiyongren, stchuchang, stbeizhu, stepc;

    @Override
    public void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_rfid);
        ButterKnife.bind(this);
        reader.reg_handler(readerHandler);
        SetTableLayout(tableLayout);
        SetHeaderTitle(headerTitle);
    }

    @Override
    public void LogicMethod() {
        headerTitle.getRightbutton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sum = 0, count = list_temp.size();
                for (String i : list_temp) {
                    String[] temp = i.split("○");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("EPC", temp[1]);
                    if (sqLiteDatabase.update("rMaterielInfo", contentValues, "mcode = ?", new String[]{temp[0]}) == 1) {
                        sum++;
                        list_change.add(temp[0]);
                        change = true;
                    }
                }
                if (sum == count) {
                    ShowWarmMsgDialog("成功绑定" + sum + "条记录！");
                    list_temp.clear();
                    String[] noheader = new String[]{};
                    SetHeader(noheader);
                    firstRowAsTitle(list_temp);
                }
            }
        });
        epc.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ReaderEvent()) {
                    SetHeader(header);
                    String temp = bianma.getText() + "○" + epc.getText();
                    list_temp.add(temp);
                    firstRowAsTitle(list_temp);
                    CommonMethod.SetFocus(bianma.getEditTextView());
                    list_rfid.add(epc.getText());
                    list_barcode.add(bianma.getText());
                }
            }
        });
    }

    @Override
    public void initValues() {
        dbhelper = new MySQLiteOpenHelper(getBaseContext(), "temp_data.db", null, 1);
        sqLiteDatabase = dbhelper.getWritableDatabase();
        list_change = getSetValues("RFID");
        list_change = new HashSet<>(list_change);
    }



    @Override
    protected void onPause() {
        super.onPause();

        reader.StopLoop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        reader.StopLoop();
    }

    private class ReaderHandler extends Handler {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.what == reader.msgreadepc) {
//                    chaxun.getEditTextView().setText(msg.obj.toString());
                    epc.SetText(msg.obj.toString());
                    reader.Clean();
                }
                if (msg.what == reader.readover) {
                    Log.e("test", "readerover");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean ReaderEvent() {
        if (!list_rfid.contains(epc.getText())) {
            Cursor cursor = sqLiteDatabase.rawQuery("Select * from rMaterielInfo where EPC = '" + epc.getText() + "'", null);
            if (cursor.getCount() != 0) {
                ShowErrMsgDialog("当前RFID已在数据库中被绑定！");
                epc.SetText("");
            } else {
                return true;
            }
            cursor.close();
        } else {
            ShowErrMsgDialog("当前RFID已在列表中被绑定！");
            epc.SetText("");
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getRepeatCount() == 0) {
            switch (keyCode) {
                case 220:
                    Scanner.Read();
                    break;
                case 221:
                    if (bianma.getText().isEmpty()) {
                        ShowWarmMsgDialog("请首先扫描条码！");
                    } else {
                        reader.InventoryLables();
                    }
                    break;
                case 4:
                    if(change){
                        SetValues("RFID", list_change);
                        addList("RFID");
                    }
//                    android.os.Process.killProcess(android.os.Process.myPid());
                    finish();
                    break;
            }
        }
        return true;
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
        if (!list_barcode.contains(bianma.getText())) {
            Cursor cursor = sqLiteDatabase.rawQuery("select * from rMaterielInfo where mcode = '" + bianma.getText() + "'", null);
            while (cursor.moveToNext()) {
                if (!cursor.getString(cursor.getColumnIndex("EPC")).isEmpty()) {
                    ShowErrMsgDialog("数据库中已经存在此资产绑定记录！");
                    bianma.SetText("");
                } else {
                    stmingchen = cursor.getString(cursor.getColumnIndex("MaterielName"));
                    stleixin = cursor.getString(cursor.getColumnIndex("MaterielKind"));
                    stchengben = cursor.getString(cursor.getColumnIndex("CostCenter"));
                    stquyu = cursor.getString(cursor.getColumnIndex("areaname"));
//            stzerenren = cursor.getString(cursor.getColumnIndex("resman"));
//            stshiyongren = cursor.getString(cursor.getColumnIndex("ownername"));
//            stchuchang = cursor.getString(cursor.getColumnIndex("productsn"));
//            stbeizhu = cursor.getString(cursor.getColumnIndex("newremark"));
                    SetScreenData();
                    CommonMethod.SetFocus(epc.getEditTextView());
                }
            }
            cursor.close();
        } else {
            ShowErrMsgDialog("当前列表已存在此资产绑定记录！");
            bianma.SetText("");
        }
    }
}
