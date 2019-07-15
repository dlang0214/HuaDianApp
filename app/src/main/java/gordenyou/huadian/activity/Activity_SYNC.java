package gordenyou.huadian.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import gordenyou.huadian.R;
import gordenyou.huadian.common.CommonMethod;
import gordenyou.huadian.common.MySQLiteOpenHelper;
import gordenyou.huadian.unity.WebParams;

public class Activity_SYNC extends BaseActivity implements View.OnClickListener {
    private static final int LISTNUMBER = 8;
    @BindView(R.id.sync_xiazai)
    Button xiazai;
    @BindView(R.id.sync_shangchuan)
    Button shangchuan;
    @BindView(R.id.linear_download)
    LinearLayout linear_download;
    @BindView(R.id.time_down)
    TextView time_download;
    @BindView(R.id.linear_upload)
    LinearLayout linear_upload;
    @BindView(R.id.time_upload)
    TextView time_upload;

    AlertDialog dialog;
    ProgressBar mProgressBar;
    TextView jindu;
    int progress = 0;

    int uploadnum = 0;
    int uploadprogress;
    int count;
    MySQLiteOpenHelper dbhelper;
    SQLiteDatabase sqLiteDatabase;
    int increase;

    @Override
    public void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sync);
        ButterKnife.bind(this);
        toggleWiFi(getBaseContext());
        //Toast.makeText(this, "已打开WIFI", Toast.LENGTH_SHORT).show();
        //dbhelper = new MySQLiteOpenHelper(getBaseContext(), "temp_data.db", null, 1);
        //sqLiteDatabase = dbhelper.getReadableDatabase();
        xiazai.setOnClickListener(this);
        shangchuan.setOnClickListener(this);
    }

    @Override
    public void LogicMethod() {
        SetTime("time_download", linear_download, time_download);
        SetTime("time_upload", linear_upload, time_upload);
    }

    private void SetTime(String key, LinearLayout linearLayout, TextView textView) {
        String time = getValues(key);
        if (time != null) {
            linearLayout.setVisibility(View.VISIBLE);
            textView.setText(time);
        }
    }

    @Override
    public void initValues() {

    }

    @Override
    public void GetRequestResult(String data, int m) {
        switch (m) {
            case 1:
                new CheckListInfoAsync().execute(data);
                break;
            case 2:
                new CheckInfoAsync().execute(data);
                break;
            case 3:
                new DeptInfoAsync().execute(data);
                break;
            case 4:
                new WorkuserAsync().execute(data);
                break;
            case 5:
                new rMaterielInfoAsync().execute(data);
                break;
            case 6:
                new AssetBorrowListAsync().execute(data);
                break;
            case 7:
                new AssetBorrwoListDetialAsync().execute(data);
                break;
            case 8:
                new AssetReturnDetailAsync().execute(data);
                break;
        }
    }

    private class CheckListInfoAsync extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                JSONArray jsonArray = new JSONObject(params[0]).getJSONArray("ListInfo");
                sqLiteDatabase.beginTransaction();
                for (int j = 0; j < jsonArray.length(); j++) {
                    ContentValues values = new ContentValues();
                    values.put("CheckListID", jsonArray.optJSONObject(j).getInt("CheckListID"));
                    values.put("CheckID", jsonArray.optJSONObject(j).getString("CheckID"));
                    values.put("AssetID", jsonArray.optJSONObject(j).getString("AssetID"));
                    values.put("AssetName", jsonArray.optJSONObject(j).getString("AssetName"));
                    values.put("CompanyCode", jsonArray.optJSONObject(j).getString("CompanyCode"));
                    values.put("CostCenter", jsonArray.optJSONObject(j).getString("CostCenter"));
                    values.put("Location", jsonArray.optJSONObject(j).getString("Location"));
                    values.put("locationname", jsonArray.optJSONObject(j).getString("locationname"));
                    values.put("room", jsonArray.optJSONObject(j).getString("room"));
                    values.put("rescode", jsonArray.optJSONObject(j).getString("rescode"));
                    values.put("resman", jsonArray.optJSONObject(j).getString("resman"));
                    values.put("UserID", jsonArray.optJSONObject(j).getString("UserID"));
                    values.put("username", jsonArray.optJSONObject(j).getString("username"));
                    values.put("NewCompanyCode", jsonArray.optJSONObject(j).getString("NewCompanyCode"));
                    values.put("NewCostCenter", jsonArray.optJSONObject(j).getString("NewCostCenter"));
                    values.put("NewLocation", jsonArray.optJSONObject(j).getString("NewLocation"));
                    values.put("NewUser", jsonArray.optJSONObject(j).getString("NewUser"));
                    values.put("newresmancode", jsonArray.optJSONObject(j).getString("newresmancode"));
                    values.put("ScrapReason", jsonArray.optJSONObject(j).getString("ScrapReason"));
                    values.put("CheckStatus", jsonArray.optJSONObject(j).getInt("CheckStatus"));
                    values.put("ScanDate", jsonArray.optJSONObject(j).getString("ScanDate"));
                    values.put("scanman", jsonArray.optJSONObject(j).getString("scanman"));
                    values.put("AcqDate", jsonArray.optJSONObject(j).getString("AcqDate"));
                    values.put("CustName", jsonArray.optJSONObject(j).getString("CustName"));
                    values.put("MakeUserid", jsonArray.optJSONObject(j).getString("MakeUserid"));
                    values.put("productsn", jsonArray.optJSONObject(j).getString("productsn"));
                    values.put("remark", jsonArray.optJSONObject(j).getString("remark"));
                    values.put("flags", jsonArray.optJSONObject(j).getInt("flags"));
                    values.put("newroom", jsonArray.optJSONObject(j).getString("newroom"));
                    values.put("newsn", jsonArray.optJSONObject(j).getString("newsn"));
                    values.put("newremark", jsonArray.optJSONObject(j).getString("newremark"));
                    values.put("newbrand", jsonArray.optJSONObject(j).getString("newbrand"));
                    values.put("newmodel", jsonArray.optJSONObject(j).getString("newmodel"));
                    values.put("model", jsonArray.optJSONObject(j).getString("model"));
                    sqLiteDatabase.insertOrThrow("rCheckListInfo", null, values);
                    progress = (((j + 1) * 100) / jsonArray.length()) / LISTNUMBER;
                    publishProgress(progress);
                }
                sqLiteDatabase.setTransactionSuccessful();
            } catch (JSONException e) {
                cancel(true);
            } finally {
                sqLiteDatabase.endTransaction();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mProgressBar.setProgress(values[0]);
            jindu.setText(String.valueOf(values[0]));
            //Log.d("updateThread", "id:" + Thread.currentThread().getId() + " name:" + Thread.currentThread().getName());//这里是UI主线程
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            WebParams.Clear();
            WebParams.SetSubOperationFunction("DownloadList_Gorden");
            WebParams.Setparam1("rCheckInfo");
            WebHttpRequest(2);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
            ShowErrMsgDialog("CheckListInfo下载数据出错，请联系管理员！");
        }
    }

    private class CheckInfoAsync extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                JSONArray jsonArray = new JSONObject(params[0]).getJSONArray("ListInfo");
                sqLiteDatabase.beginTransaction();
                for (int j = 0; j < jsonArray.length(); j++) {
                    ContentValues values = new ContentValues();
                    values.put("CheckID", jsonArray.optJSONObject(j).getString("CheckID"));
                    values.put("CheckDate", jsonArray.optJSONObject(j).getString("CheckDate"));
                    values.put("UpDatetime", jsonArray.optJSONObject(j).getString("UpDatetime"));
                    values.put("Status", jsonArray.optJSONObject(j).getString("Status"));
                    values.put("checktitle", jsonArray.optJSONObject(j).getString("checktitle"));
                    values.put("flag", jsonArray.optJSONObject(j).getString("flag"));
                    values.put("UserID", jsonArray.optJSONObject(j).getString("UserID"));
                    values.put("MakeCheckDate", jsonArray.optJSONObject(j).getString("MakeCheckDate"));
                    sqLiteDatabase.insertOrThrow("rCheckInfo", null, values);
                    progress = (((j + 1) * 100) / jsonArray.length()) / LISTNUMBER + 100 / LISTNUMBER;
                    publishProgress(progress);
                }
                sqLiteDatabase.setTransactionSuccessful();

                //Toast.makeText(getBaseContext(), "数据库导入成功！", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {

                cancel(true);
            } finally {
                sqLiteDatabase.endTransaction();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mProgressBar.setProgress(values[0]);
            jindu.setText(String.valueOf(values[0]));
            //Log.e("updateThread", "id:" + Thread.currentThread().getId() + " name:" + Thread.currentThread().getName());//这里是UI主线程
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            WebParams.Clear();
            WebParams.SetSubOperationFunction("DownloadList_Gorden");
            WebParams.Setparam1("DeptInfo");
            WebHttpRequest(3);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
            ShowErrMsgDialog("CheckInfo下载数据出错，请联系管理员！");
        }
    }

    private class DeptInfoAsync extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                JSONArray jsonArray = new JSONObject(params[0]).getJSONArray("ListInfo");
                sqLiteDatabase.beginTransaction();
                for (int j = 0; j < jsonArray.length(); j++) {
                    ContentValues values = new ContentValues();
                    values.put("DeptID", jsonArray.optJSONObject(j).getString("DeptID"));
                    values.put("DeptName", jsonArray.optJSONObject(j).getString("DeptName"));
                    values.put("DeptKind", jsonArray.optJSONObject(j).getString("DeptKind"));
                    values.put("CostCenter", jsonArray.optJSONObject(j).getString("CostCenter"));
                    values.put("DeptCode", jsonArray.optJSONObject(j).getString("DeptCode"));
                    values.put("DeptAbbr", jsonArray.optJSONObject(j).getString("DeptAbbr"));
                    values.put("ParentDeptID", jsonArray.optJSONObject(j).getString("ParentDeptID"));
                    values.put("PrimaryMan", jsonArray.optJSONObject(j).getString("PrimaryMan"));
                    sqLiteDatabase.insertOrThrow("DeptInfo", null, values);
                    progress = (((j + 1) * 100) / jsonArray.length()) / LISTNUMBER + 200 / LISTNUMBER;
                    publishProgress(progress);
                }
                sqLiteDatabase.setTransactionSuccessful();
                //Toast.makeText(getBaseContext(), "数据库导入成功！", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {

                cancel(true);
            } finally {
                sqLiteDatabase.endTransaction();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mProgressBar.setProgress(values[0]);
            jindu.setText(String.valueOf(values[0]));
            //Log.e("updateThread", "id:" + Thread.currentThread().getId() + " name:" + Thread.currentThread().getName());//这里是UI主线程
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            WebParams.Clear();
            WebParams.SetSubOperationFunction("DownloadList_Gorden");
            WebParams.Setparam1("Workuser");
            WebHttpRequest(4);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
            ShowErrMsgDialog("DeptInfo下载数据出错，请联系管理员！");
        }
    }

    private class WorkuserAsync extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                JSONArray jsonArray = new JSONObject(params[0]).getJSONArray("ListInfo");
                sqLiteDatabase.beginTransaction();
                for (int j = 0; j < jsonArray.length(); j++) {
                    ContentValues values = new ContentValues();
                    values.put("UserID", jsonArray.optJSONObject(j).getString("UserID"));
                    values.put("UserCardID", jsonArray.optJSONObject(j).getString("UserCardID"));
                    values.put("UserName", jsonArray.optJSONObject(j).getString("UserName"));
                    values.put("Password", jsonArray.optJSONObject(j).getString("Password"));
                    values.put("DeptID", jsonArray.optJSONObject(j).getString("DeptID"));
                    values.put("ERPFUserID", jsonArray.optJSONObject(j).getString("ERPFUserID"));
                    values.put("ERPName", jsonArray.optJSONObject(j).getString("ERPName"));
                    values.put("ERPPASSWORD", jsonArray.optJSONObject(j).getString("ERPPASSWORD"));
                    values.put("forbid", jsonArray.optJSONObject(j).getString("forbid"));
                    values.put("GroupID", jsonArray.optJSONObject(j).getString("GroupID"));
                    values.put("ProductionScheduleName", jsonArray.optJSONObject(j).getString("ProductionScheduleName"));
                    values.put("MOBILE", jsonArray.optJSONObject(j).getString("MOBILE"));
                    values.put("PHONE", jsonArray.optJSONObject(j).getString("PHONE"));
                    values.put("EMAIL", jsonArray.optJSONObject(j).getString("EMAIL"));
                    sqLiteDatabase.insertOrThrow("Workuser", null, values);
                    progress = (((j + 1) * 100) / jsonArray.length()) / LISTNUMBER + 300 / LISTNUMBER;
                    publishProgress(progress);
                }
                sqLiteDatabase.setTransactionSuccessful();
                //Toast.makeText(getBaseContext(), "数据库导入成功！", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {

                cancel(true);
            } finally {
                sqLiteDatabase.endTransaction();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mProgressBar.setProgress(values[0]);
            jindu.setText(String.valueOf(values[0]));
            //Log.e("updateThread", "id:" + Thread.currentThread().getId() + " name:" + Thread.currentThread().getName());//这里是UI主线程
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            WebParams.Clear();
            WebParams.SetSubOperationFunction("DownloadList_Gorden");
            WebParams.Setparam1("rMaterielInfo");
            WebHttpRequest(5);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
            ShowErrMsgDialog("Workuser表下载数据出错，请联系管理员！");
        }
    }

    private class rMaterielInfoAsync extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                JSONArray jsonArray = new JSONObject(params[0]).getJSONArray("ListInfo");
                sqLiteDatabase.beginTransaction();
                for (int j = 0; j < jsonArray.length(); j++) {
                    ContentValues values = new ContentValues();
                    values.put("MaterielID", jsonArray.optJSONObject(j).getInt("MaterielID"));
                    values.put("MaterielCode", jsonArray.optJSONObject(j).getString("MaterielCode"));
                    values.put("MaterielName", jsonArray.optJSONObject(j).getString("MaterielName"));
                    values.put("MaterielUnit", jsonArray.optJSONObject(j).getString("MaterielUnit"));
                    values.put("MaterielType", jsonArray.optJSONObject(j).getString("MaterielType"));
                    values.put("UnitName", jsonArray.optJSONObject(j).getString("UnitName"));
                    values.put("MaterielGroup", jsonArray.optJSONObject(j).getString("MaterielGroup"));
                    values.put("CostCenter", jsonArray.optJSONObject(j).getString("CostCenter"));
                    values.put("CompanyCode", jsonArray.optJSONObject(j).getString("CompanyCode"));
                    values.put("MaterielKind", jsonArray.optJSONObject(j).getString("MaterielKind"));
                    values.put("StockNO", jsonArray.optJSONObject(j).getString("StockNO"));
                    values.put("PurchaseDate", jsonArray.optJSONObject(j).getString("PurchaseDate"));
                    values.put("DeathDate", jsonArray.optJSONObject(j).getString("DeathDate"));
                    values.put("UserName", jsonArray.optJSONObject(j).getString("UserName"));
                    values.put("LicenseNO", jsonArray.optJSONObject(j).getString("LicenseNO"));
                    values.put("IsSystem", jsonArray.optJSONObject(j).getString("IsSystem"));
                    values.put("FirstCost", jsonArray.optJSONObject(j).getString("FirstCost"));
                    values.put("SysFirstCost", jsonArray.optJSONObject(j).getString("SysFirstCost"));
                    values.put("Depreciation", jsonArray.optJSONObject(j).getString("Depreciation"));
                    values.put("NetValue", jsonArray.optJSONObject(j).getString("NetValue"));
                    values.put("EndFlag", jsonArray.optJSONObject(j).getString("EndFlag"));
                    values.put("StockOrder", jsonArray.optJSONObject(j).getString("StockOrder"));
                    values.put("StatusDate", jsonArray.optJSONObject(j).getString("StatusDate"));
                    values.put("ISSAP", jsonArray.optJSONObject(j).getInt("ISSAP"));
                    values.put("Memo", jsonArray.optJSONObject(j).getString("Memo"));
                    values.put("IsSN", jsonArray.optJSONObject(j).getString("IsSN"));
                    values.put("SafetyStock", jsonArray.optJSONObject(j).getString("SafetyStock"));
                    values.put("InspectionLevel", jsonArray.optJSONObject(j).getInt("InspectionLevel"));
                    values.put("InnerPackNum", jsonArray.optJSONObject(j).getString("InnerPackNum"));
                    values.put("PackNum", jsonArray.optJSONObject(j).getString("PackNum"));
                    values.put("CustNo", jsonArray.optJSONObject(j).getString("CustNo"));
                    values.put("DEPTID", jsonArray.optJSONObject(j).getString("DEPTID"));
                    values.put("IsCalculable", jsonArray.optJSONObject(j).getString("IsCalculable"));
                    values.put("LastValue", jsonArray.optJSONObject(j).getString("LastValue"));
                    values.put("area", jsonArray.optJSONObject(j).getString("area"));
                    values.put("areaname", jsonArray.optJSONObject(j).getString("areaname"));
                    values.put("resmancode", jsonArray.optJSONObject(j).getString("resmancode"));
                    values.put("resman", jsonArray.optJSONObject(j).getString("resman"));
                    values.put("ownercode", jsonArray.optJSONObject(j).getString("ownercode"));
                    values.put("ownername", jsonArray.optJSONObject(j).getString("ownername"));
                    values.put("productsn", jsonArray.optJSONObject(j).getString("productsn"));
                    values.put("EPC", jsonArray.optJSONObject(j).getString("EPC"));
                    values.put("newarea", jsonArray.optJSONObject(j).getString("newarea"));
                    values.put("newres", jsonArray.optJSONObject(j).getString("newres"));
                    values.put("newowner", jsonArray.optJSONObject(j).getString("newowner"));
                    values.put("newsn", jsonArray.optJSONObject(j).getString("newsn"));
                    values.put("ischange", jsonArray.optJSONObject(j).getInt("ischange"));
                    values.put("subcode", jsonArray.optJSONObject(j).getString("subcode"));
                    values.put("mcode", jsonArray.optJSONObject(j).getString("mcode"));
                    values.put("newroom", jsonArray.optJSONObject(j).getString("newroom"));
                    values.put("newremark", jsonArray.optJSONObject(j).getString("newremark"));
                    sqLiteDatabase.insertOrThrow("rMaterielInfo", null, values);
                    progress = (((j + 1) * 100) / jsonArray.length()) / LISTNUMBER + 400 / LISTNUMBER;
                    publishProgress(progress);
                }
                sqLiteDatabase.setTransactionSuccessful();
                //Toast.makeText(getBaseContext(), "数据库导入成功！", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {

                cancel(true);
            } finally {
                sqLiteDatabase.endTransaction();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBar.setProgress(values[0]);
            jindu.setText(String.valueOf(values[0]));
            //Log.e("updateThread", "id:" + Thread.currentThread().getId() + " name:" + Thread.currentThread().getName());//这里是UI主线程
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            WebParams.Clear();
            WebParams.SetSubOperationFunction("DownloadList_Gorden");
            WebParams.Setparam1("AssetBorrowList");
            WebHttpRequest(6);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
            ShowErrMsgDialog("rMaterielInfo表下载数据出错，请联系管理员！");
        }
    }

    private class AssetBorrowListAsync extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                JSONArray jsonArray = new JSONObject(params[0]).getJSONArray("ListInfo");
                sqLiteDatabase.beginTransaction();
                for (int j = 0; j < jsonArray.length(); j++) {
                    ContentValues values = new ContentValues();
                    values.put("BorrowListID", jsonArray.optJSONObject(j).getString("BorrowListID"));
                    values.put("PlanDate", jsonArray.optJSONObject(j).getString("PlanDate"));
                    values.put("UserName", jsonArray.optJSONObject(j).getString("UserName"));
                    values.put("EditDate", jsonArray.optJSONObject(j).getString("EditDate"));
                    values.put("State", jsonArray.optJSONObject(j).getString("State"));
                    sqLiteDatabase.insertOrThrow("AssetBorrowList", null, values);
                    progress = (((j + 1) * 100) / jsonArray.length()) / LISTNUMBER + 500 / LISTNUMBER;
                    publishProgress(progress);
                }
                sqLiteDatabase.setTransactionSuccessful();
                //Toast.makeText(getBaseContext(), "数据库导入成功！", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {

                cancel(true);
            } finally {
                sqLiteDatabase.endTransaction();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mProgressBar.setProgress(values[0]);
            jindu.setText(String.valueOf(values[0]));
            //Log.e("updateThread", "id:" + Thread.currentThread().getId() + " name:" + Thread.currentThread().getName());//这里是UI主线程
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            WebParams.Clear();
            WebParams.SetSubOperationFunction("DownloadList_Gorden");
            WebParams.Setparam1("AssetBorrowListDetail");
            WebHttpRequest(7);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
            ShowErrMsgDialog("AssetBorrowList表下载数据出错，请联系管理员！");
        }
    }

    private class AssetBorrwoListDetialAsync extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                JSONArray jsonArray = new JSONObject(params[0]).getJSONArray("ListInfo");
                sqLiteDatabase.beginTransaction();
                for (int j = 0; j < jsonArray.length(); j++) {
                    ContentValues values = new ContentValues();
                    values.put("BorrowListID", jsonArray.optJSONObject(j).getString("BorrowListID"));
                    values.put("AssetID", jsonArray.optJSONObject(j).getString("AssetID"));
                    values.put("MaterielName", jsonArray.optJSONObject(j).getString("MaterielName"));
                    values.put("MaterielKind", jsonArray.optJSONObject(j).getString("MaterielKind"));
                    values.put("PlanDate", jsonArray.optJSONObject(j).getString("PlanDate"));
                    values.put("UnitID", jsonArray.optJSONObject(j).getString("UnitID"));
                    values.put("DeptName", jsonArray.optJSONObject(j).getString("DeptName"));
                    values.put("area", jsonArray.optJSONObject(j).getString("area"));
                    values.put("Borrowman", jsonArray.optJSONObject(j).getString("Borrowman"));
                    values.put("UserName", jsonArray.optJSONObject(j).getString("UserName"));
                    values.put("BorrowType", jsonArray.optJSONObject(j).getString("BorrowType"));
                    values.put("StockID", jsonArray.optJSONObject(j).getString("StockID"));
                    values.put("EditDate", jsonArray.optJSONObject(j).getString("EditDate"));
                    values.put("State", jsonArray.optJSONObject(j).getString("State"));
                    values.put("Remark", jsonArray.optJSONObject(j).getString("Remark"));
                    sqLiteDatabase.insertOrThrow("AssetBorrowListDetail", null, values);
                    progress = (((j + 1) * 100) / jsonArray.length()) / LISTNUMBER + 600 / LISTNUMBER;
                    publishProgress(progress);
                }
                sqLiteDatabase.setTransactionSuccessful();
                //Toast.makeText(getBaseContext(), "数据库导入成功！", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {

                cancel(true);
            } finally {
                sqLiteDatabase.endTransaction();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mProgressBar.setProgress(values[0]);
            jindu.setText(String.valueOf(values[0]));
            //Log.e("updateThread", "id:" + Thread.currentThread().getId() + " name:" + Thread.currentThread().getName());//这里是UI主线程
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            WebParams.Clear();
            WebParams.SetSubOperationFunction("DownloadList_Gorden");
            WebParams.Setparam1("AssetReturnDetail");
            WebHttpRequest(8);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
            ShowErrMsgDialog("AssetBorrowListDetail表下载数据出错，请联系管理员！");
        }
    }

    private class AssetReturnDetailAsync extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                JSONArray jsonArray = new JSONObject(params[0]).getJSONArray("ListInfo");
                sqLiteDatabase.beginTransaction();
                for (int j = 0; j < jsonArray.length(); j++) {
                    ContentValues values = new ContentValues();
                    values.put("BorrowListID", jsonArray.optJSONObject(j).getString("BorrowListID"));
                    values.put("AssetID", jsonArray.optJSONObject(j).getString("AssetID"));
                    values.put("MaterielName", jsonArray.optJSONObject(j).getString("MaterielName"));
                    values.put("MaterielKind", jsonArray.optJSONObject(j).getString("MaterielKind"));
                    values.put("ReturnNum", jsonArray.optJSONObject(j).getString("ReturnNum"));
                    values.put("Returnman", jsonArray.optJSONObject(j).getString("Returnman"));
                    values.put("ReturnDate", jsonArray.optJSONObject(j).getString("ReturnDate"));
                    values.put("UnitID", jsonArray.optJSONObject(j).getString("UnitID"));
                    values.put("Borrowman", jsonArray.optJSONObject(j).getString("Borrowman"));
                    values.put("DeptName", jsonArray.optJSONObject(j).getString("DeptName"));
                    values.put("UserName", jsonArray.optJSONObject(j).getString("UserName"));
                    values.put("ReturnType", jsonArray.optJSONObject(j).getString("ReturnType"));
                    values.put("StockID", jsonArray.optJSONObject(j).getString("StockID"));
                    values.put("EditDate", jsonArray.optJSONObject(j).getString("EditDate"));
                    values.put("Remark", jsonArray.optJSONObject(j).getString("Remark"));
                    sqLiteDatabase.insertOrThrow("AssetReturnDetail", null, values);
                    progress = (((j + 1) * 100) / jsonArray.length()) / LISTNUMBER + 700 / LISTNUMBER;
                    publishProgress(progress);
                }
                sqLiteDatabase.setTransactionSuccessful();
                SaveSharePreferences();
                //Toast.makeText(getBaseContext(), "数据库导入成功！", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                cancel(true);
            } finally {
                sqLiteDatabase.endTransaction();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBar.setProgress(values[0]);
            jindu.setText(String.valueOf(values[0]));
            //Log.e("updateThread", "id:" + Thread.currentThread().getId() + " name:" + Thread.currentThread().getName());//这里是UI主线程
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            ShowWarmMsgDialog("下载完成");
            SetTime("time_download", linear_download, time_download);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
            ShowErrMsgDialog("AssetReturnDetail表下载数据出错，请联系管理员！");
        }
    }

    private void SaveSharePreferences() {
        SetValues("time_download", CommonMethod.getTime());
        Set<String> changelist = new HashSet<>();
        SetValues("changelist", changelist);
        //各个表数据变化的记录
        SetValues("AssetBorrowList", changelist);
        SetValues("AssetBorrowListDetail", changelist);
        SetValues("RFID", changelist);
        SetValues("ZCBG", changelist);
        SetValues("ZCGH", changelist);
        SetValues("ZCPD", changelist);
    }

    /*
    WIFI网络开关
     */
    private void toggleWiFi(Context context) {
        WifiManager wm = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        assert wm != null;
        wm.setWifiEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sync_xiazai:
                getDataDownload();
                break;
            case R.id.sync_shangchuan:
                getDataUpload();
                break;
        }
    }

    @Override
    public void SuccessEvent(int m) {
        Log.i("SYNCCCCCCCCCCC", String.valueOf(m));
        dialog.dismiss();
        SetValues("time_upload", CommonMethod.getTime());
        SetTime("time_upload", linear_upload, time_upload);
        //重置记录
        Set<String> changelist = new HashSet<>();
        SetValues("changelist", changelist);
        //各个表数据变化的记录
        SetValues("AssetBorrowList", changelist);
        SetValues("AssetBorrowListDetail", changelist);
        SetValues("RFID", changelist);
        SetValues("ZCBG", changelist);
        SetValues("ZCGH", changelist);
        SetValues("ZCPD", changelist);
    }

    @Override
    public void ErrorEvent(int m) {
        dialog.dismiss();
    }

    private void getDataUpload() {
        Set<String> changelist = getSetValues("changelist");
        if (changelist != null) {
            uploadnum = changelist.size();
            increase = 100 / uploadnum;
        }
        if (uploadnum != 0) {
            showLayoutDialog(this, "上传进度");
            new UploadDataAsync().execute();
        } else {
            ShowWarmMsgDialog("暂无数据修改，无需上传！");
        }
    }

    private class UploadDataAsync extends AsyncTask<Object, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dbhelper = new MySQLiteOpenHelper(getBaseContext(), "temp_data.db", null, 1);
            sqLiteDatabase = dbhelper.getWritableDatabase();
        }

        @Override
        protected Void doInBackground(Object... params) {
            WebParams.Clear();
            WebParams.SetSubOperationFunction("UploadInfo_Gorden");
            WebParams.Setparam2(getAssetBorrowListDetialInfo());
            publishProgress(uploadprogress);
            WebParams.Setparam3(getAssetBorrowListInfo());
            publishProgress(uploadprogress);
            WebParams.Setparam4(getRFIDInfo());
            publishProgress(uploadprogress);
            WebParams.Setparam5(getZCBGInfo());
            publishProgress(uploadprogress);
            WebParams.Setparam6(getZCGHInfo());
            publishProgress(uploadprogress);
            WebParams.Setparam7(getZCPDInfo());
            WebHttpRequest(10);
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBar.setProgress(uploadprogress);
            jindu.setText(String.valueOf(uploadprogress));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
        }
    }


    private String getAssetBorrowListDetialInfo() {
        Set<String> borrowListDetail = getSetValues("AssetBorrowListDetail");
        StringBuilder list = new StringBuilder();
        for (String listID : borrowListDetail) {
            Cursor cursor = sqLiteDatabase.rawQuery("select * from AssetBorrowListDetail where BorrowListID = '" + listID + "'", null);
            String[] values = new String[]{"BorrowListID", "Borrowman", "DeptName", "AssetID", "MaterielName", "MaterielKind", "BorrowNum", "BorrowType", "UserName", "EditDate", "PlanDate", "State"};

            while (cursor.moveToNext()) {
                StringBuilder temp = new StringBuilder();
                int j = 1;
                for (String i : values) {
                    if (j != values.length) {
                        temp.append(cursor.getString(cursor.getColumnIndex(i))).append("○");
                    } else {
                        temp.append(cursor.getString(cursor.getColumnIndex(i)));
                    }
                    j++;
                }
                if (list.length() == 0) {
                    list.append(temp);
                } else {
                    list.append("$").append(temp);
                }
            }
            cursor.close();
        }
        uploadprogress += increase;
        return list.toString();
    }

    private String getAssetBorrowListInfo() {
        Set<String> borrowListDetail = getSetValues("AssetBorrowList");
        StringBuilder list = new StringBuilder();
        for (String listID : borrowListDetail) {
            Cursor cursor = sqLiteDatabase.rawQuery("select * from AssetBorrowList where BorrowListID = '" + listID + "'", null);
            String[] values = new String[]{"BorrowListID", "UserName", "EditDate"};

            while (cursor.moveToNext()) {
                StringBuilder temp = new StringBuilder();
                int j = 1;
                for (String i : values) {
                    if (j != values.length) {
                        temp.append(cursor.getString(cursor.getColumnIndex(i))).append("○");
                    } else {
                        temp.append(cursor.getString(cursor.getColumnIndex(i)));
                    }
                    j++;
                }
                if (list.length() == 0) {
                    list.append(temp);
                } else {
                    list.append("$").append(temp);
                }
            }
            cursor.close();
        }
        uploadprogress += increase;
        return list.toString();
    }

    private String getRFIDInfo() {
        Set<String> ListDetail = getSetValues("RFID");
        StringBuilder list = new StringBuilder();
        for (String listID : ListDetail) {
            Cursor cursor = sqLiteDatabase.rawQuery("select * from rMaterielInfo where mcode = '" + listID + "'", null);
            String[] values = new String[]{"mcode", "EPC"};

            while (cursor.moveToNext()) {
                StringBuilder temp = new StringBuilder();
                int j = 1;
                for (String i : values) {
                    if (j != values.length) {
                        temp.append(cursor.getString(cursor.getColumnIndex(i))).append("○");
                    } else {
                        temp.append(cursor.getString(cursor.getColumnIndex(i)));
                    }
                    j++;
                }
                if (list.length() == 0) {
                    list.append(temp);
                } else {
                    list.append("$").append(temp);
                }
            }
            cursor.close();
        }
        uploadprogress += increase;
        return list.toString();
    }

    private String getZCBGInfo() {
        Set<String> ListDetail = getSetValues("ZCBG");
        StringBuilder list = new StringBuilder();
        for (String listID : ListDetail) {
            Cursor cursor = sqLiteDatabase.rawQuery("select * from rMaterielInfo where mcode = '" + listID + "'", null);
            String[] values = new String[]{"mcode", "newres", "newowner", "newsn", "newremark", "newroom", "newarea"};

            while (cursor.moveToNext()) {
                StringBuilder temp = new StringBuilder();
                int j = 1;
                for (String i : values) {
                    if (j != values.length) {
                        temp.append(cursor.getString(cursor.getColumnIndex(i))).append("○");
                    } else {
                        temp.append(cursor.getString(cursor.getColumnIndex(i)));
                    }
                    j++;
                }
                if (list.length() == 0) {
                    list.append(temp);
                } else {
                    list.append("$").append(temp);
                }
            }
            cursor.close();
        }
        uploadprogress += increase;
        return list.toString();
    }

    private String getZCGHInfo() {
        Set<String> ListDetail = getSetValues("ZCGH");
        StringBuilder list = new StringBuilder();
        for (String listID : ListDetail) {
            Cursor cursor = sqLiteDatabase.rawQuery("select * from AssetReturnDetail where BorrowListID = '" + listID + "'", null);
            String[] values = new String[]{"Returnman", "BorrowListID", "AssetID", "MaterielName", "MaterielKind", "ReturnNum", "DeptName", "Borrowman", "UserName", "EditDate"};

            while (cursor.moveToNext()) {
                StringBuilder temp = new StringBuilder();
                int j = 1;
                for (String i : values) {
                    if (j != values.length) {
                        temp.append(cursor.getString(cursor.getColumnIndex(i))).append("○");
                    } else {
                        temp.append(cursor.getString(cursor.getColumnIndex(i)));
                    }
                    j++;
                }
                if (list.length() == 0) {
                    list.append(temp);
                } else {
                    list.append("$").append(temp);
                }
            }
            cursor.close();
        }
        uploadprogress += increase;
        return list.toString();
    }

    private String getZCPDInfo() {
        Set<String> ListDetail = getSetValues("ZCPD");
        StringBuilder list = new StringBuilder();
        for (String listID : ListDetail) {
            Cursor cursor = sqLiteDatabase.rawQuery("select * from rCheckListInfo where AssetID = '" + listID + "'", null);
            String[] values = new String[]{"CheckID", "AssetID", "NewCompanyCode", "NewCostCenter", "NewLocation", "NewUser", "flags", "newroom", "newremark"};

            while (cursor.moveToNext()) {
                StringBuilder temp = new StringBuilder();
                int j = 1;
                for (String i : values) {
                    if (j != values.length) {
                        temp.append(cursor.getString(cursor.getColumnIndex(i))).append("○");
                    } else {
                        temp.append(cursor.getString(cursor.getColumnIndex(i)));
                    }
                    j++;
                }
                if (list.length() == 0) {
                    list.append(temp);
                } else {
                    list.append("$").append(temp);
                }
            }
            cursor.close();
        }
        uploadprogress += increase;
        return list.toString();
    }

    private void getDataDownload() {
        if (sharedPreferences == null || sharedPreferences.getStringSet("changelist", null) == null ||
                sharedPreferences.getStringSet("changelist", null).size() == 0) {
            deleteDatabase("temp_data.db");
            dbhelper = new MySQLiteOpenHelper(getBaseContext(), "temp_data.db", null, 1);
            sqLiteDatabase = dbhelper.getWritableDatabase();
            progress = 0;
            WebParams.Clear();
            WebParams.SetSubOperationFunction("DownloadList_Gorden");
            WebParams.Setparam1("rCheckListInfo");
            showLayoutDialog(this, "下载进度");
            WebHttpRequest(1);
        } else {
            ShowWarmMsgDialog("PDA当前还有数据未上传数据库，请先上传数据！");
        }
    }

    public void showLayoutDialog(Context context, String title) {
        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_sync, null);
        mProgressBar = dialogview.findViewById(R.id.progerss_xiazai);
        jindu = dialogview.findViewById(R.id.progerss_jindu);
        dialog = new AlertDialog.Builder(context).setTitle(title).setView(dialogview).setCancelable(false)
                .create();
        dialog.show();
    }
}
