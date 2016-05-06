package com.sumauto.habitat.bean;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/05/06 5.6.6
 * 小区
 */
public class CommitBean {

    /**
     * id : 6
     * title : Dtff k
     * province : 北京
     * city : 北京市
     * area : 东城区
     */

    public String id;
    public String title;
    public String province;
    public String city;
    public String area;

    public String getAddress(){
        String address="";
        if (province!=null)address+=province;
        if (city!=null)address+=city;
        if (area!=null)address+=area;
        return address;
    }

}
