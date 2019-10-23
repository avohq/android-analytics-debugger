package app.avo.musicplayerexample

import android.app.Application

import app.avo.androidanalyticsdebugger.DebuggerManager
import sh.avo.Avo

class MusciPlayerExampleApplication : Application() {

    var debugger = DebuggerManager()

    override fun onCreate() {
        super.onCreate()

        Avo.initAvo(Avo.AvoEnv.DEV, object: Avo.ICustomDestination {
            override fun make(env: Avo.AvoEnv?) {
            }

            override fun logEvent(eventName: String?, eventProperties: MutableMap<String, Any>?) {
            }

            override fun setUserProperties(userId: String?, userProperties: MutableMap<String, Any>?) {
            }

            override fun identify(userId: String?) {
            }

            override fun unidentify() {
            }
        }, null, debugger)
    }
}
