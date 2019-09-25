package app.avo.analyticsdebuggerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import app.avo.androidanalyticsdebugger.Debugger;
import app.avo.androidanalyticsdebugger.DebuggerMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Debugger debugger = new Debugger();
        debugger.showDebugger(this, DebuggerMode.bubble);
    }
}
