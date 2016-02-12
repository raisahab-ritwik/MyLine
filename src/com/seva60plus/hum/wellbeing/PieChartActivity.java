package com.seva60plus.hum.wellbeing;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.BaseActivity;
import com.seva60plus.hum.staticconstants.ConstantVO;

public class PieChartActivity extends BaseActivity implements OnChartValueSelectedListener {

	private PieChart mChart;
	private ArrayList<String> dataArray = new ArrayList<String>();
	private Typeface tf;
	private int total;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.wellbeing_pie_chart);
		Bundle extras = getIntent().getExtras();
		float[] arrayData = extras.getFloatArray("numbers");

		String[] arrayLabels = extras.getStringArray("lables");
		String chartLabel = extras.getString("chartLabel");

		for (int i = 0; i < arrayData.length; i++) {

			Log.v("Numbers", "" + arrayData[i]);

		}
		for (int i = 0; i < arrayLabels.length; i++) {

			Log.v("Labels", "" + arrayLabels[i]);
		}

		mChart = (PieChart) findViewById(R.id.chart1);
		mChart.setUsePercentValues(true);
		mChart.setDescription("");
		mChart.setExtraOffsets(5, 10, 5, 5);

		mChart.setDragDecelerationFrictionCoef(0.95f);

		tf = Typeface.createFromAsset(getAssets(), "openSansRegular.ttf");

		mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "openSansRegular.ttf"));
		mChart.setCenterText(chartLabel);
		mChart.setCenterTextSize(16);
		mChart.setCenterTextColor(Color.BLUE);

		mChart.setDrawHoleEnabled(true);
		mChart.setHoleColorTransparent(true);

		mChart.setTransparentCircleColor(Color.WHITE);
		mChart.setTransparentCircleAlpha(110);

		mChart.setHoleRadius(58f);
		mChart.setTransparentCircleRadius(61f);

		mChart.setDrawCenterText(true);

		mChart.setRotationAngle(0);
		mChart.setRotationEnabled(true);
		mChart.setHighlightPerTapEnabled(true);
		mChart.setOnChartValueSelectedListener(this);
		setData(arrayLabels, arrayData, chartLabel);
		mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
		// mChart.spin(2000, 0, 360);

		Legend l = mChart.getLegend();
		l.setTextSize(15);
		l.setFormSize(15);
		l.setPosition(LegendPosition.RIGHT_OF_CHART);
		l.setXEntrySpace(15f);
		l.setYEntrySpace(0f);
		l.setYOffset(0f);
	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

	}

	@Override
	public void onNothingSelected() {

	}

	private void setData(String[] arrayLabels, float[] arrayData, String chartLabel) {

		//float mult = range;

		ArrayList<Entry> yVals1 = new ArrayList<Entry>();
		for (int i = 0; i < arrayData.length; i++) {
			yVals1.add(new Entry(arrayData[i], i));
		}
		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < arrayLabels.length; i++)
			xVals.add(arrayLabels[i % arrayLabels.length]);

		PieDataSet dataSet = new PieDataSet(yVals1, "");
		dataSet.setSliceSpace(2f);
		dataSet.setSelectionShift(5f);
		ArrayList<Integer> colors = new ArrayList<Integer>();
		if (chartLabel.equalsIgnoreCase(ConstantVO.SLEEP_HEADER)) {
			for (int c : ColorTemplate.PALETTE_SLEEP)
				colors.add(c);
		} else if (chartLabel.equalsIgnoreCase(ConstantVO.EXERCISE_HEADER)) {
			for (int c : ColorTemplate.PALETTE_EXERCISE)
				colors.add(c);
		} else if (chartLabel.equalsIgnoreCase(ConstantVO.MOOD_HEADER)) {
			for (int c : ColorTemplate.PALETTE_MOOD)
				colors.add(c);
		}
		colors.add(ColorTemplate.getHoloBlue());
		dataSet.setColors(colors);
		PieData data = new PieData(xVals, dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(14f);
		data.setValueTextColor(Color.WHITE);
		data.setValueTypeface(tf);
		mChart.setData(data);
		mChart.highlightValues(null);
		mChart.invalidate();
	}

	public void onCancelButtonClick(View v) {
		finish();
	}

}
