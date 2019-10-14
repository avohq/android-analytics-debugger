package app.avo.androidanalyticsdebugger.debuggereventslist;

import android.animation.ValueAnimator;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.avo.androidanalyticsdebugger.Debugger;
import app.avo.androidanalyticsdebugger.R;
import app.avo.androidanalyticsdebugger.Util;
import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;
import app.avo.androidanalyticsdebugger.model.DebuggerMessage;
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
        holder.expendedContent.removeAllViews();

        if (expended[0]) {
            expendItem(holder, event);
        } else {
            collapseItem(holder);
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
                expended[0] = !expended[0];
                if (expended[0]) {
                    expendItem(holder, event);

                    int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) holder.expendedContent.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
                    int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    holder.expendedContent.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
                    final int targetHeight = holder.expendedContent.getMeasuredHeight();

                    animateProps(holder, 0, targetHeight);
                } else {
                    collapseItem(holder);

                    final int startHeight = holder.expendedContent.getHeight();
                    animateProps(holder, startHeight, 0);
                }
            }
        });
    }

    private void animateProps(@NonNull final DebuggerEventViewHolder holder, int startHeight,
                              int endHeight) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startHeight, endHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                holder.expendedContent.getLayoutParams().height = (int) animation.getAnimatedValue();
                holder.expendedContent.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(500);
        valueAnimator.start();
    }

    private void collapseItem(@NonNull DebuggerEventViewHolder holder) {
        holder.expendedContent.removeAllViews();
        holder.expendButton.setImageResource(R.drawable.expend_arrow);

        Context context = holder.itemView.getContext();
        holder.expendedContent.setPadding(0, 0, 0,
                (int) Util.convertDpToPixel(0, context));
    }

    private void expendItem(@NonNull final DebuggerEventViewHolder holder, DebuggerEventItem event) {
        holder.expendButton.setImageResource(R.drawable.collapse_arrow);
        Context context = holder.itemView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        addPropRows(holder, event, context, layoutInflater, event.eventProps);
        addPropRows(holder, event, context, layoutInflater, event.userProps);

        if (holder.expendedContent.getChildCount() > 0) {
            holder.expendedContent.removeViewAt(holder.expendedContent.getChildCount() - 1);
        }

        holder.expendedContent.setPadding(0, 0, 0,
                (int) Util.convertDpToPixel(16, context));
    }

    private void addPropRows(@NonNull DebuggerEventViewHolder holder, DebuggerEventItem event,
                             Context context, LayoutInflater layoutInflater,
                             @Nullable List<DebuggerProp> props) {
        if (props == null) {
            return;
        }

        for (DebuggerProp prop: props) {
            View propRowView = layoutInflater.inflate(R.layout.event_prop_row,
                    holder.expendedContent, false);
            TextView propName = propRowView.findViewById(R.id.prop_name);
            TextView propValue = propRowView.findViewById(R.id.prop_value);

            DebuggerMessage error = null;
            if (event.messages != null) {
                for (DebuggerMessage message : event.messages) {
                    if (message.propertyId.equals(prop.id)) {
                        error = message;
                    }
                }
            }

            if (error != null) {
                propName.setTextColor(context.getResources().getColor(R.color.error));
                propValue.setTextColor(context.getResources().getColor(R.color.error));

                TextView message = propRowView.findViewById(R.id.message);
                message.setVisibility(View.VISIBLE);

                message.setText(boldifyErrorMessage(prop.name, error.message, error.allowedTypes,
                        error.providedType));
            }

            propName.setText(prop.name);
            propValue.setText(prop.value != null ? prop.value : "");

            holder.expendedContent.addView(propRowView);
            layoutInflater.inflate(R.layout.prop_divider, holder.expendedContent, true);
        }
    }

    private CharSequence boldifyErrorMessage(String propertyName, String message,
                                             List<String> allowedTypes, String providedType) {
        if (allowedTypes == null || allowedTypes.isEmpty()
                || providedType == null || providedType.isEmpty()) {
            return message;
        }

        List<Integer> boldIndexes = new ArrayList<>();
        List<Integer> boldLengths = new ArrayList<>();

        boldIndexes.add(message.indexOf(propertyName));
        boldLengths.add(propertyName.length());

        for (String allowedType : allowedTypes) {
            int index = message.indexOf(allowedType);
            boldIndexes.add(index);
            boldLengths.add(allowedType.length());
        }

        boldIndexes.add(message.indexOf(providedType));
        boldLengths.add(providedType.length());

        SpannableStringBuilder formattedMessage = new SpannableStringBuilder(message);
        for (int i = 0; i < boldIndexes.size(); i++) {
            formattedMessage.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                    boldIndexes.get(i), boldIndexes.get(i) + boldLengths.get(i),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return formattedMessage;
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
