package gordenyou.huadian.common;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Gordenyou on 2018/1/17.
 */

public class SaveInfo {
    public static Properties getProperties(){
        Properties props = new Properties();
        try {
            FileInputStream stream = new FileInputStream(Environment.getExternalStorageDirectory()
                    +"/ZhenXiong.properties");
            props.load(stream);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            props.put("WebServiceurl", "http://192.168.0.138/ZhenXiong/Service.asmx");
            props.put("Account", "BaseDB_Test");
            props.put("ShopId", "1");
            props.put("ShopName", "崇礼店");
            e.printStackTrace();
        }
        return props;
    }

    public static boolean SaveProperties(Properties properties){
        File file = new File(Environment.getExternalStorageDirectory() + "/ZhenXiong.properties");
        if(!file.exists()){
            try {
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                properties.store(out, "");
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        else{
            file.delete();
            SaveProperties(properties);
            return true;
        }
        return true;

    }
}
