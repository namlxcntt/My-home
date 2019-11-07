package com.lxn.myhome.com.lxn.base;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class BaseApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
    }

    public static Context getContext(){
        return context;
    }

    public static void Toast(String content){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

}
