package com.sumauto.habitat.callback;

import com.sumauto.util.SLog;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/04/19 5.6.6 
 */
public class SimpleUMListener implements UMAuthListener{
    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
        SLog.d("TAG",String.format("SHARE_MEDIA:%s i:%s %s", share_media.toString(), ""+i,throwable.toString()));
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        SLog.d("TAG",String.format("SHARE_MEDIA:%s i:%s", share_media.toString(), i+""));
    }
}
