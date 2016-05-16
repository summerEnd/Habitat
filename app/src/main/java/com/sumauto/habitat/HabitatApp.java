package com.sumauto.habitat;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.sumauto.SApplication;
import com.sumauto.habitat.exception.NotLoginException;
import com.sumauto.habitat.utils.BroadCastManager;
import com.sumauto.util.SLog;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.socialize.PlatformConfig;

import java.util.Set;

public class HabitatApp extends SApplication {
    private static final String TAG = "habitat";
    public static final String ACCOUNT_UID = "user_id";
    public static final String ACCOUNT_PHONE = "phone";
    public static final String ACCOUNT_BIRTHDAY = "birthday";
    public static final String ACCOUNT_NICK = "nick";
    public static final String ACCOUNT_GENDER = "gender";
    public static final String ACCOUNT_COMMID = "com_id";
    public static final String ACCOUNT_COMM_NAME = "com_name";
    public static final String ACCOUNT_AVATAR = "avatar";
    public static final String ACCOUNT_SIGNATURE = "signature";

    public static final String ACCOUNT_FANS_COUNT = "fans_count";
    public static final String ACCOUNT_TREND_COUNT = "trend_count";
    public static final String ACCOUNT_ATTENTION_COUNT = "attention_count";

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
        mAccount = getLoginAccount();
    }

    /**
     * 执行完登录接口，在调本地的方法，登入系统
     */
    public void login(String pwd, Bundle data) {
        String uid = data.getString(ACCOUNT_UID);

        if (TextUtils.isEmpty(uid)) {
            throw new IllegalStateException("must have a uid");
        }
        SLog.d(TAG,uid+" is login");

        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account accountWithSameUid = null;
        Account[] accountsByType = manager.getAccountsByType(BuildConfig.ACCOUNT_TYPE);
        for (Account temp : accountsByType) {
            String id = manager.getUserData(temp, ACCOUNT_UID);
            if (TextUtils.equals(uid, id)) {
                accountWithSameUid = temp;
            }
        }
        //通知登录状态发生改变
        Intent loginBroadcast = new Intent(BroadCastManager.ACTION_LOGIN_STATE);
        if (mAccount != null) {

        }
        SLog.d(TAG,"send login broadcast");
        sendBroadcast(loginBroadcast);

        if (accountWithSameUid != null) {
            SLog.d(TAG,"already have account");
            //已经存在一个相同的帐号
            if (!TextUtils.equals(manager.getPassword(accountWithSameUid), pwd)) {
                manager.setPassword(accountWithSameUid, pwd);//密码不一样
            }
            this.mAccount = accountWithSameUid;
            setUserData(data);
        } else {
            SLog.d(TAG,"add new account");
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

    public void setPassword(String password) {
        Account loginAccount = getLoginAccount();
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        manager.setPassword(loginAccount, password);
    }

    /**
     * @return 当前登录账号，没登录就返回null
     */
    @Nullable
    public Account getLoginAccount() {
        String uid = getSharedPreferences(PREFERANCE_NAME, MODE_PRIVATE).getString("last_login", "");
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] accountsByType = manager.getAccountsByType(BuildConfig.ACCOUNT_TYPE);
        for (Account account : accountsByType) {
            String accountUserId = manager.getUserData(account, ACCOUNT_UID);
            if (TextUtils.equals(uid, accountUserId)) {
                return account;
            }
        }

        return null;
    }

    public boolean isLogin() {
        return getLoginAccount() != null;
    }

    public String getUserData(String key) throws NotLoginException {
        Account loginAccount = getLoginAccount();
        if (loginAccount == null) {
            throw new NotLoginException();
        }
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        return manager.getUserData(loginAccount, key);
    }

    public void setUserData(String key, String value) {
        if (mAccount != null) {
            AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
            manager.setUserData(mAccount, key, value);
        } else {
            Log.e("", "not login!");
        }

    }

    public void setUserData(Bundle data) {

        if (mAccount != null) {
            AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
            //对应的账户
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                manager.setUserData(mAccount, key, data.getString(key));
            }
        } else {
            Log.e("", "not login!");
        }
    }
}
