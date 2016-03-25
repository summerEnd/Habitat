package com.sumauto.habitat.adapter.holders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.SearchHeaderBean;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.widget.recycler.ItemPaddingDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln on 16/3/22.
 */
public class SearchHeaderHolder extends BaseViewHolder{
    public final RecyclerView rv_recommend_user;
    private SearchHeaderBean bean;
    private final UserAdapter adapter;
    List<UserInfoBean> userInfoBeans = new ArrayList<>();

    public SearchHeaderHolder(ViewGroup parent) {
        super(parent,R.layout.list_item_search_header);
        rv_recommend_user= (RecyclerView) itemView.findViewById(R.id.rv_recommend_user);
        adapter = new UserAdapter();
        rv_recommend_user.addItemDecoration(new ItemPaddingDecoration(3,3,3,3));
        rv_recommend_user.setAdapter(adapter);

    }

    public void init(SearchHeaderBean bean){
        this.bean = bean;
        userInfoBeans.clear();
        userInfoBeans.addAll(bean.getUserInfoBeans());
        adapter.notifyDataSetChanged();
    }

    private class UserAdapter extends RecyclerView.Adapter<ViewHolder>{


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_recommend_user,parent,false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return userInfoBeans.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
