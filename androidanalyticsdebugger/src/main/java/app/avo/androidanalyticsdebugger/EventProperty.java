package app.avo.androidanalyticsdebugger;


import android.support.annotation.NonNull;

public class EventProperty {
    @NonNull
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String value;

    public EventProperty(@NonNull String id, @NonNull String name, @NonNull String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getValue() {
        return value;
    }

    public void setValue(@NonNull String value) {
        this.value = value;
    }
}
