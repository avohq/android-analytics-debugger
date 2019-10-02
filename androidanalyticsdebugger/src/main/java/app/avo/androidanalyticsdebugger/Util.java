package app.avo.androidanalyticsdebugger;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;

public class Util {

    public static boolean eventsHaveErrors(List<DebuggerEventItem> items) {
        for (DebuggerEventItem event: items) {
            if (!event.messages.isEmpty()) {
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
}
