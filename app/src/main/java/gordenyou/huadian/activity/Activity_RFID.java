package gordenyou.huadian.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import gordenyou.huadian.R;
import gordenyou.huadian.unity.WebParams;
import gordenyou.huadian.view.HeaderTitle;
import gordenyou.huadian.view.ScannerView;
import gordenyou.huadian.view.TextshowView;

public class Activity_RFID extends BaseActivity {
    @BindView(R.id.rfid_header)
    HeaderTitle headerTitle;
//    @BindView(R.id.rfid_spinner)
//    SpinnerView gongsi;
    @BindView(R.id.rfid_bianma)
    ScannerView bianma;
    @NotEmpty(message = "请扫描条码!")
    EditText bianma_et ;
    @BindView(R.id.rfid_mingchen)
    TextshowView mingchen;
    @BindView(R.id.rfid_leixin)
    TextshowView leixin;
//    @BindView(R.id.rfid_pingpai)
//    TextshowView pingpai;
    @BindView(R.id.rfid_quyu)
    TextshowView quyu;
    @BindView(R.id.rfid_bumen)
    TextshowView bumen;
    @BindView(R.id.rfid_zerenren)
    TextshowView zerenren;
    @BindView(R.id.rfid_suoyouren)
    TextshowView baoguanren;
    @BindView(R.id.rfid_chuchang)
    TextshowView chuchang;
    @BindView(R.id.rfid_beizhu)
    TextshowView beizhu;
    @BindView(R.id.rfid_EPC)
    ScannerView epc;
    @NotEmpty(message = "请读取EPC!")
    EditText epc_et;

    String stgongsi, stbianma, stmingchen, stleixin, stquyu, stbumen, stzerenren, stbaoguanren, stchuchang, stbeizhu, stepc;

    @Override
    public void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_rfid);
        ButterKnife.bind(this);
        bianma_et = bianma.getEditTextView();
        epc_et = epc.getEditTextView();

    }

    @Override
    public void LogicMethod() {
        headerTitle.getRightbutton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
                getScreenData();
            }
        });
        bianma.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getScreenData();
                if(stbianma.equals("")){
                    ShowErrMsgDialog("请扫描条码！");
                }else{
                    GetMaterialInfo();
                }
            }
        });
    }

    @Override
    public void initValues() {
//        if(UserInfo.companyInfo != null){
//            gongsi.setSpinnerList(this, UserInfo.companyInfo);
//        }
    }

    private void getScreenData() {
//        stgongsi = gongsi.getSpinner().getSelectedItem().toString().split(" ")[0];
        stbianma = bianma.getText();
        stepc = epc.getText();
    }

    private void SetScreenData() {
        mingchen.SetText(stmingchen);
        leixin.SetText(stleixin);
        bumen.SetText(stbumen);
        quyu.SetText(stquyu);
        zerenren.SetText(stzerenren);
        baoguanren.SetText(stbaoguanren);
        chuchang.SetText(stchuchang);
        beizhu.SetText(stbeizhu);
    }

    @Override
    public void GetRequestResult(String data, int m) {
        switch (m){
            case 1:
                try {
                    JSONArray jsonArray = new JSONObject(data).getJSONArray("MaterialInfo");
                    stmingchen = jsonArray.optJSONObject(0).getString("资产名称");
                    stleixin = jsonArray.optJSONObject(0).getString("资产类型");
                    stbumen = jsonArray.optJSONObject(0).getString("所属部门");
                    stquyu = jsonArray.optJSONObject(0).getString("所属区域");
                    stzerenren = jsonArray.optJSONObject(0).getString("责任人");
                    stbaoguanren = jsonArray.optJSONObject(0).getString("保管人");
                    stchuchang = jsonArray.optJSONObject(0).getString("出厂编号");
                    stbeizhu = jsonArray.optJSONObject(0).getString("备注");
                    SetScreenData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {

    }

    private void GetMaterialInfo() {
        WebParams.Clear();
        WebParams.SetSubOperationFunction("GetMaterialInfo");
        WebParams.Setparam1(stgongsi);
        WebParams.Setparam2(stbianma);

        ProgressDialogShow(this, "查询中...", "请等待！");
        WebHttpRequest(1);
    }
}
