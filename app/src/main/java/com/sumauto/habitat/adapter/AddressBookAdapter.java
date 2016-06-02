package com.sumauto.habitat.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.Telephony;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.holders.AddressBookTitleHolder;
import com.sumauto.habitat.adapter.holders.ContactsHolder;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.utils.SortUtils;
import com.sumauto.util.IntentUtils;
import com.sumauto.util.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Lincoln on 16/3/22.
 * 通讯录
 */
public class AddressBookAdapter extends RecyclerView.Adapter implements HeaderDecor.Callback {

    private Map<String, String> statusList = new HashMap<>();
    private List<Object> mBeans = new ArrayList<>();
    private Context context;
    private RecyclerView mRecyclerView;
    String phones = "";

    public AddressBookAdapter(Context context) {
        this.context = context;


        getData();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new AddressBookTitleHolder(parent);

        } else {
            ContactsHolder contactsHolder = new ContactsHolder(parent);

            contactsHolder.tv_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContactsHolder holder = (ContactsHolder) v.getTag();
                    UserInfoBean bean = holder.bean;

                    if (bean != null) {
                        String status = statusList.get(bean.phone);
                        if (status == null) status = "0";//以防万一为空
                        switch (status) {
                            case "1": {//未关注
                                statusList.put(bean.phone, "2");
                                holder.setStatus("2");
                                // TODO: 16/5/25 关注
                                break;
                            }
                            case "2": {//已关注
                                // TODO: 16/5/25 取消关注
                                statusList.put(bean.phone, "1");
                                holder.setStatus("1");
                                break;
                            }
                            default: {
                                // TODO: 16/5/25 邀请好友 发送短信
                                holder.setStatus("0");
                                ToastUtil.toast(context, "do invite stuffs");
                                IntentUtils.sendSms(context, bean.phone, context.getString(R.string.share_content));

                            }
                        }

                    }


                }
            });
            return contactsHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Object data = mBeans.get(position);
        if (holder instanceof AddressBookTitleHolder) {
            ((AddressBookTitleHolder) holder).setData(data.toString());
        } else if (holder instanceof ContactsHolder) {
            ContactsHolder contactsHolder = (ContactsHolder) holder;
            contactsHolder.tv_attention.setTag(contactsHolder);
            contactsHolder.setStatus(statusList.get(((UserInfoBean) data).phone));
            contactsHolder.setData(data);
        }
    }


    @Override
    public int getItemViewType(int position) {

        return mBeans.get(position) instanceof String ? 0 : 1;
    }

    @Override
    public int getItemCount() {

        return mBeans.size();
    }

    @Override
    public int getHeaderForPosition(int position) {
        int count = mBeans.size();
        if (position < count) {
            for (int i = position; i >= 0; i--) {
                if (getItemViewType(i) == 0) {
                    return i;
                }
            }
        }
        return 0;
    }

    @Override
    public boolean isHeader(RecyclerView.ViewHolder holder) {
        return holder instanceof AddressBookTitleHolder;
    }

    void getData() {

        String[] projections = new String[]{Phone.NUMBER, Phone.DISPLAY_NAME, Phone.PHOTO_URI};

        Cursor query = context.getContentResolver()
                .query(Phone.CONTENT_URI, projections, "", null, Phone.DISPLAY_NAME + " DESC");
        final ArrayList<UserInfoBean> userInfoBeans = new ArrayList<>();

        if (query.moveToFirst()) {

            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.phone = query.getString(0);
            userInfoBean.nickname = query.getString(1);
            userInfoBean.headimg = query.getString(2);
            userInfoBeans.add(userInfoBean);
            phones += userInfoBean.phone;

            while (query.moveToNext()) {
                userInfoBean = new UserInfoBean();
                userInfoBean.phone = query.getString(0);
                userInfoBean.nickname = query.getString(1);
                userInfoBean.headimg = query.getString(2);
                userInfoBeans.add(userInfoBean);
                phones += "," + userInfoBean.phone;

            }
        }
        query.close();




        HttpRequest<HashMap<String, String>> request = Requests.chargeListStatus(phones);

        HttpManager.getInstance().post(context, new JsonHttpHandler<HashMap<String, String>>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<HashMap<String, String>> request, HashMap<String, String> bean) {
                statusList.clear();
                statusList.putAll(bean);
                mBeans.clear();
                mBeans.addAll(SortUtils.insertLetterIn(userInfoBeans));

                notifyDataSetChanged();
            }
        });


    }

}
