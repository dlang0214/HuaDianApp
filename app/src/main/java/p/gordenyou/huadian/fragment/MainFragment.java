package p.gordenyou.huadian.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import p.gordenyou.huadian.R;
import p.gordenyou.huadian.unity.FunList;
import p.gordenyou.huadian.unity.SystemUser;
import p.gordenyou.huadian.util.UserInfo;

public class MainFragment extends Fragment {

//    @BindView(R.id.fragment_user)
//    TextView user;
//    @BindView(R.id.fragment_username)
//    TextView username;
//    @BindView(R.id.fragment_companycode)
//    TextView companycode;


    private List<Map<String, Object>> m_ltMenuList;
    private String[] m_sMenuText;
    private int[] m_nMenuIcon;


    private String st_usercardid, st_userid, st_username, st_companycode;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        ButterKnife.bind(this, view);

        initViews();
//        setUserinfo();
        InitMenuList(SystemUser.GetFunList().trim());
        GridView m_gvGridView = (GridView) view.findViewById(R.id.fragment_gvMentItem);
        //新建List
        m_ltMenuList = new ArrayList<Map<String, Object>>();
        //获取数据
        GetMenuItem();
        //新建适配器
        String[] sFrom = {"ivMenuIcon", "tvMenuText"};
        int[] nTo = {R.id.ivMenuIcon, R.id.tvMenuText};
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        SimpleAdapter m_saSimpleAdapter = new SimpleAdapter(getContext(), m_ltMenuList, R.layout.itemmenu, sFrom, nTo);
//        }
        //配置适配器
        m_gvGridView.setAdapter(m_saSimpleAdapter);
        m_gvGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(),m_sMenuText[position],Toast.LENGTH_LONG).show();
                OnMenuItemClick(m_sMenuText[position]);
            }
        });

        return view;
    }

//    private void setUserinfo() {
//        user.setText(st_userid);
//        username.setText(st_username);
//        companycode.setText("公司代码:" + st_companycode);
//    }

    private void initViews() {
        st_userid = UserInfo.USERID + "";
        st_username = UserInfo.USERNAME;
        st_usercardid = UserInfo.USERCARDID;
        st_companycode = UserInfo.COMPANYCODE;
    }

    private void InitMenuList(String StrList) {
        String[] FunStr = new String[21];
        for (int i = 0; i < 21; i++) {
            FunStr[i] = "00" + (7001 + i);
        }
//        String FunStr[] = StrList.split(",");
        m_sMenuText = new String[FunStr.length];
        m_nMenuIcon = new int[FunStr.length];

        for (int i = 0; i < FunStr.length; i++) {
            if (FunStr[i].toString().trim().length() == 6) {
                GetFunImageId(FunStr[i].toString().trim());
                m_sMenuText[i] = FunList.GetFunName();
                m_nMenuIcon[i] = FunList.GetImagetId();
            }
        }
    }

    public void GetMenuItem() {
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < m_nMenuIcon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ivMenuIcon", m_nMenuIcon[i]);
            map.put("tvMenuText", m_sMenuText[i]);
            m_ltMenuList.add(map);
        }
    }

    private void GetFunImageId(String FunId) {
//        switch (FunId) {
//            case "007001":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.csh);
//                FunList.SetFunName("初始化");
//                break;
//            case "007002":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.cccx);
//                FunList.SetFunName("库存查询");
//                break;
//            case "007003":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.iqc);
//                FunList.SetFunName("IQC检测");
//                break;
//            case "007004":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.cgsh);
//                FunList.SetFunName("采购收货");
//                break;
//            case "007005":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.cgth);
//                FunList.SetFunName("采购退货");
//                break;
//            case "007006":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.wltmcx);
//                FunList.SetFunName("物料条码查询");
//                break;
//            case "007007":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.cgrk);
//                FunList.SetFunName("采购入库");
//                break;
//            case "007008":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.zpfl);
//                FunList.SetFunName("装配发料");
//                break;
//            case "007009":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.jjfl);
//                FunList.SetFunName("紧急发料");
//                break;
//            case "007010":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.cpxhbd);
//                FunList.SetFunName("产品序号绑定");
//                break;
//            case "007011":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.zzjtkjsqr);
//                FunList.SetFunName("自制件入库接收确认");
//                break;
//            case "007012":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.zzjrk);
//                FunList.SetFunName("自制件退库");
//                break;
//            case "007013":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.zcck);
//                FunList.SetFunName("转仓出库");
//                break;
//            case "007014":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.zcjs);
//                FunList.SetFunName("转仓接收");
//                break;
//            case "007015":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.cp);
//                FunList.SetFunName("库存盘点");
//                break;
//            case "007016":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.fp);
//                FunList.SetFunName("差异调整");
//                break;
//            case "007017":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.yw);
//                FunList.SetFunName("移位");
//                break;
//            case "007018":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.zpwljsqr);
//                FunList.SetFunName("装配物料接收确认");
//                break;
//            case "007019":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.zptl);
//                FunList.SetFunName("装配退料");
//                break;
//            case "007020":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.zzjrk);
//                FunList.SetFunName("自制件入库");
//                break;
//            case "007021":
//                FunList.SetFunId(FunId);
//                FunList.SetImageId(R.drawable.zzjtkjsqr);
//                FunList.SetFunName("自制件退库接收确认");
//                break;
//        }
    }

    public void OnMenuItemClick(String sMenuItem) {
        Intent intent = new Intent();
        Log.i("MainActivity", sMenuItem);
//        switch (sMenuItem) {
//            case "初始化":
//                intent.setClass(getActivity(), Activity_CSH.class);
//                intent.putExtra("userid", st_userid);
//                startActivity(intent);
//                break;
//            case "库存查询":
//                intent.setClass(getActivity(), Activity_KCCX.class);
//                startActivity(intent);
//                break;
//            case "IQC检测":
//                intent.setClass(getActivity(), Activity_IQC_James.class);
//                startActivity(intent);
//                break;
//            case "采购收货":
//                intent.setClass(getActivity(), Activity_CGSH.class);
//                startActivity(intent);
//                break;
////            case "采购退货":
////                intent.setClass(getActivity(), Activity_CGTH.class);
////                startActivity(intent);
////                break;
//            case "物料条码查询":
//                intent.setClass(getActivity(), Activity_TMCX_James.class);
//                startActivity(intent);
//                break;
//            case "采购入库":
//                intent.setClass(getActivity(), Activity_JSRK_James.class);
//                startActivity(intent);
//                break;
////            case "装配发料":
////                intent.setClass(getActivity(), Activity_ZPFL.class);
////                startActivity(intent);
////                break;
////            case "紧急发料":
////                intent.setClass(getActivity(), Activity_JJFL.class);
////                startActivity(intent);
////                break;
//            case "产品序号绑定":
//                intent.setClass(getActivity(), Activity_XHBD.class);
//                startActivity(intent);
//                break;
////            case "自制件入库接收确认":
////                intent.setClass(getActivity(), Activity_ZZJRKJSQR.class);
////                startActivity(intent);
////                break;
////            case "自制件退库":
////                intent.setClass(getActivity(), Activity_ZZJTK.class);
////                startActivity(intent);
////                break;
////            case "转仓出库":
////                intent.setClass(getActivity(), Activity_ZCCK.class);
////                startActivity(intent);
////                break;
////            case "转仓接收":
////                intent.setClass(getActivity(), Activity_ZCJS.class);
////                startActivity(intent);
////                break;
////            case "库存盘点":
////                intent.setClass(getActivity(), Activity_PD.class);
////                startActivity(intent);
////                break;
//            case "差异调整":
//                break;
//            case "移位":
//                intent.setClass(getActivity(), Activity_YW.class);
//                startActivity(intent);
//                break;
////            case "装配物料接收确认":
////                intent.setClass(getActivity(), Activity_ZPWLJSQR.class);
////                startActivity(intent);
////                break;
////            case "装配退料":
////                intent.setClass(getActivity(), Activity_ZPTL.class);
////                startActivity(intent);
////                break;
////            case "自制件入库":
////                Intent intent1 = new Intent(getActivity(), Activity_ZZJRK.class);
////                startActivity(intent1);
////                break;
//            case "自制件退库接收确认":
//                break;
//
//        }
    }
}
