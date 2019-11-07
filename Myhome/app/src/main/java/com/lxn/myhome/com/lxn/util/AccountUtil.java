package com.lxn.myhome.com.lxn.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.lxn.myhome.com.lxn.base.BaseApplication;
import com.lxn.myhome.com.lxn.Constants;
import com.lxn.myhome.com.lxn.model.Account;

public class AccountUtil {

    public static void setAccount(Account account){
        SharedPreferences.Editor preferences = BaseApplication.getContext().getSharedPreferences("Account", Context.MODE_PRIVATE).edit();
        String jsonAccount = new Gson().toJson(account);
        preferences.putString(Constants.ACCOUNT,jsonAccount);
        preferences.apply();
    }

    public static Account getAccount(){
        SharedPreferences preferences = BaseApplication.getContext().getSharedPreferences("Account", Context.MODE_PRIVATE);

        String jsonAccount = preferences.getString(Constants.ACCOUNT,null);

        if (jsonAccount == null){
            return null;
        }

        return new Gson().fromJson(jsonAccount,Account.class);
    }
    public  static void Logout(){
        BaseApplication.getContext().getSharedPreferences("Account", Context.MODE_PRIVATE).edit().clear().apply();

    }
}
