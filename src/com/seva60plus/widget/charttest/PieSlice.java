package com.seva60plus.widget.charttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/**
 *
 */
public class PieSlice extends View {

    static final float D_TO_R = 0.0174532925f;
    String label;
    RectF oval;
    float startAngele, sweepAngle;
    Paint paint;
    Paint labelPaint;
    PieChart pieChart;

    public PieSlice(Context context, PieChart pieChart) {
        super(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.pieChart = pieChart;

        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setColor(Color.WHITE);
        labelPaint.setTextSize(40);
    }

    Rect labelRect = new Rect();

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(oval, startAngele, sweepAngle, true, paint);
        float angle = (startAngele + (sweepAngle / 2)) * D_TO_R;

        canvas.save();

        if (label != null) {
            labelPaint.getTextBounds(label, 0, label.length(), labelRect);
            float x = oval.centerX() + (float) Math.cos(angle) * (oval.width() / 4 + labelRect.width() / 2);
            float y = oval.centerY() + (float) Math.sin(angle) * (oval.height() / 4 + labelRect.width() / 2);
            x -= labelRect.width() / 2;
            canvas.rotate(startAngele + (sweepAngle / 2), x + labelRect.exactCenterX(), y + labelRect.exactCenterY());
            canvas.drawText(label, x, y, labelPaint);
        }

        canvas.restore();
    }
}
