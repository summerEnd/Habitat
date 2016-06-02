package com.sumauto.habitat.callback;

import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.TextView;

import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.LoginActivity;
import com.sumauto.util.ToastUtil;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/05/15 5.6.6 
 */
public class EmptyDecoration extends RecyclerView.ItemDecoration {

    private View emptyView;
    private boolean isMeasured = false;
    protected ImageView iv_empty;
    protected TextView tv_empty;
    protected View btn_login;

    public EmptyDecoration(View emptyView) {
        this.emptyView = emptyView;
    }

    public EmptyDecoration() {
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        createEmptyViewIfNeed(parent);
        //emptyView.draw(c);
        if (emptyView.getVisibility() == View.VISIBLE)
            parent.drawChild(c, emptyView, parent.getDrawingTime());
    }

    private void createEmptyViewIfNeed(RecyclerView parent) {

        if (emptyView == null) {
            emptyView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.empty_image, parent, false);
            iv_empty = (ImageView) emptyView.findViewById(R.id.iv_empty);
            tv_empty = (TextView) emptyView.findViewById(R.id.tv_empty);
            btn_login = emptyView.findViewById(R.id.tv_login);
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(new Intent(v.getContext(), LoginActivity.class));
                    ToastUtil.toast(v.getContext(), "click");
                }
            });
            parent.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (emptyView.getVisibility() == View.VISIBLE) {
                        if (btn_login.getVisibility() == View.VISIBLE) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                btn_login.performClick();
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });
        }


        if (!isMeasured) {
            emptyView.measure(
                    MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(parent.getMeasuredHeight(), MeasureSpec.EXACTLY)
            );
            emptyView.layout(0, 0, parent.getWidth(), parent.getHeight());
            isMeasured = true;
        }

        if (!HabitatApp.getInstance().isLogin()) {
            emptyView.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.VISIBLE);
            iv_empty.setImageResource(R.mipmap.image_empty_need_login);
            tv_empty.setText("您还没有登录，请登录");
        } else {
            parent.setOnClickListener(null);
            btn_login.setVisibility(View.INVISIBLE);
            initEmptyView(emptyView);
        }

    }

    protected void initEmptyView(View emptyView) {

    }

}
