package com.example.dsxdsxdsx0.cookbook.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.dsxdsxdsx0.cookbook.application.CookApplication;
import com.example.dsxdsxdsx0.cookbook.info.UpdateEntity;
import com.google.gson.Gson;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

/**
 * Created by dsxdsxdsx0 on 2016/8/11.
 */
public class Helper  {

    public Helper() {
    }

    public static void checkUpdate(){
        FIR.checkForUpdateInFIR("2dc0942063e5dff4f35dcd15495631c5", new VersionCheckCallback() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                UpdateEntity entity = new Gson().fromJson(s,UpdateEntity.class);
                String oldVersion = getVersionName(CookApplication.getContext());
                String newVersion = entity.getVersion();//获取版本号
                if(!oldVersion.equals(newVersion)){
                    //发送广播
                    Intent intent = new Intent("com.jiaji.cookbook.update");
                    intent.putExtra("update_entity",entity);
                    CookApplication.getContext().sendBroadcast(intent);
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
                Log.i("fir", "获取更新失败" + "\n" + e.getMessage());
            }

            @Override
            public void onStart() {
                super.onStart();
                Log.i("fir", "正在获取更新");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.i("fir", "成功获取更新");
            }
        });
    }

    public static void addCustomizeValue(String key,String value){
        FIR.addCustomizeValue(key,value);
    }

    public static void removeCustomizeValue(String key){
        FIR.removeCustomizeValue(key);
    }


    //获取版本名
    public static String getVersionName(Context context){
        return getPackageInfo(context).versionName;
    }

    //获取版本号
    public static int getVersionId(Context context){
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context){
        PackageInfo pInfo = null;
        try{
            PackageManager manager = context.getPackageManager();
            pInfo = manager.getPackageInfo(context.getPackageName(),PackageManager.GET_CONFIGURATIONS);
            return  pInfo;
        }catch (Exception e){
            e.printStackTrace();
        }
        return pInfo;

    }



}
