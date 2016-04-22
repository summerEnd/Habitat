package com.sumauto.habitat.adapter.holders;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.widget.recycler.adapter.BaseHolder;

/**
 * Created by Lincoln on 16/3/22.
 */

public class BaseViewHolder extends BaseHolder{

    private static View getView(ViewGroup parent,int resId){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding dataBinding = DataBindingUtil.inflate(layoutInflater, resId, parent, false);
        return dataBinding==null?layoutInflater.inflate(resId,parent,false):dataBinding.getRoot();
    }

    public BaseViewHolder(ViewGroup parent,int resId) {
        super(getView(parent,resId));
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
