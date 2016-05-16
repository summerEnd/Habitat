package com.sumauto.habitat.adapter.holders;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.AttentionBean;
import com.sumauto.habitat.bean.ImageBean;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.widget.SupperLayout;
import com.sumauto.widget.SupperLayout.LayoutParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln on 16/3/23.
 * 我关注的
 */
public class MyAttentionHolder extends BaseViewHolder {

    public AttentionBean bean;
    private final SupperLayout mSupperLayout;
    private final ImageView iv_avatar;
    private final TextView tv_time, tv_nick, tv_count;
    final int gap = 5;

    public MyAttentionHolder(ViewGroup parent) {
        super(parent, R.layout.my_attention_item);
        mSupperLayout = (SupperLayout) itemView.findViewById(R.id.supperLayout);
        iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        tv_nick = (TextView) itemView.findViewById(R.id.tv_nick);
        tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        tv_count = (TextView) itemView.findViewById(R.id.tv_count);
    }

    @Override
    protected void onInit(Object data) {
        AttentionBean bean = (AttentionBean) data;

        ImageLoader.getInstance().displayImage(bean.uimg,
                new ImageViewAware(iv_avatar), ImageOptions.options());

        tv_time.setText(bean.addtime);
        tv_nick.setText(bean.nickname);
        if (!TextUtils.isEmpty(bean.count)){
            tv_count.setText(String.format("最近浏览了 %s 篇文章",bean.count));
        }else{
            tv_count.setText("");
        }

        ArrayList<ImageBean> articleList = bean.articleList;
        final int size = articleList.size();
        LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
        int itemSize = (mSupperLayout.getWidthUnits() - gap * 4) / 5;

        for (int i = 0; i < size; i++) {

            ImageBean imageBean = articleList.get(i);
            ImageView view = (ImageView) inflater.inflate(
                    R.layout.grid_item_my_attention_image, mSupperLayout, false);

            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            int x = i % 5 * (itemSize + gap);
            int y = i / 5 * (itemSize + gap);

            lp.units(itemSize, itemSize);
            lp.position(x, y);
            ImageLoader.getInstance().displayImage(imageBean.img,
                    new ImageViewAware(view)
                    , ImageOptions.options());

            mSupperLayout.addView(view);

        }

    }

    public void init(AttentionBean bean) {
        this.bean = bean;
        List<String> images = bean.getImages();
        final int size = images.size();
        LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
        int itemSize = (mSupperLayout.getWidthUnits() - gap * 4) / 5;
        for (int i = 0; i < size; i++) {
            View view = inflater.inflate(R.layout.grid_item_my_attention_image, mSupperLayout, false);
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            int x = i % 5 * (itemSize + gap);
            int y = i / 5 * (itemSize + gap);
            lp.units(itemSize, itemSize);
            lp.position(x, y);
            mSupperLayout.addView(view);
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
