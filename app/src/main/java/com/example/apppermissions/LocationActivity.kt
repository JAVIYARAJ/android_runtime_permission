package com.example.apppermissions

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import com.example.apppermissions.util.PermissionManager
import com.example.apppermissions.util.PermissionManagerListener
import com.example.apppermissions.util.Utils
import java.util.*

class LocationActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    private lateinit var longitudeText: TextView;
    private lateinit var latitudeText: TextView;
    lateinit var city: TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_acitivity)


        var fetchBtn = findViewById<AppCompatButton>(R.id.fetchLocation);
        longitudeText = findViewById<TextView>(R.id.longitudeText);
        latitudeText = findViewById<TextView>(R.id.latitudeText);
        city = findViewById<TextView>(R.id.city);


        var permissionManager = PermissionManager("data", applicationContext);
        permissionManager.checkPermission(
            applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            object : PermissionManagerListener {
                override fun onNeedPermission() {
                    Utils.requestPermission(
                        this@LocationActivity,
                        listOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        102
                    );
                }

                @RequiresApi(Build.VERSION_CODES.TIRAMISU)
                override fun onPermissionGranted() {
                    //get location
                    getLocation();
                }

                override fun onPermissionPreviouslyDenied() {
                    Utils.requestPermission(
                        this@LocationActivity,
                        listOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        102
                    );
                }

                override fun onPermissionPreviouslyDeniedWithNeverAskAgain() {
                    Toast.makeText(
                        applicationContext, "allow permission from setting", Toast.LENGTH_LONG
                    ).show()
                    var intent = Intent();
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
                    intent.data = Uri.parse("package:$packageName");
                    startActivity(intent);
                }

            },
            this
        );
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    fun getLocation() {
        try {
            var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager;

            //check gps is enable
            var hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            var hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (hasGps && hasNetwork) {

                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 1000, .02F
                ) {
                    longitudeText.text = it.longitude.toString();
                    latitudeText.text = it.latitude.toString();

                    var geocoder = Geocoder(applicationContext, Locale.getDefault());
                    var address = geocoder.getFromLocation(it.latitude, it.longitude, 1,
                        (Geocoder.GeocodeListener { addresses -> city.text = addresses?.get(0)?.locality ?: "test"; })
                    )

                }
            } else {
                Toast.makeText(
                    applicationContext, "please start network and gps", Toast.LENGTH_LONG
                ).show();
            }
        } catch (e: Exception) {

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == 102 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(applicationContext, "ready", Toast.LENGTH_LONG).show();
        } else {
            Utils.requestPermission(
                this@LocationActivity,
                listOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                102
            );
            Toast.makeText(applicationContext, "not ready", Toast.LENGTH_LONG).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}