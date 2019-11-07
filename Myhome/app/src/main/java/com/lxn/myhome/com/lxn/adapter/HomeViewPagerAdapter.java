package com.lxn.myhome.com.lxn.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lxn.myhome.com.lxn.fragment.FragmentAccount;
import com.lxn.myhome.com.lxn.fragment.FragmentMyHome;
import com.lxn.myhome.com.lxn.fragment.FragmentSave;
import com.lxn.myhome.com.lxn.fragment.FragmentSearch;
import com.lxn.myhome.com.lxn.util.AccountUtil;

public class HomeViewPagerAdapter extends FragmentPagerAdapter {

    private String[] title = new String[]{"My home", "Search","Account"};
    private String[] title2 = new String[]{"Save","Account"};

    public HomeViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(AccountUtil.getAccount().isUser()){
            switch (position) {
                case 0:
                    return new FragmentMyHome();
                case 1:
                    return new FragmentSearch();
                case 2:
                    return new FragmentAccount();
        }
        }

        else {
            switch (position) {
                case 0:
                    return new FragmentSave();
                case 1:
                    return new FragmentAccount();
            }

        }
        return null;
    }

    @Override
    public int getCount() {
        if (AccountUtil.getAccount().isUser()){
            return 3;
        }
        else {
            return 2;
        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (AccountUtil.getAccount().isUser()){
            return title[position];
        }
        else {
            return title2[position];
        }

    }
}
