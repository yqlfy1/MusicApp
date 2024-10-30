package com.app.music.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description: CommonUtils 工具类
 * @author:marker
 * @copyright:www.itfxq.cn
 * @email:2579692606@qq.com
 * @createTime 2020/12/29 15:48
 */
public class CommonUtils extends Activity {

    public static String formatTime(int length){
        Date date = new Date(length);
        //时间格式化工具
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String totalTime = sdf.format(date);
        return totalTime;
    }

    public static boolean isEmpty(String content){
        if(content == null || "".equals(content)){
            return true;
        }else{
            return false;
        }

    }

    //跳转方法--不传参数
    public static void navigateTo(Context from, Class<?> to){
        Intent intent=new Intent();
        intent.setClass(from, to);
        from.startActivity(intent);
    }
    //跳转方法--传递参数
    public static void navigateTo(Context from, Class<?> to, Bundle bundle){
        Intent intent=new Intent();
        intent.setClass(from, to);
        intent.putExtras(bundle);
        from.startActivity(intent);
    }




}
