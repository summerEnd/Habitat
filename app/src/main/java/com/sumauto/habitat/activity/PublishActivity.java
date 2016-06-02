package com.sumauto.habitat.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.adapter.holders.DeleteAbleImageHolder;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.util.DisplayUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.PublishImageAdapter;
import com.sumauto.habitat.widget.IosListDialog;
import com.sumauto.util.ToastUtil;

import org.json.JSONException;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PublishActivity extends BaseActivity {
    final int pickImage = 1;
    final int pickWhoCanSee = 2;
    final int pickNoticeWhoSee = 3;
    private PublishImageAdapter adapter;
    private int mImagePaddings;
    @ViewId(R.id.recyclerView) RecyclerView mRecyclerView;
    @ViewId EditText edit_content;
    @ViewId TextView tv_notice_who_see;
    @ViewId TextView tv_who_can_see;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);
        startPhotoActivity();

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
    }

    private void startPhotoActivity() {
        PhotoActivity.Options options = new PhotoActivity.Options();
        options.outHeight = options.outWidth = 700;
        options.actionName = "立即发布";
        options.multiply = true;
        PhotoActivity.start(PublishActivity.this, options, 100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String ids = data.getStringExtra("ids");
            if (requestCode == pickWhoCanSee) {
                tv_who_can_see.setTag(ids);
                tv_who_can_see.setText(ids);
            } else if (requestCode == pickNoticeWhoSee) {
                tv_notice_who_see.setTag(ids);
                tv_notice_who_see.setText(ids);
            } else {
                List<Uri> uris = PhotoActivity.handleResult(data);
                for (Uri uri : uris) {
                    adapter.addImage(this, uri);
                }
            }
        }
    }

    public void onPublishClick(View view) {
        String content = edit_content.getText().toString();
        String openlevel ;
        String remind ;
        String ids="";

        if (tv_who_can_see.getTag()==null){
            openlevel="0";
        }else{
            openlevel= (String) tv_notice_who_see.getTag();
        }

        if (tv_notice_who_see.getTag()==null){
            remind="";
        }else{
            remind= (String) tv_notice_who_see.getTag();
        }

        List<DeleteAbleImageHolder.ImageBean> images = adapter.getImages();
        for (DeleteAbleImageHolder.ImageBean bean : images) {
            ids += bean.id + ",";
        }

        HttpRequest<String> request = Requests.publishArticle(content, openlevel, remind, ids);

        HttpManager.getInstance().post(PublishActivity.this, new JsonHttpHandler<String>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<String> request, String bean) {
                finish();
                ToastUtil.toast(PublishActivity.this, "发布成功");
            }
        });
    }

    public void onWhoCanSeeClick(View view) {
        new IosListDialog(PublishActivity.this)
                .listener(new IosListDialog.Listener() {
                    @Override
                    public void onClick(IosListDialog dialog, int position) {
                        switch (position) {
                            case 0: {
                                tv_who_can_see.setText(R.string.anybody_can_see);
                                tv_who_can_see.setTag("0");
                                break;
                            }
                            case 1: {
                                tv_who_can_see.setText(R.string.only_self_see);
                                tv_who_can_see.setTag(getUserData(HabitatApp.ACCOUNT_UID));
                                break;
                            }
                            case 2: {
                                tv_who_can_see.setText(R.string.partly_can_see);
                                startActivityForResult(new Intent(PublishActivity.this, NoticeFriendListActivity.class), pickWhoCanSee);
                                break;
                            }
                        }
                    }
                })
                .show(R.string.anybody_can_see, R.string.only_self_see, R.string.partly_can_see);
    }

    public void onNoticeWhoSee(View view) {
        startActivityForResult(new Intent(PublishActivity.this, NoticeFriendListActivity.class), pickNoticeWhoSee);
    }
}
