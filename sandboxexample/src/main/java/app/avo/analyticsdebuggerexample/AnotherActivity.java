package app.avo.analyticsdebuggerexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import app.avo.androidanalyticsdebugger.DebuggerManager;
import app.avo.androidanalyticsdebugger.DebuggerMode;

public class AnotherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_another);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new DebuggerManager().showDebugger(this, DebuggerMode.bubble);
    }
}
