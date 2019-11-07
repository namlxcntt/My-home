package com.lxn.myhome.com.lxn.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lxn.myhome.R;
import com.lxn.myhome.com.lxn.adapter.HomeViewPagerAdapter;
import com.lxn.myhome.com.lxn.model.Home;
import com.lxn.myhome.com.lxn.util.AccountUtil;

import java.util.List;

;

public class MainActivity extends AppCompatActivity {
    private ViewPager vpHome;
    private HomeViewPagerAdapter homeViewPagerAdapter;
    private TabLayout tabHome;
    private List<Home> homes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabviewpager();

    }




    private void tabviewpager() { // Here writes about Tablayout and wallpaper



        vpHome = (ViewPager) findViewById(R.id.vp_home);
        tabHome = (TabLayout) findViewById(R.id.tab_home);

        if (AccountUtil.getAccount().isUser()){
            vpHome.setOffscreenPageLimit(3);
        }
        else {
            vpHome.setOffscreenPageLimit(2);
        }

        homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        vpHome.setAdapter(homeViewPagerAdapter);
        tabHome.setupWithViewPager(vpHome);


        if (AccountUtil.getAccount().isUser()){
            TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_custom_fonttab, null);
            tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home, 0, 0);
            tabHome.getTabAt(0).setCustomView(tabOne);
            tabOne.setText("Home");
            TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_custom_fonttab, null);
            tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_search, 0, 0);
            tabTwo.setText("Tìm Kiếm");
            tabHome.getTabAt(1).setCustomView(tabTwo);
            TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_custom_fonttab, null);
            tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_user, 0, 0);
            tabFour.setText("Tài Khoản");
            tabHome.getTabAt(2).setCustomView(tabFour);
        }
        else{
            TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_custom_fonttab, null);
            tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_save, 0, 0);
            tabThree.setText("Bạn đã đăng");
            tabHome.getTabAt(0).setCustomView(tabThree);
            TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_custom_fonttab, null);
            tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_user, 0, 0);
            tabFour.setText("Tài Khoản");
            tabHome.getTabAt(1).setCustomView(tabFour);

//        for (int i =0;i< tabHome.getTabCount();i++) { // Vòng lặp để get được ra các phần tử của tab lay out rồi set font chữ
//           TextView textfont=(TextView) LayoutInflater.from(this).inflate(R.layout.layout_custom_fonttab,null );
//           textfont.setText(vpHome.getAdapter().getPageTitle(i));
//           tabHome.getTabAt(i).setCustomView(textfont);
//
//        }
        }





    }
}
