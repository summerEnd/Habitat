package com.sumauto.habitat.adapter.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sumauto.common.util.ContextUtil;
import com.sumauto.common.util.ViewUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.trend.TrendDetailActivity;
import com.sumauto.habitat.bean.FeedBean;

/**
 * Created by Lincoln on 16/3/22.
 */
public class TrendHolder extends BaseViewHolder implements View.OnClickListener{
    public final TextView tv_content;
    public final ImageView iv_avatar, iv_image, iv_heart, iv_comment, iv_collect, iv_more;
    private FeedBean feedBean;

    public TrendHolder(View view) {
        super(view);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        iv_image = (ImageView) view.findViewById(R.id.iv_image);
        iv_heart = (ImageView) view.findViewById(R.id.iv_heart);
        iv_comment = (ImageView) view.findViewById(R.id.iv_comment);
        iv_collect = (ImageView) view.findViewById(R.id.iv_collect);
        iv_more = (ImageView) view.findViewById(R.id.iv_more);

    }

    public void init(FeedBean bean){
        this.feedBean=bean;
        ViewUtil.registerOnClickListener(this, itemView,iv_image, iv_heart, iv_comment, iv_collect, iv_more);
        iv_heart.setImageResource(bean.isAttention()?R.mipmap.ic_heart_checked:R.mipmap.ic_heart);
        iv_collect.setImageResource(bean.isCollection()?R.mipmap.ic_collect_checked:R.mipmap.ic_collect);
    }

    @Override
    public void onClick(View v) {
        ContextUtil.toast(v.getContentDescription());
        if (v==itemView){
            startActivity(intentActivity(TrendDetailActivity.class));
            return;
        }
        switch (v.getId()){
            case R.id.iv_image:{

                break;
            }
            case R.id.iv_heart:{
                if (feedBean.isAttention()){
                    iv_heart.setImageResource(R.mipmap.ic_heart);
                    feedBean.setIsAttention(false);
                }else{
                    iv_heart.setImageResource(R.mipmap.ic_heart_checked);
                    feedBean.setIsAttention(true);
                }
                break;
            }
            case R.id.iv_comment:{
                break;
            }
            case R.id.iv_collect:{
                if (feedBean.isCollection()){
                    iv_collect.setImageResource(R.mipmap.ic_collect);
                    feedBean.setIsCollection(false);
                }else{
                    iv_collect.setImageResource(R.mipmap.ic_collect_checked);
                    feedBean.setIsCollection(true);
                }
                break;
            }
            case R.id.iv_more:{
                break;
            }

        }
    }
}
