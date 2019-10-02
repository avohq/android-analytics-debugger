package app.avo.androidanalyticsdebugger.debuggereventslist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.avo.androidanalyticsdebugger.Debugger;
import app.avo.androidanalyticsdebugger.R;
import app.avo.androidanalyticsdebugger.Util;
import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;
import app.avo.androidanalyticsdebugger.model.DebuggerProp;

public class DebuggerEventsListAdapter extends RecyclerView.Adapter<DebuggerEventsListAdapter.DebuggerEventViewHolder> {

    @NonNull
    @Override
    public DebuggerEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rawLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.debugger_events_list_item, parent, false);
        return new DebuggerEventViewHolder(rawLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull final DebuggerEventViewHolder holder, int position) {

        final DebuggerEventItem event = Debugger.events.get(position);
        boolean hasError = Util.eventHaveErrors(event);
        final boolean[] expended = {position == 0 || hasError};

        holder.eventName.setText(event.name);
        holder.timestamp.setText(Util.timeString(event.timestamp));

        if (expended[0]) {
            holder.expendButton.setImageResource(R.drawable.collapse_arrow);
            for (DebuggerProp prop: event.eventProps) {
                TextView propView = new TextView(holder.itemView.getContext());
                propView.setText(prop.name);
                holder.expendedContent.addView(propView);
            }
         } else {
            holder.expendButton.setImageResource(R.drawable.expend_arrow);
        }

        if (hasError) {
            holder.successIcon.setImageResource(R.drawable.red_warning);
            holder.eventName.setTextColor(holder.itemView.getResources().getColor(R.color.error));
        } else {
            holder.successIcon.setImageResource(R.drawable.tick);
            holder.eventName.setTextColor(holder.itemView.getResources().getColor(R.color.foreground));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expended[0]) {
                    for (DebuggerProp prop: event.eventProps) {
                        TextView propView = new TextView(view.getContext());
                        propView.setText(prop.name);
                        holder.expendedContent.addView(propView);
                    }
                    holder.expendButton.setImageResource(R.drawable.expend_arrow);
                } else {
                    holder.expendedContent.removeAllViews();
                    holder.expendButton.setImageResource(R.drawable.collapse_arrow);
                }

                expended[0] = !expended[0];
            }
        });
    }

    @Override
    public int getItemCount() {
        return Debugger.events.size();
    }

    static class DebuggerEventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        LinearLayout expendedContent;
        ImageView expendButton;
        ImageView successIcon;
        TextView timestamp;

        DebuggerEventViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.event_name);
            expendedContent = itemView.findViewById(R.id.expended_content);
            expendButton = itemView.findViewById(R.id.expend_button);
            successIcon = itemView.findViewById(R.id.success_icon);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }
}
