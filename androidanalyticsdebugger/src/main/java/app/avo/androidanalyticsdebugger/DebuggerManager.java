package app.avo.androidanalyticsdebugger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.SortedList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import app.avo.androidanalyticsdebugger.debuggereventslist.NewEventListener;
import app.avo.androidanalyticsdebugger.debuggerview.BarViewContainer;
import app.avo.androidanalyticsdebugger.debuggerview.BubbleViewContainer;
import app.avo.androidanalyticsdebugger.debuggerview.DebuggerViewContainer;
import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;

public class DebuggerManager {

    public @Nullable String schemaId = null;

    public static SortedList<DebuggerEventItem> events = new SortedList<>(DebuggerEventItem.class,
            new EventsSorting());
    public static NewEventListener eventUpdateListener = null;

    static WeakReference<DebuggerViewContainer> debuggerViewContainerRef =
            new WeakReference<>(null);

    @SuppressLint("HardwareIds")
    public DebuggerManager(@NonNull Context context) {
        String deviceId = "unknown";
        //noinspection ConstantConditions
        if (context != null) {
            deviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }

        final String finalDeviceId = deviceId;

        DebuggerAnalytics.initAvo(DebuggerAnalytics.AvoEnv.PROD, DebuggerAnalytics.Client.ANDROID_DEBUGGER,
                BuildConfig.VERSION_NAME, new DebuggerAnalytics.ICustomDestination() {
                    @Override
                    public void make(DebuggerAnalytics.AvoEnv env) {}

                    @Override
                    public void logEvent(String eventName, Map<String, Object> eventProperties) {
                        trackDebuggerStarted(finalDeviceId, eventName, eventProperties);
                    }

                    @Override
                    public void setUserProperties(String userId, Map<String, Object> userProperties) {}

                    @Override
                    public void identify(String userId) {}

                    @Override
                    public void unidentify() {}
                });
    }

    public void showDebugger(final Activity rootActivity, DebuggerMode mode) {
        showDebugger(rootActivity, mode, false);
    }

    public void setSchemaId(@NonNull String schemaId) {
        this.schemaId = schemaId;
    }

    @SuppressWarnings("WeakerAccess")
    public void showDebugger(final Activity rootActivity, DebuggerMode mode, boolean systemOverlay) {

        if (!systemOverlay || checkDrawOverlayPermission(rootActivity)) {
            hideDebugger(rootActivity);

            final WindowManager windowManager = rootActivity.getWindowManager();
            final DisplayMetrics displayMetrics = new DisplayMetrics();
            final Display display = windowManager.getDefaultDisplay();
            if (display != null) {
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            }

            final WindowManager.LayoutParams layoutParams
                    = prepareWindowManagerLayoutParams(rootActivity, displayMetrics, systemOverlay);

            final DebuggerViewContainer debuggerViewContainer = createDebuggerView(rootActivity, mode,
                    layoutParams);

            windowManager.addView(debuggerViewContainer.getView(), layoutParams);

            debuggerViewContainer.getView().setOnTouchListener(new DebuggerTouchHandler(windowManager,
                    layoutParams, debuggerViewContainer));

            debuggerViewContainerRef = new WeakReference<>(debuggerViewContainer);

            for (int i = events.size() - 1; i >= 0 ; i--) {
                DebuggerEventItem event = events.get(i);
                debuggerViewContainer.showEvent(event);
            }

            DebuggerAnalytics.debuggerStarted(null, schemaId);
        }
    }

    private void trackDebuggerStarted(final String deviceId, final String eventName, final Map eventProperties) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL apiUrl = new URL("https://api.avo.app/c/v1/track");

                    JSONObject body = buildRequestBody(deviceId, eventName, eventProperties);

                    HttpsURLConnection connection = null;
                    try {
                        connection = (HttpsURLConnection) apiUrl.openConnection();

                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);

                        writeTrackingCallHeader(connection);
                        writeTrackingCallBody(body, connection);

                        connection.connect();

                        int responseCode = connection.getResponseCode();
                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            throw new IOException("HTTP error code: " + responseCode);
                        }
                    } finally {
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void writeTrackingCallHeader(HttpsURLConnection connection) {
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");
    }

    private void writeTrackingCallBody(JSONObject body, HttpsURLConnection connection) throws IOException {
        byte[] bodyBytes = body.toString().getBytes("UTF-8");
        OutputStream os = connection.getOutputStream();
        os.write(bodyBytes);
        os.close();
    }

    private JSONObject buildRequestBody(String deviceId, String eventName, Map eventProps) throws JSONException {

        JSONObject eventProperties = new JSONObject(eventProps);

        JSONObject body = new JSONObject();
        body.put("deviceId", deviceId);
        body.put("eventName", eventName);
        body.put("eventProperties", eventProperties);

        return body;
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

    public void publishEvent(long timestamp, String name, List<EventProperty> properties, @Nullable List<PropertyError> errors) {

        List<Map<String, String>> eventProps = new ArrayList<>();

        for (EventProperty eventProperty: properties) {
            Map<String, String> propMap = new HashMap<>();

            propMap.put("id", eventProperty.getId());
            propMap.put("name", eventProperty.getName());
            propMap.put("value", eventProperty.getValue());

            eventProps.add(propMap);
        }

        List<Map<String, String>> messages = new ArrayList<>();
        if (errors != null) {
            for (PropertyError propertyError: errors) {
                Map<String, String> errorsMap = new HashMap<>();

                errorsMap.put("propertyId", propertyError.getPropertyId());
                errorsMap.put("message", propertyError.getMessage());

                messages.add(errorsMap);
            }
        }

        DebuggerEventItem event = new DebuggerEventItem(UUID.randomUUID().toString(),
                timestamp, name, messages, eventProps, null);

        publishEvent(event);
    }

    public void publishEvent(DebuggerEventItem event) {
        DebuggerViewContainer debuggerViewContainer = debuggerViewContainerRef.get();

        if (debuggerViewContainer != null) {
            debuggerViewContainer.showEvent(event);
        }

        events.add(event);
        if (eventUpdateListener != null) {
            eventUpdateListener.onNewEvent(event);
        }
    }

    @SuppressWarnings("unused")
    public void publishEvent(String id, Long timestamp, String name,
                             List<Map<String, String>> errors,
                             List<Map<String, String>> eventProps,
                             List<Map<String, String>> userProps) {

        DebuggerEventItem event = new DebuggerEventItem(id, timestamp, name,
                errors, eventProps, userProps);

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