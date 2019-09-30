package app.avo.androidanalyticsdebugger;

import java.util.ArrayList;
import java.util.List;

import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;

class Util {

    private static boolean eventsHaveErrors(List<DebuggerEventItem> items) {
        for (DebuggerEventItem event: items) {
            if (!event.messages.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    static boolean eventsHaveErrors(final DebuggerEventItem event) {
        return eventsHaveErrors(new ArrayList<DebuggerEventItem>() {{add(event);}});
    }
}
