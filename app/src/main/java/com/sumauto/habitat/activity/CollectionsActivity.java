package com.sumauto.habitat.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.CollectAdapter;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.widget.recycler.DividerDecoration;

/**
 * 收藏
 */
public class CollectionsActivity extends BaseActivity {

    @ViewId RecyclerView recyclerView;
    public static void start(Context context,String tid) {
        Intent starter = new Intent(context, CollectionsActivity.class);
        starter.putExtra("tid",tid);
        context.startActivity(starter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
        recyclerView.setAdapter(new CollectAdapter(this,getIntent().getStringExtra("tid")));
    }
}
