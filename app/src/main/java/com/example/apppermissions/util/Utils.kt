package com.example.apppermissions.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission

class Utils {
    companion object {
        //here package manager contain all information about installed packages in your mobile device.
        fun checkPermission(context: Context, permission: String): Boolean {
            return checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

        }

        fun requestPermission(activity: Activity, permission: List<String>, statusCode: Int) {
            ActivityCompat.requestPermissions(activity, permission.toTypedArray(), statusCode);
        }

    }
}