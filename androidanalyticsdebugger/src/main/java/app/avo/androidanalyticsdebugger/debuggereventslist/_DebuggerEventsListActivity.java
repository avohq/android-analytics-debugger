package app.avo.androidanalyticsdebugger.debuggereventslist;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

    private Sorting currentSorting = Sorting.Default;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideActionBar();

        setContentView(R.layout.activity_debugger_events_list_activity);

        final RecyclerView recycler = findViewById(R.id.events_list);
        adapter = new DebuggerEventsListAdapter(Sorting.Default);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        View closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final ImageView sortButton = findViewById(R.id.sort_button);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentSorting == Sorting.Default) {
                    currentSorting = Sorting.Alphabetical;
                    sortButton.setImageResource(android.R.drawable.ic_menu_sort_by_size);
                } else {
                    currentSorting = Sorting.Default;
                    sortButton.setImageResource(android.R.drawable.ic_menu_sort_alphabetically);
                }

                adapter = new DebuggerEventsListAdapter(currentSorting);
                recycler.setAdapter(adapter);
                recycler.setLayoutManager(new LinearLayoutManager(_DebuggerEventsListActivity.this));
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
