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

    @SuppressWarnings("EqualsReplaceableByObjectsCall")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebuggerEventItem that = (DebuggerEventItem) o;

        if (!key.equals(that.key)) return false;
        if (!id.equals(that.id)) return false;
        if (!timestamp.equals(that.timestamp)) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (messages != null ? !messages.equals(that.messages) : that.messages != null)
            return false;
        if (eventProps != null ? !eventProps.equals(that.eventProps) : that.eventProps != null)
            return false;
        return userProps != null ? userProps.equals(that.userProps) : that.userProps == null;
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + timestamp.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (messages != null ? messages.hashCode() : 0);
        result = 31 * result + (eventProps != null ? eventProps.hashCode() : 0);
        result = 31 * result + (userProps != null ? userProps.hashCode() : 0);
        return result;
    }
}