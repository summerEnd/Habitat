package com.sumauto.habitat.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.holders.AddImageHolder;
import com.sumauto.habitat.adapter.holders.DeleteAbleImageHolder;
import com.sumauto.habitat.adapter.holders.DeleteAbleImageHolder.ImageBean;
import com.sumauto.habitat.callback.ImageUploadHandler;
import com.sumauto.habitat.http.HttpHandler;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.utils.ImageOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;


/**
 * Created by Lincoln on 16/3/24.
 * 可删除图片列表
 */
public class PublishImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DeleteAbleImageHolder.Callback {
    public static final int MAX_IMAGES = 8;
    private final ItemClickListener mListener;
    private List<ImageBean> images=new ArrayList<>();
    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ImageBean bean= (ImageBean) msg.obj;
            bean.updateProgress(msg.arg1);
            return false;
        }
    });
    public PublishImageAdapter(ItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==0){
            return new AddImageHolder(parent);
        }
        return new DeleteAbleImageHolder(parent,this);
    }

    @Override
    public int getItemViewType(int position) {
        return position==getItemCount()-1?0:1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddImageHolder){
            AddImageHolder addImageHolder= (AddImageHolder) holder;
            addImageHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onAddImage();
                }
            });
        }else if (holder instanceof DeleteAbleImageHolder){
            ((DeleteAbleImageHolder) holder).init(images.get(position));
        }
    }

    @Override
    public int getItemCount() {
        int size = images.size();
        if (size>=MAX_IMAGES){
            return MAX_IMAGES;
        }else{
            return size+1;
        }
    }

    public void addImage(Context context,Uri path){
       if (images.size()< MAX_IMAGES){
           ImageBean imageBean = new ImageBean(path, true);
           images.add(imageBean);
           doUpload(context,imageBean);
           if (images.size()==MAX_IMAGES){
               notifyItemChanged(images.size()-1);
           }else{
               notifyItemInserted(images.size()-1);
           }
       }
    }


    void doUpload(Context context,final ImageBean bean){
        try {
            File f=new File(new URI(bean.data.toString()));
            Log.d(HttpManager.TAG,"uploading file "+f);
            HttpManager.getInstance().post(context, new JsonHttpHandler<String>(Requests.getUploadUrl(f)) {

                @Override
                public void onSuccess(HttpResponse response, HttpRequest<String> request, String bean) {

                }
                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    super.onProgress(bytesWritten, totalSize);
                    Message msg = mHandler.obtainMessage();
                    msg.obj=bean;
                    msg.arg1=(int) ((bytesWritten * 1.0 / totalSize) * 100);
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onPreProcessResponse(ResponseHandlerInterface instance, cz.msebera.android.httpclient.HttpResponse response) {
                    super.onPreProcessResponse(instance, response);
                    onProgress(0,1);
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDelete(DeleteAbleImageHolder holder) {
        int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition<0)return;
        Object tag = images.get(adapterPosition);
        if (tag!=null){
            images.remove(tag);
            holder.itemView.setTag(null);//防止快速点击导致重复删除
            notifyItemRemoved(adapterPosition);
        }
    }

    @Override
    public void onMove(DeleteAbleImageHolder holder) {
        int position=holder.getAdapterPosition();
        Log.d(HttpManager.TAG,"move:"+position+" data:"+holder.bean.data);
        ImageBean remove = images.remove(position);
        images.add(0,remove);
        notifyItemMoved(position, 0);
        ((RecyclerView) holder.itemView.getParent()).scrollBy(1,1);
        //        notifyItemChanged(0);
//        notifyItemChanged(1);
    }

    public interface ItemClickListener{
        void onAddImage();
    }
}
