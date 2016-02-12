package com.seva60plus.hum.utilities.weather;

import com.seva60plus.hum.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

public class WeatherFragmentForecast extends Fragment {

	public View v;
	String greg = "FragmentForecast";
	ProgressBar ProgressBar;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		WeatherMyLog.d(greg, "start");
		v = inflater.inflate(R.layout.weather_fragment_forecast, null);
		ProgressBar = (ProgressBar) v.findViewById(R.id.ProgressBarFF);

		if (null != WeatherFirstActivity.newData.getForecast()) {
			WeatherMyLog.d(greg, "get data");
			WeatherDayWeather[] dw = WeatherFirstActivity.newData.getForecast();
			WeatherForecastAdapter adapter = new WeatherForecastAdapter(getActivity(), dw);
			ExpandableListView elvDay = (ExpandableListView) v
					.findViewById(R.id.ELday);
			elvDay.setAdapter(adapter);
			ProgressBar.setVisibility(View.GONE);
		}

		return v;
	}

}
