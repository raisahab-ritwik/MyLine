package com.seva60plus.widget.charttest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 *
 */
public class PieChart extends FrameLayout {

    float total = 0;

    int[] palette = new int[7];

    public PieChart(Context context) {
        super(context);
        init();
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        palette[0] = Color.parseColor("#FF06D156");
        palette[1] = Color.parseColor("#FFFF0000");
        palette[2] = Color.parseColor("#FFFFC200");
        palette[3] = Color.parseColor("#FFA0A0A0");
      /*  palette[4] = Color.parseColor("#FF0090BD");
        palette[5] = Color.parseColor("#FF00B200");
        palette[6] = Color.parseColor("#FFFD4425");*/
    }

    ArrayList<PieSlice> pieSlices;

    public void setData(float[] data) {
        pieSlices = new ArrayList<PieSlice>();
        if (data == null) {
            invalidate();
            return;
        }
        total = 0;
        for (int i = 0; i < data.length; i++) {
            total += data[i];
        }
        float startAngle = 0, sweepAngle;
        for (int i = 0; i < data.length; i++) {

            sweepAngle = data[i] * (360f / total);

            PieSlice pieSlice = new PieSlice(getContext(), this);

            pieSlice.startAngele = startAngle;
            pieSlice.sweepAngle = sweepAngle;
            addView(pieSlice);
            pieSlices.add(pieSlice);
            pieSlice.paint.setColor(palette[i % 6]);

            startAngle += sweepAngle;
        }
    }

    public void setLabels(String[] labels) {
        for (int i = 0; i < pieSlices.size(); i++) {
            pieSlices.get(i).label = labels[i];
        }
    }

    RectF bounds;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float width = getMeasuredWidth();
        float height = getMeasuredHeight();

        float rectSize = Math.min(width, height);
        float widthDiff = (width - rectSize) / 2;
        float heightDiff = (height - rectSize) / 2;
        bounds = new RectF(widthDiff, heightDiff, rectSize + widthDiff, rectSize + heightDiff);

        if (pieSlices != null)
            for (PieSlice pieSlice : pieSlices) {
                pieSlice.oval = bounds;
            }
    }

    float preX, preY, angle = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            preX = event.getX();
            preY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float diffX = preX - event.getX();
            float diffY = preY - event.getY();
           angle += diffX;
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            //animate().setInterpolator(new DecelerateInterpolator(1.5f));
            //animate().rotation(angle);
            preX = event.getX();
            preY = event.getY();
        }
        return true;
    }
}
