package com.reactlibrary

import android.content.Context
import android.util.Log
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

class FlybitsModule internal constructor(context: ReactApplicationContext?) :
    ReactContextBaseJavaModule(context) {
    private val context: Context = reactApplicationContext
    override fun getName(): String {
        return "FlybitsModule"
    }

    private val TENANT_ID = "C1A63879-DEC8-4EF0-B3D6-A89AE8E5FFEE"
    private val GATEWAY = "https://api.mc-sg.flybits.com"

    @ReactMethod
    fun flybitsConnect() {
        Concierge.connect(context, AnonymousConciergeIDP())
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            Log.e("flybits_debug", "FirebaseInstanceId ${it.id}")
        }
    }

    @ReactMethod
    fun connectToFlybits() {
        flybitsConnect()
    }

    @ReactMethod
    fun sendBattery() {
//        val batteryData = BatteryData(true, 100)
//        batteryData.updateNow(context, object : BasicResultCallback {
//            override fun onException(exception: FlybitsException) {
//                Log.e("flybits_debug", "NO!!!!!")
//            }
//
//            override fun onSuccess() {
//                Log.e("flybits_debug", "YES!!!")
//            }
//        })

        Log.e("flybits_debug", "NO!!!!!")
    }

    @ReactMethod
    fun configureFlybits() {
        Concierge.setLoggingVerbosity(VerbosityLevel.ALL)

        val conciergeConfiguration = FlybitsConciergeConfiguration.Builder(context)
            .setProjectId(TENANT_ID)
            .setGateWayUrl(GATEWAY)
            .build()

        Concierge.configure(conciergeConfiguration, emptyList(), context)
        Log.d("flybitsConnect", "configureFlybits")
    }
}