package com.example.mycloset.add;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

public class CropTouchView extends View {
    public static PinPoint head;
    private PinPoint current;
    private final Paint paint;
    private boolean completed;

    private Bitmap toCrop;

    public void setToCrop(Bitmap toCrop) {
        this.toCrop = toCrop;
        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.height = toCrop.getHeight();
        params.width = toCrop.getWidth();
        this.setLayoutParams(params);
        invalidate();
    }

    public Bitmap getToCrop() {
        return toCrop;
    }

    public CropTouchView(Context context) {
        super(context);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setPathEffect(new DashPathEffect(new float[] { 10, 20 }, 0));
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);

        head = new PinPoint((float)-100, (float)-100);
        this.completed = false;
    }

    public CropTouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setPathEffect(new DashPathEffect(new float[] { 10, 20 }, 0));
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);
        head = new PinPoint((float)-100, (float)-100);
        this.completed = false;
    }

    public CropTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAtt) {
        super(context, attrs, defStyleAtt);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setPathEffect(new DashPathEffect(new float[] { 10, 20 }, 0));
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);
        head = new PinPoint((float)-100, (float)-100);
        this.completed = false;
    }


    protected void onDraw(Canvas canvas) {

        if (toCrop != null){
            canvas.drawBitmap(toCrop, 0, 0, paint);
        }
        PinPoint startPoint;
        for(startPoint = head.getNextPoint(); startPoint != null; startPoint = startPoint.getNextPoint()){
            float startX = startPoint.getFirst();
            float startY = startPoint.getSecond();
            canvas.drawCircle(startX, startY, 15, paint);
            PinPoint stopPoint = startPoint.getNextPoint();
            if (stopPoint != null){
                float stopX = stopPoint.getFirst();
                float stopY = stopPoint.getSecond();
                canvas.drawLine(startX, startY, stopX, stopY, paint);
            }
        }
        if (completed){
            startPoint = PinPoint.findLastPoint(head);
            PinPoint initPoint = head.getNextPoint();
            Log.d("STATE", "completed");
            canvas.drawLine(startPoint.getFirst(), startPoint.getSecond(), initPoint.getFirst(), initPoint.getSecond(), paint);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                boolean createPoint = true;

                for(current = head.getNextPoint(); current != null; current = current.getNextPoint()) {
                    float currentX = current.getFirst();
                    float currentY = current.getSecond();
                    if (Math.hypot(eventX - currentX, eventY - currentY) <= 15){
                        createPoint = false;
                        break;
                    }
                }
                if (createPoint){
                    for(current = head.getNextPoint(); current != null; current = current.getNextPoint()) {
                        PinPoint nextPoint = current.getNextPoint();
                        if(nextPoint != null){
                            float x1 = current.getFirst(), y1 = current.getSecond(), x2 = nextPoint.getFirst(), y2 = nextPoint.getSecond();
                            float a = y2 - y1, b = x1 - x2, c = (x2 - x1) * y1 + (y1 - y2) * x1;
                            if (Math.abs(a * eventX + b * eventY + c) / Math.hypot(a, b) <= 20){
                                createPoint = false;
                                PinPoint middlePoint = new PinPoint(eventX, eventY);
                                middlePoint.setNextPoint(nextPoint);
                                current.setNextPoint(middlePoint);
                                current = middlePoint;
                                break;
                            }
                        }
                    }
                }


                if (createPoint){
                    current = PinPoint.findLastPoint(head);
                    current.setNextPoint(new PinPoint(eventX, eventY));
                    current = current.getNextPoint();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                current.setCoordinates(eventX, eventY);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void completed() {
        this.completed = true;
        invalidate();
    }
    public void clear() {
        head.setNextPoint(null);
        this.completed = false;
        invalidate();
    }
}

class PinPoint {

    private PinPoint nextPoint;
    private Float first;
    private Float second;

    public Float getFirst() {
        return first;
    }

    public Float getSecond() {
        return second;
    }

    public PinPoint (Float first, Float second) {
        this.first = first;
        this.second = second;
        nextPoint = null;
    }

    public void setCoordinates(Float first, Float second) {
        this.first = first;
        this.second = second;
    }

    public PinPoint getNextPoint() {
        return nextPoint;
    }

    public void setNextPoint(PinPoint nextPoint) {
        this.nextPoint = nextPoint;
    }

    static PinPoint findLastPoint(PinPoint init) {
        PinPoint current = init;
        for (; current.nextPoint != null; current = current.nextPoint) ;
        return current;
    }
}
