package com.lxn.myhome.com.lxn.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lxn.myhome.R;
import com.lxn.myhome.com.lxn.Constants;
import com.lxn.myhome.com.lxn.base.BaseApplication;
import com.lxn.myhome.com.lxn.model.Home;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class Add_home_2 extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;

    private ImageView img;
    public Uri imguri;
    private Home home;
    private ProgressDialog dialog;

    private ArrayList<String> mArrayImagePath = new ArrayList<>();
    private ArrayList<String> mArrayImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_home_2);

        initView();
        getIntentData();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Đang tải lên ảnh");
        dialog.setCancelable(false);
    }

    private void getIntentData() {
        if (getIntent() != null) {
            home = (Home) getIntent().getSerializableExtra("home");
        }
    }

    private void initView() {
        img         = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null){
            mArrayImagePath = data.getStringArrayListExtra(Constants.KEY_PICK_IMAGE);

            Glide.with(getApplicationContext())
                    .load(mArrayImagePath.get(0))
                    .into(img);
            dialog.show();

            uploadImage();
        }
    }

    private void uploadImage() {
        for (int i=0; i < mArrayImagePath.size(); i ++){

            InputStream inputStream;

            try {
                inputStream = new FileInputStream(mArrayImagePath.get(i));
            } catch (FileNotFoundException e) {
                BaseApplication.Toast("Đã xảy ra lỗi");
                return;
            }

            final StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/mountains.jpg")
                    .child(System.currentTimeMillis() + i + ".png");
            ref.putStream(inputStream).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        mArrayImages.add(downloadUri.toString());

                        if (mArrayImagePath.size() == mArrayImages.size()){
                            home.setImage(mArrayImages);
                            BaseApplication.Toast("Tải ảnh lên thành công");
                            dialog.cancel();
                        }
                    } else {
                        Log.d("uuuuuuuuuuuuu",task.getException().toString());
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "Tải lên ảnh thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void onClickChooseImage(View view) {
        startActivityForResult(new Intent(getApplicationContext(),PickMultiPhotoLibrary.class), REQUEST_CODE);
    }

    public void onClickUpload(View view) {
        long id = System.currentTimeMillis();

        home.setId(id);

        FirebaseDatabase.getInstance().getReference().child("Home").child(id + "").setValue(home,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(getApplicationContext(), "Đang bài thành công", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Đăng bài thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
