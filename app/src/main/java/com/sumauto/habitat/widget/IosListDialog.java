package com.sumauto.habitat.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sumauto.util.DisplayUtil;
import com.sumauto.habitat.R;

/**
 * Created by Lincoln on 16/3/29.
 * 仿ios 弹框
 */
public class IosListDialog extends Dialog implements View.OnClickListener{
    private final ViewGroup layout_items;
    private Listener listener;

    public IosListDialog(Context context) {
        super(context);
        setContentView(R.layout.ios_dialog);
        layout_items = (ViewGroup) findViewById(R.id.layout_items);
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().getAttributes().width = DisplayUtil.getScreenWidth(context);
        this.getWindow().setBackgroundDrawable(new ColorDrawable());
    }

    public void show(CharSequence...items) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        for (CharSequence text : items) {
            TextView textView = (TextView) layoutInflater.inflate(R.layout.ios_dialog_item, layout_items, false);
            textView.setOnClickListener(this);
            textView.setText(text);
            layout_items.addView(textView);
        }

        show();
    }

    public void show(int... strRes) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        for (int text : strRes) {
            TextView textView = (TextView) layoutInflater.inflate(R.layout.ios_dialog_item, layout_items, false);
            textView.setOnClickListener(this);
            textView.setText(text);
            layout_items.addView(textView);
        }
        show();
    }

    public IosListDialog listener(Listener listener){
        this.listener=listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (listener!=null){
            int position=-1;
            int count=layout_items.getChildCount();
            for (int i = 0; i < count; i++) {
                if (v==layout_items.getChildAt(i)){
                    position=i;
                }
            }
            if (position>=0)listener.onClick(this,position);
        }
    }

    public interface Listener{
        void onClick(IosListDialog dialog,int position);
    }

}
