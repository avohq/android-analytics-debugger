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

class BarViewContainer implements DebuggerViewContainer {

    private View view;

    private TextView timestamp;
    private TextView eventName;

    private ImageView successIcon;
    private ImageView dragHandle;

    BarViewContainer(LayoutInflater layoutInflater) {
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

    public void showEvent(DebuggerEventItem event) {
        timestamp.setText(timeString(event.timestamp));
        eventName.setText(event.name);

        if (Util.eventsHaveErrors(event)) {
            setError(true);
        }
    }

    private String timeString(@Nullable Long timestamp) {
        if (timestamp == null) {
            return "";
        }

        return new SimpleDateFormat("HH:mm:ss.ms", Locale.US).format(new Date(timestamp));
    }

    public View getView() {
        return view;
    }
}
