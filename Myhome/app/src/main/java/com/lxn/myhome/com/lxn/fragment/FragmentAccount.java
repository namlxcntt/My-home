package com.lxn.myhome.com.lxn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;
import com.lxn.myhome.R;
import com.lxn.myhome.com.lxn.DetailAuthor;
import com.lxn.myhome.com.lxn.model.Account;
import com.lxn.myhome.com.lxn.util.AccountUtil;
import com.lxn.myhome.com.lxn.view.LoginActivity;

public class FragmentAccount extends Fragment {
    private Button btn_dangxuat;
    private TextView tv_User;
    private TextView tv_Email;
    private TextView tv_SDT,tv_userchoose;
    private  View view;
    private Button btn_detail;

    public FragmentAccount() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_account, container, false);

        initView();
        setData();

        return view;


    }

    private void setData() {

        Account account = AccountUtil.getAccount();
        tv_User.setText(account.getUsername());
        tv_Email.setText(account.getEmail());
        tv_SDT.setText(account.getSdt());
        if (account.isUser()== true){
            tv_userchoose.setText("Thuê phòng ");
        }
        else {
            tv_userchoose.setText("Cho thuê");
        }

        btn_dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountUtil.Logout();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(),btn_detail);
                popupMenu.getMenuInflater().inflate(R.menu.menu_account,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                       switch (menuItem.getItemId()){
                           case R.id.about:
                               Intent intent = new Intent(getContext(), DetailAuthor.class);
                               startActivity(intent);

                               break;
                       }
                       return false;
                    }
                });

            }
        });



    }

    private void initView() {
        tv_User = view.findViewById(R.id.tv_user);
        tv_Email = view.findViewById(R.id.tv_email);
        tv_SDT = view.findViewById(R.id.tv_SDT);
        btn_dangxuat=view.findViewById(R.id.btn_dangxuat);
        btn_detail = view.findViewById(R.id.btn_detail);
        tv_userchoose=view.findViewById(R.id.tv_userchoose);


    }




}
