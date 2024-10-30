package com.app.music.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.app.music.model.UserEntity;

/**
 * 数据库工具类
 */
public class DBUtils extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    public DBUtils(Context context,String dbname,int version) {
        super(context, dbname, null, version);

    }
    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        if (!tableIsExist("t_users")){
            db.execSQL("create table t_users(id integer primary key autoincrement,username varchar(50),password varchar(50),email varchar(50),tel varchar(50),createTime varchar(50))");
        }

        if (!tableIsExist("t_songs")){
            db.execSQL("create table t_songs(id integer primary key autoincrement," +
                    "song varchar(255)," +
                    "singer varchar(255)," +
                    "album varchar(255)," +
                    "duration varchar(255)," +
                    "albumArt varchar(255)," +
                    "path varchar(255))");
        }

        if (!tableIsExist("t_mylikesongs")){
            db.execSQL("create table t_mylikesongs(id integer primary key autoincrement," +
                    "song varchar(255)," +
                    "singer varchar(255)," +
                    "album varchar(255)," +
                    "duration varchar(255)," +
                    "albumArt varchar(255)," +
                    "username varchar(255)," +
                    "path varchar(255))");
        }

    }
    //升级更新
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //判断表是否存在
    public boolean tableIsExist(String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            //db = SQLiteDatabase.openDatabase(this.mDBPath,null,SQLiteDatabase.OPEN_READONLY);
            String sql = "select count(*) as c from Sqlite_master where type ='table' and name ='" + tableName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
            result = false;
        }
        return result;
    }
    //得到数据库操作对象
    public static SQLiteDatabase  getDbHelper(Context context){
        DBUtils dbUtils = new DBUtils(context, ItFxqConstants.DBNAME, 1);
        SQLiteDatabase db = dbUtils.getReadableDatabase();
        return db;
    }

    /*保存用户信息
     * username:用户名
     * password:密码
     * isChecked:是否勾选保存密码
     *
     * */
    public static boolean saveInfo(UserEntity user , boolean isChecked){
        String info = user.getUsername()+"#"
                +user.getPassword()+"#"
                +user.getVip()+"#"
                +user.getEmail()+"#"
                +user.getTel()+"#"
                +isChecked;
        File file = new File("/data/data/"+ItFxqConstants.PACKNAME+"/info.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(info.getBytes());
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }

    /**获取登录用户名**/
    public static String getLoginUsername(){
        Map<String, String> map = readInfo();
        return "username";
    }

    /*读取用户信息*/
    public static Map<String,String> readInfo(){
        Map<String,String> map = null;
        File file = new File("/data/data/"+ItFxqConstants.PACKNAME+"/info.txt");
        if(!file.exists()){
            return map;
        }
        FileInputStream fis = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fis));
            String info = br.readLine();
            Log.e("SaveUserInfo",info);
            String[] split = info.split("#");
            map = new HashMap<String,String>();
            map.put("username",split[0]);//保存读取的用户名和密码到map中
            map.put("password",split[1]);
            map.put("vip",split[2]);
            map.put("email",split[3]);
            map.put("tel",split[4]);
            map.put("isChecked",split[5]);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return map;
        }
    }

    /**
     * 检查用户是否存在
     */
    public static boolean checkUserIsExits(Context context,String name){
        SQLiteDatabase db = DBUtils.getDbHelper(context);
        String sql = "select * from "+ItFxqConstants.USER_TABLE+" where username=?";
        Cursor cursor = db.rawQuery(sql, new String[]{name});
        if(cursor.getCount()>0){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

    /**
     * 用户登录
     */
    public static boolean userLogin(Context context,String name,String pwd){
        SQLiteDatabase db = DBUtils.getDbHelper(context);
        String sql = "select * from "+ItFxqConstants.USER_TABLE+" where username=? and password=?";
        Cursor cursor = db.rawQuery(sql, new String[]{name,pwd});
        if(cursor.getCount()>0){
            //存储到文件中
            if(cursor.moveToFirst()){
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String tel = cursor.getString(cursor.getColumnIndex("tel"));
                UserEntity user = new UserEntity();
                user.setUsername(name);
                user.setVip("1");
                user.setPassword(pwd);
                user.setEmail(email);
                user.setTel(tel);
                saveInfo(user,true);
            }
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }
}
