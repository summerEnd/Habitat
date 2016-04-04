package com.sumauto.habitat.adapter.holders;

import android.view.ViewGroup;
import android.widget.TextView;

import com.sumauto.habitat.R;

/**
 * Created by Lincoln on 16/3/22.
 * 通讯录字母ABCD
 */
public class AddressBookTitleHolder extends BaseViewHolder {
    public final TextView tv_title;
    private String text;
    public AddressBookTitleHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_address_book_title);
        tv_title = (TextView) itemView.findViewById(R.id.tv_title);

    }

    public void init(String text) {
        this.text=text;
        tv_title.setText(text);
    }
}
