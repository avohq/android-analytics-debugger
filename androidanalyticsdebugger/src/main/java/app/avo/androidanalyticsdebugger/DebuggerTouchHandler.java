package app.avo.androidanalyticsdebugger;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class DebuggerTouchHandler implements View.OnTouchListener {

    private int initialY;
    private float initialTouchY;

    private int initialX;
    private float initialTouchX;

    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;

    public DebuggerTouchHandler(WindowManager windowManager, WindowManager.LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
        this.windowManager = windowManager;
    }

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

                windowManager.updateViewLayout(v, layoutParams);
                return true;
            case MotionEvent.ACTION_UP:
                float endX = event.getRawX();
                float endY = event.getRawY();
                if (isAClick(initialTouchX, endX, initialTouchY, endY)) {
                    Toast.makeText(v.getContext(), "Debugger clicked", Toast.LENGTH_SHORT).show();
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

}
