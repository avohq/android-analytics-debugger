package app.avo.androidanalyticsdebugger.model;

import androidx.annotation.Nullable;

import java.util.List;

public class DebuggerMessage {

    public String propertyId;
    public String message;
    @Nullable
    public List<String> allowedTypes;
    @Nullable
    public String providedType;

    public DebuggerMessage(String propertyId, String message,
                           @Nullable List<String> allowedTypes,
                           @Nullable String providedType) {
        this.propertyId = propertyId;
        this.message = message;
        this.allowedTypes = allowedTypes;
        this.providedType = providedType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebuggerMessage that = (DebuggerMessage) o;

        if (!propertyId.equals(that.propertyId)) return false;
        if (!message.equals(that.message)) return false;
        if (allowedTypes != null ? !allowedTypes.equals(that.allowedTypes) : that.allowedTypes != null)
            return false;
        return providedType != null ? providedType.equals(that.providedType) : that.providedType == null;
    }

    @Override
    public int hashCode() {
        int result = propertyId.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + (allowedTypes != null ? allowedTypes.hashCode() : 0);
        result = 31 * result + (providedType != null ? providedType.hashCode() : 0);
        return result;
    }
}