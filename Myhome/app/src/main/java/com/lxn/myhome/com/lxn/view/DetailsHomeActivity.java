package com.lxn.myhome.com.lxn.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.lxn.myhome.R;
import com.lxn.myhome.com.lxn.Constants;
import com.lxn.myhome.com.lxn.model.Account;
import com.lxn.myhome.com.lxn.model.Home;
import com.lxn.myhome.com.lxn.util.AccountUtil;

public class DetailsHomeActivity extends AppCompatActivity {
    private TextView tv_diachi, tv_dientich, tv_thanhpho, tv_giatien, tv_tienich, tv_mota, tv_sdt;
    private Home home;
    private ViewFlipper viewFlipper;
    private Account account;
    private Button btn_call, btn_previous, btn_next;
    private ImageView img1, img2, img3;
    private Animation in, out;
    private Button btn_delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_home);

        initview();
        getIntentData();
        setData();


    }

    private void setData() {
        tv_diachi.setText(home.getDiachi());
        tv_dientich.setText(home.getDientich() + "m2");
        tv_thanhpho.setText(home.getThanhpho());
        tv_giatien.setText(home.getGiatien() + " VND");

        if (home.getImage().size() >= 1) {
            Glide.with(getApplicationContext())
                    .load(home.getImage().get(0))
                    .into(img1);
            viewFlipper.addView(img1);

        }

        if (home.getImage().size() >= 2) {
            Glide.with(getApplicationContext())
                    .load(home.getImage().get(1))
                    .into(img2);
            viewFlipper.addView(img2);
        }

        if (home.getImage().size() >= 3) {
            Glide.with(getApplicationContext())
                    .load(home.getImage().get(2))
                    .into(img3);
            viewFlipper.addView(img3);
        }

        viewFlipper.setInAnimation(in);
        viewFlipper.setOutAnimation(out);
        viewFlipper.setFlipInterval(000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewFlipper.isAutoStart()) {
                    viewFlipper.stopFlipping();
                    viewFlipper.showNext();
                    viewFlipper.startFlipping();
                    viewFlipper.setAutoStart(true);
                }
            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewFlipper.isAutoStart()) {
                    viewFlipper.stopFlipping();
                    viewFlipper.showPrevious();
                    viewFlipper.startFlipping();
                    viewFlipper.setAutoStart(true);
                }
            }
        });


        tv_sdt.setText(home.getTelephone());
        tv_mota.setText("- " + home.getDescription());

        for (int i = 0; i < home.getTienich().size(); i++) {

            tv_tienich.append("- " + home.getTienich().get(i) + "\n");
        }


        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallPerson();
            }
        });
        Log.d("xxxxxxxxxx",AccountUtil.getAccount().getEmail());
        Log.d("xxxxxxxxxx",home.getId_account());
        if (AccountUtil.getAccount().getEmail().equals(home.getId_account())){
            btn_delete.setVisibility(View.VISIBLE);

        }
        else {
            btn_delete.setVisibility(View.GONE);
        }

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsHomeActivity.this);
                builder.setTitle("Xóa");
                builder.setMessage("Bạn có muốn xóa không?");
                builder.setCancelable(false);
                builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(DetailsHomeActivity.this, "Chưa xóa ", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Có ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference()
                                .child("Home")
                                .child(home.getId()+"")
                                .setValue(null);
                        Toast.makeText(DetailsHomeActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

            }

        });
    }


    private void getIntentData() {
        if (getIntent() != null) {
            home = (Home) getIntent().getSerializableExtra(Constants.KEY_INTENT);
        }
    }

    private void initview() {
        tv_diachi = findViewById(R.id.tv_diachi);
        tv_dientich = findViewById(R.id.tv_dientich);
        tv_thanhpho = findViewById(R.id.tv_thanhpho);
        tv_giatien = findViewById(R.id.tv_giatien);
        tv_tienich = findViewById(R.id.tv_tienich);
        tv_mota = findViewById(R.id.tv_mota);
        tv_sdt = findViewById(R.id.tv_sdt);
        viewFlipper = findViewById(R.id.viewFliper);
        btn_delete = findViewById(R.id.btn_xoa);
        btn_call = findViewById(R.id.btn_call);
        img1 = new ImageView(this);
        img2 = new ImageView(this);
        img3 = new ImageView(this);
        btn_next = findViewById(R.id.btn_next);
        btn_previous = findViewById(R.id.btn_previous);
        in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        out = AnimationUtils.loadAnimation(this, R.anim.fade_out);

    }


    private void CallPerson() {
        int number = Integer.parseInt(home.getTelephone());
        Log.d("Nam", "" + number);
        Intent intentnumber = new Intent(Intent.ACTION_CALL);
        String uri = "tel:" + "0" + number;
        intentnumber.setData(Uri.parse(uri));
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {   // Check permision
            Toast.makeText(DetailsHomeActivity.this, "Please Grant Permision", Toast.LENGTH_LONG).show();
            requestPermissions();
        } else {
            startActivity(intentnumber);
        }
    }

    private void requestPermissions() {

        ActivityCompat.requestPermissions(DetailsHomeActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1); // Permission
    }

}
