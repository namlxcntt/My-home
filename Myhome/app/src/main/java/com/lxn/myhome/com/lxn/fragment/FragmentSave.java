package com.lxn.myhome.com.lxn.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.lxn.myhome.R;
import com.lxn.myhome.com.lxn.adapter.SaveAdapter;
import com.lxn.myhome.com.lxn.model.Home;
import com.lxn.myhome.com.lxn.util.AccountUtil;
import com.lxn.myhome.com.lxn.view.Add_home;

import java.util.ArrayList;

public class FragmentSave extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Home> listhome = new ArrayList<>();;
    private SaveAdapter saveAdapter;
    private Button btn_addhome;

    public FragmentSave() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_save, container, false);
        checkPermision();
        initView();
        createHomeList();
        getHomeList();
        addhome();
        return view;
    }

    private void checkPermision() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {   // Check permision
            Toast.makeText(getContext(), "Please Grant Permision", Toast.LENGTH_LONG).show();
            requestPermissions();
        }
        if ( ActivityCompat.checkSelfPermission( getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            Toast.makeText(getContext(), "Please Grant Permision", Toast.LENGTH_LONG).show();
            requestPermissions();

        }

    }

    private void addhome() {
        btn_addhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AccountUtil.getAccount().isUser()){
                    btn_addhome.setVisibility(View.GONE);
                }
                btn_addhome = view.findViewById(R.id.btn_addhome);
                btn_addhome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), Add_home.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void getHomeList() {
        FirebaseDatabase.getInstance().getReference().child("Home")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.exists()){

                            Home home =dataSnapshot.getValue(Home.class);

                            if (home.getId_account().equals(AccountUtil.getAccount().getEmail())){
                                listhome.add(home);
                                saveAdapter.notifyItemInserted(listhome.size() - 1);
                            }
                        }
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }


    private void createHomeList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setLayoutManager(linearLayoutManager);
        saveAdapter = new SaveAdapter(listhome, getActivity());
        recyclerView.setAdapter(saveAdapter);
    }


    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_save);
        btn_addhome = (Button) view.findViewById(R.id.btn_addhome);
    }
    private void requestPermissions() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION}, 1); // Permission

    }
}
