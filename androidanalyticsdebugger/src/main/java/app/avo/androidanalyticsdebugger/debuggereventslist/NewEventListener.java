package app.avo.androidanalyticsdebugger.debuggereventslist;

import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;

public interface NewEventListener {
    void onNewEvent(DebuggerEventItem event);
}
