package app.avo.androidanalyticsdebugger.model;

public class DebuggerProp {

    public String id;
    public String name;
    public String value;

    public DebuggerProp() {}

    DebuggerProp(String id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebuggerProp prop = (DebuggerProp) o;

        if (!id.equals(prop.id)) return false;
        if (!name.equals(prop.name)) return false;
        return value.equals(prop.value);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}