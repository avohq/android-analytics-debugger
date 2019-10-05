package app.avo.analyticsdebuggerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        debugger = new Debugger();
        debugger.showDebugger(this, DebuggerMode.bar);

        DebuggerEventItem event = new DebuggerEventItem("App open", "app open id",
                System.currentTimeMillis(), "App open", new ArrayList<DebuggerMessage>(),
                new ArrayList<DebuggerProp>(), new ArrayList<DebuggerProp>());
        debugger.publishEvent(event);

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
                    DebuggerEventItem event = new DebuggerEventItem();
                    event.id = "ew23fe";
                    event.key = "Key";
                    event.name = "Error event";
                    event.timestamp = System.currentTimeMillis();
                    event.eventProps = new ArrayList<>();

                    event.eventProps.add(new DebuggerProp());
                    event.eventProps.get(0).id = "event prop id";
                    event.eventProps.get(0).name = "Post Type";
                    event.eventProps.get(0).value = "gif";

                    event.eventProps.add(new DebuggerProp());
                    event.eventProps.get(1).id = "good event id";
                    event.eventProps.get(1).name = "Comment Id";
                    event.eventProps.get(1).value = "sdfdf2";

                    event.messages = new ArrayList<>();
                    event.messages.add(new DebuggerMessage("tag", "event prop id",
                            "Post Type should match one of: GIF, Image, Video or Quote but you provided gif.",
                            new ArrayList<String>() {{
                                add("GIF");
                                add("Image");
                                add("Video");
                                add("Quote");
                            }},
                            "gif"));

                    debugger.publishEvent(event);
                }
            }
        });

        Button triggerEventButton = findViewById(R.id.trigger_event_button);

        triggerEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (debugger != null) {
                    DebuggerEventItem event = new DebuggerEventItem();
                    event.id = "ef42ee";
                    event.key = "Key";
                    event.name = "Something happened";
                    event.timestamp = System.currentTimeMillis();
                    event.userProps = new ArrayList<>();
                    event.userProps.add(new DebuggerProp());
                    event.userProps.get(0).id = "";
                    event.userProps.get(0).name = "Author Id";
                    event.userProps.get(0).value = "Qew423ffdm";
                    event.userProps.add(new DebuggerProp());
                    event.userProps.get(1).id = "";
                    event.userProps.get(1).name = "Author Name";
                    event.userProps.get(1).value = "Dinesh";

                    event.eventProps = new ArrayList<>();

                    event.eventProps.add(new DebuggerProp());
                    event.eventProps.get(0).id = "good event id";
                    event.eventProps.get(0).name = "Comment Id";
                    event.eventProps.get(0).value = "sdfdf2";

                    debugger.publishEvent(event);
                }
            }
        });

        Button triggerEventWithDelayButton = findViewById(R.id.trigger_event_delayed_button);

        triggerEventWithDelayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (debugger != null) {
                            DebuggerEventItem event = new DebuggerEventItem();
                            event.id = "ef42aee";
                            event.key = "Key";
                            event.name = "Delayed";
                            event.timestamp = System.currentTimeMillis();
                            event.userProps = new ArrayList<>();
                            event.userProps.add(new DebuggerProp());
                            event.userProps.get(0).id = "";
                            event.userProps.get(0).name = "Author Id";
                            event.userProps.get(0).value = "Qew423ffdm";
                            event.userProps.add(new DebuggerProp());
                            event.userProps.get(1).id = "";
                            event.userProps.get(1).name = "Author Name";
                            event.userProps.get(1).value = "Dinesh";

                            event.eventProps = new ArrayList<>();

                            event.eventProps.add(new DebuggerProp());
                            event.eventProps.get(0).id = "good event id";
                            event.eventProps.get(0).name = "Comment Id";
                            event.eventProps.get(0).value = "sdfdf2";

                            debugger.publishEvent(event);

                            Toast.makeText(MainActivity.this, "Event posted", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 5000);
            }
        });
    }
}
