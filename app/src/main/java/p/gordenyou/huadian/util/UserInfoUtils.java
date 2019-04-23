package p.gordenyou.huadian.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gordenyou on 2018/3/12.
 */

public class UserInfoUtils {

    public static boolean saveUserInfor(String username, String companycode){

        try {
            String result = username + "##" + companycode;
            File file = new File("/data/data/gordenyou.zhenxiong/userInfo.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(result.getBytes());
            fileOutputStream.close();
            return true;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }


    }
    public static Map<String, String> readUserInfo(){

        try {
            Map<String, String> userInfoMap = new HashMap<String, String>();
            File file = new File("/data/data/gordenyou.zhenxiong/userInfo.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String content = bufferedReader.readLine();
            String[] userinfo = content.split("##");
            userInfoMap.put("username", userinfo[0]);
            userInfoMap.put("companycode", userinfo[1]);
            fileInputStream.close();
            return userInfoMap;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
