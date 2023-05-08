package com.example.apppermissions.util

interface PermissionManagerListener {

    fun onNeedPermission();  //request permission
    fun onPermissionGranted(); // ready for use requested permission feature
    fun onPermissionPreviouslyDenied(); // feature not work without allow requested permission
    fun onPermissionPreviouslyDeniedWithNeverAskAgain(); //feature not work please allow permission from settings
}