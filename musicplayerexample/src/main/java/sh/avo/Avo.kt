// Generated by Avo VERSION 51.37.0, PLEASE EDIT WITH CARE
package sh.avo

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.URL
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.text.SimpleDateFormat
import javax.net.ssl.HttpsURLConnection
import android.os.AsyncTask


import org.json.JSONException
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.InvocationTargetException


enum class AvoEnv {
    PROD,
    DEV
}

private sealed class AvoAssertMessage { abstract val propertyId: String; abstract val message: String }
private data class AvoAssertMax(override val propertyId: String, override val message: String) : AvoAssertMessage()
private data class AvoAssertMin(override val propertyId: String, override val message: String) : AvoAssertMessage()


private object AvoAssert {
    fun assertMax(propertyId: String, property: String, max: Double, value: Double): List<AvoAssertMessage> {
        if (value > max) {
            return listOf(AvoAssertMax(propertyId, "$property has a maximum value of $max but you provided the value $value"))
        } else {
            return emptyList()
        }
    }

    fun assertMax(propertyId: String, property: String, max: Int, value: Int): List<AvoAssertMessage> {
        if (value > max) {
            return listOf(AvoAssertMax(propertyId, "$property has a maximum value of $max but you provided the value $value"))
        } else {
            return emptyList()
        }
    }

    fun assertMax(propertyId: String, property: String, max: Long, value: Long): List<AvoAssertMessage> {
        if (value > max) {
            return listOf(AvoAssertMax(propertyId, "$property has a maximum value of $max but you provided the value $value"))
        } else {
            return emptyList()
        }
    }

    fun assertMin(propertyId: String, property: String, min: Double, value: Double): List<AvoAssertMessage> {
        if (value < min) {
            return listOf(AvoAssertMin(propertyId, "$property has a minimum value of $min but you provided the value $value"))
        } else {
            return emptyList()
        }
    }

    fun assertMin(propertyId: String, property: String, min: Int, value: Int): List<AvoAssertMessage> {
        if (value < min) {
            return listOf(AvoAssertMin(propertyId, "$property has a minimum value of $min but you provided the value $value"))
        } else {
            return emptyList()
        }
    }

    fun assertMin(propertyId: String, property: String, min: Long, value: Long): List<AvoAssertMessage> {
        if (value < min) {
            return listOf(AvoAssertMin(propertyId, "$property has a minimum value of $min but you provided the value $value"))
        } else {
            return emptyList()
        }
    }
}

private object AvoLogger {
    fun logEventSent(
        eventName: String,
        eventProperties: Map<String, *>,
        userProperties: Map<String, *>
    ) {
        println("[avo] Event Sent: $eventName Event Props: $eventProperties User Props: $userProperties")
    }
}

interface ICustomDestination {
    fun make(env: AvoEnv)

    fun logEvent(eventName: String, eventProperties: Map<String, *>)

    fun setUserProperties(userId: String, userProperties: Map<String, *>)

    fun identify(userId: String)

    fun unidentify()
}

var __MOBILE_DEBUGGER__: Any? = null

private fun __MOBILE_DEBUGGER_ENABLED__(): Boolean {
    if (__MOBILE_DEBUGGER__ != null) {
        try {
            val method = __MOBILE_DEBUGGER__?.javaClass?.getMethod("isEnabled")
            return method?.invoke(__MOBILE_DEBUGGER__) == true
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
            return false
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return false
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
            return false
        }
    } else {
        return false
    }
}

private fun __MOBILE_DEBUGGER_POST_EVENT__(id: String, name: String,
                                           messages: List<Map<String, String>>,
                                           eventProperties: List<Map<String, String>>,
                                           userProperties: List<Map<String, String>>) {
    if (__MOBILE_DEBUGGER__ != null) {
        try {
            val method = __MOBILE_DEBUGGER__?.javaClass?.getMethod("publishEvent",
                    String::class.java, Long::class.javaObjectType, String::class.java,
                    List::class.java, List::class.java, List::class.java)
            method?.invoke(__MOBILE_DEBUGGER__,
                    id, System.currentTimeMillis(), name, messages, eventProperties, userProperties)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }
}


private object AvoInvoke {
    private class HttpPostAsyncTask(val json: JSONObject, val onComplete: (Double) -> Unit): AsyncTask<String, Void, Void>() {
        override fun doInBackground(vararg parts: String): Void? {
            try {
              val connection = URL("https://api.avo.app/i").openConnection() as HttpsURLConnection
              connection.requestMethod = "POST"
              connection.doOutput = true
              connection.setRequestProperty("Content-Type", "application/json")

              val writer = OutputStreamWriter(connection.outputStream)
              writer.write(json.toString())
              writer.flush()

              val statusCode = connection.responseCode
              if (statusCode == 200) {
                  BufferedReader(InputStreamReader(connection.inputStream)).use {
                      val response = StringBuffer()
                      var inputLine = it.readLine()
                      while (inputLine != null) {
                          response.append(inputLine)
                          inputLine = it.readLine()
                      }
                      it.close()
                      val json = JSONObject(response.toString())
                      onComplete(json.getDouble("sa"))
                  }
              }
              connection.disconnect()
              return null
            } catch (e: Throwable) {
                return null
            }
        }
    }

    var samplingRate = 1.0

    fun invoke(eventId: String, hash: String, messages: List<AvoAssertMessage>) {
        if (samplingRate > 0) {
            if (Math.random() < samplingRate) {
                val json = JSONObject()
                json.put("ac", "CPMKOVAr1ZQsJW6yarmb")
                json.put("br", "master")
                json.put("en", "dev")
                json.put("ev", eventId)
                json.put("ha", hash)
                json.put("sc", "0cd8DLUxoxnhXaqRxL6O")
                json.put("se", toISO8601UTC(Date()))
                json.put("so", "M75yQelC4")
                json.put("va", messages.isEmpty())
                json.put("or", "event")

                val me = JSONArray()
                for (message in messages) {
                    val obj = JSONObject()
                    when (message) {
                        is AvoAssertMax -> {
                            obj.put("tag", "expectedMax")
                            obj.put("propertyId", message.propertyId)
                        }
                        is AvoAssertMin -> {
                            obj.put("tag", "expectedMin")
                            obj.put("propertyId", message.propertyId)
                        }
                    }
                    me.put(obj)
                }
                json.put("me", me)
                HttpPostAsyncTask(json) {
                    rate -> samplingRate = rate
                }.execute()
            }
        }
    }

    fun invokeMeta(type: String, messages: List<AvoAssertMessage>) {
        if (samplingRate > 0) {
            if (Math.random() < samplingRate) {
                val json = JSONObject()
                json.put("ac", "CPMKOVAr1ZQsJW6yarmb")
                json.put("br", "master")
                json.put("en", "dev")
                json.put("ty", type)
                json.put("sc", "0cd8DLUxoxnhXaqRxL6O")
                json.put("se", toISO8601UTC(Date()))
                json.put("so", "M75yQelC4")
                json.put("va", messages.isEmpty())
                json.put("me", JSONArray())
                HttpPostAsyncTask(json) {
                    rate -> samplingRate = rate
                }.execute()
            }
        }
    }

    fun toISO8601UTC(date: Date): String {
        val tz = TimeZone.getTimeZone("UTC")
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.US)
        df.timeZone = tz
        return df.format(date)
    }
}

interface Avo {
    fun initAvo(
        env: AvoEnv,
        customDestination: ICustomDestination,
        debugger: Any
    )
    
    fun initAvo(
        env: AvoEnv,
        customDestination: ICustomDestination,
        debugger: Any,
        strict: Boolean
    )
    
    fun initAvo(
        env: AvoEnv,
        customDestination: ICustomDestination
    )
    
    fun initAvo(
        env: AvoEnv,
        customDestination: ICustomDestination,
        strict: Boolean
    )
    
    /**
     * App Opened: No description
     *
     * @see [App Opened in Avo](https://www.avo.app/schemas/0cd8DLUxoxnhXaqRxL6O/events/oZvpnm2MM)
     */
    fun appOpened()
    /**
     * Play: Sent when the user plays a track.
     * 
     * @param currentSongName The name of the song that's currently playing.
     *
     * @see [Play in Avo](https://www.avo.app/schemas/0cd8DLUxoxnhXaqRxL6O/events/6p9dLEHQVr)
     */
    fun play(currentSongName: String)
    /**
     * Pause: Sent when the user pauses a track.
     * 
     * @param currentSongName The name of the song that's currently playing.
     *
     * @see [Pause in Avo](https://www.avo.app/schemas/0cd8DLUxoxnhXaqRxL6O/events/Ei7HeAerpy)
     */
    fun pause(currentSongName: String)
    /**
     * Play Next Track: Sent when the user plays next track.
     * 
     * @param currentSongName The name of the song that's currently playing.
     * @param upcomingTrackName The name of the upcoming track.
     *
     * @see [Play Next Track in Avo](https://www.avo.app/schemas/0cd8DLUxoxnhXaqRxL6O/events/rQvcOWggzs)
     */
    fun playNextTrack(
        currentSongName: String,
        upcomingTrackName: String
    )
    
    /**
     * Play Previous Track: Sent when the user plays previous track.
     * 
     * @param currentSongName The name of the song that's currently playing.
     * @param upcomingTrackName The name of the upcoming track.
     *
     * @see [Play Previous Track in Avo](https://www.avo.app/schemas/0cd8DLUxoxnhXaqRxL6O/events/xBjjLugyOM)
     */
    fun playPreviousTrack(
        currentSongName: String,
        upcomingTrackName: String
    )
    
    
    companion object : Avo {
        var __STRICT__: Boolean = true
        lateinit var __ENV__: AvoEnv
        
        lateinit var custom: ICustomDestination
        
        override fun initAvo(
            env: AvoEnv,
            customDestination: ICustomDestination,
            debugger: Any
        ) {
            __MOBILE_DEBUGGER__ = debugger
            
            initAvo(env, customDestination, false)
        }
        
        override fun initAvo(
            env: AvoEnv,
            customDestination: ICustomDestination,
            debugger: Any,
            strict: Boolean
        ) {
            __MOBILE_DEBUGGER__ = debugger
            
            initAvo(env, customDestination, strict)
        }
        
        override fun initAvo(
            env: AvoEnv,
            customDestination: ICustomDestination
        ) {
            initAvo(env, customDestination, true)
        }
        
        override fun initAvo(
            env: AvoEnv,
            customDestination: ICustomDestination,
            strict: Boolean
        ) {
            __STRICT__ = strict
            __ENV__ = env
            
            custom = customDestination
            custom.make(env=__ENV__)
            if (__ENV__ != AvoEnv.PROD) {
                // debug console in Avo
                AvoInvoke.invokeMeta("init", emptyList())
            }
        }
        
        /**
         * App Opened: No description
         *
         * @see [App Opened in Avo](https://www.avo.app/schemas/0cd8DLUxoxnhXaqRxL6O/events/oZvpnm2MM)
         */
        override fun appOpened() {
            // assert properties
            if (__ENV__ != AvoEnv.PROD || __MOBILE_DEBUGGER_ENABLED__()) {
                val messages = mutableListOf<AvoAssertMessage>()
                // debug console in Avo
                AvoInvoke.invoke("oZvpnm2MM", "e60cf562c83dd6e3d56d30c4de3193c2fc896868e2ba853b7b79edc23d66b110", messages)
                if (__ENV__ != AvoEnv.PROD && __MOBILE_DEBUGGER__ != null || __ENV__ == AvoEnv.PROD && __MOBILE_DEBUGGER_ENABLED__()) {
                    // Avo mobile debugger
                    val eventProps: List<Map<String, String>> = listOf()
                    val userProps: List<Map<String, String>> = listOf()
                    __MOBILE_DEBUGGER_POST_EVENT__("oZvpnm2MM", "App Opened", eventProperties = eventProps, userProperties = userProps, 
                            messages = messages.map {
                                mapOf("tag" to it.javaClass.simpleName, "propertyId" to it.propertyId,
                                        "message" to it.message)
                            })
                }
            }
            
            if (__ENV__ != AvoEnv.PROD) {
                val avoLogEventProperties = emptyMap<String, Any>()
                val avoLogUserProperties = emptyMap<String, Any>()
                AvoLogger.logEventSent("App Opened", avoLogEventProperties, avoLogUserProperties)
            }
            
            // destination custom
            val customEventProperties = emptyMap<String, Any>()
            val customUserProperties = emptyMap<String, Any>()
            custom.logEvent("App Opened", customEventProperties)
        }
        
        /**
         * Play: Sent when the user plays a track.
         * 
         * @param currentSongName The name of the song that's currently playing.
         *
         * @see [Play in Avo](https://www.avo.app/schemas/0cd8DLUxoxnhXaqRxL6O/events/6p9dLEHQVr)
         */
        override fun play(currentSongName: String) {
            // assert properties
            if (__ENV__ != AvoEnv.PROD || __MOBILE_DEBUGGER_ENABLED__()) {
                val messages = mutableListOf<AvoAssertMessage>()
                // debug console in Avo
                AvoInvoke.invoke("6p9dLEHQVr", "7530f4d2c8ec5c0258ba79091af59205b15522159fb3339ce9792c7f242f84c8", messages)
                if (__ENV__ != AvoEnv.PROD && __MOBILE_DEBUGGER__ != null || __ENV__ == AvoEnv.PROD && __MOBILE_DEBUGGER_ENABLED__()) {
                    // Avo mobile debugger
                    val eventProps: List<Map<String, String>> = listOf(
                      mapOf("id" to "kwANmf381A", "name" to "Current Song Name", "value" to (currentSongName.toString())))
                    val userProps: List<Map<String, String>> = listOf()
                    __MOBILE_DEBUGGER_POST_EVENT__("6p9dLEHQVr", "Play", eventProperties = eventProps, userProperties = userProps, 
                            messages = messages.map {
                                mapOf("tag" to it.javaClass.simpleName, "propertyId" to it.propertyId,
                                        "message" to it.message)
                            })
                }
            }
            
            if (__ENV__ != AvoEnv.PROD) {
                val avoLogEventProperties = mapOf(
                    "Current Song Name" to currentSongName
                )
                val avoLogUserProperties = emptyMap<String, Any>()
                AvoLogger.logEventSent("Play", avoLogEventProperties, avoLogUserProperties)
            }
            
            // destination custom
            val customEventProperties = mapOf(
                "Current Song Name" to currentSongName
            )
            val customUserProperties = emptyMap<String, Any>()
            custom.logEvent("Play", customEventProperties)
        }
        
        /**
         * Pause: Sent when the user pauses a track.
         * 
         * @param currentSongName The name of the song that's currently playing.
         *
         * @see [Pause in Avo](https://www.avo.app/schemas/0cd8DLUxoxnhXaqRxL6O/events/Ei7HeAerpy)
         */
        override fun pause(currentSongName: String) {
            // assert properties
            if (__ENV__ != AvoEnv.PROD || __MOBILE_DEBUGGER_ENABLED__()) {
                val messages = mutableListOf<AvoAssertMessage>()
                // debug console in Avo
                AvoInvoke.invoke("Ei7HeAerpy", "56e604e18496894cf278be342b5b440f2057259a7dc348c7c14050a4cbb52c71", messages)
                if (__ENV__ != AvoEnv.PROD && __MOBILE_DEBUGGER__ != null || __ENV__ == AvoEnv.PROD && __MOBILE_DEBUGGER_ENABLED__()) {
                    // Avo mobile debugger
                    val eventProps: List<Map<String, String>> = listOf(
                      mapOf("id" to "kwANmf381A", "name" to "Current Song Name", "value" to (currentSongName.toString())))
                    val userProps: List<Map<String, String>> = listOf()
                    __MOBILE_DEBUGGER_POST_EVENT__("Ei7HeAerpy", "Pause", eventProperties = eventProps, userProperties = userProps, 
                            messages = messages.map {
                                mapOf("tag" to it.javaClass.simpleName, "propertyId" to it.propertyId,
                                        "message" to it.message)
                            })
                }
            }
            
            if (__ENV__ != AvoEnv.PROD) {
                val avoLogEventProperties = mapOf(
                    "Current Song Name" to currentSongName
                )
                val avoLogUserProperties = emptyMap<String, Any>()
                AvoLogger.logEventSent("Pause", avoLogEventProperties, avoLogUserProperties)
            }
            
            // destination custom
            val customEventProperties = mapOf(
                "Current Song Name" to currentSongName
            )
            val customUserProperties = emptyMap<String, Any>()
            custom.logEvent("Pause", customEventProperties)
        }
        
        /**
         * Play Next Track: Sent when the user plays next track.
         * 
         * @param currentSongName The name of the song that's currently playing.
         * @param upcomingTrackName The name of the upcoming track.
         *
         * @see [Play Next Track in Avo](https://www.avo.app/schemas/0cd8DLUxoxnhXaqRxL6O/events/rQvcOWggzs)
         */
        override fun playNextTrack(
            currentSongName: String,
            upcomingTrackName: String
        ) {
            // assert properties
            if (__ENV__ != AvoEnv.PROD || __MOBILE_DEBUGGER_ENABLED__()) {
                val messages = mutableListOf<AvoAssertMessage>()
                // debug console in Avo
                AvoInvoke.invoke("rQvcOWggzs", "efcd87917a305c400c4d1ffa62a608914016478a7381b959068773a1bd009334", messages)
                if (__ENV__ != AvoEnv.PROD && __MOBILE_DEBUGGER__ != null || __ENV__ == AvoEnv.PROD && __MOBILE_DEBUGGER_ENABLED__()) {
                    // Avo mobile debugger
                    val eventProps: List<Map<String, String>> = listOf(
                      mapOf("id" to "kwANmf381A", "name" to "Current Song Name", "value" to (currentSongName.toString())), 
                      mapOf("id" to "Lvi0sAE1Am", "name" to "Upcoming Track Name", "value" to (upcomingTrackName.toString())))
                    val userProps: List<Map<String, String>> = listOf()
                    __MOBILE_DEBUGGER_POST_EVENT__("rQvcOWggzs", "Play Next Track", eventProperties = eventProps, userProperties = userProps, 
                            messages = messages.map {
                                mapOf("tag" to it.javaClass.simpleName, "propertyId" to it.propertyId,
                                        "message" to it.message)
                            })
                }
            }
            
            if (__ENV__ != AvoEnv.PROD) {
                val avoLogEventProperties = mapOf(
                    "Current Song Name" to currentSongName,
                    "Upcoming Track Name" to upcomingTrackName
                )
                val avoLogUserProperties = emptyMap<String, Any>()
                AvoLogger.logEventSent("Play Next Track", avoLogEventProperties, avoLogUserProperties)
            }
            
            // destination custom
            val customEventProperties = mapOf(
                "Current Song Name" to currentSongName,
                "Upcoming Track Name" to upcomingTrackName
            )
            val customUserProperties = emptyMap<String, Any>()
            custom.logEvent("Play Next Track", customEventProperties)
        }
        
        /**
         * Play Previous Track: Sent when the user plays previous track.
         * 
         * @param currentSongName The name of the song that's currently playing.
         * @param upcomingTrackName The name of the upcoming track.
         *
         * @see [Play Previous Track in Avo](https://www.avo.app/schemas/0cd8DLUxoxnhXaqRxL6O/events/xBjjLugyOM)
         */
        override fun playPreviousTrack(
            currentSongName: String,
            upcomingTrackName: String
        ) {
            // assert properties
            if (__ENV__ != AvoEnv.PROD || __MOBILE_DEBUGGER_ENABLED__()) {
                val messages = mutableListOf<AvoAssertMessage>()
                // debug console in Avo
                AvoInvoke.invoke("xBjjLugyOM", "bbe3fe829f6e51dc4ab3b85bece5b765576bb6505395d8cf1e63456d8d9e11a8", messages)
                if (__ENV__ != AvoEnv.PROD && __MOBILE_DEBUGGER__ != null || __ENV__ == AvoEnv.PROD && __MOBILE_DEBUGGER_ENABLED__()) {
                    // Avo mobile debugger
                    val eventProps: List<Map<String, String>> = listOf(
                      mapOf("id" to "kwANmf381A", "name" to "Current Song Name", "value" to (currentSongName.toString())), 
                      mapOf("id" to "Lvi0sAE1Am", "name" to "Upcoming Track Name", "value" to (upcomingTrackName.toString())))
                    val userProps: List<Map<String, String>> = listOf()
                    __MOBILE_DEBUGGER_POST_EVENT__("xBjjLugyOM", "Play Previous Track", eventProperties = eventProps, userProperties = userProps, 
                            messages = messages.map {
                                mapOf("tag" to it.javaClass.simpleName, "propertyId" to it.propertyId,
                                        "message" to it.message)
                            })
                }
            }
            
            if (__ENV__ != AvoEnv.PROD) {
                val avoLogEventProperties = mapOf(
                    "Current Song Name" to currentSongName,
                    "Upcoming Track Name" to upcomingTrackName
                )
                val avoLogUserProperties = emptyMap<String, Any>()
                AvoLogger.logEventSent("Play Previous Track", avoLogEventProperties, avoLogUserProperties)
            }
            
            // destination custom
            val customEventProperties = mapOf(
                "Current Song Name" to currentSongName,
                "Upcoming Track Name" to upcomingTrackName
            )
            val customUserProperties = emptyMap<String, Any>()
            custom.logEvent("Play Previous Track", customEventProperties)
        }
        
    }
    
}

// AVOMODULEMAP:"Avo"
// AVOEVENTMAP:["appOpened","play","pause","playNextTrack","playPreviousTrack"]
