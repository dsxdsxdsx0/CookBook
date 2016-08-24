package com.example.dsxdsxdsx0.cookbook.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by dsxdsxdsx0 on 2016/8/15.
 */
public class ProgressDialogUtil {

    private static ProgressDialog pd;

    public static void show(Context context){
        //创建进度对话框
        pd = new ProgressDialog(context);
        pd.setMax(100);
        pd.setTitle("下载更新");
        pd.setMessage("进度...");
        pd.setCancelable(false);//设置进度框不能使用取消按钮
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);//设置进度框是否显示进度
        pd.show();
    }

    public static void setProgress(int progress){
        if(null == pd){
            return;
        }
        pd.setProgress(progress);
    }

        public static void dismiss(){
            pd.dismiss();
            pd = null;
        }

    public static void destory(){
        pd = null;
    }

}
