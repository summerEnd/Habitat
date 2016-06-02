package com.sumauto.habitat.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.TrendAdapter;
import com.sumauto.habitat.bean.FeedBean;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.http.SyncHttpHandler;

import java.util.List;

public class UserTrendActivity extends BaseActivity {
    @ViewId SwipeRefreshLayout swipeLayout;
    @ViewId RecyclerView recyclerView;
    String uid;

    void initView() {
        uid = getIntent().getData().getQueryParameter("uid");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_trend);
        initView();
    }

    private class MyAdapter extends TrendAdapter {

        public MyAdapter(BaseActivity context) {
            super(context);
        }

        @Override
        public List onLoadData(int page) {
            HttpRequest<List<FeedBean>> request = Requests.getMyActive(page, uid);
            SyncHttpHandler<List<FeedBean>> httpHandler = new SyncHttpHandler<>(request);
            HttpManager.getInstance().postSync(getContext(), httpHandler);
            return httpHandler.getResult();
        }
    }
}
