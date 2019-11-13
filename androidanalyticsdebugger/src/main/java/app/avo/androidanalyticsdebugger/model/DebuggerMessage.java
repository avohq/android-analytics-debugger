package app.avo.androidanalyticsdebugger.model;

import androidx.annotation.Nullable;

import java.util.List;

public class DebuggerMessage {

    public String tag;
    public String propertyId;
    public String message;
    public List<String> allowedTypes;
    @Nullable
    public String providedType;

    public DebuggerMessage(String tag, String propertyId, String message, List<String> allowedTypes,
                           @Nullable String providedType) {
        this.tag = tag;
        this.propertyId = propertyId;
        this.message = message;
        this.allowedTypes = allowedTypes;
        this.providedType = providedType;
    }
}