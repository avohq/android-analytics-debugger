package app.avo.androidanalyticsdebugger;

import android.view.View;

import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;

public interface DebuggerViewContainer {
    void showEvent(DebuggerEventItem event);
    View getView();
    View.OnClickListener getOnClickListener();
}
