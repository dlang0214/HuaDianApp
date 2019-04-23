package p.gordenyou.huadian.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import p.gordenyou.huadian.R;


/**
 * Created by Gordenyou on 2018/3/15.
 */

public class HeaderTitle extends LinearLayout {
    private TextView textView;
    public HeaderTitle(Context context) {
        this(context, null);
    }

    public HeaderTitle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderTitle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.headertitle,this);
        textView = view.findViewById(R.id.header_title);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HeaderTitle);
        textView.setText(a.getText(R.styleable.HeaderTitle_headertitle));
        a.recycle();
    }
}
