package messagelogix.com.k12campusalerts.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * This class is a helper to store data to shared preferences
 * some sensitive values need to be encrypted in method putString()
 * You must add logic to decrypt them when retrieving them back
 * in the method getString()
 */

public class Preferences {

    public static final String SHARED_PREFERENCES = "com.messagelogix.k12campusalerts";
    private static final String LOG_TAG = Preferences.class.getSimpleName();

    public static SharedPreferences mPreferences;

    public static void init(Context context) {
        mPreferences = context.getSharedPreferences(SHARED_PREFERENCES, 0);
    }


    public static String getString(String key) {
        return mPreferences.getString(key, "");
    }

    public static void putString(String key, String s) {
        mPreferences.edit().putString(key, s).apply();
    }



    public static boolean getBoolean(String key) {
        return mPreferences.getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return mPreferences.getBoolean(key, defaultValue);
    }

    public static void putBoolean(String key, boolean bool) {
        mPreferences.edit().putBoolean(key, bool).apply();
    }

    public static void putInteger(String key, Integer integer) {
        mPreferences.edit().putInt(key, integer).apply();
    }

    public static void putLong(String key, long longValue) {
        mPreferences.edit().putLong(key, longValue).apply();
    }

    public static int getInteger(String key) {
        return mPreferences.getInt(key, 0);
    }

    public static int getInteger(String key, int defaultValue) {
        return mPreferences.getInt(key, defaultValue);
    }

    public static long getLong(String key) {
        return mPreferences.getLong(key, 0);
    }

    public static void clear() {
        mPreferences.edit().clear().apply();
    }

    public static void remove(String key) {
        mPreferences.edit().remove(key).apply();
    }


}
