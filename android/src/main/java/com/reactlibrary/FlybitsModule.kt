package com.reactlibrary

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.flybits.commons.library.api.results.callbacks.BasicResultCallback
import com.flybits.commons.library.exceptions.FlybitsException
import com.flybits.commons.library.logging.VerbosityLevel
import com.flybits.concierge.Concierge
import com.flybits.concierge.FlybitsConciergeConfiguration
import com.flybits.context.ContextManager
import com.flybits.context.plugins.battery.BatteryData
import com.flybits.flybitscoreconcierge.idps.AnonymousConciergeIDP
import com.google.firebase.iid.FirebaseInstanceId

private const val LOG_TAG = "flybits_debug"

class FlybitsModule internal constructor(context: ReactApplicationContext?) :
    ReactContextBaseJavaModule(context) {
    private val context: Context = reactApplicationContext
    override fun getName(): String {
        return "FlybitsModule"
    }

    private val TENANT_ID = "C1A63879-DEC8-4EF0-B3D6-A89AE8E5FFEE"

    //    private val TENANT_ID = "E80B1836-C335-4D44-ACEA-0F9C2D090926"
    private val GATEWAY = "https://api.mc-sg.flybits.com"
//    private val GATEWAY = "https://api.demo.flybits.com"

    @ReactMethod
    fun flybitsConnect() {
        Concierge.connect(context, AnonymousConciergeIDP())
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            Log.e(LOG_TAG, "FirebaseInstanceId ${it.id}")
        }
    }

    @ReactMethod
    fun connectToFlybits() {
        flybitsConnect()
    }

    @ReactMethod
    fun sendBattery() {
        val batteryData = BatteryData(true, 100)
        batteryData.updateNow(context, object : BasicResultCallback {
            override fun onException(exception: FlybitsException) {
                Log.e(LOG_TAG, "NO!!!!!")
                Toast.makeText(context, "batteryData send onException", Toast.LENGTH_LONG).show()
            }

            override fun onSuccess() {
                Toast.makeText(context, "batteryData send onSuccess", Toast.LENGTH_LONG).show()
                Log.e(LOG_TAG, "YES!!!")
            }
        })
    }

    @ReactMethod
    fun configureFlybits() {
        Concierge.setLoggingVerbosity(VerbosityLevel.ALL)
        val conciergeConfiguration = FlybitsConciergeConfiguration.Builder(context)
            .setProjectId(TENANT_ID)
            .setGateWayUrl(GATEWAY)
            .build()

        Concierge.configure(conciergeConfiguration, emptyList(), context)
        Log.d(LOG_TAG, "configureFlybits")
    }

    @ReactMethod
    fun disconnectToFlybits() {
        Concierge.disconnect(context, callback = object : BasicResultCallback {
            override fun onException(exception: FlybitsException) {
                Toast.makeText(context, "disconnect onException", Toast.LENGTH_LONG).show()
            }
            override fun onSuccess() {
                Toast.makeText(context, "disconnect onSuccess", Toast.LENGTH_LONG).show()
            }
        })
        Log.d(LOG_TAG, "disconnectToFlybits")
    }


}