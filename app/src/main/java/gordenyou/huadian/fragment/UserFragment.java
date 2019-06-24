package gordenyou.huadian.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import gordenyou.huadian.R;
import gordenyou.huadian.util.UserInfo;
import gordenyou.huadian.view.SpinnerView;
import gordenyou.huadian.view.TextshowView;


/**
 * Created by Gordenyou on 2018/1/5.
 */

public class UserFragment extends Fragment {
    @BindView(R.id.user_name)
    TextshowView name;
    @BindView(R.id.user_id)
    TextshowView cardid;
    @BindView(R.id.user_department)
    TextshowView department;
    @BindView(R.id.user_organization)
    TextshowView organization;
    @BindView(R.id.user_companycode)
    SpinnerView companycode;
    @BindView(R.id.user_save)
    Button save;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        ButterKnife.bind(this, view);

        setText();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo.COMPANY = companycode.getSpinner().getSelectedItem().toString();
                UserInfo.COMPANYCODE = UserInfo.COMPANY.substring(0, 3);
                new AlertDialog.Builder(getContext()).setTitle("成功信息")
                        .setMessage("信息保存成功！").setPositiveButton("确定", null).show();
                organization.getTextview().setText(UserInfo.COMPANY);
            }
        });

        return view;
    }

    private void setText() {
        name.getTextview().setText(UserInfo.USERNAME);
        cardid.getTextview().setText(UserInfo.USERCARDID);
        department.getTextview().setText(UserInfo.DEPARTMENT);
        organization.getTextview().setText(UserInfo.COMPANY);
        //companycode.setSpinnerList(getContext(), R.array.companycode);
    }
}
