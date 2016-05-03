package com.sumauto.habitat.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.ChooseUserAdapter;
import com.sumauto.habitat.adapter.HeaderDecor;
import com.sumauto.habitat.adapter.holders.AddressBookTitleHolder;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.habitat.utils.DialogUtils;
import com.sumauto.widget.recycler.DividerDecoration;
import com.sumauto.widget.recycler.FloatHeadRecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NoticeFriendListActivity extends BaseActivity {
    private ChooseUserAdapter adapter;
    @ViewId RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_friend_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChooseUserAdapter(getData());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
        recyclerView.addItemDecoration(new HeaderDecor());

        findViewById(R.id.item_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<UserInfoBean> selectedUser = adapter.getSelectedUser();
                StringBuilder builder = new StringBuilder();

                builder.append(selectedUser.size() + " users is select \n");
                for (UserInfoBean bean : selectedUser) {
                    builder.append(bean.getNick());
                    builder.append("\n");
                }
                DialogUtils.show(NoticeFriendListActivity.this, builder);
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
                object.setNameFirstLetter(letter);
                object.setNick(letter + " Lincoln " + j);
                beans.add(object);
            }
        }


        return data;
    }
}
