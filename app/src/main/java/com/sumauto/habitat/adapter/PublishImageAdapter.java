package com.sumauto.habitat.adapter;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.common.util.ImageUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.holders.DeleteAbleImageHolder;
import com.sumauto.habitat.utils.ImageOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln on 16/3/24.
 * 可删除图片列表
 */
public class PublishImageAdapter extends RecyclerView.Adapter<DeleteAbleImageHolder>{
    public static final int MAX_IMAGES = 8;
    private final ItemClickListener mListener;
    private List<Uri> images=new ArrayList<>();

    public PublishImageAdapter(ItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public DeleteAbleImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DeleteAbleImageHolder(parent);
    }

    @Override
    public void onBindViewHolder(final DeleteAbleImageHolder holder, int position) {
        if (position>=images.size()){
            holder.cover.setVisibility(View.INVISIBLE);
            holder.iv_image.setImageResource(R.drawable.add_image);
            holder.iv_delete.setVisibility(View.GONE);
            holder.iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onAddImage();
                }
            });
        }else{
            holder.iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=holder.getAdapterPosition();

                    Uri remove = images.remove(position);
                    images.add(0,remove);
                    notifyItemMoved(position, 0);
                    notifyItemChanged(0);
                    notifyItemChanged(1);
                }
            });
            holder.cover.setVisibility(position == 0 ? View.VISIBLE : View.INVISIBLE);
            ImageLoader.getInstance().displayImage(images.get(position).toString(),holder.iv_image, ImageOptions.options());
            holder.iv_delete.setVisibility(View.VISIBLE);
            holder.iv_delete.setTag(images.get(position));
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object tag = v.getTag();
                    if (tag!=null){
                        images.remove(tag);
                        v.setTag(null);//防止快速点击导致重复删除
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int size = images.size();
        if (size>=MAX_IMAGES){
            return MAX_IMAGES;
        }else{
            return size+1;
        }
    }

    public void addImage(Uri path){
       if (images.size()< MAX_IMAGES){
           images.add(path);
           if (images.size()==MAX_IMAGES){
               notifyItemChanged(images.size()-1);
           }else{
               notifyItemInserted(images.size()-1);
           }
       }
    }

    public interface ItemClickListener{
        void onAddImage();
    }
}
