package app.avo.androidanalyticsdebugger.debuggerview;

import android.view.View;

import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;

public interface DebuggerViewContainer {
    void showEvent(DebuggerEventItem event);
    View getView();
    View.OnClickListener getOnClickListener();
}
