package com.example.dsxdsxdsx0.cookbook.util;

import android.content.Context;
import android.os.Environment;

/**
 * Created by dsxdsxdsx0 on 2016/8/15.
 *
 * 文件管理类
 */
public class FileUtil {

    public static String getAppCacheDir(Context context){
        String cachePath = null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                ||!Environment.isExternalStorageRemovable()){
            cachePath = context.getExternalCacheDir().getPath();
        }else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

}
