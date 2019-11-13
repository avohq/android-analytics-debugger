package app.avo.androidanalyticsdebugger.debuggerview;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import app.avo.androidanalyticsdebugger.R;
import app.avo.androidanalyticsdebugger.Util;
import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;

public class BubbleViewContainer implements DebuggerViewContainer {

    private View view;

    private ImageView bubble;
    private TextView counter;

    private int countedEvents = 0;

    @SuppressLint("InflateParams")
    public BubbleViewContainer(LayoutInflater layoutInflater) {
        this.view = layoutInflater.inflate(R.layout.bubble_view, null);

        this.bubble = view.findViewById(R.id.bubble);
        this.counter = view.findViewById(R.id.counter);
    }

    @Override
    public View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setError(false);
                countedEvents = 0;
                counter.setText(String.format(Locale.US, "%d", countedEvents));
            }
        };
    }

    private void setError(boolean hasError) {
        if (hasError) {
            bubble.setImageResource(R.drawable.avo_bubble_error);
            counter.setBackgroundResource(R.drawable.white_oval);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                counter.setTextColor(view.getResources().getColor(R.color.error, null));
            } else {
                counter.setTextColor(view.getResources().getColor(R.color.error));
            }
        } else {
            bubble.setImageResource(R.drawable.avo_bubble);
            counter.setBackgroundResource(R.drawable.green_oval);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                counter.setTextColor(view.getResources().getColor(R.color.background, null));
            } else {
                counter.setTextColor(view.getResources().getColor(R.color.background));
            }
        }
    }

    @Override
    public void showEvent(DebuggerEventItem event) {
        countedEvents += 1;
        counter.setText(String.format(Locale.US, "%d", countedEvents));

        if (Util.eventHaveErrors(event)) {
            setError(true);
        }
    }

    @Override
    public View getView() {
        return view;
    }
}
