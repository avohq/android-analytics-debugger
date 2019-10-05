package app.avo.androidanalyticsdebugger.debuggereventslist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.avo.androidanalyticsdebugger.Debugger;
import app.avo.androidanalyticsdebugger.R;

public class DebuggerEventsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

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

        Debugger.eventUpdateListener = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Debugger.eventUpdateListener = null;
    }
}
