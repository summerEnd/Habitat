package com.sumauto.habitat.activity.publish;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.sumauto.common.util.DisplayUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.adapter.PublishImageAdapter;
import com.sumauto.habitat.widget.IosListDialog;

public class PublishActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private TextView tv_who_can_see;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);
        initToolBar(R.id.toolBar);
        tv_who_can_see= (TextView) findViewById(R.id.tv_who_can_see);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4) {
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                //super.onMeasure(recycler, state, widthSpec, heightSpec);
                View child = null;
                try {
                    child = recycler.getViewForPosition(0);
                } catch (Exception e) {
                    //运行删除动画时，0这个位置偏移量为－1，会抛出异常
                    child = recycler.getViewForPosition(1);
                }
                if (child != null) {
                    measureChild(child, widthSpec, heightSpec);
                    //后面加3为了缩小由于误差带来的错误
                    int itemHeight = child.getMeasuredHeight() + (int) DisplayUtil.dp(5, getResources()) / 2 + 3;
                    int count = getItemCount();

                    setMeasuredDimension(
                            View.MeasureSpec.getSize(widthSpec), count <= 4
                                    ? itemHeight
                                    : itemHeight * 2);
                }

            }
        });
        recyclerView.setAdapter(new PublishImageAdapter());

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int size = (int) DisplayUtil.dp(5, getResources()) / 2;

                outRect.set(size, size, size, size);
            }
        });

        findViewById(R.id.v_notice_who_see).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PublishActivity.this,NoticeFriendListActivity.class));
            }
        });

        findViewById(R.id.v_who_can_see).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IosListDialog(PublishActivity.this)
                        .listener(new IosListDialog.Listener() {
                            @Override
                            public void onClick(IosListDialog dialog, int position) {
                                switch (position) {
                                    case 0: {
                                        tv_who_can_see.setText(R.string.anybody_can_see);
                                        break;
                                    }
                                    case 1: {
                                        tv_who_can_see.setText(R.string.only_self_see);
                                        break;
                                    }
                                    case 2: {
                                        tv_who_can_see.setText(R.string.partly_can_see);
                                        break;
                                    }
                                }
                            }
                        })
                        .show(R.string.anybody_can_see, R.string.only_self_see, R.string.partly_can_see);
            }
        });
    }

    private class WhoCanSee extends Dialog implements View.OnClickListener {
        public WhoCanSee(Context context) {
            super(context);
            setContentView(R.layout.window_notice_who_see);
            getWindow().getAttributes().gravity = Gravity.BOTTOM;
            getWindow().getAttributes().width = DisplayUtil.getScreenWidth(context);
            this.getWindow().setBackgroundDrawable(new ColorDrawable());
            findViewById(R.id.anybody_can_see).setOnClickListener(this);
            findViewById(R.id.only_self_see).setOnClickListener(this);
            findViewById(R.id.partly_can_see).setOnClickListener(this);
            findViewById(R.id.partly_can_see).setOnClickListener(this);
            findViewById(R.id.tv_cancel).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            dismiss();

            switch (v.getId()) {
                case R.id.anybody_can_see: {
                    tv_who_can_see.setText(R.string.anybody_can_see);
                    break;
                }
                case R.id.only_self_see: {
                    tv_who_can_see.setText(R.string.only_self_see);
                    break;
                }
                case R.id.partly_can_see: {
                    tv_who_can_see.setText(R.string.partly_can_see);
                    break;
                }
                case R.id.tv_cancel: {
                    break;
                }
            }
        }
    }

}
