package com.sumauto.habitat;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sumauto.SApplication;
import com.sumauto.habitat.exception.NotLoginException;
import com.sumauto.habitat.utils.BroadCastManager;
import com.sumauto.util.SLog;

import java.util.List;
import java.util.Set;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

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


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setDebug(BuildConfig.DEBUG);

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

        if (TextUtils.isEmpty(nick)) {
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
        //账号不存在，或者被删除了
        if (account == null) {
            account = new Account(accountName, BuildConfig.ACCOUNT_TYPE);
            accountManager.addAccountExplicitly(account, pwd, data);
        } else {
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
                return account;
            }
        }

        return null;
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
        Account account = getLoginAccount();
        if (account != null) {
            AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
            manager.setUserData(account, key, value);
        } else {
            Log.e("", "not login!");
        }
    }

    public void setLoginUserData(Bundle data) {
        Account account = getLoginAccount();
        if (account != null) {
            setUserData(account, data);
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

    public Location getPosition() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            //获取所有可用的位置提供器
            List<String> providers = locationManager.getProviders(true);
            String locationProvider;
            if (providers.contains(LocationManager.GPS_PROVIDER)) {
                //如果是GPS
                locationProvider = LocationManager.GPS_PROVIDER;
            } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                //如果是Network
                locationProvider = LocationManager.NETWORK_PROVIDER;
            } else {
                SLog.e("App","get location failed");
                Location l = new Location("");
                return l;
            }
            //获取Location
            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return new Location("");
            }
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if (location == null) {
                SLog.e("App","get location failed");
                return new Location("");
            }
            return location;
        } catch (Exception e) {
            e.printStackTrace();
            return new Location("");
        }
    }

}
