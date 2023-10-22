package app.avo.androidanalyticsdebugger.debuggereventslist;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.avo.androidanalyticsdebugger.DebuggerManager;
import app.avo.androidanalyticsdebugger.R;
import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;

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

        View closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setUpFilterToggle();

        DebuggerManager.eventUpdateListener = new NewEventListener() {
            @Override
            public void onNewEvent(DebuggerEventItem item) {
                adapter.onNewItem(item);
            }
        };
    }

    private void setUpFilterToggle() {
        final View filterToggleButton = findViewById(R.id.filter_input_toggle);
        final ImageView filterToggleIcon = findViewById(R.id.filter_input_toggle_icon);
        filterInput = findViewById(R.id.filter_input);

        filterToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterInput.setVisibility(isFilterVisible() ? View.GONE : View.VISIBLE);
                filterToggleIcon.setImageResource(isFilterVisible() ? R.drawable.collapse_arrow : R.drawable.expend_arrow);
            }
        });
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
