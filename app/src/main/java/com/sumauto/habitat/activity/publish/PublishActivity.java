package com.sumauto.habitat.activity.publish;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sumauto.common.util.DisplayUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.PublishImageAdapter;
import com.sumauto.widget.recycler.ItemPaddingDecoration;

public class PublishActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);
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
    }
}
