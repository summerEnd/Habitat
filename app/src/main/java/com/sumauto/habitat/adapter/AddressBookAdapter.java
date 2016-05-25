package com.sumauto.habitat.adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.sumauto.habitat.adapter.holders.AddressBookTitleHolder;
import com.sumauto.habitat.adapter.holders.ContactsHolder;
import com.sumauto.habitat.adapter.holders.UserListHolder;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.util.SUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Lincoln on 16/3/22.
 * 通讯录
 */
public class AddressBookAdapter extends RecyclerView.Adapter implements HeaderDecor.Callback {

    private List<Object> beans = new ArrayList<>();
    private Context context;
    public AddressBookAdapter(Context context) {
        this.context=context;
        String[] projections = new String[]{Phone.NUMBER, Phone.DISPLAY_NAME, Phone.PHOTO_URI};

        Cursor query = context.getContentResolver()
                .query(Phone.CONTENT_URI, projections, "", null, Phone.DISPLAY_NAME + " DESC");

        ArrayList<UserInfoBean> userInfoBeans = new ArrayList<>();
        while (query.moveToNext()) {
            Log.d("--->", "ls:" + query.getString(0) + " " + query.getString(1) + " " + query.getString(2));
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.phone = query.getString(0);
            userInfoBean.nickname = query.getString(1);
            userInfoBean.headimg = query.getString(2);
            userInfoBeans.add(userInfoBean);
        }
        query.close();

        //按首字母排序
        Collections.sort(userInfoBeans, new Comparator<UserInfoBean>() {
            @Override
            public int compare(UserInfoBean lhs, UserInfoBean rhs) {
                return lhs.getNameSort().compareTo(rhs.getNameSort());
            }
        });
        //按拼音分组，并插入组标
        String groupName = "";
        for (UserInfoBean bean : userInfoBeans) {
            String firstLetter = bean.getNameFirstLetter().toUpperCase();
            if (!TextUtils.equals(groupName, firstLetter)) {
                //组标发生变化
                beans.add(firstLetter);
                groupName = firstLetter;
            }
            beans.add(bean);
        }
        getData();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new AddressBookTitleHolder(parent);

        } else {
            return new ContactsHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof AddressBookTitleHolder) {
            ((AddressBookTitleHolder) holder).setData(beans.get(position).toString());
        } else if (holder instanceof ContactsHolder) {
            ((ContactsHolder) holder).setData(beans.get(position));
        }
    }


    @Override
    public int getItemViewType(int position) {

        return beans.get(position) instanceof String ? 0 : 1;
    }

    @Override
    public int getItemCount() {

        return beans.size();
    }

    @Override
    public int getHeaderForPosition(int position) {
        int count = beans.size();
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

    void getData(){
        HttpRequest<List<UserInfoBean>> request = Requests.getUserFriends();

        HttpManager.getInstance().post(context, new JsonHttpHandler<List<UserInfoBean>>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<List<UserInfoBean>> request, List<UserInfoBean> bean) {

            }
        });
    }

}
