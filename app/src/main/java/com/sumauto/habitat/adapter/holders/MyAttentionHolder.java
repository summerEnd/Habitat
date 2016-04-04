package com.sumauto.habitat.adapter.holders;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.common.util.DisplayUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.AttentionBean;
import com.sumauto.widget.recycler.WrapContentGridLayoutManager;

/**
 * Created by Lincoln on 16/3/23.
 * 我关注的
 */
public class MyAttentionHolder extends BaseViewHolder {

    public final RecyclerView rv_images;
    public AttentionBean bean;
    private int itemPaddings;

    public MyAttentionHolder(ViewGroup parent) {
        super(parent, R.layout.my_attention_item);
        rv_images = (RecyclerView) itemView.findViewById(R.id.rv_images);

        itemPaddings = (int) DisplayUtil.dp(3,itemView.getResources());

        WrapContentGridLayoutManager manager = new WrapContentGridLayoutManager(parent.getContext(), 5);
        manager.setVerticalSpacing(itemPaddings*2);
        rv_images.setLayoutManager(manager);
        rv_images.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(itemPaddings, itemPaddings, itemPaddings, itemPaddings);
            }
        });
        rv_images.setAdapter(new Adapter());
    }


    public void init(AttentionBean bean) {
        this.bean = bean;
        rv_images.getAdapter().notifyDataSetChanged();
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
