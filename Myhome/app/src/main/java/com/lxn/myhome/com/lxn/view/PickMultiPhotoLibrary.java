package com.lxn.myhome.com.lxn.view;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxn.myhome.R;
import com.lxn.myhome.com.lxn.Constants;
import com.lxn.myhome.com.lxn.adapter.PickMultiPhotoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class PickMultiPhotoLibrary extends AppCompatActivity {

    private RecyclerView recyc_PickImage;
    private PickMultiPhotoAdapter mPickMultiPhotoAdapter;
    private ImageView mDone;
    private TextView mCountImage;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_multi_photo_library);

        initView();
        createToolbar();
        createPhotoLibrary();
        onAmountPickCallback();
    }

    private void onAmountPickCallback() {
        mPickMultiPhotoAdapter.setPickedListener(new PickMultiPhotoAdapter.NumberPickedListener() {
            @Override
            public void onNumber(int number) {
                mCountImage.setText(number+"/8");
                if (number>0){
                    mDone.setVisibility(View.VISIBLE);
                }else {
                    mDone.setVisibility(View.GONE);
                }
            }
        });
    }

    private void createPhotoLibrary() {
        recyc_PickImage.setHasFixedSize(true);
        recyc_PickImage.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        mPickMultiPhotoAdapter = new PickMultiPhotoAdapter(getFilePaths());
        recyc_PickImage.setAdapter(mPickMultiPhotoAdapter);
    }

    private void createToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
    }

    public void  viewDone(View view){
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_PICK_IMAGE, mPickMultiPhotoAdapter.mListImagePicked());
        setResult(RESULT_OK,intent);
        finish();
    }

    private void initView() {
        recyc_PickImage = findViewById(R.id.recyc_PickImage);
        mDone = findViewById(R.id.mDone);
        mCountImage = findViewById(R.id.mCountImage);
        toolbar = findViewById(R.id.toolbar);
    }

    private ArrayList<String> getFilePaths() {
        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<>();
        ArrayList<String> resultIAV = new ArrayList<>();

        String[] directories = null;
        if (u != null) {
            c = managedQuery(u, projection, null, null, null);
        }

        if ((c != null) && (c.moveToFirst())) {
            do {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try{
                    dirList.add(tempDir);
                }
                catch(Exception e) {
                }
            } while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);
        }
        for(int i=0;i<dirList.size();i++) {
            File imageDir = new File(directories[i]);
            File[] imageList = imageDir.listFiles();
            if(imageList == null)
                continue;
            for (File imagePath : imageList) {
                try {
                    if(imagePath.isDirectory()) {
                        imageList = imagePath.listFiles();
                    }
                    if (       imagePath.getName().contains(".jpg") || imagePath.getName().contains(".JPG")
                            || imagePath.getName().contains(".jpeg")|| imagePath.getName().contains(".JPEG")
                            || imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")
                            || imagePath.getName().contains(".gif") || imagePath.getName().contains(".GIF")
                            || imagePath.getName().contains(".bmp") || imagePath.getName().contains(".BMP")) {
                        String path= imagePath.getAbsolutePath();
                        resultIAV.add(path);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resultIAV;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
