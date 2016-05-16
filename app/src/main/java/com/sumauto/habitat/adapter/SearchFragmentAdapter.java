package com.sumauto.habitat.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.sumauto.habitat.adapter.holders.SearchHeaderHolder;
import com.sumauto.habitat.adapter.holders.TrendHolder;
import com.sumauto.habitat.bean.ImageBean;
import com.sumauto.habitat.bean.FeedBean;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.http.SyncHttpHandler;
import com.sumauto.widget.recycler.adapter.BaseHolder;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lincoln on 16/3/22.
 * 搜索Fragment
 */
@SuppressWarnings("unchecked")
public class SearchFragmentAdapter extends LoadMoreAdapter {


    public SearchFragmentAdapter(Context context) {
        super(context, new ArrayList());
    }


    @Override
    public BaseHolder onCreateHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new SearchHeaderHolder(parent);
        } else {
            return new TrendHolder(parent, null);
        }
    }

    @Override
    public void onBindHolder(BaseHolder holder, int position) {
        Object o = getDataList().get(position);

        if (holder instanceof TrendHolder) {
            ((TrendHolder) holder).init((FeedBean) o);
        } else if (holder instanceof SearchHeaderHolder) {
            HashMap<String, ArrayList> header= (HashMap<String, ArrayList>) o;
            ((SearchHeaderHolder) holder).init(header.get("banner"), header.get("user"));
        }
    }

    @Override
    public List onLoadData(int page) {
        HttpRequest<Object[]> request = Requests.searchInfo(page, 5);
        SyncHttpHandler<Object[]> httpHandler = new SyncHttpHandler<>(request);
        HttpManager.getInstance().postSync(getContext(), httpHandler);
        Object[] result = httpHandler.getResult();

        ArrayList<Object> arrayList = new ArrayList<>();
        if (page == 0) {
            //第一页数据才有头
            HashMap<String, ArrayList> header = new HashMap<>();
            header.put("banner", (ArrayList<ImageBean>) result[0]);
            header.put("user", (ArrayList<UserInfoBean>) result[1]);
            arrayList.add(header);
        }
        if (result[2] != null) {
            arrayList.addAll((ArrayList<FeedBean>) result[2]);
        }
        return arrayList;
    }


    @Override
    public int getViewType(int position) {
        return position == 0 ? 0 : 1;
    }
}
