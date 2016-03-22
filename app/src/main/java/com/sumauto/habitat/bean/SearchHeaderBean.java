package com.sumauto.habitat.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln on 16/3/22.
 */
public class SearchHeaderBean {
    public  List<UserInfoBean> getUserInfoBeans(){
        ArrayList<UserInfoBean> list=new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            list.add(new UserInfoBean());
        }
        return list;
    }
}
