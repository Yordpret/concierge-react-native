// FlybitsConciergeManager.java

package com.reactlibrary;

import androidx.activity.ComponentActivity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Choreographer;
import android.view.FrameMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import com.flybits.commons.library.api.Region;
import com.flybits.commons.library.api.idps.AnonymousIDP;
import com.flybits.commons.library.logging.VerbosityLevel;
import com.flybits.concierge.*;
import com.flybits.concierge.enums.ShowMode;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

public class FlybitsConciergeManager extends SimpleViewManager<FrameLayout> {

    public static final String REACT_CLASS = "RNFlybitsConcierge";
//    public static final String REACT_CLASS = "FlybitsModule";

    public int COMMAND_CREATE = 1;
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public FrameLayout createViewInstance(ThemedReactContext context) {
        // TODO: Implement some actually useful functionality
         FlybitsConcierge concierge = FlybitsConcierge.with(context);
         concierge.setLoggingVerbosity(VerbosityLevel.ALL);
         ConciergeConfiguration conciergeConfiguration  = new ConciergeConfiguration.Builder("73C75DFB-3728-4062-943F-2D7FCDA19B75")
                 .setGatewayUrl(Region.CANADA.getUrl())
                 .build();
         concierge.initialize(conciergeConfiguration);
        Fragment  conciergeFragment = ConciergeFragment.newInstance(  new DisplayConfiguration(
                ConciergeFragment.MenuType.MENU_TYPE_APP_BAR,
                ShowMode.NEW_ACTIVITY,
                true));

        concierge.authenticate(new AnonymousIDP());
        FragmentActivity a = (FragmentActivity)context.getCurrentActivity();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.frame, null);
        setupLayoutHack(frameLayout);

        a.getSupportFragmentManager().beginTransaction().replace(
                R.id.concierge_fragment,
                conciergeFragment,
                ConciergeFragment.CONCIERGE_FRAGMENT_TAG
        ).commit();
        return frameLayout;
    }

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(
                "create", COMMAND_CREATE
        );
    }

    void setupLayoutHack(ViewGroup view) {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                manuallyLayoutChildren(view);
                view.getViewTreeObserver().dispatchOnGlobalLayout();
                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }

    void manuallyLayoutChildren(ViewGroup view) {
        for (int i = 0; i < view.getChildCount(); i++) {
            View child = view.getChildAt(i);
            child.measure(
                    View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(),
                            View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(),
                            View.MeasureSpec.EXACTLY));
            child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        }
    }
}


