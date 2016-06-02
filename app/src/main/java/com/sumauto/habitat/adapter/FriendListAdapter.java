package com.sumauto.habitat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sumauto.habitat.adapter.holders.AddressBookTitleHolder;
import com.sumauto.habitat.adapter.holders.BaseViewHolder;
import com.sumauto.habitat.adapter.holders.ChooseUserHolder;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.utils.SortUtils;
import com.sumauto.widget.recycler.adapter.BaseHolder;
import com.sumauto.widget.recycler.adapter.ListAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Lincoln on 16/3/22.
 * 选择用户
 */
public class FriendListAdapter extends ListAdapter implements ChooseUserHolder.Listener, HeaderDecor.Callback {


    ArrayList<Object> beans = new ArrayList<>();
    Set<UserInfoBean> selectedUser = new HashSet<>();

    public FriendListAdapter(Context context) {
        super(context);
        getData();
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new AddressBookTitleHolder(parent);

        } else {
            return new ChooseUserHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        Object bean = beans.get(position);
        if (holder instanceof AddressBookTitleHolder) {
            holder.setData(bean.toString());
        } else if (holder instanceof ChooseUserHolder) {
            ((ChooseUserHolder) holder).init((UserInfoBean) bean, this, selectedUser.contains(bean));
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

    public Set<UserInfoBean> getSelectedUser() {
        return selectedUser;
    }

    @Override
    public void onUserSelectChanged(UserInfoBean bean, boolean isSelect) {
        if (isSelect) selectedUser.add(bean);
        else selectedUser.remove(bean);
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

    void getData() {
        HttpRequest<List<UserInfoBean>> request = Requests.getUserFriends();

        HttpManager.getInstance().post(getContext(), new JsonHttpHandler<List<UserInfoBean>>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<List<UserInfoBean>> request, List<UserInfoBean> bean) {
                beans.clear();
                beans.addAll(SortUtils.insertLetterIn(bean));
                notifyDataSetChanged();
            }
        });
    }
}
