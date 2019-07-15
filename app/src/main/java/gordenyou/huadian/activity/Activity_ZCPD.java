package gordenyou.huadian.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.barcode.Scanner;
import android.hardware.uhf.magic.reader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import gordenyou.huadian.R;
import gordenyou.huadian.common.MySQLiteOpenHelper;
import gordenyou.huadian.view.EdittextView;
import gordenyou.huadian.view.HeaderTitle;
import gordenyou.huadian.view.ScannerView;
import gordenyou.huadian.view.SpinnerView;
import gordenyou.huadian.view.TextshowView;
import tableLayout.TableLayout;

public class Activity_ZCPD extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.zcpd_header)
    HeaderTitle headerTitle;
    @BindView(R.id.zcpd_danhao)
    SpinnerView danhao;
    @BindView(R.id.zcpd_chengben)
    SpinnerView chengben;
    @BindView(R.id.zcpd_quyu)
    EdittextView quyu;
    @BindView(R.id.zcpd_fangjian)
    EdittextView fangjain;
    @BindView(R.id.zcpd_chaxun)
    ScannerView chaxun;
    @BindView(R.id.zcpd_saomiao)
    ScannerView saomiao;

    @BindView(R.id.zcpd_kaishi)
    Button kaishi;
    @BindView(R.id.zcpd_jieshu)
    Button jieshu;
    @BindView(R.id.zcpd_weizai)
    Button weizai;
    @BindView(R.id.zcpd_state)
    TextView state;

    @BindView(R.id.zcpd_tablename)
    TextView tablename;
    @BindView(R.id.zcpd_linearlayout)
    LinearLayout linearLayout;
    @BindView(R.id.zcpd_table)
    TableLayout tableLayout;
    @BindView(R.id.zcpd_radiogroup)
    LinearLayout radiogroup;
    @BindView(R.id.zcpd_quanxuan)
    Button quanxuan;
    @BindView(R.id.radio1)
    CheckBox radio1;
    @BindView(R.id.radio2)
    CheckBox radio2;
    @BindView(R.id.radio3)
    CheckBox radio3;
    @BindView(R.id.radio4)
    CheckBox radio4;
    @BindView(R.id.radio5)
    CheckBox radio5;
    @BindView(R.id.radio6)
    CheckBox radio6;
    @BindView(R.id.zcpd_shangyiye)
    Button shangyiye;
    @BindView(R.id.zcpd_xiayiye)
    Button xiayiye;
    @BindView(R.id.zcpd_yema)
    TextView yema;
    @BindView(R.id.zcpd_yemasum)
    TextView yemasum;

    @BindView(R.id.zcpd_weipandian)
    Button weipandian;
    @BindView(R.id.zcpd_yipandian)
    Button yipandian;
    @BindView(R.id.zcpd_chayi)
    Button chayi;
    @BindView(R.id.zcpd_yiqueren)
    Button yiqueren;
    @BindView(R.id.zcpd_queren)
    Button queren;
    @BindView(R.id.header_rightbutton)
    Button wancheng;

    private static int TABLE_LENGTH = 6;//表格行数

    String stdanhao = "", stchengben, stquyu, stfangjian, stchaxun, stsaomiao, sql;
    String[] sp_danhao, sp_chengben;
    int intyema = 1;
    HashSet<String> set_id = new HashSet<>();
    static HashSet<String> set_yipandian = new HashSet<>(); //已盘点的id
    static HashSet<String> set_chayi = new HashSet<>();
    Set<String> list_scan; //盘点中的缓存
    Set<String> list_reader; //盘点过程中的RFID
    Set<String> list_out; //未在盘点单的列表
    ArrayList<String> temp_list = new ArrayList<>();//表格的数据
    ArrayList<String> list;

    static Set<String> list_change;
    static boolean change = false;
    static Button pandian;

    private int preselectid;//保存上一个单号的id
    private static String presql = "";//保存上一次查询条件

    MySQLiteOpenHelper dbhelper;
    SQLiteDatabase sqLiteDatabase;
    public static String str_danhao;
    String[] header;

    private Handler readerHandler = new ReaderHandler();

    AlertDialog alertDialog;

    @Override
    public void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_zcpd);
        ButterKnife.bind(this);
        SetLayout(linearLayout);
        SetTableLayout(tableLayout);
        SetHeaderTitle(headerTitle);
        header = new String[]{"资产ID", "资产名称", "公司代码", "区域编码", "区域", "房间", "负责人", "使用人", "新公司",
                "新成本中心", "新区域", "新使用人", "扫描人", "新房间", "新备注", "新标签", "扫描时间", "产品序号", "备注"};
        SetHeader(header);

        sp_chengben = new String[1];
        sp_chengben[0] = "请选择成本中心";
        chengben.setSpinnerList(this, sp_chengben);

        reader.reg_handler(readerHandler);
        pandian = findViewById(R.id.zcpd_yipandian);
    }

    @Override
    public void initValues() {
        dbhelper = new MySQLiteOpenHelper(getBaseContext(), "temp_data.db", null, 1);
        sqLiteDatabase = dbhelper.getWritableDatabase();
        list_change = getSetValues("ZCPD");
        list_change = new HashSet<>(list_change);
    }

    @Override
    public void LogicMethod() {
        saomiao.getEditTextView().setFocusable(false);
        GetStockCheckID();
        danhao.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0 && preselectid != i) {
                    if (set_chayi.isEmpty() && set_yipandian.isEmpty()) {
                        ClearData();
                        radiogroup.setVisibility(View.GONE);
                        yema.setVisibility(View.GONE);
                        yemasum.setVisibility(View.GONE);
                        GetCostCenterInfo(getBaseContext(), sqLiteDatabase, danhao.getID(), chengben);
                        preselectid = i;
                        str_danhao = danhao.getID();
                    } else {
                        new AlertDialog.Builder(Activity_ZCPD.this).setTitle("提示信息")
                                .setMessage("有盘点信息未确认盘点，选择新盘点单将会清空盘点信息。确认清除？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ClearData();
                                //将盘点单的记录清空
                                set_yipandian = new HashSet<>();
                                set_chayi = new HashSet<>();
                                radiogroup.setVisibility(View.GONE);
                                yema.setVisibility(View.GONE);
                                yemasum.setVisibility(View.GONE);
                                GetCostCenterInfo(getBaseContext(), sqLiteDatabase, danhao.getID(), chengben);
                                preselectid = i;
                                str_danhao = danhao.getID();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //设置为上次的选项
                                danhao.getSpinner().setSelection(preselectid);
                            }
                        }).show();
                    }

                }
                if (i == 0) {
                    danhao.getSpinner().setSelection(preselectid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SetClickListener();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        reader.StopLoop();

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        reader.StopLoop();

    }

    private class ReaderHandler extends Handler
    {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg)
        {
            try
            {
                if (msg.what == reader.msgreadepc)
                {
//                    chaxun.getEditTextView().setText(msg.obj.toString());
                    if(list_reader != null){
                        list_reader.add(msg.obj.toString());
                    }
                    saomiao.SetText(msg.obj.toString());
                    reader.Clean();
                }
                if (msg.what == reader.readover)
                {
                    Log.e("test", "readerover");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (event.getRepeatCount() == 0)
        {
            switch (keyCode){
                case 220:
                    Scanner.Read();
                    break;
                case 221:
                    Scanner.Read();
                    break;
                case 4:
                    if(change){
                        SetValues("ZCPD", list_change);
                        addList("ZCPD");
                    }
//                    android.os.Process.killProcess(android.os.Process.myPid());
                    finish();
                    break;
            }
        }
        return true;
    }

    public boolean showLayoutDialog(final Context context, final String barcode, final String str_danhao) {
        final AlertDialog dialog;
        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_zcpd, null);
        HeaderTitle header = dialogview.findViewById(R.id.dlzcpd_header);
        TextshowView tiaoma = dialogview.findViewById(R.id.dlzcpd_tiama);
        tiaoma.SetText(barcode);
        TextshowView mingchen = dialogview.findViewById(R.id.dlzcpd_mingcheng);
//        TextshowView xinghao = dialogview.findViewById(R.id.dlzcpd_xinghao);
        TextshowView chengben = dialogview.findViewById(R.id.dlzcpd_chengben);
        TextshowView quyu = dialogview.findViewById(R.id.dlzcpd_quyu);
        TextshowView fangjian = dialogview.findViewById(R.id.dlzcpd_fangjian);
        TextshowView shiyongren = dialogview.findViewById(R.id.dlzcpd_shiyongren);
        TextshowView fuzeren = dialogview.findViewById(R.id.dlzcpd_fuzeren);

        final SpinnerView xinchengben = dialogview.findViewById(R.id.dlzcpd_xinchengben);
        final EdittextView xinquyu = dialogview.findViewById(R.id.dlzcpd_xinquyu);
        final EdittextView xinfangjian = dialogview.findViewById(R.id.dlzcpd_xinfangjian);
        final EdittextView xinbeizhu = dialogview.findViewById(R.id.dlzcpd_xinbeizhu);
        final EdittextView xinshiyongren = dialogview.findViewById(R.id.dlzcpd_xinshiyongren);
//        final EdittextView xinfuzeren = dialogview.findViewById(R.id.dlzcpd_xinfuzeren);

        MySQLiteOpenHelper dbhelper = new MySQLiteOpenHelper(context, "temp_data.db", null, 1);
        final SQLiteDatabase sqLiteDatabase = dbhelper.getWritableDatabase();
        GetCostCenterInfo(context, sqLiteDatabase, str_danhao, xinchengben);
        Cursor cursor = sqLiteDatabase.rawQuery("Select AssetName, CostCenter, Locationname, room" +
                ", username, resman from rCheckListInfo where AssetID = '" + barcode + "' and CheckID = '" + str_danhao + "'", null);
        while (cursor.moveToNext()) {
            mingchen.SetText(cursor.getString(cursor.getColumnIndex("AssetName")));
            chengben.SetText(cursor.getString(cursor.getColumnIndex("CostCenter")));
            quyu.SetText(cursor.getString(cursor.getColumnIndex("locationname")));
            fangjian.SetText(cursor.getString(cursor.getColumnIndex("room")));
            shiyongren.SetText(cursor.getString(cursor.getColumnIndex("username")));
            fuzeren.SetText(cursor.getString(cursor.getColumnIndex("resman")));
        }
        cursor.close();
        dialog = new AlertDialog.Builder(context).setView(dialogview).create();

        header.getRightbutton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_chengben = xinchengben.getID();
                String str_quyu = xinquyu.getText();
                String str_fangjian = xinfangjian.getText();
                String str_beizhu = xinbeizhu.getText();
                String str_shiyongren = xinshiyongren.getText();
//                String str_fuzeren = xinfuzeren.getText();

                ContentValues values = new ContentValues();
                values.put("NewCostCenter", str_chengben);
                values.put("NewLocation", str_quyu);
                values.put("newroom", str_fangjian);
                values.put("newremark", str_beizhu);
                values.put("newUser", str_shiyongren);
//                values.put("DeptAbbr", str_fuzeren);
                values.put("flags", 2);
                String whereClause = "CheckID = ? and AssetID = ?";
                String[] select = new String[]{str_danhao, barcode};
                sqLiteDatabase.beginTransaction();
                if (sqLiteDatabase.update("rCheckListInfo", values, whereClause, select) == 1) {

                    sqLiteDatabase.setTransactionSuccessful();
                    if (set_yipandian.contains(barcode)) {
                        set_yipandian.remove(barcode);
                        set_chayi.add(barcode);
                    }
                    dialog.dismiss();
                    new AlertDialog.Builder(context).setTitle("提示信息")
                            .setMessage("资产差异调整成功!").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            list_change.add(barcode);
                            change = true;
                            pandian.performClick();
//                            Activity_ZCPD activity_zcpd = new Activity_ZCPD();
//                            Set<String> list_change = activity_zcpd.getSetValues("ZCPD");
//                            list_change = new HashSet<>();
//                            list_change.add(barcode);
//                            SetValues("ZCPD", list_change);
//                            activity_zcpd.yipandianclick();
                        }
                    }).show();

                }
                sqLiteDatabase.endTransaction();
            }
        });
        dialog.show();
        return true;
    }

    private void SetClickListener() {
        kaishi.setOnClickListener(this);
        jieshu.setOnClickListener(this);
        weizai.setOnClickListener(this);
        quanxuan.setOnClickListener(this);
        radio1.setOnClickListener(this);
        radio2.setOnClickListener(this);
        radio3.setOnClickListener(this);
        radio4.setOnClickListener(this);
        radio5.setOnClickListener(this);
        radio6.setOnClickListener(this);
        shangyiye.setOnClickListener(this);
        xiayiye.setOnClickListener(this);
        weipandian.setOnClickListener(this);
        yipandian.setOnClickListener(this);
        chayi.setOnClickListener(this);
        yiqueren.setOnClickListener(this);
        queren.setOnClickListener(this);
        wancheng.setOnClickListener(this);
    }

    /**
     * 获取查询条件
     */
    private boolean GetSearchFields() {
        GetScreenData();
        if (stdanhao.equals("")) {
            ShowWarmMsgDialog("请先选择盘点单！");
            return false;
        }
        return true;
    }

    private void GetScreenData() {
        stdanhao = danhao.getID();
        if (chengben.getSpinner().getSelectedItemId() == 0) {
            stchengben = "";
        } else {
            stchengben = chengben.getID();
        }
        stquyu = quyu.getText();
        stfangjian = fangjain.getText();
        stchaxun = chaxun.getText();
        stsaomiao = saomiao.getText();
    }

    private void GetCheckListInfo(String sql, int m) {
        BaseActivity.m = m;
        String sql_checklistinfo = "select AssetID 资产ID, AssetName 资产名称, CompanyCode 公司代码, Location 区域编码, Locationname 区域" +
                ",room 房间, resman 负责人, username 使用人, NewCompanyCode 新公司, NewCostCenter 新成本中心" +
                ", NewLocation 新区域, NewUser 新使用人, scanman 扫描人, newroom 新房间, newremark 新备注, newbrand 新标签" +
                ", ScanDate 扫描时间, productsn 产品序号, remark 备注 from rCheckListInfo cli where CheckId ='" + stdanhao + "'";
        if (!stchengben.isEmpty()) {
            sql_checklistinfo += " and (cli.CostCenter ='" + stchengben + "' or cli.newCostCenter = '" + stchengben + "')";
        }
        if (!stquyu.isEmpty()) {
            sql_checklistinfo += " and (cli.locationname like '%" + stquyu + "%' or cli.newLocation like '%" + stquyu + "%')";
        }
        if (!stfangjian.isEmpty()) {
            sql_checklistinfo += " and (cli.room ='" + stfangjian + "' or cli.newroom ='" + stfangjian + "')";
        }
        if (!stchaxun.isEmpty()) {
            sql_checklistinfo += " and (cli.AssetID like '%" + stchaxun + "%' or cli.Location like '%" + stchaxun + "%' or cli.rescode like '%" + stchaxun
                    + "%' or cli.productsn like '%" + stchaxun + "%' or cli.resman like '%" + stchaxun
                    + "%' or cli.username like '%" + stchaxun + "%' or cli.productsn like '%" + stchaxun + "%' or cli.remark like '%" + stchaxun + "%')";
        }
        if (!presql.equals(sql_checklistinfo)) {
            presql = sql_checklistinfo;
            set_id = new HashSet<>();
            set_chayi = new HashSet<>();
            set_yipandian = new HashSet<>();
        }
        sql_checklistinfo += sql;


        Cursor cursor = sqLiteDatabase.rawQuery(sql_checklistinfo, null);
        temp_list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("资产ID"));
            String name = cursor.getString(cursor.getColumnIndex("资产名称"));
            String daima = cursor.getString(cursor.getColumnIndex("公司代码"));
            String bianma = cursor.getString(cursor.getColumnIndex("区域编码"));
            String quyu = cursor.getString(cursor.getColumnIndex("区域"));
            String fangjian = cursor.getString(cursor.getColumnIndex("房间"));
            String fuzeren = cursor.getString(cursor.getColumnIndex("负责人"));
            String shiyongren = cursor.getString(cursor.getColumnIndex("使用人"));
            String xingongsi = cursor.getString(cursor.getColumnIndex("新公司"));
            String xinchengben = cursor.getString(cursor.getColumnIndex("新成本中心"));
            String xinquyu = cursor.getString(cursor.getColumnIndex("新区域"));
            String xinshiyongren = cursor.getString(cursor.getColumnIndex("新使用人"));
            String saomiaoren = cursor.getString(cursor.getColumnIndex("扫描人"));
            String xinfangjian = cursor.getString(cursor.getColumnIndex("新房间"));
            String xinbeizhu = cursor.getString(cursor.getColumnIndex("新备注"));
            String xinbiaoqian = cursor.getString(cursor.getColumnIndex("新标签"));
            String saomiaoshijian = cursor.getString(cursor.getColumnIndex("扫描时间"));
            String chanpinxuhao = cursor.getString(cursor.getColumnIndex("产品序号"));
            String beizhu = cursor.getString(cursor.getColumnIndex("备注"));
            String temp = id + "○" + name + "○" + daima + "○" + bianma + "○" + quyu + "○" + fangjian + "○" + fuzeren
                    + "○" + shiyongren + "○" + xingongsi + "○" + xinchengben + "○" + xinquyu + "○" + xinshiyongren + "○" +
                    saomiaoren + "○" + xinfangjian + "○" + xinbeizhu + "○" + xinbiaoqian + "○" + saomiaoshijian + "○" + chanpinxuhao
                    + "○" + beizhu + " ";
            temp_list.add(temp);
        }
        cursor.close();
        switch (m) {
            case 4: //已盘点
                intyema = 1;
                tablename.setText("已盘点");
                SetHeader(header);
                //记录以盘点的选项。
                if (set_yipandian == null) {
                    set_yipandian = new HashSet<>();
                }
                set_id = set_yipandian;
                radiogroup.setVisibility(View.VISIBLE);
                BuildTableAndPage(temp_list, intyema);
                SetCheckboxState();
                break;
            case 5: //差异
                intyema = 1;
                tablename.setText("差异");
                SetHeader(header);
                //记录以盘点的选项。
                if (set_chayi == null) {
                    set_chayi = new HashSet<>();
                }
                set_id = set_chayi;
                radiogroup.setVisibility(View.VISIBLE);
                BuildTableAndPage(temp_list, intyema);
                SetCheckboxState();
                break;
            case 3://未盘点
                intyema = 1;
                tablename.setText("未盘点");
                SetHeader(header);
                radiogroup.setVisibility(View.GONE);
                BuildTableAndPage(temp_list, intyema);
                break;
            case 6://已确认
                intyema = 1;
                tablename.setText("已确认");
                SetHeader(header);
                radiogroup.setVisibility(View.GONE);
                BuildTableAndPage(temp_list, intyema);
                break;
        }

    }

    private void GetCostCenterInfo(Context context, SQLiteDatabase sqLiteDatabase, String danhaoid, SpinnerView spinnerView) {
        Cursor cursor = sqLiteDatabase.rawQuery("select distinct sc.CostCenter 成本中心,di.DeptName 部门名称 from rCheckListInfo sc" +
                ",DeptInfo di where sc.CostCenter = di.CostCenter and CheckID= '" + danhaoid + "'", null);
        sp_chengben = new String[cursor.getCount() + 1];
        sp_chengben[0] = "请选择成本中心";
        while (cursor.moveToNext()) {
            String code = cursor.getString(cursor.getColumnIndex("成本中心"));
            String name = cursor.getString(cursor.getColumnIndex("部门名称"));
            sp_chengben[cursor.getPosition() + 1] = code + "_" + name;
        }
        cursor.close();
        if (sp_chengben != null) {
            spinnerView.setSpinnerList(context, sp_chengben);
        }
    }

    private void GetStockCheckID() {
        Cursor cursor = sqLiteDatabase.query("rCheckInfo", new String[]{"CheckID 盘点单, checktitle 盘点单名"}
                , null, null, null, null, null);
        sp_danhao = new String[cursor.getCount() + 1];
        sp_danhao[0] = "请选择盘点单";
        while (cursor.moveToNext()) {
            String code = cursor.getString(cursor.getColumnIndex("盘点单"));
            String name = cursor.getString(cursor.getColumnIndex("盘点单名"));
            sp_danhao[cursor.getPosition() + 1] = code + "_" + name;
        }
        if (sp_danhao != null) {
            danhao.setSpinnerList(this, sp_danhao);
        }
        cursor.close();
    }

    @Override
    public void GetRequestResult(String data, int m) {
    }

    /**
     * 绘制表格和页码
     * @param temp_list
     * @param intyema
     */
    private void BuildTableAndPage(ArrayList<String> temp_list, int intyema) {
        int qishi = intyema * TABLE_LENGTH < temp_list.size() ? intyema * TABLE_LENGTH : temp_list.size();
        int jiewei = (intyema - 1 > 0 ? intyema - 1 : 0) * TABLE_LENGTH;
        list = new ArrayList<>();
        for (int i = qishi - 1; i >= jiewei; i--) {
            list.add(temp_list.get(i));
        }
        firstRowAsTitle(list);
        yema.setVisibility(View.VISIBLE);
        yemasum.setVisibility(View.VISIBLE);
        yema.setText(String.valueOf(intyema));
        if (temp_list.size() % TABLE_LENGTH == 0) {
            yemasum.setText(String.valueOf(temp_list.size() / TABLE_LENGTH));
        } else {
            yemasum.setText(String.valueOf((temp_list.size() / TABLE_LENGTH) + 1));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zcpd_quanxuan:
                if (!radio1.isChecked()) {
                    radio1.setChecked(true);
                    AddCheckList(radio1);
                    radio2.setChecked(true);
                    AddCheckList(radio2);
                    radio3.setChecked(true);
                    AddCheckList(radio3);
                    radio4.setChecked(true);
                    AddCheckList(radio4);
                    radio5.setChecked(true);
                    AddCheckList(radio5);
                    radio6.setChecked(true);
                    AddCheckList(radio6);
                } else {
                    radio1.setChecked(false);
                    RemoveFromList(radio1);
                    radio2.setChecked(false);
                    RemoveFromList(radio2);
                    radio3.setChecked(false);
                    RemoveFromList(radio3);
                    radio4.setChecked(false);
                    RemoveFromList(radio4);
                    radio5.setChecked(false);
                    RemoveFromList(radio5);
                    radio6.setChecked(false);
                    RemoveFromList(radio6);
                }

                break;
            case R.id.radio1:
            case R.id.radio2:
            case R.id.radio3:
            case R.id.radio4:
            case R.id.radio5:
            case R.id.radio6:
                if (Integer.parseInt(((CheckBox) view).getText().toString()) - 1 < temp_list.size()) {
                    if (((CheckBox) view).isChecked()) {
                        AddCheckList((CheckBox) view);

                    } else {
                        RemoveFromList((CheckBox) view);
                    }
                } else {
                    ((CheckBox) view).setChecked(false);
                }

                break;
            case R.id.zcpd_shangyiye:
                if (intyema - 1 >= 1) {
                    BuildTableAndPage(temp_list, --intyema);
                    if (m == 4 || m == 5) {
                        SetCheckboxState();
                    }

                }
                break;
            case R.id.zcpd_xiayiye:

                if (intyema + 1 <= Integer.parseInt(yemasum.getText().toString())) {
                    BuildTableAndPage(temp_list, ++intyema);
                    if (m == 4 || m == 5) {
                        SetCheckboxState();
                    }
                }
                break;
            case R.id.zcpd_weipandian:
                sql = " and cli.flags = 0";
                if (GetSearchFields()) {
                    GetCheckListInfo(sql, 3);
                }
                break;
            case R.id.zcpd_yipandian:
                sql = " and cli.flags = 1";
                if (GetSearchFields()) {
                    GetCheckListInfo(sql, 4);
                }
                break;
            case R.id.zcpd_chayi:
                sql = " and cli.flags = 2";
                if (GetSearchFields()) {
                    GetCheckListInfo(sql, 5);
                }
                break;
            case R.id.zcpd_yiqueren:
                sql = " and (cli.flags = 3 or cli.flags = 4)";
                if (GetSearchFields()) {
                    GetCheckListInfo(sql, 6);
                }
                break;
            case R.id.zcpd_queren:
                int sum_chayi = 0, sum_yipandian = 0, check_chayi = set_chayi.size(), check_yipandian = set_yipandian.size();
                if (set_yipandian.isEmpty() && set_chayi.isEmpty()) {
                    ShowErrMsgDialog("请先选择资产！");
                } else {
                    try {
                        sqLiteDatabase.beginTransaction();
                        for (String i : set_chayi) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("flags", 3);
                            if (sqLiteDatabase.update("rCheckListInfo", contentValues
                                    , "CheckID = ? and AssetID = ?", new String[]{str_danhao, i}) == 1) {
                                sum_chayi++;
                                list_change.add(i);
                                change = true;
                            }
                        }
                        sqLiteDatabase.setTransactionSuccessful();
                    } catch (Exception e) {
                        ShowErrMsgDialog(e.getMessage());
                    } finally {
                        sqLiteDatabase.endTransaction();
                    }

                    try {
                        sqLiteDatabase.beginTransaction();
                        for (String i : set_yipandian) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("flags", 4);

                            if (sqLiteDatabase.update("rCheckListInfo", contentValues
                                    , "CheckID = ? and AssetID = ?", new String[]{str_danhao, i}) == 1) {
                                sum_yipandian++;
                                list_change.add(i);
                                change = true;
                            }
                        }
                        sqLiteDatabase.setTransactionSuccessful();
                    } catch (Exception e) {
                        ShowErrMsgDialog(e.getMessage());
                    } finally {
                        sqLiteDatabase.endTransaction();
                    }

                    if (sum_chayi == check_chayi && sum_yipandian == check_yipandian) {
                        ShowWarmMsgDialog("确认盘点成功！");
                        set_chayi.clear();
                        set_yipandian.clear();
                        if(sum_yipandian > 0){
                            yipandian.performClick();
                        }else{
                            chayi.performClick();
                        }
                    } else {
                        if (sum_chayi < set_chayi.size()) {
                            ShowErrMsgDialog("差异表中有" + (set_chayi.size() - sum_chayi) + "条数据插入错误");
                        }
                        if (sum_yipandian < set_yipandian.size()) {
                            ShowErrMsgDialog("已盘表中有" + (set_yipandian.size() - sum_yipandian) + "条数据插入错误");
                        }
                    }
                }
                break;
            case R.id.header_rightbutton:
                break;
            case R.id.zcpd_kaishi:
                if(str_danhao == null){
                    ShowErrMsgDialog("请先选择盘点单！");
                }else {
                    if(kaishi.getText().toString().equals("开始盘点")){
                        list_scan = new HashSet<>();
                        list_reader = new HashSet<>();
                        list_out = new HashSet<>();
                        reader.InventoryLablesLoop();
                        kaishi.setText("结束盘点");
                        state.setText("盘点中");
                        state.setTextColor(getResources().getColor(R.color.pandianzhong));
                        SetFocus(saomiao.getEditTextView());
                    }else{
                        List<Set<String>> list_set = new ArrayList<>();
                        list_set.add(list_scan);
                        list_set.add(list_reader);
                        reader.StopLoop();
                        saomiao.getEditTextView().setFocusable(false);
                        new CheckListInfoAsync().execute(list_set);
                        alertDialog = new AlertDialog.Builder(this).setTitle("提示").setMessage("处理数据中").show();
                    }
                }
                break;
            case R.id.zcpd_jieshu:
                break;
            case R.id.zcpd_weizai:
                tablename.setText("未在盘点单");
                radiogroup.setVisibility(View.GONE);
                ArrayList<String> list_weizai = new ArrayList<>();
                String[] newheader = new String[]{"资产条码", "资产名称", "资产类型", "成本中心", "所在区域", "负责人", "使用人"};
                String[] values = new String[]{"mcode", "MaterielName", "MaterielKind", "CostCenter", "areaname", "resman", "ownername"};
                for (String i: list_out){
                    Cursor cursor = sqLiteDatabase.rawQuery("Select * from rMaterielInfo where mcode = '" + i + "'",null);
                    StringBuilder str_temp = new StringBuilder();
                    if(cursor.getCount() == 1){
                        while(cursor.moveToNext()){
                            for(String j: values){
                                String temp = cursor.getString(cursor.getColumnIndex(j));
                                if(str_temp.length() == 0){
                                    str_temp.append(temp);
                                }else{
                                    str_temp.append("○").append(temp);
                                }
                            }
                        }
                        list_weizai.add(str_temp.toString());
                    }
                    cursor.close();
                }
                SetHeader(newheader);
                BuildTableAndPage(list_weizai, 1);
                break;
        }
    }

    private void RemoveFromList(CheckBox view) {
        if (Integer.parseInt(view.getText().toString()) - 1 < temp_list.size()) {
            String id = temp_list.get(Integer.parseInt(view.getText().toString()) - 1).split("○")[0];
            set_id.remove(id);
        }
    }

    private void AddCheckList(CheckBox view) {
        if (Integer.parseInt(view.getText().toString()) - 1 < list.size()) {
            String id = list.get(Integer.parseInt(view.getText().toString()) - 1).split("○")[0];
            set_id.add(id);
        } else {
            view.setChecked(false);
        }
    }

    private void SetCheckboxState() {
        for (int i = 0; i < TABLE_LENGTH; i++) {
            if (i >= list.size()) {
                switch (i) {
                    case 0:
                        radio1.setChecked(false);
                        break;
                    case 1:
                        radio2.setChecked(false);
                        break;
                    case 2:
                        radio3.setChecked(false);
                        break;
                    case 3:
                        radio4.setChecked(false);
                        break;
                    case 4:
                        radio5.setChecked(false);
                        break;
                    case 5:
                        radio6.setChecked(false);
                        break;
                }
            } else {
                if (set_id.contains(list.get(i).split("○")[0])) {
                    switch (i) {
                        case 0:
                            radio1.setChecked(true);
                            break;
                        case 1:
                            radio2.setChecked(true);
                            break;
                        case 2:
                            radio3.setChecked(true);
                            break;
                        case 3:
                            radio4.setChecked(true);
                            break;
                        case 4:
                            radio5.setChecked(true);
                            break;
                        case 5:
                            radio6.setChecked(true);
                            break;
                    }
                }
                if (!set_id.contains(list.get(i).split("○")[0])) {
                    switch (i) {
                        case 0:
                            radio1.setChecked(false);
                            break;
                        case 1:
                            radio2.setChecked(false);
                            break;
                        case 2:
                            radio3.setChecked(false);
                            break;
                        case 3:
                            radio4.setChecked(false);
                            break;
                        case 4:
                            radio5.setChecked(false);
                            break;
                        case 5:
                            radio6.setChecked(false);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void ScannerEvent() {
        if(list_scan != null){
            list_scan.add(saomiao.getText());
        }
    }

    private void SetFocus(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    private class CheckListInfoAsync extends AsyncTask<List<Set<String>>, Integer, Void> {

        String str_danhao;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            str_danhao = danhao.getID();
            dbhelper = new MySQLiteOpenHelper(getBaseContext(), "temp_data.db", null, 1);
            sqLiteDatabase = dbhelper.getWritableDatabase();
        }

        @Override
        protected Void doInBackground(List<Set<String>>... params) {
            for(String i: params[0].get(0)){//条码处理
                Cursor cursor = sqLiteDatabase.rawQuery("Select * from rCheckListInfo where CheckID = '"
                        + str_danhao +"' and AssetID = '" + i + "'", null);
                if(cursor.getCount() == 0){
                    list_out.add(i);
                }else{
                    while (cursor.moveToNext()){
                        if(cursor.getString(cursor.getColumnIndex("flags")).equals("0")){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("flags", 1);
                            if(sqLiteDatabase.update("rCheckListInfo", contentValues, "CheckID = ? and AssetID = ?", new String[]{str_danhao, i}) == 1){
                                list_change.add(i);
                                change = true;
                            }
                        }
                    }
                }
                cursor.close();
            }

            for(String i: params[0].get(1)){//RFID处理
                Cursor cursor = sqLiteDatabase.rawQuery("select * from rMaterielInfo where EPC = '" + i + "'", null);
                if(cursor.getCount() != 0){
                    String assetid = "";
                    while(cursor.moveToNext()){
                        assetid = cursor.getString(cursor.getColumnIndex("mcode"));
                    }
                    Cursor cursor1 = sqLiteDatabase.rawQuery("Select * from rCheckListInfo where CheckID = '"
                            + str_danhao +"' and AssetID = '" + assetid + "'", null);
                    if(cursor1.getCount() == 0){
                        list_out.add(assetid);
                    }else{
                        while (cursor1.moveToNext()){
                            if(cursor1.getString(cursor1.getColumnIndex("flags")).equals("0")){
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("flags", 1);
                                if(sqLiteDatabase.update("rCheckListInfo", contentValues, "CheckID = ? and AssetID = ?", new String[]{str_danhao, assetid}) == 1){
                                    list_change.add(assetid);
                                    change = true;
                                }
                            }
                        }
                    }
                    cursor1.close();
                }
                cursor.close();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            kaishi.setText("开始盘点");
            state.setText("未盘点");
            state.setTextColor(getResources().getColor(R.color.deepgrey));
            alertDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
