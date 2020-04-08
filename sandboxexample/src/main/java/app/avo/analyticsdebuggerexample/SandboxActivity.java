package app.avo.analyticsdebuggerexample;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.avo.androidanalyticsdebugger.DebuggerManager;
import app.avo.androidanalyticsdebugger.DebuggerMode;
import app.avo.androidanalyticsdebugger.model.DebuggerEventItem;
import app.avo.androidanalyticsdebugger.model.DebuggerProp;
import app.avo.independent.Independent;

public class SandboxActivity extends AppCompatActivity {

    DebuggerManager debuggerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox);

        debuggerManager = new DebuggerManager(this);

        debuggerManager.publishEvent(System.currentTimeMillis(), "Start event", new ArrayList<Map<String, String>>() {{
            add(new HashMap<String, String>() {{
                put()
            }})
        }});

        Independent.setDebugger(debuggerManager);
        Independent.sendEvent("app open id", System.currentTimeMillis(), "App open",
                new ArrayList<Map<String, String>>(),
                new ArrayList<Map<String, String>>(),
                new ArrayList<Map<String, String>>());

        Button triggerErrorButton = findViewById(R.id.trigger_error_button);

        triggerErrorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (debuggerManager != null) {
                    ArrayList<Map<String, String>> eventProps = new ArrayList<>();

                    eventProps.add(new HashMap<String, String>() {{
                        put("id", "event prop id");
                        put("name", "Post Type");
                        put("value", "gif");
                    }});
                    eventProps.add(new HashMap<String, String>() {{
                        put("id", "good event id");
                        put("name", "Comment Id");
                        put("value", "sdfdf2");
                    }});

                    ArrayList<Map<String, String>> messages = new ArrayList<>();
                    messages.add(new HashMap<String, String>() {{
                        put("tag", "tagValue");
                        put("propertyId", "event prop id");
                        put("message", "Post Type should match one of: GIF, Image, Video or Quote but you provided gif.");
                        put("allowedTypes", "GIF,Image,Video,Quote");
                        put("providedType", "gif");
                    }});

                    Independent.sendEvent("ew23fe",
                            System.currentTimeMillis(),
                            "Error event Error event Error event Error event Error event Error event",
                            messages,
                            eventProps, new ArrayList<Map<String, String>>());
                }
            }
        });

        Button triggerEventButton = findViewById(R.id.trigger_event_button);

        triggerEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (debuggerManager != null) {
                    DebuggerEventItem event = new DebuggerEventItem();
                    event.id = "ef42ee";
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

                    debuggerManager.publishEvent(event);
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
                        if (debuggerManager != null) {
                            DebuggerEventItem event = new DebuggerEventItem();
                            event.id = "ef42aee";
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

                            debuggerManager.publishEvent(event);

                            Toast.makeText(SandboxActivity.this, "Event posted", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 5000);
            }
        });

        Button openAnotherActivity = findViewById(R.id.open_another_activity);
        openAnotherActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SandboxActivity.this, AnotherActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        debuggerManager.showDebugger(this, DebuggerMode.bar);
    }
}
