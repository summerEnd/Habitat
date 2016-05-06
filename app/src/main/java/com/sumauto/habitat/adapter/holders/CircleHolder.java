package com.sumauto.habitat.adapter.holders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.CommitBean;
import com.sumauto.widget.CheckableLinearLayout;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/05/06 5.6.6 
 */
public class CircleHolder extends BaseViewHolder {
    private final TextView tv_circle, tv_address;
    private final CheckableLinearLayout btn_attention;
    private CommitBean commitBean;

    public CircleHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_circle);
        btn_attention = (CheckableLinearLayout) findViewById(R.id.btn_attention);
        tv_circle = (TextView) findViewById(R.id.tv_circle);
        tv_address = (TextView) findViewById(R.id.tv_address);
        btn_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onInit(Object data) {
        commitBean = (CommitBean) data;
        tv_circle.setText(commitBean.title);
        tv_address.setText(commitBean.getAddress());
    }
}
