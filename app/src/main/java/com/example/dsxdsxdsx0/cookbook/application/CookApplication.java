package com.example.dsxdsxdsx0.cookbook.application;

import android.app.Application;
import android.content.Context;

import im.fir.sdk.FIR;

/**
 * Created by dsxdsxdsx0 on 2016/8/11.
 */
public class CookApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this.getApplicationContext();
        FIR.init(context);
    }

    public static Context getContext() {
        return context;
    }
}
