package com.sumauto.habitat;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.sumauto.SApplication;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.socialize.PlatformConfig;

import java.util.Set;

public class HabitatApp extends SApplication {
    public static final String ACCOUNT_UID="user_id";
    public static final String ACCOUNT_PHONE="phone";
    public static final String ACCOUNT_BIRTHDAY="birthday";
    public static final String ACCOUNT_NICK="nick";
    public static final String ACCOUNT_GENDER="gender";
    public static final String ACCOUNT_COMMID="com_id";
    public static final String ACCOUNT_COMM_NAME="com_name";
    public static final String ACCOUNT_AVATAR="avatar";
    public static final String ACCOUNT_SIGNATURE="signature";
    private static HabitatApp instance;
    private String PREFERANCE_NAME = "config";

    public static HabitatApp getInstance() {
        return instance;
    }

    private Account mAccount;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setDebug(BuildConfig.DEBUG);
        AnalyticsConfig.setAppkey(this, BuildConfig.APP_KEY);
        AnalyticsConfig.setChannel(BuildConfig.CHANNEL);

        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");//微信 appid appsecret
        PlatformConfig.setSinaWeibo("283177913", "76dac8780978535ef7df5e4d62b67847");//新浪微博 appkey appsecret
        PlatformConfig.setQQZone("1105280086", "vFc4waVtlXDxMUVt");// QQ和Qzone appid appkey

    }

    public void login(String pwd, Bundle data) {
        String uid = data.getString(ACCOUNT_UID);

        if (TextUtils.isEmpty(uid)) {
            throw new IllegalStateException("must have a uid");
        }
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account accountWithSameUid = null;
        Account[] accountsByType = manager.getAccountsByType(BuildConfig.ACCOUNT_TYPE);
        for (Account temp : accountsByType) {
            String id = manager.getUserData(temp,ACCOUNT_UID);
            if (TextUtils.equals(uid, id)) {
                accountWithSameUid = temp;
            }
        }

        if (accountWithSameUid != null) {
            //已经存在一个手机号相同的帐号
            if (!TextUtils.equals(manager.getPassword(accountWithSameUid), pwd)) {
                manager.setPassword(accountWithSameUid, pwd);//密码不一样
            }
            this.mAccount = accountWithSameUid;
            setUserData(data);
        } else {
            String phone = data.getString(ACCOUNT_PHONE);
            String nick = data.getString(ACCOUNT_NICK);
            String nickName = (TextUtils.isEmpty(nick) ? phone : nick) +
                    "(" + uid + ")";
            Account account = new Account(nickName, BuildConfig.ACCOUNT_TYPE);
            manager.addAccountExplicitly(account, pwd, data);
            this.mAccount = account;
        }
        saveLastLogin(uid);
    }

    private void saveLastLogin(String uid) {
        //保存登录id
        getSharedPreferences(PREFERANCE_NAME, MODE_PRIVATE).edit().putString("last_login", uid).apply();
    }

    public void setPassword(String password){
        Account loginAccount = getLoginAccount();
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        manager.setPassword(loginAccount, password);
    }

    /**
     * @return 当前登录账号，没登录就返回null
     */
    @Nullable
    public Account getLoginAccount(){
        String uid = getSharedPreferences(PREFERANCE_NAME, MODE_PRIVATE).getString("last_login", "");
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] accountsByType = manager.getAccountsByType(BuildConfig.ACCOUNT_TYPE);
        for(Account account:accountsByType){
            String accountUserId = manager.getUserData(account,ACCOUNT_UID);
            if (TextUtils.equals(uid,accountUserId)){
                return account;
            }
        }

        return null;
    }

    public boolean isLogin(){
        return getLoginAccount()!=null;
    }

    public String getUserData(String key){
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        return manager.getUserData(getLoginAccount(),key);
    }

    public void setUserData(Bundle data) {

        if (mAccount != null) {
            AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
            //对应的账户
            manager.setUserData(mAccount, ACCOUNT_BIRTHDAY, "");

            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                manager.setUserData(mAccount, key, data.getString(key));
            }
        } else {
            Log.e("", "not login!");
        }
    }
}
