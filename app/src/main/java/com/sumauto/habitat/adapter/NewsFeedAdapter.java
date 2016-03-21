package com.sumauto.habitat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sumauto.common.util.ViewUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.FeedBean;

import java.util.ArrayList;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.ViewHolder> {

    private ArrayList<FeedBean> beans=new ArrayList<>();
    public NewsFeedAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView tv_content;
        public final ImageView iv_avatar, iv_image, iv_heart, iv_comment, iv_collect, iv_more;

        public ViewHolder(View view) {
            super(view);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            iv_heart = (ImageView) view.findViewById(R.id.iv_heart);
            iv_comment = (ImageView) view.findViewById(R.id.iv_comment);
            iv_collect = (ImageView) view.findViewById(R.id.iv_collect);
            iv_more = (ImageView) view.findViewById(R.id.iv_more);
            ViewUtil.registerOnClickListener(this, iv_image, iv_heart, iv_comment, iv_collect, iv_more);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_image:{
                    break;
                }
                case R.id.iv_heart:{
                    break;
                }
                case R.id.iv_comment:{
                    break;
                }
                case R.id.iv_collect:{
                    break;
                }
                case R.id.iv_more:{
                    break;
                }

            }
        }
    }
}
