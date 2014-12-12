package de.lukasniemeier.foregroundlinearlayouttest;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

import java.lang.reflect.Field;

import it.gmariotti.cardslib.library.view.ForegroundLinearLayout;

public class HackedForegroundLinearLayout extends ForegroundLinearLayout {

    private static final String TAG = HackedForegroundLinearLayout.class.getSimpleName();

    protected Field mForegroundBoundsChangedField;
    private boolean isFixable;

    public HackedForegroundLinearLayout(Context context) {
        super(context);
        initField();
    }

    public HackedForegroundLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initField();
    }

    public HackedForegroundLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initField();
    }

    private void initField() {
        isFixable = true;
        try {
            mForegroundBoundsChangedField = getClass().getSuperclass().getDeclaredField("mForegroundBoundsChanged");
        } catch (NoSuchFieldException e) {
            isFixable = false;
        }
        mForegroundBoundsChangedField.setAccessible(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, String.format("onLayout changed = %s", changed));

        if (isFixable) {
            boolean foregroundBoundsChanged = getForegroundBoundsChanged();
            super.onLayout(changed, left, top, right, bottom);
            if (foregroundBoundsChanged) {
                setForegroundBoundsChanged(true);
            }
        } else {
            super.onLayout(changed, left, top, right, bottom);
        }
    }

    protected boolean getForegroundBoundsChanged() {
        try {
            return mForegroundBoundsChangedField.getBoolean(this);
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    protected void setForegroundBoundsChanged(boolean value) {
        try {
            mForegroundBoundsChangedField.setBoolean(this, value);
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, String.format("onSizeChanged"));
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, String.format("onDraw"));
        super.onDraw(canvas);
    }
}
