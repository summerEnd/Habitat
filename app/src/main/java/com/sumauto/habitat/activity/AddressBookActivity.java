package com.sumauto.habitat.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sumauto.habitat.R;
import com.sumauto.habitat.adapter.AddressBookAdapter;
import com.sumauto.habitat.adapter.HeaderDecor;
import com.sumauto.widget.recycler.DividerDecoration;

public class AddressBookActivity extends BaseActivity {

    private AddressBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressBookAdapter();
        recyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#e5e5e5")));
        recyclerView.addItemDecoration(new HeaderDecor());
        recyclerView.setAdapter(adapter);


    }
}
