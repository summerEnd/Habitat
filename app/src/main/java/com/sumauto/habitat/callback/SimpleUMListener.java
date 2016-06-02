package com.sumauto.habitat.callback;

import android.os.Bundle;

import com.sumauto.util.SLog;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;

import java.util.Map;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/04/19 5.6.6 
 */
public class SimpleUMListener implements SocializeListeners.UMAuthListener,SocializeListeners.UMDataListener{

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onComplete(Bundle bundle, SHARE_MEDIA share_media) {

    }

    @Override
    public void onError(SocializeException e, SHARE_MEDIA share_media) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onComplete(int i, Map<String, Object> map) {

    }
}
