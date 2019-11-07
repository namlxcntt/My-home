package com.lxn.myhome.com.lxn.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lxn.myhome.R;
import com.lxn.myhome.com.lxn.base.BaseApplication;
import com.lxn.myhome.com.lxn.model.Account;
import com.lxn.myhome.com.lxn.util.AccountUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {


    private Spinner spinner;
    private ArrayList arrayList;

    private TextInputLayout textInputUser, textInputPass, textInputEmail, textInputPhone;
    private EditText edt_username, edt_password, edt_email, edt_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);
        initView();
        chooseLogin();

    }

    public void onClickLogin(View view) {
        String txt_username = edt_username.getText().toString().toLowerCase().trim();
        String txt_email = edt_email.getText().toString().trim();
        String txt_password = edt_password.getText().toString().trim();
        String txt_sdt = edt_phone.getText().toString().trim();
        boolean checkuser;


        if (TextUtils.isEmpty(txt_username)) {

            BaseApplication.Toast("Vui lòng điền tên tài khoản");
            return;
        }

        if (TextUtils.isEmpty(txt_email)) {

            BaseApplication.Toast("Vui lòng điền email");
            return;
        }

        if (TextUtils.isEmpty(txt_password)) {

            BaseApplication.Toast("Vui lòng điền mật khẩu");
            return;
        }
        if (TextUtils.isEmpty(txt_sdt)) {

            BaseApplication.Toast("Vui lòng điền Số điện thoại");
            return;
        }
        if (spinner.getSelectedItem().toString().equals("Bạn Muốn Tìm và thuê Nhà ")) {
            checkuser = true;

        } else {
            checkuser = false;
        }


        register(txt_username, txt_email, txt_password, txt_sdt, checkuser);

    }

    private void register(final String username, final String email, final String password, final String sdt, final boolean user) {
        FirebaseDatabase.getInstance().getReference().child("Account")
                .child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(getApplicationContext(), "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        } else {
                            // Tài khoản chưa tồn tại sẽ đăng ký

                            final Account account = new Account();
                            account.setUsername(username);
                            account.setPassword(password);
                            account.setEmail(email);
                            account.setSdt(sdt);
                            if (spinner.getSelectedItem().toString().equals("Bạn Muốn Tìm và thuê Nhà ")) {
                                account.setUser(true);

                            } else {
                                account.setUser(false);
                            }


                            FirebaseDatabase.getInstance().getReference().child("Account")
                                    .child(username)
                                    .setValue(account)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                AccountUtil.setAccount(account);

                                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    public void chooseLogin() {
        ArrayList<String> chooseLogin = new ArrayList<>();
        chooseLogin.add("Bạn Muốn cho Thuê Nhà ");
        chooseLogin.add("Bạn Muốn Tìm và thuê Nhà ");
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, chooseLogin);
        spinner.setAdapter(arrayAdapter);


    }

    private void initView() {


        textInputUser = findViewById(R.id.textInputUser);

        textInputPass = findViewById(R.id.textInputPass);
        textInputEmail = findViewById(R.id.textInputEmail);
        textInputPhone = findViewById(R.id.textInputPhone);

        edt_username = findViewById(R.id.edt_Usernamer);
        edt_password = findViewById(R.id.edt_PasswordLoginr);
        edt_email = findViewById(R.id.edt_Emailr);
        edt_phone = findViewById(R.id.edt_Phoner);


        spinner = findViewById(R.id.spinner);
    }
}
