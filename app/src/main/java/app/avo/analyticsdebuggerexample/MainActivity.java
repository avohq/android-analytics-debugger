package app.avo.analyticsdebuggerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import app.avo.androidanalyticsdebugger.Debugger;
import app.avo.androidanalyticsdebugger.DebuggerMode;
import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;
import app.avo.androidanalyticsdebugger.model.DebuggerMessage;
import app.avo.androidanalyticsdebugger.model.DebuggerProp;

public class MainActivity extends AppCompatActivity {

    Debugger debugger;

    boolean hasError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.hello_world).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Hello world", Toast.LENGTH_SHORT).show();
            }
        });

        Button triggerErrorButton = findViewById(R.id.trigger_error_button);

        triggerErrorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (debugger != null) {
                    hasError = !hasError;

                    DebuggerEventItem event = new DebuggerEventItem();
                    event.id = "ew23fe";
                    event.key = "Key";
                    event.name = "Something happened";
                    event.timestamp = System.currentTimeMillis();
                    event.eventProps = new ArrayList<>();
                    event.eventProps.add(new DebuggerProp());
                    event.eventProps.get(0).id = "event prop id";
                    event.eventProps.get(0).name = "event prop name";
                    event.eventProps.get(0).value = "event prop value";

                    event.messages = new ArrayList<>();
                    event.messages.add(new DebuggerMessage("tag", "event prop id",
                            "wrong format", new ArrayList<String>() {{add("Gif");}},
                            "gif"));

                    debugger.publishEvent(event);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        debugger = new Debugger();
        debugger.showDebugger(this, DebuggerMode.bar);
    }
}
