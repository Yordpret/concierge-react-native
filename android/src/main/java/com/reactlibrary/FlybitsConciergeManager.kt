// FlybitsConciergeManager.java
package com.reactlibrary

import android.content.Context
import android.util.Log
import android.view.Choreographer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.flybits.commons.library.logging.VerbosityLevel
import com.flybits.concierge.*
import com.flybits.concierge.enums.ConciergeOptions
import com.flybits.concierge.enums.Container



class FlybitsConciergeManager : SimpleViewManager<FrameLayout>() {
    //    public static final String REACT_CLASS = "FlybitsModule";
    var COMMAND_CREATE = 1
    override fun getName(): String {
        return REACT_CLASS
    }

    private val TENANT_ID = "C1A63879-DEC8-4EF0-B3D6-A89AE8E5FFEE"
    private val GATEWAY = "https://api.mc-sg.flybits.com"
    public override fun createViewInstance(context: ThemedReactContext): FrameLayout {
        Concierge.setLoggingVerbosity(VerbosityLevel.ALL)

        val conciergeConfiguration = FlybitsConciergeConfiguration.Builder(context)
            .setProjectId(TENANT_ID)
            .setGateWayUrl(GATEWAY)
            .build()

        Concierge.configure(conciergeConfiguration, emptyList(), context)

        val conciergeFragment = Concierge.fragment(
            context,
            Container.None,
            null,
            emptyList()
        )
        Log.e("flybits_debug", "createViewInstance: in FlybitsConciergeManager")

        val a = context.currentActivity as FragmentActivity?
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val frameLayout = inflater.inflate(R.layout.frame, null) as FrameLayout
        setupLayoutHack(frameLayout)
        a!!.supportFragmentManager.beginTransaction().replace(
            R.id.concierge_fragment,
            conciergeFragment,
            ConciergeFragment.CONCIERGE_LOG_TAG
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