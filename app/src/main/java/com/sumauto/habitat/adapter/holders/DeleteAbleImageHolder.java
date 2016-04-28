package com.sumauto.habitat.adapter.holders;

import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.R;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.utils.ImageOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Lincoln on 16/3/24.
 * 可删除的图片
 */
public class DeleteAbleImageHolder extends BaseViewHolder {
    public final ImageView iv_image, iv_delete;
    public final View cover;
    private final View progressLayout;
    private final TextView tv_progress;
    private final Callback callback;
    public ImageBean bean;

    public DeleteAbleImageHolder(ViewGroup parent, Callback callback) {
        super(parent, R.layout.grid_item_delete_able_image);
        this.callback = callback;
        iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
        iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
        tv_progress = (TextView) itemView.findViewById(R.id.tv_progress);
        cover = itemView.findViewById(R.id.v_cover);
        progressLayout = itemView.findViewById(R.id.progressLayout);
    }

    public void init(final ImageBean bean) {
        this.bean=bean;
        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onMove(DeleteAbleImageHolder.this);
            }
        });
        Uri uri = bean.data;
        ImageLoader.getInstance().displayImage(uri.toString(), iv_image, ImageOptions.options());
        iv_delete.setVisibility(View.VISIBLE);
        iv_delete.setTag(uri);
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onDelete(DeleteAbleImageHolder.this);
            }
        });

        if (bean.doUpload){
            progressLayout.setVisibility(View.VISIBLE);
            setProgress(bean.progress);
            bean.listener(new Runnable() {
                @Override
                public void run() {
                    setProgress(bean.progress);
                }
            });
        }else{
            progressLayout.setVisibility(View.INVISIBLE);
        }
        cover.setVisibility(getAdapterPosition()==0?View.VISIBLE:View.INVISIBLE);
    }

    private void setProgress(int progress) {
        String str=progress+"%";
        Log.d(HttpManager.TAG,"setProgress:"+str);
        tv_progress.setText(str);
        if (progress==100){
            progressLayout.setVisibility(View.INVISIBLE);
        }
    }


    public static class ImageBean {
        public Uri data;
        public boolean doUpload;

        private int progress;
        private Runnable mRunnable;

        public ImageBean(Uri data, boolean doUpload) {
            this.data = data;
            this.doUpload = doUpload;
        }

        private void listener(Runnable runnable){
            mRunnable=runnable;
        }

        public void updateProgress(int progress){
            this.progress=progress;
            if (mRunnable!=null)mRunnable.run();
            if (progress==100){
                doUpload=false;
            }
        }
    }

    public  interface Callback {
        void onDelete(DeleteAbleImageHolder holder);

        void onMove(DeleteAbleImageHolder holder);
    }
}
