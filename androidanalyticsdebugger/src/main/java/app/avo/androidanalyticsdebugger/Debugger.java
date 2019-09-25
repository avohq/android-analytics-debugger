package app.avo.androidanalyticsdebugger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class Debugger {

    static WeakReference<TextView> debuggerViewRef;

    @SuppressLint("ClickableViewAccessibility")
    public void showDebugger(final Activity rootActivity, DebuggerMode mode) {
        final WindowManager windowManager = rootActivity.getWindowManager();

        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;

        int debuggerViewDimention = (int) convertDpToPixel(48, rootActivity);

        final TextView debuggerView;

        if (mode == DebuggerMode.bar) {
            debuggerView = new TextView(rootActivity);
            debuggerView.setText("Debugger");
            debuggerView.setBackgroundColor(Color.BLACK);
            debuggerView.setTextColor(Color.WHITE);
            debuggerView.setGravity(Gravity.CENTER);

            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

            debuggerView.setHeight(debuggerViewDimention);
        } else {
            debuggerView = new TextView(rootActivity);
            debuggerView.setText("Debugger");
            debuggerView.setBackgroundColor(Color.BLACK);
            debuggerView.setTextColor(Color.WHITE);
            debuggerView.setGravity(Gravity.CENTER);

            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;

            debuggerView.setHeight(debuggerViewDimention);
            debuggerView.setWidth(debuggerViewDimention);
        }

        debuggerViewRef = new WeakReference<>(debuggerView);

        rootActivity.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        layoutParams.y = (displayMetrics.heightPixels - debuggerViewDimention) / 2;

        windowManager.addView(debuggerView, layoutParams);

        debuggerView.setOnTouchListener(new View.OnTouchListener() {
            private int initialY;
            private float initialTouchY;

            private int initialX;
            private float initialTouchX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialY = layoutParams.y;
                        initialX = layoutParams.x;

                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        layoutParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                        layoutParams.x = initialX + (int) (event.getRawX() - initialTouchX);

                        windowManager.updateViewLayout(debuggerView, layoutParams);
                        return true;
                    case MotionEvent.ACTION_UP:
                        float endX = event.getRawX();
                        float endY = event.getRawY();
                        if (isAClick(initialTouchX, endX, initialTouchY, endY)) {
                            Toast.makeText(rootActivity, "Debugger clicked", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                }
                return false;
            }

            private boolean isAClick(float startX, float endX, float startY, float endY) {
                float differenceX = Math.abs(startX - endX);
                float differenceY = Math.abs(startY - endY);
                return !(differenceX > 5 || differenceY > 5);
            }
        });
    }

    public void hideDebugger(Activity rootActivity) {
        TextView debuggerView = debuggerViewRef.get();
        if (debuggerView != null) {
            rootActivity.getWindowManager().removeView(debuggerView);
        }
    }

    private float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}