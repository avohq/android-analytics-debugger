package app.avo.independent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Independent {

    private static Object debugger = null;

    public static void setDebugger(Object debugger) {
        Independent.debugger = debugger;
    }

    public static void sendEvent(String key, String id, Long timestamp, String name,
                                 List<Map<String, String>> messages,
                                 List<Map<String, String>> eventProps,
                                 List<Map<String, String>> userProps) {
        if (Independent.debugger != null) {

            try {
                Method method = Independent.debugger.getClass().getMethod("publishEvent",
                        String.class, String.class, Long.class, String.class, List.class, List.class,
                        List.class);

                method.invoke(Independent.debugger,
                        key, id, timestamp, name, messages, eventProps, userProps);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }
}
