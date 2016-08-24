package com.example.dsxdsxdsx0.cookbook;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.dsxdsxdsx0.cookbook.application.CookApplication;
import com.example.dsxdsxdsx0.cookbook.util.FileUtil;
import com.example.dsxdsxdsx0.cookbook.util.ProgressDialogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dsxdsxdsx0 on 2016/8/15.
 *
 * 更新服务
 *
 * IntentService在执行onCreate操作的时候，内部开了一个线程，去你执行你的耗时操作
 * IntentService是通过Handler looper message的方式实现了一个多线程的操作，
 * 同时耗时操作也可以被这个线程管理和执行，同时不会产生ANR的情况。
 */
public class UpdateService extends IntentService {

    public static final String UPDATE_URL = "update_url";

    public static final String APK_LOCAL = "apk_local";

    private String appName = "cookbook.apk";

    public UpdateService() {
        super(UpdateService.class.getSimpleName());
    }

    public static void start(String url){
        Intent intent = new Intent(CookApplication.getContext(),UpdateService.class);
        intent.putExtra(UPDATE_URL,url);
        CookApplication.getContext().startService(intent);
    }

    /**
     * 处理网络连接和下载的耗时操作
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            URL url = new URL(intent.getStringExtra(UPDATE_URL));
            //创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            //获取文件大小
            int length = connection.getContentLength();
            //创建输出流
            InputStream is = connection.getInputStream();
            File file = new File(FileUtil.getAppCacheDir(CookApplication.getContext()));
            File apkFile = new File(file,appName);
            FileOutputStream fos = new FileOutputStream(apkFile);
            int count = 0;
            int progress;
            //缓存
            byte []buf = new byte[1024];
            while (true){
                int numRead = is.read(buf);
                count += numRead;
                //计算进度条位置
                progress = (int) (((float)count/length) * 100);
                Intent intent1 = new Intent("com.jiaji.cookbook.update_progress");
                if(numRead < 0){
                    //下载完成
                    ProgressDialogUtil.dismiss();
                    intent1.putExtra(APK_LOCAL, apkFile.getAbsoluteFile());
                    sendBroadcast(intent1);
                    break;
                }
                //写入文件
                fos.write(buf,0,numRead);
                //更新进度
                ProgressDialogUtil.setProgress(progress);
            }

            fos.close();
            is.close();
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
