// FlybitsConciergeManager.java
package com.reactlibrary

import android.content.Context
import android.util.Log
import com.facebook.react.uimanager.SimpleViewManager
import android.widget.FrameLayout
import com.facebook.react.uimanager.ThemedReactContext
import com.flybits.concierge.FlybitsConcierge
import com.flybits.commons.library.logging.VerbosityLevel
import com.flybits.concierge.ConciergeConfiguration
import com.flybits.concierge.ConciergeFragment
import com.flybits.concierge.DisplayConfiguration
import com.flybits.concierge.enums.ShowMode
import com.flybits.commons.library.api.idps.AnonymousIDP
import android.view.LayoutInflater
import com.reactlibrary.R
import android.view.ViewGroup
import android.view.Choreographer
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.facebook.react.common.MapBuilder
import com.flybits.commons.library.api.Region

class FlybitsConciergeManager : SimpleViewManager<FrameLayout>() {
    //    public static final String REACT_CLASS = "FlybitsModule";
    var COMMAND_CREATE = 1
    override fun getName(): String {
        return REACT_CLASS
    }

    public override fun createViewInstance(context: ThemedReactContext): FrameLayout {
        val concierge = FlybitsConcierge.with(context)
        concierge.setLoggingVerbosity(VerbosityLevel.ALL)

        val conciergeConfiguration: ConciergeConfiguration =
            ConciergeConfiguration.Builder("73C75DFB-3728-4062-943F-2D7FCDA19B75")
                .setGatewayUrl(Region.CANADA.url)
                .build()


        concierge.initialize(conciergeConfiguration)
        val conciergeFragment: Fragment = ConciergeFragment.newInstance(
            DisplayConfiguration(
                ConciergeFragment.MenuType.MENU_TYPE_APP_BAR, ShowMode.NEW_ACTIVITY,
                true
            )
        )
        Log.e("flybits_debug", "createViewInstance: in FlybitsConciergeManager", )

        val a = context.currentActivity as FragmentActivity?
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val frameLayout = inflater.inflate(R.layout.frame, null) as FrameLayout
        setupLayoutHack(frameLayout)
        a!!.supportFragmentManager.beginTransaction().replace(
            R.id.concierge_fragment,
            conciergeFragment,
            ConciergeFragment.CONCIERGE_FRAGMENT_TAG
        ).commit()
        return frameLayout
    }

    override fun getCommandsMap(): Map<String, Int>? {
        return MapBuilder.of(
            "create", COMMAND_CREATE
        )
    }

    fun setupLayoutHack(view: ViewGroup) {
        Choreographer.getInstance().postFrameCallback(object : Choreographer.FrameCallback {
            override fun doFrame(frameTimeNanos: Long) {
                manuallyLayoutChildren(view)
                view.viewTreeObserver.dispatchOnGlobalLayout()
                Choreographer.getInstance().postFrameCallback(this)
            }
        })
    }

    fun manuallyLayoutChildren(view: ViewGroup) {
        for (i in 0 until view.childCount) {
            val child = view.getChildAt(i)
            child.measure(
                View.MeasureSpec.makeMeasureSpec(
                    view.measuredWidth,
                    View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.makeMeasureSpec(
                    view.measuredHeight,
                    View.MeasureSpec.EXACTLY
                )
            )
            child.layout(0, 0, child.measuredWidth, child.measuredHeight)
        }
    }

    companion object {
        const val REACT_CLASS = "RNFlybitsConcierge"
    }
}