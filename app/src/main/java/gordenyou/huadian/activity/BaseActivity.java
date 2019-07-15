package gordenyou.huadian.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.hardware.barcode.Scanner;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gordenyou.huadian.common.GlobalConstant;
import gordenyou.huadian.common.WebServiceRequest;
import gordenyou.huadian.unity.WebParams;
import gordenyou.huadian.view.HeaderTitle;
import tableLayout.TableAdapter;
import tableLayout.TableLayout;

/**
 * Created by Gordenyou on 2018/1/18.
 */

public abstract class BaseActivity extends AppCompatActivity implements Validator.ValidationListener {

    private Handler mHandler = new MainHandler();
    private final int duration = 1; // seconds
    private final int sampleRate = 2000;
    private final int numSamples = duration * sampleRate;
    private final double[] sample = new double[numSamples];
    private final byte[] generatedSnd = new byte[2 * numSamples];
    private EditText editText;

    private TableLayout tableLayout;
    private String[] header;
    private LinearLayout linearLayout;

    private ArrayList<String> table_list = new ArrayList<>();
    public String data;
    public ProgressDialog progressDialog;
    private static HeaderTitle headerTitle;

    //网络请求代码
    public static int m;

    public SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews(savedInstanceState);

        initValues();
        LogicMethod();

    }

//    public void ReshowTable(@Nullable Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            Log.i(getBaseContext().toString(), "Save msg_list");
//            ArrayList<String> msg_list = savedInstanceState.getStringArrayList("msg_list");
//            firstRowAsTitle(msg_list);
//        }
//    }
//
//    /**
//     * 横屏保存列表状态
//     *
//     * @param outState
//     */
//    public void onSaveInstanceState(Bundle outState , ArrayList<String> msg_list) {
//        outState.putStringArrayList("msg_list", msg_list);
//        super.onSaveInstanceState(outState);
//    }

    /**
     * 视图初始化
     * @param savedInstanceState
     */
    public abstract void initViews(Bundle savedInstanceState);

    /**
     * 逻辑初始化
     */
    public abstract void LogicMethod();

    /**
     * 数据初始化
     */
    public abstract void initValues();

    /**
     * 处理WebService返回数据。
     * @param data 返回数据
     * @param m 请求编号
     */
    public abstract void GetRequestResult(String data, int m);

    public void SetHeaderTitle(HeaderTitle headerTitle){
        this.headerTitle = headerTitle;
    }

    public static String getHeadertitle(){
        return headerTitle.getHeadertitle();
    }

    @Override
    protected void onStart() {
        //赋值handle句柄
        Scanner.m_handler = mHandler;
        //初始化扫描头
        Scanner.InitSCA();
        Thread thread = new Thread(run1);
        thread.start();
        super.onStart();
    }

    public void SetEdittext(EditText editText) {
        this.editText = editText;
    }

    public void SetTableLayout(TableLayout tableLayout) {
        this.tableLayout = tableLayout;
    }

    public void SetHeader(String[] header) {
        this.header = header;
        firstRowAsTitle(table_list);
    }

    public void SetLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public void SetValues(String key, String value){
        SharedPreferences.Editor editor = getSharedPreferences("data_log", MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.apply();
    }
    public void SetValues(String key, Set<String> set){
        SharedPreferences.Editor editor = getSharedPreferences("data_log", MODE_PRIVATE).edit();
        editor.putStringSet(key,set);
        editor.apply();
    }

    public String getValues(String key){
        sharedPreferences = getSharedPreferences("data_log",MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }
    public Set<String> getSetValues(String key){
        sharedPreferences = getSharedPreferences("data_log",MODE_PRIVATE);
        return sharedPreferences.getStringSet(key, null);
    }

    public void addList(String value){
        Set<String> changelist = getSetValues("changelist");
        changelist = new HashSet<>(changelist);
        changelist.add(value);
        SetValues("changelist", changelist);
    }

    /**
     * 绘制表格
     */
    public void firstRowAsTitle(ArrayList<String> msg_list) {
        final ArrayList<String> list = msg_list;
        tableLayout.setAdapter(new TableAdapter() {
            @Override
            public int getColumnCount() {
                return header.length;
            }

            @Override
            public String[] getColumnContent(int position) {
                int rowCount = list.size() + 1;
                String contents[] = new String[rowCount];
                for (int i = 0; i < rowCount; i++) {
                    //设置标题栏
                    if (i == 0) {
                        contents[i] = header[position];
                    } else {
                        String[] temp = list.get(i - 1).split("○");
                        contents[i] = temp[position];
                    }
                }
                return contents;
            }
        });
    }

    public void ClearData() {
        firstRowAsTitle(table_list);
    }

    /**
     * 表单校验成功
     */
    @Override
    public void onValidationSucceeded() {

    }

    /**
     * 表单校验失败提醒
     * @param errors 错误单
     */
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // 显示上面注解中添加的错误提示信息
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                // 显示edittext外的提示，如：必须接受服务协议条款的CheckBox
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    Runnable run1 = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                View rootview = BaseActivity.this.getWindow().getDecorView();
                View focus = rootview.findFocus();
                if (focus != null) {
//                    if (focus.getId() == R.id.sc_edittext) {
//                        SetEdittext((EditText) focus);
//                    } else {
//                        SetEdittext(null);
//                    }
                    SetEdittext((EditText) focus);
                }
            }
        }
    };




    /**
     * 扫描头常规事件
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getRepeatCount() == 0) {
            if ((keyCode == 220) || (keyCode == 221)) {
                Scanner.Read();
                Log.i("BASEACTIVITY", "Scanner Start");
            } else if (keyCode == 4) {
                finish();

            }
        }
//        Log.i("OOOOOOOO", keyCode + "");
        return true;
    }

    public void StartScan() {
        Scanner.Read();
    }

    public void WebHttpRequest(final int m) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(200);
                WebServiceRequest webServiceRequest = new WebServiceRequest();
                SoapObject soapobj = new SoapObject(GlobalConstant.NAMESPACE, "GetAppData");
                soapobj.addProperty("subOperationFunction", WebParams.GetSubOperationFunction());
                soapobj.addProperty("param1", WebParams.Getparam1());
                soapobj.addProperty("param2", WebParams.Getparam2());
                soapobj.addProperty("param3", WebParams.Getparam3());
                soapobj.addProperty("param4", WebParams.Getparam4());
                soapobj.addProperty("param5", WebParams.Getparam5());
                soapobj.addProperty("param6", WebParams.Getparam6());
                soapobj.addProperty("param7", WebParams.Getparam7());
                soapobj.addProperty("param8", WebParams.Getparam8());
                soapobj.addProperty("param9", WebParams.Getparam9());
                soapobj.addProperty("param10", WebParams.Getparam10());
                soapobj.addProperty("param11", WebParams.Getparam11());
                soapobj.addProperty("param12", WebParams.Getparam12());
                soapobj.addProperty("param13", WebParams.Getparam13());
                soapobj.addProperty("param14", WebParams.Getparam14());
                soapobj.addProperty("param15", WebParams.Getparam15());
                webServiceRequest.WebServiceRequest("GetAppData", soapobj, m, handler);
            }
        });
        thread.start();

    }

    public void ErrorEvent(int m){}

    public void SuccessEvent(int m){}

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            if (msg.what == -99) {
                ShowErrMsgDialog(msg.obj.toString().trim());
            }
            else{
                m= msg.what;
                Log.e("获取数据",msg.obj.toString().trim());
                if(msg.obj.toString().trim().startsWith("Error:")){
                    ErrorEvent(m);
                    ShowErrMsgDialog(msg.obj.toString().trim().substring(6));
                }
                else if (msg.obj.toString().trim().startsWith("Err:")){
                    ErrorEvent(m);
                    ShowErrMsgDialog(msg.obj.toString().trim().substring(4));
                }
                else if(msg.obj.toString().trim().startsWith("Success:")){
                    SuccessEvent(m);
                    ShowWarmMsgDialog(msg.obj.toString().trim().substring(8));
                }
                else {
                    if(!msg.obj.toString().trim().equals("")){
                        GetRequestResult(msg.obj.toString().trim(), m);
                    }
                }
            }
        }
    };
    public void ShowErrMsgDialog(String ErrorMsg){
        new AlertDialog.Builder(BaseActivity.this).setTitle("错误信息")
                .setMessage(ErrorMsg).setPositiveButton("确定", null).show();
    }

    public void ShowWarmMsgDialog(String ErrorMsg){
        new AlertDialog.Builder(BaseActivity.this).setTitle("提示信息")
                .setMessage(ErrorMsg).setPositiveButton("确定", null).show();
    }

    public void ScannerEvent(){
    }

    private class MainHandler extends Handler {
        public void handleMessage(final Message msg) {
            switch (msg.what) {

                case Scanner.BARCODE_READ: {
                    //显示读到的条码
                    if (editText != null) {

                        editText.setText((String) msg.obj);
                        Log.i("BaseActivity", "EditText");
                        ScannerEvent();
                        //播放声音
                        play();
                    }

                    break;
                }
                case Scanner.BARCODE_NOREAD: {
                    break;
                }
                default:
                    break;
            }
        }
    }


    void genTone() {
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            // HZ
            double freqOfTone = 1600;
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone));
        }
        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (double dVal : sample) {
            short val = (short) (dVal * 32767);
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
    }

    void playSound() {
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, numSamples, AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, numSamples);
        audioTrack.play();
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        audioTrack.release();
    }

    void play() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                genTone();
                playSound();
            }
        });
        thread.start();
    }

}
