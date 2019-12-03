package app.avo.musicplayerexample

import android.app.Application

import app.avo.androidanalyticsdebugger.DebuggerManager
import sh.avo.Avo
import sh.avo.AvoEnv
import sh.avo.ICustomDestination

class MusicPlayerExampleApplication : Application() {

    var debugger = DebuggerManager()

    override fun onCreate() {
        super.onCreate()

          Avo.initAvo(AvoEnv.DEV,
                object: ICustomDestination {
                    override fun make(env: AvoEnv) {
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
