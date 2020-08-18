package app.avo.musicplayerexample

import android.app.Application

import app.avo.androidanalyticsdebugger.DebuggerManager
import sh.avo.*
import timber.log.Timber

class MusicPlayerExampleApplication : Application() {

    lateinit var debugger: DebuggerManager

    override fun onCreate() {
        super.onCreate()

        debugger = DebuggerManager(this)

        Avo.initAvo(this, this.applicationContext, Avo.AvoEnv.DEV,
            object: Avo.ICustomDestination {
                override fun make(env: Avo.AvoEnv) {
                }

                override fun logEvent(eventName: String, eventProperties: Map<String, *>) {
                }

                override fun setUserProperties(userId: String, userProperties: Map<String, *>) {
                }

                override fun identify(userId: String) {
                }

                override fun unidentify() {
                }
        }, debugger)

        Avo.appOpened()
    }
}
