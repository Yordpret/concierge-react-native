package com.reactlibrary

import android.content.Context
import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.flybits.commons.library.SharedElementsFactory.get
import com.flybits.commons.library.api.Region
import com.flybits.commons.library.api.idps.AnonymousIDP
import com.flybits.commons.library.logging.VerbosityLevel
import com.flybits.concierge.ConciergeConfiguration
import com.flybits.concierge.FlybitsConcierge
import com.flybits.context.ContextManager.start
import com.flybits.context.ReservedContextPlugin
import com.flybits.context.plugins.FlybitsContextPlugin
import com.google.firebase.iid.FirebaseInstanceId

class FlybitsModule internal constructor(context: ReactApplicationContext?) :
    ReactContextBaseJavaModule(context) {
    private val context: Context = reactApplicationContext
    private val concierge = FlybitsConcierge.with(context)
    override fun getName(): String {
        return "FlybitsModule"
    }

    @ReactMethod
    fun flybitsConnect() {
        val sharedElements = get(context)
        sharedElements.setUniqueDevice("testdeviceid")
        concierge.authenticate(AnonymousIDP())
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener{
            Log.e("flybits_debug", "FirebaseInstanceId ${it.id}")
        }
    }

    @ReactMethod
    fun connectToFlybits() {
        concierge.setLoggingVerbosity(VerbosityLevel.ALL)
        val conciergeConfiguration: ConciergeConfiguration =
            ConciergeConfiguration.Builder("73C75DFB-3728-4062-943F-2D7FCDA19B75")
                .setGatewayUrl(Region.CANADA.url)
                .build()
        concierge.initialize(conciergeConfiguration)
        concierge.authenticate(AnonymousIDP())
    }

    @ReactMethod
    fun sendBattery() {
        start(
            context,
            FlybitsContextPlugin.Builder(ReservedContextPlugin.BATTERY).build()
        )
    }

    @ReactMethod
    fun sendContext() {
        start(
            context,
            FlybitsContextPlugin.Builder(ReservedContextPlugin.BATTERY).build()
        )
    }

    @ReactMethod
    fun configureFlybits() {
        Log.d("flybitsConnect", "configureFlybits")
    } //
    //    @ReactMethod
    //    public void flybitsShow() {
    //        DisplayConfiguration displayConfiguration = new DisplayConfiguration(
    //                ConciergeFragment.MenuType.MENU_TYPE_APP_BAR,
    //                ShowMode.NEW_ACTIVITY,
    //                true,
    //                "This is Customizable Title",
    //                "This is Customizable Message", true//or false
    //        );
    //        concierge.show(displayConfiguration);
    //        Log.d("flybitsConnect", "flybitsShow " );
    //    }
}