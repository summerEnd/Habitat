package com.sumauto.habitat.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.FriendListAdapter;
import com.sumauto.habitat.adapter.HeaderDecor;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.habitat.utils.DialogUtils;
import com.sumauto.widget.recycler.DividerDecoration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NoticeFriendListActivity extends BaseActivity {
    private FriendListAdapter adapter;
    @ViewId RecyclerView recyclerView;

    public static void start(Activity context, int requestCode) {
        Intent starter = new Intent(context, NoticeFriendListActivity.class);
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_friend_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
        recyclerView.addItemDecoration(new HeaderDecor());

        findViewById(R.id.item_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<UserInfoBean> selectedUser = adapter.getSelectedUser();
                StringBuilder idBuilder = new StringBuilder();
                int selectCount = selectedUser.size();
                if (selectCount != 0) {
                    for (UserInfoBean bean : selectedUser) {
                        idBuilder.append(",").append(bean.id);
                    }

                    Intent intent = new Intent();
                    intent.putExtra("ids", idBuilder.toString().substring(1));
                    setResult(RESULT_OK, intent);
                    finish();
                    //DialogUtils.show(NoticeFriendListActivity.this, nameBuilder);
                    Log.d(TAG, idBuilder.toString());
                }
            }
        });
    }

    private Map<String, List<?>> getData() {
        Map<String, List<?>> data = new LinkedHashMap<>();
        ArrayList<UserInfoBean> beans = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            UserInfoBean object = new UserInfoBean();
            object.setNick("Lincoln_" + i);
            beans.add(object);
        }
        data.put(getString(R.string.recent_contacts), beans);
        int N = 4;//每个标题的数据量
        for (int i = 0; i < 26 * N; i++) {
            int j = i % N;
            String letter = String.valueOf((char) ('A' + i / N));

            if (j == 0) {
                beans = new ArrayList<>();
                data.put(letter, beans);
            } else {
                UserInfoBean object = new UserInfoBean();
                object.setNick(letter + " Lincoln " + j);
                beans.add(object);
            }
        }


        return data;
    }
}
