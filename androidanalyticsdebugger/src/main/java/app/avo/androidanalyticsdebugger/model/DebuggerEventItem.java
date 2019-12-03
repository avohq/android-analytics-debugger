package app.avo.androidanalyticsdebugger.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DebuggerEventItem {

    public String id;
    public Long timestamp;
    public String name;
    public List<DebuggerMessage> messages;
    public List<DebuggerProp> eventProps;
    public List<DebuggerProp> userProps;

    public DebuggerEventItem() {}

    public DebuggerEventItem(String id, Long timestamp, String name,
                             List<Map<String, String>> messages,
                             List<Map<String, String>> eventProps,
                             List<Map<String, String>> userProps) {
        this.id = id;
        this.timestamp = timestamp;
        this.name = name;

        this.messages = new ArrayList<>();
        if (messages != null) {
            for (Map<String, String> message : messages) {
                DebuggerMessage debuggerMessage = createMessage(message);

                if (debuggerMessage != null) {
                    this.messages.add(debuggerMessage);
                }
            }
        }

        this.eventProps = new ArrayList<>();
        if (eventProps != null) {
            for (Map<String, String> eventProp : eventProps) {
                DebuggerProp prop = createProp(eventProp);
                if (prop != null) {
                    this.eventProps.add(prop);
                }
            }
        }

        this.userProps = new ArrayList<>();
        if (userProps != null) {
            for (Map<String, String> userProp : userProps) {
                DebuggerProp prop = createProp(userProp);
                if (prop != null) {
                    this.userProps.add(prop);
                }
            }
        }
    }

    private DebuggerMessage createMessage(Map<String, String> messageMap) {

        String tag = messageMap.get("tag");
        String propertyId = messageMap.get("propertyId");
        String message = messageMap.get("message");

        if (tag == null || propertyId == null || message == null) {
            return null;
        }

        List<String> allowedTypesList = new ArrayList<>();
        String allowedTypesString = messageMap.get("allowedTypes");
        if (allowedTypesString != null) {
            allowedTypesList = Arrays.asList(allowedTypesString.split(","));
        }

        return new DebuggerMessage(tag, propertyId, message, allowedTypesList,
                messageMap.get("providedType"));
    }

    private DebuggerProp createProp(Map<String, String> prop) {
        String id = prop.get("id");
        String name = prop.get("name");
        String value = prop.get("value");

        if (id == null || name == null || value == null) {
            return null;
        }

        return new DebuggerProp(id, name, value);
    }

    @SuppressWarnings("EqualsReplaceableByObjectsCall")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebuggerEventItem that = (DebuggerEventItem) o;

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
        int result = id.hashCode();
        result = 31 * result + timestamp.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (messages != null ? messages.hashCode() : 0);
        result = 31 * result + (eventProps != null ? eventProps.hashCode() : 0);
        result = 31 * result + (userProps != null ? userProps.hashCode() : 0);
        return result;
    }
}