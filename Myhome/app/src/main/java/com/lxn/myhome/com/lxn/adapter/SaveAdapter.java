package com.lxn.myhome.com.lxn.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lxn.myhome.R;
import com.lxn.myhome.com.lxn.Constants;
import com.lxn.myhome.com.lxn.model.Home;
import com.lxn.myhome.com.lxn.view.DetailsHomeActivity;

import java.util.ArrayList;

public class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.ViewHolder> {
    ArrayList<Home> listHome;
    Context mContext;

    public SaveAdapter(ArrayList<Home> listHome, Context mContext) {
        this.listHome = listHome;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemview= inflater.inflate(R.layout.item_recycleview_save,parent,false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tv_name.setText(listHome.get(position).getDiachi());
        holder.tv_price.setText("Giá : "+listHome.get(position).getGiatien()+" VND");
        holder.tv_place.setText("TP  : "+listHome.get(position).getThanhpho());

        Glide.with(mContext)
                .load(listHome.get(position).getImage().get(0))
                .into(holder.imgAvarta);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailsHomeActivity.class);
                intent.putExtra(Constants.KEY_INTENT,listHome.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listHome.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvarta;

        TextView tv_name,tv_place,tv_price;
        View view ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tv_name=(TextView) itemView.findViewById(R.id.tv_name);
            tv_place=(TextView) itemView.findViewById(R.id.tv_place);
            tv_price=(TextView) itemView.findViewById(R.id.tv_price);
            imgAvarta=(ImageView) itemView.findViewById(R.id.img_avarta);
        }
    }
}
