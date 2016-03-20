package com.sumauto.widget.recycler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    Object data;

    public BaseHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    public View findViewById(int id) {
        return itemView == null?null:itemView.findViewById(id);
    }

    @Override
    public void onClick(View v) {

    }

    public final Object getData() {
        return data;
    }

    /**
     * @param data 用来展示的数据
     */
    public final void setData(Object data) {
        this.data = data;
        onInit(data);
    }

    /**
     * 调用setData后触发这个方法，在这里进行初始化
     */
    protected void onInit(Object data) {

    }
}
