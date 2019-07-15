package gordenyou.huadian.common;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonMethod {

    public static String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        return time;
    }

    public static String getListTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        return time;
    }

    public static void CloseKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void SetFocus(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }
}
