package app.avo.androidanalyticsdebugger.debuggereventslist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import app.avo.androidanalyticsdebugger.DebuggerManager;
import app.avo.androidanalyticsdebugger.R;

public class DebuggerEventsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideActionBar();

        setContentView(R.layout.activity_debugger_events_list_activity);

        final RecyclerView recycler = findViewById(R.id.events_list);
        final DebuggerEventsListAdapter adapter = new DebuggerEventsListAdapter();
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        View closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        DebuggerManager.eventUpdateListener = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
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
