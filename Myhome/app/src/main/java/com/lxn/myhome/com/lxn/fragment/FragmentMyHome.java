package com.lxn.myhome.com.lxn.fragment;

import android.Manifest;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.lxn.myhome.R;
import com.lxn.myhome.com.lxn.adapter.MyhomeAdapter;
import com.lxn.myhome.com.lxn.model.Home;

import java.util.ArrayList;

public class FragmentMyHome extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Home> listhome = new ArrayList<>();
    ;
    private MyhomeAdapter myhomeAdapter;
    private Button btn_addHome;


    public FragmentMyHome() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myhome, container, false);

        initView();
        createHomeList();
        checkPermission();
        getHomeList();
        checkPermission();

        return view;
    }

    private void getHomeList() {
        FirebaseDatabase.getInstance().getReference().child("Home")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.exists()) {
                            listhome.add(dataSnapshot.getValue(Home.class));
                            myhomeAdapter.notifyItemInserted(listhome.size() - 1);
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

    public void filter(String search) {
        ArrayList<Home> arrayList = new ArrayList<>();
        for (Home home : listhome) {
            if (home.getThanhpho().equals(search)) {
                arrayList.add(home);

            }
        }
        myhomeAdapter.setNewData(arrayList);

    }

        private void checkPermission() {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {   // Check permision
                Toast.makeText(getContext(), "Please Grant Permision", Toast.LENGTH_LONG).show();
                requestPermissions();
            }
            if ( ActivityCompat.checkSelfPermission( getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                Toast.makeText(getContext(), "Please Grant Permision", Toast.LENGTH_LONG).show();

            }



    }

    private void createHomeList() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        myhomeAdapter = new MyhomeAdapter(listhome, getActivity());
        recyclerView.setAdapter(myhomeAdapter);
    }


    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    private void requestPermissions() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION }, 1); // Permission

    }
}
