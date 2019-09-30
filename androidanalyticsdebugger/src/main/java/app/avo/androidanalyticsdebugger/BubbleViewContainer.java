package app.avo.androidanalyticsdebugger;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;

public class BubbleViewContainer implements DebuggerViewContainer {

    private View view;

    private ImageView bubble;
    private TextView counter;

    private int countedEvents = 0;

    BubbleViewContainer(LayoutInflater layoutInflater) {
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
            counter.setTextColor(view.getResources().getColor(R.color.error));
        } else {
            bubble.setImageResource(R.drawable.avo_bubble);
            counter.setBackgroundResource(R.drawable.green_oval);
            counter.setTextColor(view.getResources().getColor(R.color.background));
        }
    }

    @Override
    public void showEvent(DebuggerEventItem event) {
        countedEvents += 10;
        counter.setText(String.format(Locale.US, "%d", countedEvents));

        if (Util.eventsHaveErrors(event)) {
            setError(true);
        }
    }

    @Override
    public View getView() {
        return view;
    }
}
