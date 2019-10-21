// Generated by Avo VERSION 48.32.0, PLEASE EDIT WITH CARE
package sh.avo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import javax.net.ssl.HttpsURLConnection;

import android.os.AsyncTask;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public final class Avo {
    public enum AvoEnv {
        PROD,
        DEV;
    }

    private static Boolean __STRICT__ = null;
    private static AvoEnv __AVO_ENV__ = null;

    private static Object __MOBILE_DEBUGGER__ = null;

    private static boolean __MOBILE_DEBUGGER_ENABLED__() {
        if (__MOBILE_DEBUGGER__ != null) {
            try {
                Method method = __MOBILE_DEBUGGER__.getClass().getMethod("isEnabled");
                return Boolean.TRUE.equals(method.invoke(__MOBILE_DEBUGGER__));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return false;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    private static void __MOBILE_DEBUGGER_POST_EVENT__(String id, long timestamp, String name, List<Map<String, String>> messages, List<Map<String, String>> eventProperties, List<Map<String, String>> userProperties) {
        if (__MOBILE_DEBUGGER__ != null) {
            try {
                Method method = __MOBILE_DEBUGGER__.getClass().getMethod("publishEvent",
                        String.class, Long.class, String.class, List.class, List.class,
                        List.class);
                method.invoke(__MOBILE_DEBUGGER__,
                        id, timestamp, name, messages, eventProperties, userProperties);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private interface ISerializable {
        Map<String, Object> serialize();
    }

    private static <T> List<String> strings(List<T> objects) {
        final List<String> list = new ArrayList<>();
        for (T t: objects) {
            if (t != null) {
                list.add(t.toString());
            }
        }
        return list;
    }

    private static <T extends ISerializable> List<Map<String, Object>> objects(List<T> objects) {
        final List<Map<String, Object>> list = new ArrayList<>();
        for (T t: objects) {
            if (t != null) {
                list.add(t.serialize());
            }
        }
        return list;
    }

    private interface AvoAssertMessage {
        String getPropertyId();

        String getAssertionType();

        String getMessage();
    }

    private static class AvoAssertMax implements AvoAssertMessage {
        private final String propertyId;
        private final String message;

        AvoAssertMax(String propertyId, String message) {
            this.propertyId = propertyId;
            this.message = message;
        }

        public String getPropertyId() { return propertyId; }

        public String getAssertionType() { return "expectedMax"; }

        public String getMessage() { return message; }
    }

    private static class AvoAssertMin implements AvoAssertMessage {
        private final String propertyId;
        private final String message;

        AvoAssertMin(String propertyId, String message) {
            this.propertyId = propertyId;
            this.message = message;
        }

        public String getPropertyId() { return propertyId; }

        public String getAssertionType() { return "expectedMin"; }

        public String getMessage() { return message; }
    }

    private static class AvoAssertNonOptional implements AvoAssertMessage {
        private final String propertyId;
        private final String message;

        AvoAssertNonOptional(String propertyId, String message) {
            this.propertyId = propertyId;
            this.message = message;
        }

        public String getPropertyId() { return propertyId; }

        public String getAssertionType() { return "expectedNonOptional"; }

        public String getMessage() { return message; }
    }

    static class AvoAssert {
        static List<AvoAssertMessage> assertMax(String propertyId, String property, double max, double value) {
            if (value > max) {
                return Collections.<AvoAssertMessage>singletonList(new AvoAssertMax(propertyId, property + " has a maximum value of " + max + " but you provided the value " + value));
            }
            return Collections.emptyList();
        }

        static List<AvoAssertMessage> assertMax(String propertyId, String property, int max, int value) {
            if (value > max) {
                return Collections.<AvoAssertMessage>singletonList(new AvoAssertMax(propertyId, property + " has a maximum value of " + max + " but you provided the value " + value ));
            }
            return Collections.emptyList();
        }

        static List<AvoAssertMessage> assertMax(String propertyId, String property, long max, long value) {
            if (value > max) {
                return Collections.<AvoAssertMessage>singletonList(new AvoAssertMax(propertyId, property + " has a maximum value of " + max + " but you provided the value " + value));
            }
            return Collections.emptyList();
        }

        static List<AvoAssertMessage> assertMin(String propertyId, String property, double min, double value) {
            if (value < min) {
                return Collections.<AvoAssertMessage>singletonList(new AvoAssertMin(propertyId, property + " has a minimum value of " + min + " but you provided the value " + value));
            }
            return Collections.emptyList();
        }

        static List<AvoAssertMessage> assertMin(String propertyId, String property, int min, int value) {
            if (value < min) {
                return Collections.<AvoAssertMessage>singletonList(new AvoAssertMin(propertyId, property + " has a minimum value of " + min + " but you provided the value " + value));
            }
            return Collections.emptyList();
        }

        static List<AvoAssertMessage> assertMin(String propertyId, String property, long min, long value) {
            if (value < min) {
                return Collections.<AvoAssertMessage>singletonList(new AvoAssertMin(propertyId, property + " has a minimum value of " + min + " but you provided the value " + value));
            }
            return Collections.emptyList();
        }

        static <T> List<AvoAssertMessage> assertNonOptional(String propertyId, String property, T prop) {
            if (prop == null) {
                return Collections.<AvoAssertMessage>singletonList(new AvoAssertNonOptional(propertyId, property + " is a required property but you provided null"));
            }
            return Collections.emptyList();
        }
    }


    private static final String TAG = "Avo";

    static class AvoLogger {
        static void logEventSent(String eventName, Map<String, Object> eventProps, Map<String, Object> userProps) {
            Log.i(TAG, "[avo] Event Sent: " + eventName + " Event Props: " +
                    (eventProps != null ? eventProps : "empty") + " User Props: " +
                    (userProps != null ? userProps : "empty"));
        }
    }


    public static interface ICustomDestination {
        void make(AvoEnv env);

        void logEvent(String eventName, Map<String, Object> eventProperties);

        void setUserProperties(String userId, Map<String, Object> userProperties);

        void identify(String userId);

        void unidentify();
    }


    private static class AvoInvoke {
        interface Callback {
            void apply(Double sa);
        }

        private static class HttpPostAsyncTask extends AsyncTask<String, Void, Void> {
            final JSONObject json;
            final Callback onComplete;
            HttpPostAsyncTask(JSONObject json, Callback onComplete) {
                this.json = json;
                this.onComplete = onComplete;
            }

            @Override
            protected Void doInBackground(String... strings) {
                try {
                    HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.avo.app/i").openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");

                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(json.toString());
                    writer.flush();

                    int statusCode = connection.getResponseCode();
                    if (statusCode == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        try {
                            StringBuffer response = new StringBuffer();
                            String inputLine = reader.readLine();
                            while (inputLine != null) {
                                response.append(inputLine);
                                inputLine = reader.readLine();
                            }
                            JSONObject json = new JSONObject(response.toString());
                            onComplete.apply(json.getDouble("sa"));
                        } finally {
                            reader.close();
                        }
                    }
                    connection.disconnect();
                    return null;
                } catch (Throwable e) {
                    return null;
                }
            }
        }

        static private double samplingRate = 1.0;

        static void _invoke(String eventId, String hash, List<AvoAssertMessage> messages) throws JSONException {
            if (samplingRate > 0) {
                if (Math.random() < samplingRate) {
                    JSONObject json = new JSONObject();
                    json.put("ac", "JPOIYxL9pgFcQUUqsI6a");
                    json.put("br", "myhPpB0aV");
                    json.put("en", "dev");
                    json.put("ev", eventId);
                    json.put("ha", hash);
                    json.put("sc", "0cd8DLUxoxnhXaqRxL6O");
                    json.put("se", toISO8601UTC(new Date()));
                    json.put("so", "YlFip7cPY");
                    json.put("va", messages.isEmpty());
                    json.put("or", "event");

                    JSONArray me = new JSONArray();
                    for (AvoAssertMessage message: messages) {
                        JSONObject obj = new JSONObject();
                        obj.put("tag", message.getAssertionType());
                        obj.put("propertyId", message.getPropertyId());
                        me.put(obj);
                    }
                    json.put("me", me);

                    new HttpPostAsyncTask(json, new Callback() {
                        @Override
                        public void apply(Double rate) {
                            samplingRate = rate;
                        }
                    }).execute();
                }
            }
        }

        static void invoke(String eventId, String hash, List<AvoAssertMessage> messages) {
            try {
                _invoke(eventId, hash, messages);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        static void _invokeMeta(String type, List<AvoAssertMessage> messages) throws JSONException {
            if (samplingRate > 0) {
                if (Math.random() < samplingRate) {
                    JSONObject json = new JSONObject();
                    json.put("ac", "JPOIYxL9pgFcQUUqsI6a");
                    json.put("br", "myhPpB0aV");
                    json.put("en", "dev");
                    json.put("ty", type);
                    json.put("sc", "0cd8DLUxoxnhXaqRxL6O");
                    json.put("se", toISO8601UTC(new Date()));
                    json.put("so", "YlFip7cPY");
                    json.put("va", messages.isEmpty());

                    JSONArray me = new JSONArray();
                    for (AvoAssertMessage message: messages) {
                        JSONObject obj = new JSONObject();
                        obj.put("tag", message.getAssertionType());
                        obj.put("propertyId", message.getPropertyId());
                        me.put(obj);
                    }
                    json.put("me", me);

                    new HttpPostAsyncTask(json, new Callback() {
                        @Override
                        public void apply(Double rate) {
                            samplingRate = rate;
                        }
                    }).execute();
                }
            }
        }

        static void invokeMeta(String type, List<AvoAssertMessage> messages) {
            try {
                _invokeMeta(type, messages);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        static String toISO8601UTC(Date date) {
            TimeZone tz = TimeZone.getTimeZone("UTC");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.US);
            df.setTimeZone(tz);
            return df.format(date);
        }
    }

    private static ICustomDestination custom = null;

    public static void initAvo(AvoEnv env,
                               ICustomDestination customDestination) {
        initAvo(env, customDestination, null, null);
    }

    public static void initAvo(AvoEnv env,
                               ICustomDestination customDestination,
                               Boolean strict, Object debugger) {
        if (strict != null) {
            __STRICT__ = strict != false;
        }
        __AVO_ENV__ = env;
        if (debugger != null) {
            __MOBILE_DEBUGGER__ = debugger;
        }

        if (__AVO_ENV__ == AvoEnv.PROD) {
        }
        if (__AVO_ENV__ == AvoEnv.DEV) {
        }
        custom = customDestination;
        custom.make(env);
        if (__AVO_ENV__ != AvoEnv.PROD) {
            // debug console in Avo
            AvoInvoke.invokeMeta("init", Collections.<AvoAssertMessage>emptyList());
        }
    }

    private static List<AvoAssertMessage> assertUpcomingTrackName(String upcomingTrackName) {
        final List<AvoAssertMessage> messages = new ArrayList<>();
        messages.addAll(AvoAssert.assertNonOptional("Lvi0sAE1Am", "Upcoming Track Name", upcomingTrackName));
        return messages;
    }

    private static List<AvoAssertMessage> assertCurrentSongName(String currentSongName) {
        final List<AvoAssertMessage> messages = new ArrayList<>();
        messages.addAll(AvoAssert.assertNonOptional("kwANmf381A", "Current Song Name", currentSongName));
        return messages;
    }

    /**
     * App Opened: No description
     *
     * @see <a href="https://www.avo.app/schemas/0cd8DLUxoxnhXaqRxL6O/branches/myhPpB0aV/events/oZvpnm2MM">App Opened</a>
     */
    public static void appOpened() {
        // assert properties
        if (__AVO_ENV__ != AvoEnv.PROD || (__MOBILE_DEBUGGER_ENABLED__())) {
            final List<AvoAssertMessage> messages = new ArrayList<>();
            // debug console in Avo
            AvoInvoke.invoke("oZvpnm2MM", "69e1fdcb79d69ac99e813df3cc951f797327331ee8e368a794c5c12e7a371754", messages);
            if (__MOBILE_DEBUGGER_ENABLED__()) {
                // Avo mobile debugger
                __MOBILE_DEBUGGER_POST_EVENT__("oZvpnm2MM", System.currentTimeMillis(), "App Opened", new ArrayList<Map<String, String>>() {{
                    for (final AvoAssertMessage message: messages) {
                        add(new HashMap<String, String>() {{
                            put("tag", message.getAssertionType());
                            put("propertyId", message.getPropertyId());
                            put("message", message.getMessage());
                        }});
                    }
                }}, new ArrayList<Map<String, String>>() {{
                }}, new ArrayList<Map<String, String>>() {{
                }});
            }
        }

        if (__AVO_ENV__ != AvoEnv.PROD) {
            final Map<String, Object> avoLogEventProperties = new HashMap<>();

            final Map<String, Object> avoLogUserProperties = new HashMap<>();

            AvoLogger.logEventSent("App Opened", avoLogEventProperties, avoLogUserProperties);
        }

        // destination custom
        final Map<String, Object> customEventProperties = new HashMap<>();

        final Map<String, Object> customUserProperties = new HashMap<>();

        custom.logEvent("App Opened", customEventProperties);
    }

    /**
     * Play: Sent when the user plays a track.
     *
     * @param currentSongName The name of the song that's currently playing.
     *
     * @see <a href="https://www.avo.app/schemas/0cd8DLUxoxnhXaqRxL6O/branches/myhPpB0aV/events/6p9dLEHQVr">Play</a>
     */
    public static void play(final String currentSongName) {
        // assert properties
        if (__AVO_ENV__ != AvoEnv.PROD || (__MOBILE_DEBUGGER_ENABLED__())) {
            final List<AvoAssertMessage> messages = new ArrayList<>();
            messages.addAll(assertCurrentSongName(currentSongName));
            // debug console in Avo
            AvoInvoke.invoke("6p9dLEHQVr", "05e0c366ec9e69b66bce9ef162b6dbe0df8122f56aa62cd68a705c7c30fc4ef4", messages);
            if (__MOBILE_DEBUGGER_ENABLED__()) {
                // Avo mobile debugger
                __MOBILE_DEBUGGER_POST_EVENT__("6p9dLEHQVr", System.currentTimeMillis(), "Play", new ArrayList<Map<String, String>>() {{
                    for (final AvoAssertMessage message: messages) {
                        add(new HashMap<String, String>() {{
                            put("tag", message.getAssertionType());
                            put("propertyId", message.getPropertyId());
                            put("message", message.getMessage());
                        }});
                    }
                }}, new ArrayList<Map<String, String>>() {{
                    add(new HashMap<String, String>() {{
                        put("id", "kwANmf381A");
                        put("name", "Current Song Name");
                        put("value", currentSongName != null ? currentSongName.toString() : "");
                    }});
                }}, new ArrayList<Map<String, String>>() {{
                }});
            }
            if ((__STRICT__ == null && !(__MOBILE_DEBUGGER_ENABLED__())) || __STRICT__ == true) {
                // throw exception if messages is not empty
                if (!messages.isEmpty()) {
                    throw new IllegalArgumentException("Error sending event 'Play': " + messages.get(0).getMessage());
                }
            } else {
                for (AvoAssertMessage m: messages) {
                    Log.w(TAG, "[avo] " + m.getMessage());
                }
            }
        }

        if (__AVO_ENV__ != AvoEnv.PROD) {
            final Map<String, Object> avoLogEventProperties = new HashMap<>();
            if (currentSongName != null) {
                avoLogEventProperties.put("Current Song Name", currentSongName);
            }

            final Map<String, Object> avoLogUserProperties = new HashMap<>();

            AvoLogger.logEventSent("Play", avoLogEventProperties, avoLogUserProperties);
        }

        // destination custom
        final Map<String, Object> customEventProperties = new HashMap<>();
        if (currentSongName != null) {
            customEventProperties.put("Current Song Name", currentSongName);
        }

        final Map<String, Object> customUserProperties = new HashMap<>();

        custom.logEvent("Play", customEventProperties);
    }

    /**
     * Pause: Sent when the user pauses a track.
     *
     * @param currentSongName The name of the song that's currently playing.
     *
     * @see <a href="https://www.avo.app/schemas/0cd8DLUxoxnhXaqRxL6O/branches/myhPpB0aV/events/Ei7HeAerpy">Pause</a>
     */
    public static void pause(final String currentSongName) {
        // assert properties
        if (__AVO_ENV__ != AvoEnv.PROD || (__MOBILE_DEBUGGER_ENABLED__())) {
            final List<AvoAssertMessage> messages = new ArrayList<>();
            messages.addAll(assertCurrentSongName(currentSongName));
            // debug console in Avo
            AvoInvoke.invoke("Ei7HeAerpy", "7b1a826ff48bd59d6b01e1df87e5afe51b766dad0954e71d49133525842644b7", messages);
            if (__MOBILE_DEBUGGER_ENABLED__()) {
                // Avo mobile debugger
                __MOBILE_DEBUGGER_POST_EVENT__("Ei7HeAerpy", System.currentTimeMillis(), "Pause", new ArrayList<Map<String, String>>() {{
                    for (final AvoAssertMessage message: messages) {
                        add(new HashMap<String, String>() {{
                            put("tag", message.getAssertionType());
                            put("propertyId", message.getPropertyId());
                            put("message", message.getMessage());
                        }});
                    }
                }}, new ArrayList<Map<String, String>>() {{
                    add(new HashMap<String, String>() {{
                        put("id", "kwANmf381A");
                        put("name", "Current Song Name");
                        put("value", currentSongName != null ? currentSongName.toString() : "");
                    }});
                }}, new ArrayList<Map<String, String>>() {{
                }});
            }
            if ((__STRICT__ == null && !(__MOBILE_DEBUGGER_ENABLED__())) || __STRICT__) {
                // throw exception if messages is not empty
                if (!messages.isEmpty()) {
                    throw new IllegalArgumentException("Error sending event 'Pause': " + messages.get(0).getMessage());
                }
            } else {
                for (AvoAssertMessage m: messages) {
                    Log.w(TAG, "[avo] " + m.getMessage());
                }
            }
        }

        if (__AVO_ENV__ != AvoEnv.PROD) {
            final Map<String, Object> avoLogEventProperties = new HashMap<>();
            if (currentSongName != null) {
                avoLogEventProperties.put("Current Song Name", currentSongName);
            }

            final Map<String, Object> avoLogUserProperties = new HashMap<>();

            AvoLogger.logEventSent("Pause", avoLogEventProperties, avoLogUserProperties);
        }

        // destination custom
        final Map<String, Object> customEventProperties = new HashMap<>();
        if (currentSongName != null) {
            customEventProperties.put("Current Song Name", currentSongName);
        }

        final Map<String, Object> customUserProperties = new HashMap<>();

        custom.logEvent("Pause", customEventProperties);
    }

    /**
     * Play Next Track: Sent when the user plays next track.
     *
     * @param currentSongName The name of the song that's currently playing.
     * @param upcomingTrackName The name of the upcoming track.
     *
     * @see <a href="https://www.avo.app/schemas/0cd8DLUxoxnhXaqRxL6O/branches/myhPpB0aV/events/rQvcOWggzs">Play Next Track</a>
     */
    public static void playNextTrack(final String currentSongName,
                                     final String upcomingTrackName) {
        // assert properties
        if (__AVO_ENV__ != AvoEnv.PROD || (__MOBILE_DEBUGGER_ENABLED__())) {
            final List<AvoAssertMessage> messages = new ArrayList<>();
            messages.addAll(assertCurrentSongName(currentSongName));
            messages.addAll(assertUpcomingTrackName(upcomingTrackName));
            // debug console in Avo
            AvoInvoke.invoke("rQvcOWggzs", "b560e43057828b34f5ee9127f85d1c7b6f73a6b22de640e2b6d75d571157272c", messages);
            if (__MOBILE_DEBUGGER_ENABLED__()) {
                // Avo mobile debugger
                __MOBILE_DEBUGGER_POST_EVENT__("rQvcOWggzs", System.currentTimeMillis(), "Play Next Track", new ArrayList<Map<String, String>>() {{
                    for (final AvoAssertMessage message: messages) {
                        add(new HashMap<String, String>() {{
                            put("tag", message.getAssertionType());
                            put("propertyId", message.getPropertyId());
                            put("message", message.getMessage());
                        }});
                    }
                }}, new ArrayList<Map<String, String>>() {{
                    add(new HashMap<String, String>() {{
                        put("id", "kwANmf381A");
                        put("name", "Current Song Name");
                        put("value", currentSongName != null ? currentSongName.toString() : "");
                    }});
                    add(new HashMap<String, String>() {{
                        put("id", "Lvi0sAE1Am");
                        put("name", "Upcoming Track Name");
                        put("value", upcomingTrackName != null ? upcomingTrackName.toString() : "");
                    }});
                }}, new ArrayList<Map<String, String>>() {{
                }});
            }
            if ((__STRICT__ == null && !(__MOBILE_DEBUGGER_ENABLED__())) || __STRICT__) {
                // throw exception if messages is not empty
                if (!messages.isEmpty()) {
                    throw new IllegalArgumentException("Error sending event 'Play Next Track': " + messages.get(0).getMessage());
                }
            } else {
                for (AvoAssertMessage m: messages) {
                    Log.w(TAG, "[avo] " + m.getMessage());
                }
            }
        }

        if (__AVO_ENV__ != AvoEnv.PROD) {
            final Map<String, Object> avoLogEventProperties = new HashMap<>();
            if (currentSongName != null) {
                avoLogEventProperties.put("Current Song Name", currentSongName);
            }
            if (upcomingTrackName != null) {
                avoLogEventProperties.put("Upcoming Track Name", upcomingTrackName);
            }

            final Map<String, Object> avoLogUserProperties = new HashMap<>();

            AvoLogger.logEventSent("Play Next Track", avoLogEventProperties, avoLogUserProperties);
        }

        // destination custom
        final Map<String, Object> customEventProperties = new HashMap<>();
        if (currentSongName != null) {
            customEventProperties.put("Current Song Name", currentSongName);
        }
        if (upcomingTrackName != null) {
            customEventProperties.put("Upcoming Track Name", upcomingTrackName);
        }

        final Map<String, Object> customUserProperties = new HashMap<>();

        custom.logEvent("Play Next Track", customEventProperties);
    }

    /**
     * Play Previous Track: Sent when the user plays previous track.
     *
     * @param currentSongName The name of the song that's currently playing.
     * @param upcomingTrackName The name of the upcoming track.
     *
     * @see <a href="https://www.avo.app/schemas/0cd8DLUxoxnhXaqRxL6O/branches/myhPpB0aV/events/xBjjLugyOM">Play Previous Track</a>
     */
    public static void playPreviousTrack(final String currentSongName,
                                         final String upcomingTrackName) {
        // assert properties
        if (__AVO_ENV__ != AvoEnv.PROD || (__MOBILE_DEBUGGER_ENABLED__())) {
            final List<AvoAssertMessage> messages = new ArrayList<>();
            messages.addAll(assertCurrentSongName(currentSongName));
            messages.addAll(assertUpcomingTrackName(upcomingTrackName));
            // debug console in Avo
            AvoInvoke.invoke("xBjjLugyOM", "0d88a383e59745393c9bbf4caf59ee869e8c826c79179a5bf77e55b3775ecddd", messages);
            if (__MOBILE_DEBUGGER_ENABLED__()) {
                // Avo mobile debugger
                __MOBILE_DEBUGGER_POST_EVENT__("xBjjLugyOM", System.currentTimeMillis(), "Play Previous Track", new ArrayList<Map<String, String>>() {{
                    for (final AvoAssertMessage message: messages) {
                        add(new HashMap<String, String>() {{
                            put("tag", message.getAssertionType());
                            put("propertyId", message.getPropertyId());
                            put("message", message.getMessage());
                        }});
                    }
                }}, new ArrayList<Map<String, String>>() {{
                    add(new HashMap<String, String>() {{
                        put("id", "kwANmf381A");
                        put("name", "Current Song Name");
                        put("value", currentSongName != null ? currentSongName.toString() : "");
                    }});
                    add(new HashMap<String, String>() {{
                        put("id", "Lvi0sAE1Am");
                        put("name", "Upcoming Track Name");
                        put("value", upcomingTrackName != null ? upcomingTrackName.toString() : "");
                    }});
                }}, new ArrayList<Map<String, String>>() {{
                }});
            }
            if ((__STRICT__ == null && !(__MOBILE_DEBUGGER_ENABLED__())) || __STRICT__) {
                // throw exception if messages is not empty
                if (!messages.isEmpty()) {
                    throw new IllegalArgumentException("Error sending event 'Play Previous Track': " + messages.get(0).getMessage());
                }
            } else {
                for (AvoAssertMessage m: messages) {
                    Log.w(TAG, "[avo] " + m.getMessage());
                }
            }
        }

        if (__AVO_ENV__ != AvoEnv.PROD) {
            final Map<String, Object> avoLogEventProperties = new HashMap<>();
            if (currentSongName != null) {
                avoLogEventProperties.put("Current Song Name", currentSongName);
            }
            if (upcomingTrackName != null) {
                avoLogEventProperties.put("Upcoming Track Name", upcomingTrackName);
            }

            final Map<String, Object> avoLogUserProperties = new HashMap<>();

            AvoLogger.logEventSent("Play Previous Track", avoLogEventProperties, avoLogUserProperties);
        }

        // destination custom
        final Map<String, Object> customEventProperties = new HashMap<>();
        if (currentSongName != null) {
            customEventProperties.put("Current Song Name", currentSongName);
        }
        if (upcomingTrackName != null) {
            customEventProperties.put("Upcoming Track Name", upcomingTrackName);
        }

        final Map<String, Object> customUserProperties = new HashMap<>();

        custom.logEvent("Play Previous Track", customEventProperties);
    }


}
// AVOMODULEMAP:"Avo"
// AVOEVENTMAP:["appOpened","play","pause","playNextTrack","playPreviousTrack"]