// FlybitsConciergeManager.java

package com.reactlibrary;

import androidx.activity.ComponentActivity;
import android.app.Application;
import android.graphics.Color;
import android.view.FrameMetrics;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import com.flybits.concierge.*;
import com.flybits.concierge.enums.ShowMode;

public class FlybitsConciergeManager extends SimpleViewManager<FrameLayout> {

    public static final String REACT_CLASS = "RNFlybitsConcierge";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public FrameLayout createViewInstance(ThemedReactContext context) {
        // TODO: Implement some actually useful functionality
       final FrameLayout view = new FrameLayout((context));

        FlybitsConcierge concierge = FlybitsConcierge.with(context);
//
        ConciergeConfiguration conciergeConfiguration  = new ConciergeConfiguration.Builder("PROJECT-ID-HERE")
                .setGatewayUrl("Set Gateway Url Here")
                .build();
//
        concierge.initialize(conciergeConfiguration);
//
        Fragment fragment = ConciergeFragment.newInstance(
                new DisplayConfiguration(
                        ConciergeFragment.MenuType.MENU_TYPE_APP_BAR,
                        ShowMode.OVERLAY,
                        true));

        FragmentActivity a = (FragmentActivity)context.getCurrentActivity();
        a.getSupportFragmentManager().beginTransaction().add(fragment, "MY_TAG").commit();


        view.addView(fragment.getView(), new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));

        view.addView(fragment.getView(), FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        return view;
    }
}


