package app.avo.analyticsdebuggerexample;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

        new DebuggerManager(this).showDebugger(this, DebuggerMode.bubble);
    }
}
