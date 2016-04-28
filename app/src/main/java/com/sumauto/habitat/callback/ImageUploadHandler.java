package com.sumauto.habitat.callback;

import android.util.Log;

import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sumauto.habitat.http.HttpManager;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/04/27 5.6.6 
 */
public class ImageUploadHandler extends TextHttpResponseHandler{

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.d(HttpManager.TAG,"onFailure "+responseString);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        Log.d(HttpManager.TAG,"onSuccess "+responseString);
    }

    @Override
    public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
        super.onPostProcessResponse(instance, response);
    }

    @Override
    public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
        super.onPreProcessResponse(instance, response);
    }

    @Override
    public void onProgress(long bytesWritten, long totalSize) {
        super.onProgress(bytesWritten, totalSize);
        Log.d(HttpManager.TAG,bytesWritten+"/"+totalSize);
    }
}
