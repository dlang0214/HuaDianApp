package gordenyou.huadian.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Dialog_zcpd extends Dialog {
    public Dialog_zcpd(@NonNull Context context) {
        super(context);
    }

    public Dialog_zcpd(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected Dialog_zcpd(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
