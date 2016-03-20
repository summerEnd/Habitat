package com.sumauto.widget.recycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

/**
 * Created by Lincoln on 15/6/18.
 * adapter基类
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<BaseHolder> {
    private Context        context;
    private LayoutInflater inflater;


    public BaseAdapter(@NonNull Context context) {
        this.context = context;
    }

    public LayoutInflater getInflater() {
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }
        return inflater;
    }


    public String str(int resId) {
        return getContext().getString(resId);
    }

    public String str(int resId, Object... args) {
        return getContext().getString(resId, args);
    }

    public Context getContext() {
        return context;
    }
}
