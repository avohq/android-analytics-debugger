package app.avo.androidanalyticsdebugger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.ref.WeakReference;


public class Debugger {

    private static WeakReference<View> debuggerViewRef;

    @SuppressLint("ClickableViewAccessibility")
    public void showDebugger(final Activity rootActivity, DebuggerMode mode) {
        final WindowManager windowManager = rootActivity.getWindowManager();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        final WindowManager.LayoutParams layoutParams = prepareWindowManagerLayoutParams(rootActivity, displayMetrics);

        final View debuggerView = createDebuggerView(rootActivity, mode, layoutParams);

        windowManager.addView(debuggerView, layoutParams);

        debuggerView.setOnTouchListener(new DebuggerTouchHandler(windowManager, layoutParams));

        debuggerViewRef = new WeakReference<>(debuggerView);
    }

    private View createDebuggerView(Activity rootActivity, DebuggerMode mode, WindowManager.LayoutParams layoutParams) {
        final View debuggerView;
        if (mode == DebuggerMode.bar) {
            debuggerView = createBarView(rootActivity, layoutParams);
        } else {
            debuggerView = createBubbleView(rootActivity, layoutParams);
        }
        return debuggerView;
    }

    private WindowManager.LayoutParams prepareWindowManagerLayoutParams(Context context, DisplayMetrics displayMetrics) {
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

    private View createBubbleView(Activity rootActivity, WindowManager.LayoutParams layoutParams) {
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        return rootActivity.getLayoutInflater().inflate(R.layout.bubble_view, null);
    }

    private View createBarView(Activity rootActivity, WindowManager.LayoutParams layoutParams) {
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        return rootActivity.getLayoutInflater().inflate(R.layout.bar_view, null);
    }

    public void hideDebugger(Activity rootActivity) {
        View debuggerView = debuggerViewRef.get();
        if (debuggerView != null) {
            rootActivity.getWindowManager().removeView(debuggerView);
        }
    }

/*    private float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }*/
}