package com.example.apppermissions.util

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(prefName: String, context: Context) {

    //initialize shared pref
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    //initialize editor
    var editor: SharedPreferences.Editor = sharedPreferences.edit();


    //save changes
    private fun commitChanges() {
        if (sharedPreferences != null) {
            editor.commit();
        }
    }

    //save data into shared pref
    fun saveData(value: Boolean) {
        editor.putBoolean("permission", value);
        commitChanges();
    }

    //get permission boolean
    fun getStorageValue(key: String): Boolean {
        return sharedPreferences.getBoolean(key, true);
    }
}