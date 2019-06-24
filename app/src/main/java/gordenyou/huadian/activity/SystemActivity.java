package gordenyou.huadian.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Properties;

import gordenyou.huadian.R;
import gordenyou.huadian.common.SaveInfo;



/**
 * Created by James on 2017-07-12.
 */

public class SystemActivity extends AppCompatActivity {
    private EditText edWebURL,edAccount,edShopId,edShopName;
    private Button btnSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);
        edWebURL= findViewById(R.id.edURL);
//        edAccount=(EditText)findViewById(R.id.edAccount);
//        edShopId=(EditText)findViewById(R.id.edShopId);
//        edShopName=(EditText)findViewById(R.id.edShopName);
        btnSave= findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new ButtonListener());
        edWebURL.setText(SaveInfo.getProperties().getProperty("WebServiceurl").trim());
//        edAccount.setText(SaveInfo.getProperties().getProperty("Account").trim());
//        edShopId.setText(SaveInfo.getProperties().getProperty("ShopId").trim());
//        edShopName.setText(SaveInfo.getProperties().getProperty("ShopName").trim());
    }
    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnSave:
                    try {
                        Properties prop=new Properties();
                        prop.put("WebServiceurl", edWebURL.getText().toString().trim());
//                        prop.put("Account", edAccount.getText().toString().trim());
//                        prop.put("ShopId", edShopId.getText().toString().trim());
//                        prop.put("ShopName", edShopName.getText().toString().trim());
                        if(SaveInfo.SaveProperties(prop))
                        {
                            new AlertDialog.Builder(SystemActivity.this).setTitle("系统信息").setIcon(android.R.drawable.ic_dialog_info).setMessage("系统配置信息保存成功！").setPositiveButton("确定", null).show();
                        }
                        else
                        {
                            new AlertDialog.Builder(SystemActivity.this).setTitle("错误信息").setIcon(android.R.drawable.ic_dialog_alert).setMessage("系统配置信息保存失败！").setPositiveButton("确定", null).show();
                        }
                    }
                    catch (Exception e) {
                        Log.e("Test", "config.properties Not Found Exception",e);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
