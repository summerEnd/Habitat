package com.sumauto.habitat.adapter.holders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.BannerBean;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.utils.Constant;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.widget.pager.BannerPager;
import com.sumauto.widget.recycler.ItemPaddingDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln on 16/3/22.
 * 搜索大头部
 */
public class SearchHeaderHolder extends BaseViewHolder {
    public final RecyclerView rv_recommend_user;
    public final BannerPager mViewPager;
    private final UserAdapter adapter;
    private final View bannerLayout, userLayout;
    List<UserInfoBean> userInfoBeans = new ArrayList<>();

    public SearchHeaderHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_search_header);
        rv_recommend_user = (RecyclerView) itemView.findViewById(R.id.rv_recommend_user);
        userLayout = itemView.findViewById(R.id.userLayout);
        bannerLayout = itemView.findViewById(R.id.bannerLayout);
        mViewPager = (BannerPager) itemView.findViewById(R.id.mViewPager);

        adapter = new UserAdapter();
        rv_recommend_user.addItemDecoration(new ItemPaddingDecoration(3, 3, 3, 3));
        rv_recommend_user.setAdapter(adapter);
    }

    public void init(ArrayList<BannerBean> banners, ArrayList<UserInfoBean> users) {
        userInfoBeans.clear();
        if (users == null || users.isEmpty()) {
            userLayout.setVisibility(View.GONE);
            //没有用户
        } else {
            userLayout.setVisibility(View.VISIBLE);
            userInfoBeans.addAll(users);
            adapter.notifyDataSetChanged();
        }

        if (banners == null || banners.isEmpty()) {
            //没有banenr
            bannerLayout.setVisibility(View.GONE);
        } else {
            bannerLayout.setVisibility(View.VISIBLE);
            ArrayList<String> imageUrls = new ArrayList<>();
            for (int i = 0; i < banners.size(); i++) {
                imageUrls.add(banners.get(i).img);
            }
            mViewPager.setImageUrls(imageUrls);
            mViewPager.start(2000);
        }
    }

    private class UserAdapter extends RecyclerView.Adapter<ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_recommend_user, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            UserInfoBean bean = userInfoBeans.get(position);
            ImageLoader.getInstance().displayImage(bean.headimg, (ImageView) holder.itemView, ImageOptions.options());
        }

        @Override
        public int getItemCount() {
            return userInfoBeans.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
