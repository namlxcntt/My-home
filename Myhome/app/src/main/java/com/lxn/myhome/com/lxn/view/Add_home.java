package com.lxn.myhome.com.lxn.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lxn.myhome.R;
import com.lxn.myhome.com.lxn.Map_activity;
import com.lxn.myhome.com.lxn.base.BaseApplication;
import com.lxn.myhome.com.lxn.model.Home;
import com.lxn.myhome.com.lxn.util.AccountUtil;
import com.lxn.myhome.com.lxn.util.GPSTracker;

import java.util.ArrayList;

public class Add_home extends AppCompatActivity {
    private Button btn_NextStep2;
    private EditText et_diachi;
    private EditText et_giatien;
    private EditText et_dientich, et_mota, et_sdt;
    private double latitude; // Vĩ độ
    private double longitude; // Kinh độ
    private ArrayList<String> spinnerlist;

    private Spinner spinner;
    private CheckBox checkBox_wifi, checkBox_bep, checkBox_vesinh, checkBox_choDexe, checkBox_thoigian, checkBox_dieuhoa, checkBox_nonglanh, checkBox_tuLanh;
    private ArrayList<String> mArrayTienIch = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_home);

        initView();


        btn_NextStep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Home home = new Home();
                if (et_diachi.getText().toString().length() > 0 || et_giatien.getText().toString().length() > 0 || et_dientich.getText().toString().length() > 0 || et_mota.getText().toString().length() > 0) {
                    home.setTienich(mArrayTienIch);
                    home.setDiachi(et_diachi.getText().toString());
                    home.setGiatien(Integer.valueOf(et_giatien.getText().toString()));
                    home.setDientich(Integer.valueOf(et_dientich.getText().toString()));
                    home.setThanhpho(spinner.getAdapter().getItem(0).toString());
                    home.setDescription(et_mota.getText().toString());
                    home.setLongitude(longitude);
                    home.setLatitude(latitude);
                    home.setTelephone(et_sdt.getText().toString());
                    home.setId_account(AccountUtil.getAccount().getEmail()); // id account

                    Intent intent = new Intent(Add_home.this, Add_home_2.class);
                    intent.putExtra("home", home);
                    startActivity(intent);
                } else {
                    BaseApplication.Toast("Ban Phai Nhap vao du du lieu");
                }
            }
        });
    }

    private void initView() {
        btn_NextStep2 = (Button) findViewById(R.id.btn_Nextstep);
        et_diachi = findViewById(R.id.et_diachi);
        et_dientich = findViewById(R.id.et_dientich);
        et_giatien = findViewById(R.id.et_giatien);
        et_mota = findViewById(R.id.et_mota);
        et_sdt = findViewById(R.id.et_sdt);

        spinner = findViewById(R.id.spinner);

        checkBox_wifi = findViewById(R.id.checkBoxwifi);
        checkBox_bep = findViewById(R.id.checkBoxcooker);
        checkBox_vesinh = findViewById(R.id.checkBoxtoilet);
        checkBox_choDexe = findViewById(R.id.checkBoxMotor);
        checkBox_thoigian = findViewById(R.id.checkBoxclock);
        checkBox_dieuhoa = findViewById(R.id.checkBoxAircondition);
        checkBox_nonglanh = findViewById(R.id.checkBoxWater);
        checkBox_tuLanh = findViewById(R.id.checkBoxFreeze);

        checkBox_wifi.setOnCheckedChangeListener(checkedChangeListener);
        checkBox_bep.setOnCheckedChangeListener(checkedChangeListener);
        checkBox_vesinh.setOnCheckedChangeListener(checkedChangeListener);
        checkBox_choDexe.setOnCheckedChangeListener(checkedChangeListener);
        checkBox_thoigian.setOnCheckedChangeListener(checkedChangeListener);
        checkBox_dieuhoa.setOnCheckedChangeListener(checkedChangeListener);
        checkBox_nonglanh.setOnCheckedChangeListener(checkedChangeListener);
        checkBox_tuLanh.setOnCheckedChangeListener(checkedChangeListener);
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (compoundButton.getId() == R.id.checkBoxwifi) {
                if (b) {
                    mArrayTienIch.add("Internet");
                } else {
                    mArrayTienIch.remove("Internet");
                }
            } else if (compoundButton.getId() == R.id.checkBoxcooker) {
                if (b) {
                    mArrayTienIch.add("Bếp");
                } else {
                    mArrayTienIch.remove("Bếp");
                }
            } else if (compoundButton.getId() == R.id.checkBoxtoilet) {
                if (b) {
                    mArrayTienIch.add("Vệ sinh");
                } else {
                    mArrayTienIch.remove("Vệ sinh");
                }

            } else if (compoundButton.getId() == R.id.checkBoxMotor) {
                if (b) {
                    mArrayTienIch.add("Để xe");
                } else {
                    mArrayTienIch.remove("Để xe");
                }

            } else if (compoundButton.getId() == R.id.checkBoxAircondition) {
                if (b) {
                    mArrayTienIch.add("Điều Hòa");
                } else {
                    mArrayTienIch.remove("Điều Hòa");
                }

            } else if (compoundButton.getId() == R.id.checkBoxWater) {
                if (b) {
                    mArrayTienIch.add("Nóng Lạnh");
                } else {
                    mArrayTienIch.remove("Nóng Lạnh");
                }

            } else if (compoundButton.getId() == R.id.checkBoxFreeze) {
                if (b) {
                    mArrayTienIch.add("Tủ Lạnh");
                } else {
                    mArrayTienIch.remove("Tủ Lạnh");
                }
            }
        }
    };

    public void btn_Local(View view) {
        final String[] items = {"Vị trí hiện tại", "Bản đồ "};


        new AlertDialog.Builder(this)
                .setTitle("Bạn muốn chọn địa chỉ ở : ")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                GPSTracker gps = new GPSTracker(getApplicationContext());
                                if (gps.canGetLocation()) {
                                    longitude = gps.getLongitude();
                                    latitude = gps.getLatitude();
                                    BaseApplication.Toast("Đã lấy được vị trí của bạn");
                                }
                                break;
                            case 1:
                                startActivityForResult(new Intent(getApplicationContext(), Map_activity.class), 222);
                                break;
                        }
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 222 && resultCode == RESULT_OK && data != null) {
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);
            et_diachi.setText(data.getStringExtra("nameAddress"));

            Log.d("namlxcntt", data.getStringExtra("nameAddress"));
            Log.d("namlxcntt", latitude + "");
            Log.d("namlxcntt", longitude + "");
        }


    }
}