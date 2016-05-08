package com.sumauto.habitat.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.sumauto.util.DisplayUtil;
import com.sumauto.habitat.R;

/**
 * Created by Lincoln on 16/3/23.
 * 评论窗口
 */
public class CommentWindow extends Dialog{
    EditText edit_comment;
    public CommentWindow(Context context) {
        super(context);
        setContentView(R.layout.window_comment);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.gravity= Gravity.BOTTOM;
        attributes.width= DisplayUtil.getScreenWidth(context);
        getWindow().setBackgroundDrawable(new ColorDrawable());
        edit_comment= (EditText) findViewById(R.id.edit_comment);
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCommit(edit_comment.getText().toString());
            }
        });
    }

    protected void onCommit(String text){
        //do noting
    }
}
