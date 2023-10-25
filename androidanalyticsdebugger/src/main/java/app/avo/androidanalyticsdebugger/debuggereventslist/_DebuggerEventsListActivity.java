package app.avo.androidanalyticsdebugger.debuggereventslist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

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
                adapter.notifyDataSetChanged();
            }
        });

        View closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        DebuggerManager.eventUpdateListener = new NewEventListener() {
            @Override
            public void onNewEvent(DebuggerEventItem item) {
                adapter.onNewItem(item);
            }
        };
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
