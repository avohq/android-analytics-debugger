package app.avo.androidanalyticsdebugger;

import androidx.annotation.NonNull;

public class PropertyError {

    @NonNull
    private String propertyId;
    @NonNull
    private String message;

    public PropertyError(@NonNull String propertyId, @NonNull String message) {
        this.propertyId = propertyId;
        this.message = message;
    }

    @NonNull
    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(@NonNull String propertyId) {
        this.propertyId = propertyId;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }
}
