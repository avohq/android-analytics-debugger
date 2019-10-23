# android-analytics-debugger

## Add gradle dependency

We host the library on JitPack.io, so

add the following to the root build.gradle:

```
    allprojects {
        repositories {
          ...
          maven { url 'https://jitpack.io' }
        }
    }
```

and in your module build.gradle:

```
    dependencies {
	      implementation 'com.github.avohq:android-analytics-debugger:x.x.x'
    }
```

Use the latest github release tag to get the latest version of the library.

## Create the debugger manager instance

```
    DebuggerManager debuggerManager = new DebuggerManager();
```

## Show the debugger

```
    @Override
    protected void onStart() {
        super.onStart();

        debuggerManager.showDebugger(this, DebuggerMode.bar); // Can also be DebuggerMode.bubble
    }
```
    
If your app uses multiple activities:

- Option 1: add `showDebugger` call to every activity's `onStart` method similar to example above. You can use separate `DebuggerManager` instances, the library will make sure only 1 debugger is shown and all events are available.
- Option 2: use `showDebugger(this, DebuggerMode.bar, true)` method. The debugger view will become a system-wide overlay. Might be useful if you want to see events while your app is in background. In this case you'll need to enable "Draw over other apps" in Settings - Apps - Your app.
    
# Using with Avo
```
    class MusciPlayerExampleApplication : Application() {

        val debuggerManager = DebuggerManager()

        override fun onCreate() {
            super.onCreate()

            Avo.initAvo(Avo.AvoEnv.DEV, debuggerManager)
            Avo.appOpened()
        }
    }
````
    
 Then after you call `showDebugger` in your activity the debugger view will appear with the `App Opened` event inside.