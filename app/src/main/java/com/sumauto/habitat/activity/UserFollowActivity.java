package com.sumauto.habitat.activity;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.holders.UserListHolder;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.http.SyncHttpHandler;
import com.sumauto.widget.recycler.adapter.BaseHolder;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

public class UserFollowActivity extends BaseActivity {
    @ViewId SwipeRefreshLayout swipeLayout;
    @ViewId RecyclerView recyclerView;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_follow);
        initView();
    }

    void initView() {
        uid = getIntent().getData().getQueryParameter("uid");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(this));
    }

    private class MyAdapter extends LoadMoreAdapter {


        public MyAdapter(Context context) {
            super(context, new ArrayList());
        }

        @Override
        public BaseHolder onCreateHolder(ViewGroup parent, int viewType) {
            return new UserListHolder(parent);
        }

        @Override
        public void onBindHolder(BaseHolder holder, int position) {
            holder.setData(getDataList().get(position));
        }

        @Override
        public List onLoadData(int page) {
            HttpRequest<List<UserInfoBean>> request = Requests.getMyFriends(page, uid);
            SyncHttpHandler<List<UserInfoBean>> httpHandler = new SyncHttpHandler<>(request);
            HttpManager.getInstance().postSync(getContext(), httpHandler);
            return httpHandler.getResult();

        }
    }
}
