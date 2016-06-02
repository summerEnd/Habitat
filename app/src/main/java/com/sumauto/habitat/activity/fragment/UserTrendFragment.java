package com.sumauto.habitat.activity.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.adapter.TrendAdapter;
import com.sumauto.habitat.bean.FeedBean;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.http.SyncHttpHandler;
import com.sumauto.widget.recycler.DividerDecoration;
import com.sumauto.widget.recycler.adapter.LoadMoreAdapter;

import java.util.List;

/**
 * 用户的动态
 */
public class UserTrendFragment extends ListFragment {

    protected RecyclerView mRecyclerView;
    protected String uid;
    private TrendAdapter adapter;

    public static UserTrendFragment newInstance(String uid) {

        Bundle args = new Bundle();
        args.putString("uid", uid);
        UserTrendFragment fragment = new UserTrendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString("uid");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new UserTrendAdapter((BaseActivity) getActivity(), uid);
        adapter.setOnDataSetChangeListener(new LoadMoreAdapter.OnDataSetChangeListener() {
            @Override
            public void onLoadPageStart() {

            }

            @Override
            public void onLoadPageEnd() {

            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
        processListBottomMargins(mRecyclerView);
        return view;
    }

    @Override
    public void scrollToPosition(int position) {
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(position);
        }
    }

    public class UserTrendAdapter extends TrendAdapter {
        private String uid;

        public UserTrendAdapter(BaseActivity context, String uid) {
            super(context);
            this.uid = uid;
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
