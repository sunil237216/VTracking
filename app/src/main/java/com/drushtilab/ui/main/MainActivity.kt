package com.drushtilab.ui.main

import android.Manifest
import android.content.Context

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.drushtilab.R
import com.drushtilab.ui.base.BaseActivity
import com.drushtilab.utils.addFragment
import com.drushtilab.utils.replaceFragment
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import android.content.pm.PackageManager

import android.location.Location
import android.location.LocationManager
import android.os.Looper

import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.drushtilab.network.model.DropRequest
import com.drushtilab.network.model.ReportRequest
import com.drushtilab.network.model.prequest
import com.drushtilab.utils.AppConstants.Companion.DROP
import com.drushtilab.utils.AppConstants.Companion.DROP_MSG
import com.drushtilab.utils.AppConstants.Companion.PICK
import com.drushtilab.utils.AppConstants.Companion.PICK_MSG
import com.drushtilab.utils.AppConstants.Companion.REPORT
import com.drushtilab.utils.AppConstants.Companion.RFD
import com.drushtilab.utils.AppConstants.Companion.ROLL
import com.google.android.gms.location.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var mainViewModel: MainViewModel

    //location variable

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val INTERVAL: Long = 2000
    private val FASTEST_INTERVAL: Long = 1000
    lateinit var mLastLocation: Location
    internal lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MainViewModel::class.java)
        setContentView(

            R.layout.activity_main
        )
        mLocationRequest = LocationRequest()
        startLocationUpdates()

        addfragment()

        checkPermissionForLocation(this)
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }

        mainViewModel?.isLoading?.observe(this, Observer {
            if(it == true)
            {
              showDialog()
            }
            else
            {
               hideDialog()
            }

        })
    }

    fun complete(winno: String,reporttext:String) {


        if(::mLastLocation.isInitialized) {
            if (mainViewModel.status.value == 1) {
                val request =
                    prequest(mLastLocation.latitude, mLastLocation.longitude, mainViewModel.status.value!!, winno)
                mainViewModel.complete(request);
            }
            if (mainViewModel.status.value == 1 && mainViewModel.requestType.value == RFD) {
                val request =
                    DropRequest(mLastLocation.latitude, mLastLocation.longitude, mainViewModel.status.value!!, winno)
                mainViewModel.completeDrop(request);
            } else if (mainViewModel.status.value == 2) {
                val request =
                    DropRequest(mLastLocation.latitude, mLastLocation.longitude, mainViewModel.status.value!!, winno)
                mainViewModel.completeDrop(request);
            } else if (mainViewModel.status.value == 3 && mainViewModel.requestType.value == REPORT) {
                val request = ReportRequest(reporttext, winno)
                mainViewModel.sendReport(request);
            } else if (mainViewModel.requestType.value == ROLL) {
                val request =
                    prequest(mLastLocation.latitude, mLastLocation.longitude, mainViewModel.status.value!!, winno)
                mainViewModel.complete(request);
            }
        }
    }


    fun addfragment() {
        val fragment = LandingFragment()
        addFragment(fragment, R.id.container, "tag")
    }

    fun launchFragment()
    {
        val fragment = ScanFragment()
        replaceFragment(fragment, R.id.container, "tag")
    }

    fun replaceFragment() {


        if(mainViewModel.requestType.value == ROLL)

        {
            launchFragment()
        }

       else if(mainViewModel.requestType.value == PICK)
        {
            if(mainViewModel.vehicleStatus.value == 2)
            {
                launchFragment()
            }
            else{
                fireToast(DROP_MSG)
            }
        }

        else if(mainViewModel.requestType.value == DROP) {

            if(mainViewModel.vehicleStatus.value == 1) {
                launchFragment()
            }
            else{

                fireToast(PICK_MSG)
            }
        }

         else  if(mainViewModel.requestType.value == RFD) {
            if(mainViewModel.vehicleStatus.value == 1) {
                val fragment = ScanFragment()
                replaceFragment(fragment, R.id.container, "tag")
            }
            else{

               fireToast(PICK_MSG)
            }
        }

        else if(mainViewModel.requestType.value == REPORT) {
            launchFragment()
        }

    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
        stoplocationUpdates()
    }


    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }


    protected fun startLocationUpdates() {

        // Create the location request to start receiving updates

        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.setInterval(INTERVAL)
        mLocationRequest!!.setFastestInterval(FASTEST_INTERVAL)

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {


            return
        }
        mFusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // do work here
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

      fun onLocationChanged(location: Location) {
        // New location has now been determined

        mLastLocation = location
        val date: Date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("hh:mm:ss a")

        mainViewModel.location.postValue(mLastLocation);
        println("latitude " + mLastLocation.latitude)
        println("longiude " + mLastLocation.longitude)

    }

    private fun stoplocationUpdates() {
        mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()

            } else {
                Toast.makeText(this@MainActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                // Show the permission request
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }
    }

    override fun onBackPressed() {
        val fragments:Int =  supportFragmentManager.backStackEntryCount

        if(fragments == 0)
        {
           logoutAlert()
        }
        else {
            super.onBackPressed()

        }

    }

}
