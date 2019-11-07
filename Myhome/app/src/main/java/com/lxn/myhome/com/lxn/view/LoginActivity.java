package com.lxn.myhome.com.lxn.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static String TAG;
    private TextInputLayout textInputUser,textInputPass;
    private EditText edt_username,edt_password;
    private LoginButton login_button;
    private CallbackManager callbackManager;

    private TextInputLayout textInputUserup, textInputPassup, textInputEmailup, textInputPhoneup;
    private EditText edt_usernameup, edt_passwordup, edt_emailup, edt_phoneup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login_activity);


        if (Profile.getCurrentProfile() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        printHashKey(getApplicationContext());
        initView();
        initAccount();

        addLoginFB();
    }

    public static void printHashKey(Context pContext) { // Get hashcode in hear /
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.d("Tag", "printHashKey() Hash Key: " + hashKey); // Check Log in hashkey
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private void addLoginFB() { // Login with facebook
        login_button.setReadPermissions(Arrays.asList("public_profile"));
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {  // Call back
            @Override
            public void onSuccess(final LoginResult loginResult) {
                FirebaseDatabase.getInstance().getReference().child("Account")
                        .child(loginResult.getAccessToken().getUserId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    Account account = dataSnapshot.getValue(Account.class);

                                    AccountUtil.setAccount(account);

                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.alertdialog_update, null);
                                    builder.setView(view);
                                    builder.setCancelable(false);
                                    AlertDialog alertDialog = builder.create();
                                    final EditText edt_username = view.findViewById(R.id.edt_Usernameup);
                                    final EditText edt_phone = view.findViewById(R.id.edt_Phoneup);
                                    final EditText edt_emai = view.findViewById(R.id.edt_Emailup);
                                    final Spinner spinner = view.findViewById(R.id.spinnerup);
                                    ArrayList<String> arrayList = new ArrayList<>();
                                    arrayList.add("Bạn Muốn cho Thuê Nhà ");
                                    arrayList.add("Bạn Muốn Tìm và thuê Nhà ");
                                    ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                                    spinner.setAdapter(arrayAdapter);

                                    Button btn_capnhat = view.findViewById(R.id.btn_update);

                                    btn_capnhat.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {  // Setep 1
                                            if (edt_username.getText().toString().length() > 0 && edt_phone.getText().toString().length() > 0 && edt_emai.getText().toString().length() > 0) {
                                                Account account = new Account();
                                                account.setUsername(edt_username.getText().toString());
                                                account.setEmail(edt_emai.getText().toString());
                                                account.setSdt(edt_phone.getText().toString());
                                                if (spinner.getSelectedItem().toString().equals("Bạn Muốn Tìm và thuê Nhà ")) {
                                                    account.setUser(true);

                                                } else {
                                                    account.setUser(false);
                                                }

                                                FirebaseDatabase.getInstance().getReference().child("Account")
                                                        .child(loginResult.getAccessToken().getUserId())
                                                        .setValue(account);

                                                AccountUtil.setAccount(account);

                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                finish();
                                            } else {

                                                BaseApplication.Toast("BAN PHAI NHAP VAO DU DU LIEU");
                                            }
                                        }
                                    });


                                    alertDialog.show();

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initAccount() {
        if (AccountUtil.getAccount() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    private void initView() {
        textInputUser = findViewById(R.id.textInputUser);
        textInputPass= findViewById(R.id.textInputPass);

        edt_username = findViewById(R.id.edUsername);
        edt_password = findViewById(R.id.edPasswordLogin);

        login_button = findViewById(R.id.login_button);
    }


    public void onClickRegister(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }

    public void onClickLogin(View view) {
        String username = edt_username.getText().toString();
        final String password = edt_password.getText().toString();

        if (TextUtils.isEmpty(username)) {
            BaseApplication.Toast("Vui lòng nhập tên tài khoản");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            BaseApplication.Toast("Vui lòng nhập mật khẩu");
            return;
        }

        FirebaseDatabase.getInstance().getReference().child("Account")
                .child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            Account account = dataSnapshot.getValue(Account.class);

                            if (!account.getPassword().equals(password)) {
                                BaseApplication.Toast("Sai mật khẩu");
                                return;
                            }

                            AccountUtil.setAccount(account);

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        } else {
                            BaseApplication.Toast("Tài khoản không tồn tại");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}

