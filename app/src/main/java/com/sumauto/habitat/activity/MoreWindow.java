package com.sumauto.habitat.activity;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;

import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.bean.FeedBean;
import com.sumauto.habitat.exception.NotLoginException;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.widget.IosListDialog;
import com.sumauto.util.ToastUtil;

import java.util.ArrayList;


/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/06/03 5.6.6 
 */
public class MoreWindow extends IosListDialog implements IosListDialog.Listener {

    ArrayList<CharSequence> items = new ArrayList<>();
    FeedBean feedBean;

    public MoreWindow(Context context) {
        super(context);
        listener(this);
    }

    public void showMoreFor(FeedBean feedBean) {
        this.feedBean = feedBean;
        buildItems();
    }

    private void buildItems() {
        String loginId = null;

        try {
            loginId = HabitatApp.getInstance().getLoginUserData(HabitatApp.ACCOUNT_UID);
        } catch (NotLoginException e) {
            e.printStackTrace();
        }
        items.clear();

        if (loginId != null && TextUtils.equals(loginId, feedBean.uid)) {

            SpannableStringBuilder sb = new SpannableStringBuilder("删除");
            sb.setSpan(new CharacterStyle() {
                @Override
                public void updateDrawState(TextPaint tp) {
                    tp.setColor(Color.RED);
                }
            }, 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            items.add(sb);

        }
        items.add("分享到其他平台");
        items.add("举报");
        CharSequence data[] = new CharSequence[items.size()];
        show(items.toArray(data));
    }

    @Override
    public void onClick(IosListDialog dialog, int position) {
        Context ctx = getContext();
        CharSequence item = items.get(position);
        if ("分享给好友".equals(item)) {
            ShareActivity.start(ctx, feedBean.content, Requests.getShareUrl(feedBean.id));
        } else if ("分享到其他平台".equals(item)) {
            ShareActivity.start(ctx, feedBean.content, Requests.getShareUrl(feedBean.id));
        } else if ("举报".equals(item)) {
            HttpRequest<String> request = Requests.reportIllegal(feedBean.id);

            HttpManager.getInstance().post(ctx, new JsonHttpHandler<String>(request) {
                @Override
                public void onSuccess(HttpResponse response, HttpRequest<String> request, String bean) {
                    ToastUtil.toast(getContext(),"举报成功");
                }
            });

        } else if (item instanceof SpannableStringBuilder) {
            //删除
            HttpRequest<String> request = Requests.delSubject(feedBean.id);

            HttpManager.getInstance().post(ctx, new JsonHttpHandler<String>(request) {
                @Override
                public void onSuccess(HttpResponse response, HttpRequest<String> request, String bean) {
                    onDeleteSuccess();
                }
            });
        }
    }

    public void onDeleteSuccess() {

    }
}
