package com.sumauto.habitat.activity.search;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.sumauto.common.util.ViewUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.BaseActivity;
import com.sumauto.habitat.adapter.AddressBookAdapter;
import com.sumauto.habitat.adapter.holders.AddressBookTitleHolder;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.widget.recycler.DividerDecoration;
import com.sumauto.widget.recycler.FloatHeadRecyclerView;

public class AddressBookActivity extends BaseActivity {

    private AddressBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);
        initToolBar(R.id.toolBar);
        FloatHeadRecyclerView floatRecyclerView = (FloatHeadRecyclerView) findViewById(R.id.floatRecyclerView);
        RecyclerView recyclerView = floatRecyclerView.getRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressBookAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
        floatRecyclerView.setCallback(new FloatHeadRecyclerView.Callback() {
            @Override
            public void invalidateHeader(RecyclerView.ViewHolder holder, int position) {
                AddressBookTitleHolder addressBookTitleHolder = (AddressBookTitleHolder) holder;
                Object o = adapter.getBeans().get(position);
                String title;
                if (o instanceof UserInfoBean) {
                    title = ((UserInfoBean) o).getNameFirstLetter();
                } else {
                    title = o.toString();
                }
                if (!TextUtils.isEmpty(title)) {
                    if (!title.equals(addressBookTitleHolder.tv_title.getText().toString())) {
                        addressBookTitleHolder.tv_title.setText(title);
                    }

                }
            }

            @Override
            public boolean shouldMoveHeader(FloatHeadRecyclerView parent, View firstView, View secondView) {
                RecyclerView recyclerView = parent.getRecyclerView();

                return recyclerView.getChildViewHolder(secondView) instanceof AddressBookTitleHolder;
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder getHeaderHolder(FloatHeadRecyclerView parent) {
                return new AddressBookTitleHolder(parent);
            }


        });

    }
}
