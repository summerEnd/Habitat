package com.sumauto.habitat.utils;

import android.text.TextUtils;

import com.sumauto.habitat.bean.UserInfoBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/05/25 5.6.6 
 */
public class SortUtils {

    public static void sortUserByFirstLetter(List<UserInfoBean> userInfoBeans) {
        //按首字母排序
        Collections.sort(userInfoBeans, new Comparator<UserInfoBean>() {
            @Override
            public int compare(UserInfoBean lhs, UserInfoBean rhs) {
                return lhs.getNameSort().compareTo(rhs.getNameSort());
            }
        });
    }

    //按拼音分组，并插入组标
    public static List<Object> insertLetterIn(List<UserInfoBean> userInfoBeans) {

        sortUserByFirstLetter(userInfoBeans);
        //按拼音分组，并插入组标
        ArrayList<Object> mBeans = new ArrayList<>();
        String groupName = "";
        for (UserInfoBean userInfoBean : userInfoBeans) {
            String firstLetter = userInfoBean.getNameFirstLetter().toUpperCase();
            if (!TextUtils.equals(groupName, firstLetter)) {
                //组标发生变化
                mBeans.add(firstLetter);
                groupName = firstLetter;
            }
            mBeans.add(userInfoBean);
        }
        return mBeans;
    }
}
