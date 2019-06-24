package gordenyou.huadian.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Gordenyou on 30/5/19.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private Context context;

    private String sql_checklistinfo = "create table rCheckListInfo (CheckListID int," +
            "CheckID text ," +
            "AssetID text ," +
            "AssetName text ," +
            "CompanyCode text ," +
            "CostCenter text ," +
            "Location text ," +
            "locationname text ," +
            "room text ," +
            "rescode text ," +
            "resman text ," +
            "UserID text ," +
            "username text ," +
            "NewCompanyCode text ," +
            "NewCostCenter text ," +
            "NewLocation text ," +
            "NewUser text ," +
            "newresmancode text ," +
            "ScrapReason text ," +
            "CheckStatus int ," +
            "ScanDate datatime ," +
            "scanman int ," +
            "AcqDate datatime," +
            "CustName text ," +
            "MakeUserid ntext ," +
            "productsn text ," +
            "remark text ," +
            "flags int ," +
            "newroom text ," +
            "newsn text ," +
            "newremark text ," +
            "newbrand text ," +
            "newmodel text ," +
            "model text)";

    private String sql_checkinfo = "create table rCheckInfo(CheckID text," +
            "CheckDate text," +
            "UpDatetime datatime," +
            "Status text," +
            "checktitle text," +
            "flag int," +
            "UserID int," +
            "MakeCheckDate datatime)";

    private String sql_deptinfo = "create table DeptInfo(DeptID text ," +
            "DeptName text," +
            "DeptKind text," +
            "CostCenter text," +
            "DeptCode text," +
            "DeptAbbr text," +
            "ParentDeptID text," +
            "PrimaryMan text)";

    private String sql_Workuser = "create table Workuser(UserID text, " +
            "UserCardID text, " +
            "UserName text, " +
            "Password text ," +
            "DeptID text ," +
            "ERPFUserID numeric," +
            "ERPName text," +
            "ERPPASSWORD  text," +
            "forbid text," +
            "GroupID text," +
            "ProductionScheduleName text," +
            "MOBILE text," +
            "PHONE text," +
            "EMAIL text)";

    private String sql_rMaterielInfo = "create table rMaterielInfo(MaterielID int ," +
            "MaterielCode text ," +
            "MaterielName text ," +
            "MaterielUnit text ," +
            "MaterielType text ," +
            "UnitName text ," +
            "MaterielGroup text ," +
            "CostCenter text ," +
            "CompanyCode text ," +
            "MaterielKind text ," +
            "StockNO text ," +
            "PurchaseDate datetime ," +
            "DeathDate datetime ," +
            "UserName text ," +
            "LicenseNO text ," +
            "IsSystem text ," +
            "FirstCost numeric ," +
            "SysFirstCost numeric ," +
            "Depreciation numeric ," +
            "NetValue numeric ," +
            "EndFlag text ," +
            "StockOrder text ," +
            "StatusDate datetime ," +
            "ISSAP int ," +
            "Memo text ," +
            "IsSN text ," +
            "SafetyStock numeric ," +
            "InspectionLevel int ," +
            "InnerPackNum numeric ," +
            "PackNum numeric ," +
            "CustNo text ," +
            "DEPTID text ," +
            "IsCalculable text ," +
            "LastValue numeric ," +
            "area text ," +
            "areaname text ," +
            "resmancode text ," +
            "resman text ," +
            "ownercode text ," +
            "ownername text ," +
            "productsn text ," +
            "EPC text ," +
            "newarea text ," +
            "newres text ," +
            "newowner text ," +
            "newsn text ," +
            "ischange int ," +
            "subcode text ," +
            "mcode text ," +
            "newroom text ," +
            "newremark text)";

    private String sql_AssetBorrowList = "Create table AssetBorrowList(BorrowListID text," +
            "PlanDate text," +
            "UserName text," +
            "EditDate text," +
            "Remarks text," +
            "State int)";

    private String sql_AssetBorrowListDetail = "Create table AssetBorrowListDetail(BorrowListID text," +
            "AssetID text," +
            "MaterielName text," +
            "MaterielKind text," +
            "BorrowNum int," +
            "PlanDate datetime," +
            "UnitID text," +
            "DeptID text," +
            "area text," +
            "Borrowman text," +
            "UserName text," +
            "BorrowType text," +
            "StockID text," +
            "EditDate datetime," +
            "State int," +
            "Remark text)";

    private String sql_AssetReturnDetail = "Create table AssetReturnDetail(BorrowListID text," +
            "AssetID text," +
            "ReturnNum int," +
            "Returnman text," +
            "ReturnDate text," +
            "UnitID text," +
            "Borrowman text," +
            "DeptID text," +
            "UserName text," +
            "ReturnType text," +
            "StockID text," +
            "EditDate text," +
            "Remark text)";

    //在SQLiteOpenHelper的子类当中，必须有该构造函数
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                              int version) {
        //必须通过super调用父类当中的构造函数
        super(context, name, factory, version);
        this.context = context;
    }
    //参数说明
    //context:上下文对象
    //name:数据库名称
    //param:factory
    //version:当前数据库的版本，值必须是整数并且是递增的状态


    //当数据库创建的时候被调用
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Log.e("MySQLiteOpenHelper", "创建表格");
        //创建了数据库并创建一个叫records的表
        //SQLite数据创建支持的数据类型： 整型数据，字符串类型，日期类型，二进制的数据类型
        //String sql = "create table user(id int primary key,name text)";

        //execSQL用于执行SQL语句
        //完成数据库的创建
        db.execSQL(sql_checklistinfo);
        db.execSQL(sql_checkinfo);
        db.execSQL(sql_deptinfo);
        db.execSQL(sql_Workuser);
        db.execSQL(sql_rMaterielInfo);
        db.execSQL(sql_AssetBorrowList);
        db.execSQL(sql_AssetBorrowListDetail);
        db.execSQL(sql_AssetReturnDetail);
        //数据库实际上是没有被创建或者打开的，直到getWritableDatabase() 或者 getReadableDatabase() 方法中的一个被调用时才会进行创建或者打开
        Toast.makeText(context, "创建数据库成功", Toast.LENGTH_SHORT).show();
    }

    //数据库升级时调用
    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade（）方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("更新数据库版本为:"+newVersion);
    }

}
