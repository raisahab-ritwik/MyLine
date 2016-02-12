package com.seva60plus.hum.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.seva60plus.hum.staticconstants.ConstantVO;

import android.util.Log;

public class UserFunctions {

	private JsonParser jsonParser;

	public UserFunctions() {
		jsonParser = new JsonParser();
	}

	public JSONObject registerUser(String submit, String subId, String imei, String possibleEmail, String userName, String userCountry, String userPhone,
			String dateOfBirth, String sex, String address1, String cityName, String state, String pinNum, String saathiName1, String emailID1,
			String saathiName2, String emailID2, String saathiCountry1, String con1, String saathiCountry2, String con2, String pherma, String geoLocation,
			String userStatus) {
		Log.v("User Functions", "City name: " + cityName + "State name: " + state);
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("submit", submit));
		params.add(new BasicNameValuePair("sub_id", subId));
		params.add(new BasicNameValuePair("imei", imei));
		params.add(new BasicNameValuePair("email", possibleEmail));
		params.add(new BasicNameValuePair("name", userName));
		params.add(new BasicNameValuePair("country_code", userCountry));
		params.add(new BasicNameValuePair("phone", userPhone));
		params.add(new BasicNameValuePair("dob", dateOfBirth));
		params.add(new BasicNameValuePair("gender", sex));
		params.add(new BasicNameValuePair("address1", address1));
		params.add(new BasicNameValuePair("city", cityName));
		params.add(new BasicNameValuePair("state", state));
		params.add(new BasicNameValuePair("pincode", pinNum));
		params.add(new BasicNameValuePair("sathi_name1", saathiName1));
		params.add(new BasicNameValuePair("sathi_email1", emailID1));
		params.add(new BasicNameValuePair("sathi_name2", saathiName2));
		params.add(new BasicNameValuePair("sathi_email2", emailID2));
		params.add(new BasicNameValuePair("sathi1_country_code", saathiCountry1));
		params.add(new BasicNameValuePair("sathi_contact_no1", con1));
		params.add(new BasicNameValuePair("sathi2_country_code", saathiCountry2));
		params.add(new BasicNameValuePair("sathi_contact_no2", con2));
		params.add(new BasicNameValuePair("pharmacy_no", pherma));
		params.add(new BasicNameValuePair("status", userStatus));
		params.add(new BasicNameValuePair("is_geo_enabled", geoLocation));

		// return json Object
		System.out.println(" Responce: ");
		JSONObject json = jsonParser.getJSONFromUrl(ConstantVO.REGISTER_URL, params);
		System.out.println("JSON Responce: ON reistration : " + json);
		return json;
	}

	public JSONObject sendStatisticsData(String mood, String result, String phoneNo, String iDate, String iTime) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("notification_type", mood));
		params.add(new BasicNameValuePair("phone_no", phoneNo));
		params.add(new BasicNameValuePair("ans", result));
		params.add(new BasicNameValuePair("added_date", iDate));
		params.add(new BasicNameValuePair("added_time", iTime));

		JSONObject json = jsonParser.getJSONFromUrl(ConstantVO.SEND_STATISTICS_DETAILS, params);
		System.out.println("JSON Responce: " + json);
		return json;
	}

	public JSONObject sendLatlang(String Phone, String MyLat, String MyLang, String myTime) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("phone_no", Phone));
		params.add(new BasicNameValuePair("latitude", MyLat));
		params.add(new BasicNameValuePair("longitude", MyLang));
		params.add(new BasicNameValuePair("latlongtime", myTime));

		JSONObject json = jsonParser.getJSONFromUrl(ConstantVO.SEND_LAT_LANG_URL, params);
		System.out.println("JSON Responce: " + json);
		return json;
	}

}
