package messagelogix.com.k12campusalerts.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ahmed Daou on 7/13/2015.
 * This is just a wrapper for shared Preference meant to ease its management
 */
public class SharedPreferencesManager {
    private final SharedPreferences.Editor _prefsEditor;
    private SharedPreferences _sharedPreferences;

    public SharedPreferencesManager(Context context){
        this._sharedPreferences = context.getSharedPreferences("Session Data", context.MODE_PRIVATE);
        this._prefsEditor = this._sharedPreferences.edit();
    }

    /***
     * Save a Boolean value to Shared Preferences
     * @param key
     * @param value
     */
    public void setBool(String key, boolean value){
        _prefsEditor.putBoolean(key, value);
        _prefsEditor.commit();
    }

    /**
     * Get an Boolean value from shared Preferences
     * @param key
     * @return
     */
    public boolean getBool(String key){
        return _sharedPreferences.getBoolean(key, false);
    }

    /***
     * Save String value to Shared Preferences
     * @param key
     * @param value
     */
    public void setString(String key, String value){
        _prefsEditor.putString(key, value);
        _prefsEditor.commit();
    }

    /**
     * Get String value from shared Preferences
     * @param key
     * @return
     */
    public String getString(String key){
        return _sharedPreferences.getString(key, "");
    }

    /***
     * Save Long value to Shared Preferences
     * @param key
     * @param value
     */
    public void setLong(String key, long value){
        _prefsEditor.putLong(key, value);
        _prefsEditor.commit();
    }

    /**
     * Get a Long value from shared Preferences
     * @param key
     * @return
     */
    public long getLong(String key){
        return _sharedPreferences.getLong(key, 0);
    }


    /***
     * Save Integer to Shared Preferences
     * @param key
     * @param value
     */
    public void setInt(String key, int value){
        _prefsEditor.putInt(key, value);
        _prefsEditor.commit();
    }

    /**
     * Get an Integer value from shared Preferences
     * @param key
     * @return
     */
    public int getInt(String key){
        return _sharedPreferences.getInt(key, 0);
    }

}
