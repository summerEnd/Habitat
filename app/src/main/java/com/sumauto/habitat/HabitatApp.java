package com.sumauto.habitat;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.sumauto.SApplication;
import com.sumauto.habitat.exception.NotLoginException;
import com.sumauto.habitat.utils.BroadCastManager;
import com.sumauto.util.SLog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.UMAnalyticsConfig;
import com.umeng.socialize.PlatformConfig;

import java.util.Set;

public class HabitatApp extends SApplication {
    private static final String TAG = "habitat";
    public static final String ACCOUNT_UID = "user_id";
    public static final String ACCOUNT_PHONE = "phone";
    public static final String ACCOUNT_OPENID = "snsaccount";
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

    private Account mLoginAccount;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setDebug(BuildConfig.DEBUG);

        MobclickAgent.startWithConfigure(new UMAnalyticsConfig(this, BuildConfig.APP_KEY, BuildConfig.CHANNEL));
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");//微信 appid appsecret
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("3397240833", "293cba99c41d4df2694ec4c70e8a87e6");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("1105345733", "0nodokOURqi71RPx");
        mLoginAccount = getLoginAccount();
    }

    /**
     * 执行完登录接口，在调本地的方法，登入系统
     */
    public void login(String pwd, Bundle data) {

        String uid = data.getString(ACCOUNT_UID);
        String nick = data.getString(ACCOUNT_NICK);
        Account account = null;

        if (TextUtils.isEmpty(uid))
            throw new IllegalStateException("must have a uid");

        if (TextUtils.isEmpty(nick)){
            nick = data.getString(ACCOUNT_PHONE);
        }

        String accountName = String.format("%s(%s)", nick, uid);
        SLog.d(TAG, accountName + " is login");

        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] accountsByType = accountManager.getAccountsByType(BuildConfig.ACCOUNT_TYPE);
        for (Account temp : accountsByType) {
            String id = accountManager.getUserData(temp, ACCOUNT_UID);
            if (TextUtils.equals(uid, id)) {
                account = temp;
            }
        }

        if (account != null) {
            if (!TextUtils.equals(account.name, accountName)) {
                SLog.d(TAG, uid + " already exists and accountName changed " + accountName);
                //用户名发生改变
                removeAccount(account);
                account = null;
            } else if (!TextUtils.equals(accountManager.getPassword(account), pwd)) {
                accountManager.setPassword(account, pwd);//密码不一样
            }
        }

        if (account == null) {
            account = new Account(accountName, BuildConfig.ACCOUNT_TYPE);
            accountManager.addAccountExplicitly(account, pwd, data);
            this.mLoginAccount = account;
        } else {
            this.mLoginAccount = account;
            setLoginUserData(data);
        }

        saveLastLogin(uid);
        //通知登录状态发生改变
        Intent loginBroadcast = new Intent(BroadCastManager.ACTION_LOGIN_STATE);
        sendBroadcast(loginBroadcast);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private void removeAccount(Account account) {
        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            accountManager.removeAccount(account, null, null, null);
        } else {
            //noinspection deprecation
            accountManager.removeAccount(account, null, null);
        }
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
        if (mLoginAccount == null) {

            String uid = getSharedPreferences(PREFERANCE_NAME, MODE_PRIVATE)
                    .getString("last_login", "");

            if (TextUtils.isEmpty(uid)) {
                return null;
            }

            AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
            Account[] accounts = manager.getAccountsByType(BuildConfig.ACCOUNT_TYPE);
            for (Account account : accounts) {
                String accountUserId = manager.getUserData(account, ACCOUNT_UID);
                if (TextUtils.equals(uid, accountUserId)) {
                    mLoginAccount = account;
                    break;
                }
            }
        }

        return mLoginAccount;
    }

    public boolean isLogin() {
        return getLoginAccount() != null;
    }

    public String getLoginUserData(String key) throws NotLoginException {
        Account loginAccount = getLoginAccount();
        if (loginAccount == null) {
            throw new NotLoginException();
        }
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        return manager.getUserData(loginAccount, key);
    }

    public void setLoginUserData(String key, String value) {
        if (mLoginAccount != null) {
            AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
            manager.setUserData(mLoginAccount, key, value);
        } else {
            Log.e("", "not login!");
        }

    }

    public void setLoginUserData(Bundle data) {

        if (mLoginAccount != null) {
            setUserData(mLoginAccount, data);
        } else {
            Log.e("", "not login!");
        }
    }

    public void setUserData(Account account, Bundle data) {
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        //对应的账户
        Set<String> keySet = data.keySet();
        for (String key : keySet) {
            manager.setUserData(account, key, data.getString(key));
        }
    }
}
