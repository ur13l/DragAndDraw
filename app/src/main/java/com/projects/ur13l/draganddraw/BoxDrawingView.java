package com.projects.ur13l.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by ur13l on 16/01/15.
 */
public class BoxDrawingView extends View {
    public static final String TAG="BoxDrawingView";
    private static final String EXTRA_SAVED_STATE = "SavedState";
    private static final String EXTRA_BOXES = "Boxes";
    private Box mCurrentBox;
    private ArrayList<Box> mBoxes = new ArrayList<Box>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    @Override
    public boolean onTouchEvent(MotionEvent event){
        PointF curr = new PointF(event.getX(), event.getY());
        Log.i(TAG, "Received an event at x="+curr.x+", y="+curr.y + ":");
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG," ACTION_DOWN");
                mCurrentBox = new Box(curr);
                mBoxes.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG," ACTION_MOVE");
                if(mCurrentBox != null) {
                    mCurrentBox.setCurrent(curr);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG," ACTION_UP");
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG," ACTION_CANCEL");
                mCurrentBox = null;
                break;
        }
        return true;
    }

    //Used when creating the view in code
    public BoxDrawingView(Context context) {
        this(context,null);
    }

    //Used when inflating the view from XML
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context,attrs);

        //Paint the boxes a nice semitransparent red (ARGB)
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        //Paint the background off-white
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }


    @Override
    protected  void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);

        for(Box box : mBoxes) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable savedState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SAVED_STATE,savedState);
        bundle.putSerializable(EXTRA_BOXES, mBoxes);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        super.onRestoreInstanceState(((Bundle)state).getParcelable(EXTRA_SAVED_STATE));
        mBoxes = (ArrayList<Box>)((Bundle)state).getSerializable(EXTRA_BOXES);
        invalidate();

    }
}
