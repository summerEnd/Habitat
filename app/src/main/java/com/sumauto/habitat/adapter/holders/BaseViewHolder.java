package com.sumauto.habitat.adapter.holders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.widget.recycler.adapter.BaseHolder;

/**
 * Created by Lincoln on 16/3/22.
 */
public class BaseViewHolder extends BaseHolder{
    public BaseViewHolder(ViewGroup parent,int resId) {
        super(LayoutInflater.from(parent.getContext()).inflate(resId,parent,false));
    }

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public Intent intentActivity(Class<? extends Activity> activity){
        return new Intent(itemView.getContext(),activity);
    }
    public void startActivity(Intent intent){
        itemView.getContext().startActivity(intent);
    }

}
