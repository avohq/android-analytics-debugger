package app.avo.androidanalyticsdebugger.debuggereventslist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.avo.androidanalyticsdebugger.DebuggerManager;
import app.avo.androidanalyticsdebugger.R;
import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;

@SuppressLint("NotifyDataSetChanged")
public class _DebuggerEventsListActivity extends AppCompatActivity {

    public DebuggerEventsListAdapter adapter;
    private EditText filterInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideActionBar();

        setContentView(R.layout.activity_debugger_events_list_activity);

        final RecyclerView recycler = findViewById(R.id.events_list);
        adapter = new DebuggerEventsListAdapter();
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        View clearButton = findViewById(R.id.clear_events_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DebuggerManager.events.clear();
                adapter.clearEvents();
            }
        });

        View closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setUpFilter();

        DebuggerManager.eventUpdateListener = new NewEventListener() {
            @Override
            public void onNewEvent(DebuggerEventItem item) {
                adapter.onNewItem(item);
            }
        };
    }

    private void setUpFilter() {
        final View filterToggleButton = findViewById(R.id.filter_input_toggle);
        final ImageView filterToggleIcon = findViewById(R.id.filter_input_toggle_icon);
        filterInput = findViewById(R.id.filter_input);

        View.OnClickListener filterInputToggleClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterInput.setVisibility(isFilterVisible() ? View.GONE : View.VISIBLE);
                filterToggleIcon.setImageResource(isFilterVisible() ? R.drawable.collapse_arrow : R.drawable.expend_arrow);
                if (isFilterVisible()) {
                    adapter.filter(filterInput.getText().toString());
                } else {
                    adapter.filter("");
                }
            }
        };
        filterToggleButton.setOnClickListener(filterInputToggleClick);
        filterToggleIcon.setOnClickListener(filterInputToggleClick);

        filterInput.addTextChangedListener(onFilterChangeWatcher());
    }

    @NonNull
    private TextWatcher onFilterChangeWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private boolean isFilterVisible() {
        return filterInput.getVisibility() == View.VISIBLE;
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        DebuggerManager.eventUpdateListener = null;
    }
}
