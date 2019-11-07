package com.lxn.myhome.com.lxn.util;

import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import com.lxn.myhome.com.lxn.base.BaseApplication;

import static android.content.Context.WINDOW_SERVICE;

public class ScreenUtil {

    private static int mWidth;
    private static int mHeight;
    private static float mDensity;

    public static int getWidth(){
        if(mWidth == 0) {
            Point size = new Point();
            WindowManager mWindowManager = (WindowManager) BaseApplication.getContext().getSystemService(WINDOW_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mWindowManager.getDefaultDisplay().getSize(size);
                mWidth = size.x;
                mHeight = size.y;
            } else {
                Display d = mWindowManager.getDefaultDisplay();
                mWidth = d.getWidth();
                mHeight = d.getHeight();
            }
        }
        return mWidth;
    }
    public static int getHeight(){
        if(mHeight == 0) {
            Point size = new Point();
            WindowManager mWindowManager = (WindowManager) BaseApplication.getContext().getSystemService(WINDOW_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mWindowManager.getDefaultDisplay().getSize(size);
                mWidth = size.x;
                mHeight = size.y;
            } else {
                Display d = mWindowManager.getDefaultDisplay();
                mWidth = d.getWidth();
                mHeight = d.getHeight();
            }
        }
        return mHeight;
    }

    public static float getDensity(){
        if (mDensity == 0){
            mDensity = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        }
        return mDensity;
    }
}
