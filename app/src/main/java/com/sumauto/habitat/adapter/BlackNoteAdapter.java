package com.sumauto.habitat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.adapter.holders.AddressBookTitleHolder;
import com.sumauto.habitat.adapter.holders.BlackListHolder;
import com.sumauto.habitat.adapter.holders.UserListHolder;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.utils.SortUtils;
import com.sumauto.util.ToastUtil;
import com.sumauto.widget.recycler.adapter.BaseHolder;
import com.sumauto.widget.recycler.adapter.ListAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lincoln on 16/3/22.
 * 通讯录
 */
public class BlackNoteAdapter extends ListAdapter implements HeaderDecor.Callback {

    private String fid;

    public BlackNoteAdapter(Context context, String fid) {
        super(context, new ArrayList());
        this.fid = fid;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new AddressBookTitleHolder(parent);

        } else {
            return new BlackListHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        Object data = getDataList().get(position);
        if (holder instanceof AddressBookTitleHolder) {
            holder.setData(data.toString());
        } else if (holder instanceof BlackListHolder) {
            BlackListHolder blackListHolder = (BlackListHolder) holder;
            blackListHolder.setData(data);
            blackListHolder.btn_remove.setTag(data);
            blackListHolder.btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInfoBean tag = (UserInfoBean) v.getTag();
                    int index = getDataList().indexOf(tag);
                    if (index >= 0) {
                        getDataList().remove(index);
                        notifyItemRemoved(index);
                    } else {
                        ToastUtil.toast(getContext(), "index not found!");
                    }
                    removeFromBlack(tag);
                }
            });
        }
    }

    void removeFromBlack(UserInfoBean bean) {
        HttpRequest<String> request = Requests.removeFriendFromBlack(bean.id);

        HttpManager.getInstance().post(getContext(), new JsonHttpHandler<String>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<String> request, String bean) {
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return getDataList().get(position) instanceof String ? 0 : 1;
    }

    @Override
    public int getHeaderForPosition(int position) {
        int count = getDataList().size();
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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        getData();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    void getData() {
        HttpRequest<List<UserInfoBean>> request = Requests.getBlackList(fid);

        HttpManager.getInstance().post(getContext(), new JsonHttpHandler<List<UserInfoBean>>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<List<UserInfoBean>> request, List<UserInfoBean> bean) {
                //List<Object> objects = SortUtils.insertLetterIn(bean);
                setDataList(bean);
                notifyDataSetChanged();
            }
        });
    }
}
