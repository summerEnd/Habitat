package com.sumauto.habitat.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.util.DisplayUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.PublishImageAdapter;
import com.sumauto.habitat.widget.IosListDialog;

import org.json.JSONException;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PublishActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private TextView tv_who_can_see;
    private PublishImageAdapter adapter;
    private int mImagePaddings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);
        initToolBar(R.id.toolBar);
        startPhotoActivity();

        tv_who_can_see = (TextView) findViewById(R.id.tv_who_can_see);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4) {
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                //super.onMeasure(recycler, state, widthSpec, heightSpec);
                int itemPadding = mImagePaddings * 2;
                int width = View.MeasureSpec.getSize(widthSpec);
                int itemWidth = (width - getPaddingLeft() - getPaddingRight() - itemPadding * getSpanCount()) / getSpanCount();
                int height = itemWidth + itemPadding;

                if (getItemCount() > getSpanCount()) {
                    height *= 2;
                }
                setMeasuredDimension(width, height + getPaddingTop() + getPaddingBottom());

            }
        });
        adapter = new PublishImageAdapter(new PublishImageAdapter.ItemClickListener() {
            @Override
            public void onAddImage() {
                startPhotoActivity();
            }
        });
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                mImagePaddings = (int) DisplayUtil.dp(5, getResources()) / 2;

                outRect.set(mImagePaddings, mImagePaddings, mImagePaddings, mImagePaddings);
            }
        });

        findViewById(R.id.v_notice_who_see).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PublishActivity.this, NoticeFriendListActivity.class));
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

    private void startPhotoActivity() {
        PhotoActivity.start(PublishActivity.this, 700, 700, 100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<Uri> uris = PhotoActivity.handleResult(data);
            for (Uri uri : uris) {
                adapter.addImage(this, uri);
            }
        }
    }
}
