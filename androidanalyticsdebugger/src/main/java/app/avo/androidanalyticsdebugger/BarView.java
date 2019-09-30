package app.avo.androidanalyticsdebugger;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;

class BarView {

    private View view;

    private TextView timestamp;
    private TextView eventName;

    private ImageView successIcon;
    private ImageView dragHandle;

    BarView(LayoutInflater layoutInflater) {
        view = layoutInflater.inflate(R.layout.bar_view, null);

        timestamp = view.findViewById(R.id.timestamp);
        eventName = view.findViewById(R.id.event_name);
        successIcon = view.findViewById(R.id.success_icon);
        dragHandle = view.findViewById(R.id.drag_handle);
    }

    void setError(boolean hasError) {
        if (hasError) {
            successIcon.setImageResource(R.drawable.warning);
            dragHandle.setImageResource(R.drawable.drag_handle_white);
            view.setBackgroundResource(R.color.error);
            timestamp.setTextColor(view.getResources().getColor(R.color.foregroundLighter));
            eventName.setTextColor(view.getResources().getColor(R.color.background));
        } else {
            successIcon.setImageResource(R.drawable.tick);
            dragHandle.setImageResource(R.drawable.drag_handle_grey);
            view.setBackgroundResource(R.color.background);
            timestamp.setTextColor(view.getResources().getColor(R.color.foregroundLight));
            eventName.setTextColor(view.getResources().getColor(R.color.foreground));
        }
    }

    void showEvent(DebuggerEventItem event) {
        timestamp.setText(timeString(event.timestamp));
        eventName.setText(event.name);
    }

    private String timeString(@Nullable Long timestamp) {
        if (timestamp == null) {
            return "";
        }

        return new SimpleDateFormat("HH:mm:ss.ms", Locale.US).format(new Date(timestamp));
    }

    View getView() {
        return view;
    }
}
