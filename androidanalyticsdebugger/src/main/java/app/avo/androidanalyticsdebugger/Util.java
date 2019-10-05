package app.avo.androidanalyticsdebugger;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;

public class Util {

    private static boolean eventsHaveErrors(List<DebuggerEventItem> items) {
        if (items == null) {
            return false;
        }

        for (DebuggerEventItem event: items) {
            if (event.messages != null && !event.messages.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean eventHaveErrors(final DebuggerEventItem event) {
        return eventsHaveErrors(new ArrayList<DebuggerEventItem>() {{add(event);}});
    }

    public static String timeString(@Nullable Long timestamp) {
        if (timestamp == null) {
            return "";
        }

        return new SimpleDateFormat("HH:mm:ss.ms", Locale.US).format(new Date(timestamp));
    }

    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
