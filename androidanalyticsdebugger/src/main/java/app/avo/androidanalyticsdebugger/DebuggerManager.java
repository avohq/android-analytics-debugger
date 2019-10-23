package app.avo.androidanalyticsdebugger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.recyclerview.widget.SortedList;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;
import app.avo.androidanalyticsdebugger.debuggerview.BarViewContainer;
import app.avo.androidanalyticsdebugger.debuggerview.BubbleViewContainer;
import app.avo.androidanalyticsdebugger.debuggerview.DebuggerViewContainer;

public class DebuggerManager {

    public static SortedList<DebuggerEventItem> events = new SortedList<>(DebuggerEventItem.class,
            new EventsSorting());
    public static Runnable eventUpdateListener = null;

    private static WeakReference<DebuggerViewContainer> debuggerViewContainerRef =
            new WeakReference<>(null);

    public void showDebugger(final Activity rootActivity, DebuggerMode mode) {
        showDebugger(rootActivity, mode, false);
    }

    @SuppressWarnings("WeakerAccess")
    public void showDebugger(final Activity rootActivity, DebuggerMode mode, boolean systemOverlay) {

        if (checkDrawOverlayPermission(rootActivity)) {
            hideDebugger(rootActivity);

            final WindowManager windowManager = rootActivity.getWindowManager();
            final DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);

            final WindowManager.LayoutParams layoutParams
                    = prepareWindowManagerLayoutParams(rootActivity, displayMetrics, systemOverlay);

            final DebuggerViewContainer debuggerViewContainer = createDebuggerView(rootActivity, mode,
                    layoutParams);

            windowManager.addView(debuggerViewContainer.getView(), layoutParams);

            debuggerViewContainer.getView().setOnTouchListener(new DebuggerTouchHandler(windowManager,
                    layoutParams, debuggerViewContainer));

            debuggerViewContainerRef = new WeakReference<>(debuggerViewContainer);
        }
    }

    private boolean checkDrawOverlayPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(activity)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(intent, 101);
                Toast.makeText(activity, "Enable \"Draw over other apps\" in " +
                                "Settings - Apps - Your app to use the debugger and restart the app",
                        Toast.LENGTH_LONG).show();
                return false;
            } else {
               return true;
            }
        } else {
            return true;
        }
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
            events.add(event);
            if (eventUpdateListener != null) {
                eventUpdateListener.run();
            }
        }
    }

    @SuppressWarnings("unused")
    public void publishEvent(String id, Long timestamp, String name,
                             List<Map<String, String>> messages,
                             List<Map<String, String>> eventProps,
                             List<Map<String, String>> userProps) {

        DebuggerEventItem event = new DebuggerEventItem(id, timestamp, name,
                messages, eventProps, userProps);

        publishEvent(event);
    }

    @SuppressWarnings("unused")
    public Boolean isEnabled() {
        DebuggerViewContainer debuggerViewContainer = debuggerViewContainerRef.get();
        return debuggerViewContainer != null;
    }

    private WindowManager.LayoutParams prepareWindowManagerLayoutParams(Context context,
                                                                        DisplayMetrics displayMetrics,
                                                                        boolean overlay) {
        int barHeight = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            barHeight = resources.getDimensionPixelSize(resourceId);
        }

        int LAYOUT_FLAG;
        if (overlay) {
            if (Build.VERSION.SDK_INT >= 26) {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
            }
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION;
        }

        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = LAYOUT_FLAG;
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

    public void hideDebugger(Activity anyActivity) {
        DebuggerViewContainer debuggerViewContainer = debuggerViewContainerRef.get();
        if (debuggerViewContainer != null) {
            try {
                anyActivity.getWindowManager().removeView(debuggerViewContainer.getView());
                debuggerViewContainerRef = new WeakReference<>(null);
            } catch (Throwable ignored) {}
        }
    }

    private static class EventsSorting extends SortedList.Callback<DebuggerEventItem> {
        @Override
        public int compare(DebuggerEventItem o1, DebuggerEventItem o2) {
            return o2.timestamp.compareTo(o1.timestamp);
        }

        @Override
        public void onChanged(int position, int count) {}

        @Override
        public boolean areContentsTheSame(DebuggerEventItem oldItem, DebuggerEventItem newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(DebuggerEventItem item1, DebuggerEventItem item2) {
            return item1 == item2;
        }

        @Override
        public void onInserted(int position, int count) {}

        @Override
        public void onRemoved(int position, int count) {}

        @Override
        public void onMoved(int fromPosition, int toPosition) {}
    }
}