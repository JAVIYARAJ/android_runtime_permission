package com.example.apppermissions

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import com.example.apppermissions.util.PermissionManager
import com.example.apppermissions.util.PermissionManagerListener
import com.example.apppermissions.util.SharedPrefManager
import com.example.apppermissions.util.Utils

class MainActivity : AppCompatActivity() {

    final var READ_STORAGE_STATUS_CODE = 101;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var runtimeCameraPermission = findViewById<AppCompatButton>(R.id.runtimeCameraPermission);

        //this permission is normal permission so don't need to check at run time
        runtimeCameraPermission.setOnClickListener {
            var cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(cameraIntent);
        }

        var runtimeStoragePermission = findViewById<AppCompatButton>(R.id.runtimeStoragePermission);

        //this permission is normal permission so don't need to check at run time
        runtimeStoragePermission.setOnClickListener {


            /*
            if (Utils.checkPermission(
                    applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Toast.makeText(
                    applicationContext, "Permission is already granted", Toast.LENGTH_LONG
                ).show();
            } else {
                Toast.makeText(applicationContext, "Permission is not granted", Toast.LENGTH_LONG)
                    .show();

                var sharedPrefStatus =
                    SharedPrefManager("data", applicationContext).getStorageValue("permission")

                //return true if permission asked before but user denied without checking don't ask checkbox.

                //return false if permission asked first time or permission asked before but user denied with checking don't ask checkbox.

                //handle second case
                //if shred pref contain permission flag means permission not asked first time and if not than permission denied by user and checking don't ask checkbox

                var result = shouldShowRequestPermissionRationale(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                );


                Utils.requestPermission(
                    this,
                    listOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_STATUS_CODE
                );
            }

             */


            var permissionManger = PermissionManager("data", applicationContext);
            permissionManger.checkPermission(
                applicationContext,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                object : PermissionManagerListener {

                    override fun onNeedPermission() {
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            READ_STORAGE_STATUS_CODE
                        );
                    }


                    override fun onPermissionGranted() {
                        Toast.makeText(
                            applicationContext, "ready to write data", Toast.LENGTH_LONG
                        ).show();
                    }

                    override fun onPermissionPreviouslyDenied() {
                        Toast.makeText(
                            applicationContext,
                            "read data not work without permission read storage",
                            Toast.LENGTH_LONG
                        ).show();

                        Utils.requestPermission(
                            this@MainActivity,
                            listOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            READ_STORAGE_STATUS_CODE
                        ); }

                    override fun onPermissionPreviouslyDeniedWithNeverAskAgain() {
                        Toast.makeText(
                            applicationContext,
                            "Open setting and allow read permission",
                            Toast.LENGTH_LONG
                        ).show();
                        openSetting()
                    }

                },
                this
            )


        }


    }

    fun openSetting() {
        var intent = Intent();
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
        var uri = Uri.parse("package:$packageName");
        intent.data = uri;
        startActivity(intent);
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            READ_STORAGE_STATUS_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        applicationContext,
                        "Storage Permission granted Successfully.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Storage Permission Denied by user!!!!!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}