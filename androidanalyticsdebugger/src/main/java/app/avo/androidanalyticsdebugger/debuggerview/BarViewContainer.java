package app.avo.androidanalyticsdebugger.debuggerview;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.avo.androidanalyticsdebugger.R;
import app.avo.androidanalyticsdebugger.Util;
import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;

public class BarViewContainer implements DebuggerViewContainer {

    private View view;

    private TextView timestamp;
    private TextView eventName;

    private ImageView successIcon;
    private ImageView dragHandle;

    @SuppressLint("InflateParams")
    public BarViewContainer(LayoutInflater layoutInflater) {
        view = layoutInflater.inflate(R.layout.bar_view, null);

        timestamp = view.findViewById(R.id.timestamp);
        eventName = view.findViewById(R.id.event_name);
        successIcon = view.findViewById(R.id.success_icon);
        dragHandle = view.findViewById(R.id.drag_handle);
    }

    @Override
    public View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setError(false);
            }
        };
    }

    private void setError(boolean hasError) {
        if (hasError) {
            successIcon.setImageResource(R.drawable.warning);
            dragHandle.setImageResource(R.drawable.drag_handle_white);
            view.setBackgroundResource(R.color.error);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timestamp.setTextColor(view.getResources().getColor(R.color.foregroundLighter, null));
            } else {
                timestamp.setTextColor(view.getResources().getColor(R.color.foregroundLighter));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                eventName.setTextColor(view.getResources().getColor(R.color.background, null));
            } else {
                eventName.setTextColor(view.getResources().getColor(R.color.background));
            }
        } else {
            successIcon.setImageResource(R.drawable.tick);
            dragHandle.setImageResource(R.drawable.drag_handle_grey);
            view.setBackgroundResource(R.color.background);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timestamp.setTextColor(view.getResources().getColor(R.color.foregroundLight, null));
            } else {
                timestamp.setTextColor(view.getResources().getColor(R.color.foregroundLight));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                eventName.setTextColor(view.getResources().getColor(R.color.foreground, null));
            } else {
                eventName.setTextColor(view.getResources().getColor(R.color.foreground));
            }
        }
    }

    public void showEvent(DebuggerEventItem event) {
        timestamp.setText(Util.timeString(event.timestamp));
        eventName.setText(event.name);

        if (Util.eventHaveErrors(event)) {
            setError(true);
        }
    }

    public View getView() {
        return view;
    }
}
