package com.sumauto.habitat.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sumauto.habitat.R;
import com.sumauto.util.DisplayUtil;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/04/22 5.6.6 
 */
public class LoadingDialog extends DialogFragment{


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        Dialog dialog=new Dialog(context);
        int p= (int) DisplayUtil.dp(12,context.getResources());
        LinearLayout layout=new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(0x70000000);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(p*3,p,p*3,p);

        ImageView progressImage=new ImageView(context);
        progressImage.setBackgroundResource(R.drawable.loadding);
        ((AnimationDrawable) progressImage.getBackground()).run();

        TextView textView=new TextView(context);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(12);
        textView.setText("加载中...");

        layout.addView(progressImage);
        layout.addView(textView);

        ((ViewGroup.MarginLayoutParams) textView.getLayoutParams()).topMargin=p/2;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        dialog.setContentView(layout);
        return dialog;
    }

}
