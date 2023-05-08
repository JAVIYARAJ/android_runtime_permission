package com.example.apppermissions.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat

class PermissionManager(prefName: String, context: Context) {


    var sharedPrefManager = SharedPrefManager(prefName, context);


    //check version of SDK because run time permission work if SDK>=23
    private fun checkVersion(): Boolean {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    }

    //check that if it is already permission granted or not
    private fun shouldAskPermission(context: Context, permission: String): Boolean {
        if (checkVersion()) {
            var result = ActivityCompat.checkSelfPermission(context, permission);
            return result != PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    //handle all cases that require to request run time permission
    fun checkPermission(
        context: Context,
        permission: String,
        listener: PermissionManagerListener,
        activity: Activity
    ) {
        if (shouldAskPermission(context, permission)) {
            //this case for user denied permission without checking ask again checkbox
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity, permission
                )
            ) {
                listener.onPermissionPreviouslyDenied();
            } else {
                //return true means request permission first time
                if (sharedPrefManager.getStorageValue("permission")) {
                    sharedPrefManager.saveData(false);
                    listener.onNeedPermission();
                    //here request denied by user and check never ask again checkbox
                } else {
                    listener.onPermissionPreviouslyDeniedWithNeverAskAgain();
                }
            }
        } else {
            listener.onPermissionGranted();
        }
    }

}