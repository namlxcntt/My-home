package com.lxn.myhome.com.lxn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lxn.myhome.R;
import com.lxn.myhome.com.lxn.base.BaseApplication;
import com.lxn.myhome.com.lxn.util.ScreenUtil;

import java.util.ArrayList;

public class PickMultiPhotoAdapter extends RecyclerView.Adapter<PickMultiPhotoAdapter.ViewHolder>  {

    private ArrayList<String> mListItem;
    private ArrayList<String> mListImagePicked;
    private NumberPickedListener pickedListener;

    public PickMultiPhotoAdapter(ArrayList<String> mListItem) {
        this.mListItem      = mListItem;
        mListImagePicked    = new ArrayList<>();
    }

    public interface NumberPickedListener{
        void onNumber(int number);
    }

    public void setPickedListener(NumberPickedListener pickedListener) {
        this.pickedListener = pickedListener;
    }

    public ArrayList<String> mListImagePicked(){
        return mListImagePicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(BaseApplication.getContext()).inflate(R.layout.row_pick_multi_photo,null);
        return new ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        setSizeViewHolder(viewHolder);

        Glide.with(BaseApplication.getContext())
                .load(mListItem.get(i))
                .override((int)((ScreenUtil.getWidth() / 3) * ScreenUtil.getDensity()))
                .into(viewHolder.mImage);
//        viewHolder.container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(BaseApplication.getContext(), ImageViewerActivity.class);
//                intent.putExtra(Constant.KEY_IMAGE_ARRAYS,mListItem);
//                intent.putExtra(Constant.KEY_POSITION,i);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                BaseApplication.getContext().startActivity(intent);
//            }
//        });
        viewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (mListImagePicked.size()<=8){
                        mListImagePicked.add(mListItem.get(i));
                        pickedListener.onNumber(mListImagePicked.size());
                    }else {
                        viewHolder.mCheckBox.setChecked(false);
                    }
                }else {
                    mListImagePicked.remove(mListItem.get(i));
                    pickedListener.onNumber(mListImagePicked.size());
                }
            }
        });
    }

    private void setSizeViewHolder(ViewHolder viewHolder) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.getWidth()/3, ScreenUtil.getWidth()/3);
        viewHolder.container.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView   mImage;
        FrameLayout container;
        CheckBox    mCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage      = itemView.findViewById(R.id.mImage);
            container   = itemView.findViewById(R.id.container);
            mCheckBox   = itemView.findViewById(R.id.mCheckBox);
        }
    }
}
