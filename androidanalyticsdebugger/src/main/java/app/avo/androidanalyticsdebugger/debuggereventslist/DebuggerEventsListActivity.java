package app.avo.androidanalyticsdebugger.debuggereventslist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.avo.androidanalyticsdebugger.R;

public class DebuggerEventsListActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_debugger_events_list_activity);

        RecyclerView recycler = findViewById(R.id.events_list);
        recycler.setAdapter(new DebuggerEventsListAdapter());
        recycler.setLayoutManager(new LinearLayoutManager(this));

        View closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
