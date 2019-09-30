package app.avo.androidanalyticsdebugger.model;

import java.util.List;

public class DebuggerEventItem {

    public String key;
    public String id;
    public Long timestamp;
    public String name;
    public List<DebuggerMessage> messages;
    public List<DebuggerProp> eventProps;
    public List<DebuggerProp> userProps;

    public DebuggerEventItem() {}

    public DebuggerEventItem(String key, String id, Long timestamp, String name, List<DebuggerMessage> messages, List<DebuggerProp> eventProps, List<DebuggerProp> userProps) {
        this.key = key;
        this.id = id;
        this.timestamp = timestamp;
        this.name = name;
        this.messages = messages;
        this.eventProps = eventProps;
        this.userProps = userProps;
    }
}