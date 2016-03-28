package com.sumauto.habitat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sumauto.habitat.adapter.holders.ImageViewHolder;

/**
 * Created by Lincoln on 16/3/28.
 * 用户照片
 */
public class UserPictureAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 17;
    }
}
