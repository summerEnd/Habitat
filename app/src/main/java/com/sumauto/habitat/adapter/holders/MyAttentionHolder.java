package com.sumauto.habitat.adapter.holders;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.util.DisplayUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.AttentionBean;
import com.sumauto.widget.SupperLayout;
import com.sumauto.widget.SupperLayout.LayoutParams;
import com.sumauto.widget.recycler.WrapContentGridLayoutManager;

import java.util.List;

/**
 * Created by Lincoln on 16/3/23.
 * 我关注的
 */
public class MyAttentionHolder extends BaseViewHolder {

    public AttentionBean bean;
    private int itemPaddings;
    SupperLayout supperLayout;
    final int gap=5;
    public MyAttentionHolder(ViewGroup parent) {
        super(parent, R.layout.my_attention_item);
        supperLayout= (SupperLayout) itemView.findViewById(R.id.supperLayout);
    }


    public void init(AttentionBean bean) {
        this.bean = bean;
        List<String> images = bean.getImages();
        final int size=images.size();
        LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
        int itemSize = (supperLayout.getWidthUnits()-gap*4)/5;
        for (int i = 0; i < size; i++) {
            View view=inflater.inflate(R.layout.grid_item_my_attention_image,supperLayout,false);
            LayoutParams lp= (LayoutParams) view.getLayoutParams();
            int x=i%5*(itemSize+gap);
            int y=i/5*(itemSize+gap);
            lp.units(itemSize,itemSize);
            lp.position(x,y);
            supperLayout.addView(view);
        }
    }

    class ViewHolder extends BaseViewHolder {
        public ViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.grid_item_my_attention_image);
        }
    }

    class Adapter extends RecyclerView.Adapter<ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            if (bean == null || bean.getImages() == null)
                return 0;

            return bean.getImages().size();
        }
    }

}
