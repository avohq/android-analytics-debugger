package app.avo.androidanalyticsdebugger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;
import app.avo.androidanalyticsdebugger.debuggerview.BarViewContainer;
import app.avo.androidanalyticsdebugger.debuggerview.BubbleViewContainer;
import app.avo.androidanalyticsdebugger.debuggerview.DebuggerViewContainer;

public class Debugger {

    public static List<DebuggerEventItem> events = new ArrayList<>();
    public static Runnable eventUpdateListener = null;

    private static WeakReference<DebuggerViewContainer> debuggerViewContainerRef = new WeakReference<>(null);

    @SuppressLint("ClickableViewAccessibility")
    public void showDebugger(final Activity rootActivity, DebuggerMode mode) {

        hideDebugger(rootActivity);

        final WindowManager windowManager = rootActivity.getWindowManager();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        final WindowManager.LayoutParams layoutParams
                = prepareWindowManagerLayoutParams(rootActivity, displayMetrics);

        final DebuggerViewContainer debuggerViewContainer = createDebuggerView(rootActivity, mode,
                layoutParams);

        windowManager.addView(debuggerViewContainer.getView(), layoutParams);

        debuggerViewContainer.getView().setOnTouchListener(new DebuggerTouchHandler(windowManager,
                layoutParams, debuggerViewContainer));

        debuggerViewContainerRef = new WeakReference<>(debuggerViewContainer);
    }

    private DebuggerViewContainer createDebuggerView(Activity rootActivity, DebuggerMode mode,
                                                     WindowManager.LayoutParams layoutParams) {
        final DebuggerViewContainer debuggerViewContainer;
        if (mode == DebuggerMode.bar) {
            debuggerViewContainer = createBarView(rootActivity, layoutParams);
        } else {
            debuggerViewContainer = createBubbleView(rootActivity, layoutParams);
        }
        return debuggerViewContainer;
    }

    public void publishEvent(DebuggerEventItem event) {
        DebuggerViewContainer debuggerViewContainer = debuggerViewContainerRef.get();

        if (debuggerViewContainer != null) {
            debuggerViewContainer.showEvent(event);
            events.add(0, event);
            if (eventUpdateListener != null) {
                eventUpdateListener.run();
            }
        }
    }

    private WindowManager.LayoutParams prepareWindowManagerLayoutParams(Context context,
                                                                        DisplayMetrics displayMetrics) {
        int barHeight = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            barHeight = resources.getDimensionPixelSize(resourceId);
        }

        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        layoutParams.y = (displayMetrics.heightPixels - barHeight) / 2;
        layoutParams.x = (displayMetrics.widthPixels) / 2;

        return layoutParams;
    }

    private DebuggerViewContainer createBubbleView(Activity rootActivity, WindowManager.LayoutParams layoutParams) {
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;

        return new BubbleViewContainer(rootActivity.getLayoutInflater());
    }

    private DebuggerViewContainer createBarView(Activity rootActivity, WindowManager.LayoutParams layoutParams) {
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

        return new BarViewContainer(rootActivity.getLayoutInflater());
    }

    public void hideDebugger(Activity rootActivity) {
        DebuggerViewContainer debuggerViewContainer = debuggerViewContainerRef.get();
        if (debuggerViewContainer != null) {
            try {
                rootActivity.getWindowManager().removeView(debuggerViewContainer.getView());
            } catch (Throwable ignored) {}
        }
    }
}