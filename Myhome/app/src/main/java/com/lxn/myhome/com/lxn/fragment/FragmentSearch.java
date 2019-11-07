package com.lxn.myhome.com.lxn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.lxn.myhome.R;
import com.lxn.myhome.com.lxn.Constants;
import com.lxn.myhome.com.lxn.Find_around;
import com.lxn.myhome.com.lxn.model.Home;
import com.lxn.myhome.com.lxn.view.DetailsHomeActivity;
import com.lxn.myhome.com.lxn.view.GMapsActivity;

import java.util.ArrayList;

public class FragmentSearch extends Fragment {
    private AutoCompleteTextView tv_search;
    private TextView tv_timquanhday, tv_lichsu,tv_maps;
    private ListView lv_history;
    private View view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> strings;
    private ArrayList<Home> homes;
    private ArrayAdapter<String> arrayAdapterH;
    private ArrayList<String> history;
    private ArrayList<String> historykey;


    public FragmentSearch() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        initView();
        createSearch();
        getLocal();
        historySearch();

        tv_timquanhday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Find_around.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void historySearch() {
        arrayAdapterH = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, history);
        lv_history.setAdapter(arrayAdapterH);

        FirebaseDatabase.getInstance().getReference().child("search").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    history.add(dataSnapshot.getValue(Home.class).getDiachi());
                    historykey.add(dataSnapshot.getValue(Home.class).getId() + "");
                    arrayAdapterH.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        lv_history.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                Log.d("qqqqqqqqqqqqqqqqqq",position + "");

                FirebaseDatabase.getInstance().getReference().child("search").child(historykey.get(position)).removeValue();
                history.remove(position);
                arrayAdapterH.notifyDataSetChanged();
                return true;
            }
        });


    }

    private void getLocal() {
        FirebaseDatabase.getInstance().getReference().child("Home").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    Home home = dataSnapshot.getValue(Home.class);
                    strings.add(home.getDiachi());
                    homes.add(home);
                    arrayAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void createSearch() {
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, strings);
        tv_search.setAdapter(arrayAdapter);
        tv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FirebaseDatabase.getInstance().getReference().child("search").child(homes.get(i).getId() + "").setValue(homes.get(i));
                Intent intent = new Intent(getContext(), DetailsHomeActivity.class);
                intent.putExtra(Constants.KEY_INTENT, homes.get(i));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        tv_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GMapsActivity.class);

                startActivity(intent);
            }
        });
    }

    private void initView() {
        tv_search = view.findViewById(R.id.tv_search);
        tv_timquanhday = view.findViewById(R.id.tv_timquanhday);
        tv_lichsu = view.findViewById(R.id.tv_lichsusearch);
        lv_history = view.findViewById(R.id.lv_history);
        tv_maps = view.findViewById(R.id.tv_viewMap);
        strings = new ArrayList<>();
        homes = new ArrayList<>();
        history = new ArrayList<>();
        historykey = new ArrayList<>();


    }

}
