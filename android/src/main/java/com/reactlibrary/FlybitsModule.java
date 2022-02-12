package com.reactlibrary;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.flybits.commons.library.api.Region;
import com.flybits.commons.library.api.idps.AnonymousIDP;
import com.flybits.commons.library.logging.VerbosityLevel;
import com.flybits.concierge.ConciergeConfiguration;
import com.flybits.concierge.ConciergeFragment;
import com.flybits.concierge.DisplayConfiguration;
import com.flybits.concierge.FlybitsConcierge;
import com.flybits.concierge.enums.ShowMode;
import com.flybits.context.ContextManager;
import com.flybits.context.ReservedContextPlugin;
import com.flybits.context.plugins.FlybitsContextPlugin;


public class FlybitsModule extends ReactContextBaseJavaModule {
    FlybitsModule(ReactApplicationContext context) {
        super(context);
    }

    private final Context context = getReactApplicationContext();
    private final FlybitsConcierge concierge = FlybitsConcierge.with(context);

    @NonNull
    @Override
    public String getName() {
        return "FlybitsModule";
    }

    @ReactMethod
    public void flybitsConnect() {
        concierge.setLoggingVerbosity(VerbosityLevel.ALL);
        ConciergeConfiguration conciergeConfiguration =
                new ConciergeConfiguration.Builder("73C75DFB-3728-4062-943F-2D7FCDA19B75")
                .setGatewayUrl(Region.CANADA.getUrl())
                .build();
        concierge.initialize(conciergeConfiguration);
        concierge.authenticate(new AnonymousIDP());
    }

    @ReactMethod
    public void connectToFlybits() {
        concierge.setLoggingVerbosity(VerbosityLevel.ALL);
        ConciergeConfiguration conciergeConfiguration = new
                ConciergeConfiguration.Builder("73C75DFB-3728-4062-943F-2D7FCDA19B75")
                .setGatewayUrl(Region.CANADA.getUrl())
                .build();
        concierge.initialize(conciergeConfiguration);
        concierge.authenticate(new AnonymousIDP());
    }


    @ReactMethod
    public void sendBattery() {
        ContextManager.start(
                context,
                new FlybitsContextPlugin.Builder(ReservedContextPlugin.BATTERY).build()
        );
    }

    @ReactMethod
    public void sendContext() {
        ContextManager.start(
                context,
                new FlybitsContextPlugin.Builder(ReservedContextPlugin.BATTERY).build()
        );
    }

    @ReactMethod
    public void configureFlybits() {
        Log.d("flybitsConnect", "configureFlybits");
    }

//
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