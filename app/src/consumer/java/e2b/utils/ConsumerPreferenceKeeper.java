package e2b.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.e2b.utils.AppConstant;
import com.e2b.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import e2b.model.response.UserResponse;

/**
 * Class is used to save profile data in preference.
 */
public class ConsumerPreferenceKeeper {

    private static ConsumerPreferenceKeeper keeper;
    private static Context context;
    private SharedPreferences prefs;

    private ConsumerPreferenceKeeper(Context context) {
        if (context != null)
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static ConsumerPreferenceKeeper getInstance() {
        if (keeper == null) {
            keeper = new ConsumerPreferenceKeeper(context);
        }
        return keeper;
    }

    public static void setContext(Context context1) {
        context = context1;
    }

    public void clearData() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public boolean getIsLogin() {
        return prefs.getBoolean(AppConstant.PreferenceKeeperNames.LOGIN, false);
    }
    public void setIsLogin(boolean isLogin) {
        prefs.edit().putBoolean(AppConstant.PreferenceKeeperNames.LOGIN, isLogin).commit();
    }

    public String getAccessToken() {
        return prefs.getString(AppConstant.PreferenceKeeperNames.ACCESS_TOKEN, "");
    }

    public void setAccessToken(String accessToken) {
        prefs.edit().putString(AppConstant.PreferenceKeeperNames.ACCESS_TOKEN, accessToken).commit();
    }
    public void saveUser(UserResponse user) {
        if (user != null) {
            prefs.edit().putString(AppConstant.PreferenceKeeperNames.USER, GsonUtils.getJson(user)).commit();
        } else {
            prefs.edit().putString(AppConstant.PreferenceKeeperNames.USER, "{}").commit();
        }
    }

    public UserResponse getUser() {
        Type type = new TypeToken<UserResponse>() {
        }.getType();
        return GsonUtils.parseJson(prefs.getString(AppConstant.PreferenceKeeperNames.USER, "{}"), type);
    }

    public void setFCMToken(String token) {
        prefs.edit().putString(AppConstant.PreferenceKeeperNames.FCM_TOKEN, token).commit();
    }

    public String getFCMToken() {
        return prefs.getString(AppConstant.PreferenceKeeperNames.FCM_TOKEN, "");
    }

    public String getLat() {
        return null;
    }

    public String getLong() {
        return null;
    }

    public String getUserId() {
        return prefs.getString(AppConstant.PreferenceKeeperNames.USER_ID, "");
    }

    public void setUserId(String userId) {
        prefs.edit().putString(AppConstant.PreferenceKeeperNames.USER_ID, userId).commit();
    }

}
