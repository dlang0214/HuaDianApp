package p.gordenyou.huadian.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import p.gordenyou.huadian.MainActivity;
import p.gordenyou.huadian.R;
import p.gordenyou.huadian.unity.WebParams;
import p.gordenyou.huadian.util.UserInfo;
import p.gordenyou.huadian.util.UserInfoUtils;
import p.gordenyou.huadian.view.SpinnerView;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.login_businesscode)
    SpinnerView spinnerView;
    @BindView(R.id.login_user)
    EditText et_user;
    @BindView(R.id.login_password)
    EditText et_password;
    @BindView(R.id.login_checkbox)
    CheckBox checkBox;
    @BindView(R.id.login_login)
    Button btn_login;
    @BindView(R.id.login_cancel)
    Button btn_cancel;
    @BindView(R.id.login_test)
    Button btn_test;

    private String userid = "", password = "", companycode;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //getPastUser();
//    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        InitUIControl();
    }

    @Override
    public void LogicMethod() {

//        spinnerView.getSpinner().setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String orgid = spinnerView.getSpinner().getSelectedItem().toString().substring(0,3);
//                WebParams.Clear();
//                WebParams.SetSubOperationFunction("GetStockInfobyOrgID");
//                WebParams.Setparam1(orgid);
//                progressDialog = ProgressDialog.show(LoginActivity.this,
//                        "查询仓库中", "请等待...", true, false);
//                WebHttpRequest(1);
//            }
//        });

        spinnerView.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String orgid = spinnerView.getSpinner().getSelectedItem().toString().substring(0, 3).trim();
                    WebParams.Clear();
                    WebParams.SetSubOperationFunction("GetStockInfobyOrgID");
                    WebParams.Setparam1(orgid);
                    progressDialog = ProgressDialog.show(LoginActivity.this,
                            "查询仓库中", "请等待...", true, false);
                    WebHttpRequest(1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void initValues() {

    }

    @Override
    public void GetRequestResult(String data, int m) {

        switch (m) {
            case 1:
                try {
                    JSONArray jsonArray = new JSONObject(data).getJSONArray("stockinfo");
                    UserInfo.stockinfo = new String[jsonArray.length() + 1];
                    UserInfo.stockinfo[0] = "请选择";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        UserInfo.stockinfo[i + 1] = jsonArray.optJSONObject(i).getString("STID")
                                + "_" + jsonArray.optJSONObject(i).getString("StockID") + "_"
                                + jsonArray.optJSONObject(i).getString("Stockname");
                        Log.i("stockinfo", UserInfo.stockinfo[i]);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                String[] temp = data.trim().split("\\|");
                String[] user = temp[temp.length - 1].split("@");
                String[] deatil = user[user.length - 1].split("_");

                UserInfo.USERID = Integer.parseInt(deatil[0]);
                UserInfo.COMPANYCODE = companycode.substring(0, 3).trim();
                UserInfo.COMPANY = companycode;
                UserInfo.USERCARDID = deatil[1];
                UserInfo.USERNAME = deatil[2];
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                break;
        }

    }

//    private void getPastUser() {
//        Map<String, String> userInfoMap = UserInfoUtils.readUserInfo();
//        if (userInfoMap != null) {
//            userid = userInfoMap.get("username");
//            companycode = userInfoMap.get("companycode");
//            et_user.setText(userid);
//            spinnerView.setDefault(companycode, R.array.companycode);
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 82:
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, SystemActivity.class);
                startActivity(intent);
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void InitUIControl() {
        btn_login.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_test.setOnClickListener(this);
        //spinnerView.setSpinnerList(this, R.array.companycode);
    }

    private void showTips() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("退出提醒")
                .setMessage("是否退出程序?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create(); // 创建对话框
        alertDialog.show(); // 显示对话框
    }

    private void Login(String UserId, String password) {
        WebParams.Clear();
        WebParams.SetSubOperationFunction("MC3000UserLogin");
        WebParams.Setparam1(UserId);
        WebParams.Setparam2(password);
        progressDialog = ProgressDialog.show(LoginActivity.this,
                "Loading...", "请等待...", true, false);
        WebHttpRequest(2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login:
                userid = et_user.getText().toString().trim();
                password = et_password.getText().toString().trim();
                companycode = spinnerView.getSpinner().getSelectedItem().toString();
                if (userid.trim().length() == 0 || password.trim().length() == 0) {
                    new AlertDialog.Builder(LoginActivity.this).setTitle("错误信息").setMessage("用户名和密码不能为空！").setPositiveButton("确定", null).show();
                } else {
                    if (checkBox.isChecked()) {
                        UserInfoUtils.saveUserInfor(userid, companycode);
                    }
                    Login(userid, password);
                }
                break;
            case R.id.login_cancel:
                showTips();
                break;
            case R.id.login_test:
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
