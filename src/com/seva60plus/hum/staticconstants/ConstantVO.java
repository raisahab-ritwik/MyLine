package com.seva60plus.hum.staticconstants;

public class ConstantVO {

	//REGISTER 
	public static String REGISTER_URL = "http://seva60plus.co.in/seva60PlusAndroidAPI/register";

	public static final String HUM_UPDATE_URL = "http://seva60plus.co.in/seva60PlusAndroidAPI/register/updateHum";

	public static final String CHECK_USER_EXISTS = "http://seva60plus.co.in/seva60PlusAndroidAPI/register/checkUserRegistered/";

	public static final String GET_USER_DETAILS = "http://seva60plus.co.in/seva60PlusAndroidAPI/register/checkUserExists/";

	// Send well-being statistics
	public static String SEND_STATISTICS_DETAILS = "http://seva60plus.co.in/seva60PlusAndroidAPI/insert_hum_stat";

	//SEND LAT LANG
	public static String SEND_LAT_LANG_URL = "http://seva60plus.co.in/seva60PlusAndroidAPI/latlong/insert";

	//Fetch Saathi List
	public static String FETCH_SAATHI_LIST = "http://seva60plus.co.in/seva60PlusAndroidAPI/register/fetchSaathiList/";

	//Add Saathi
	public static String ADD_SAATHI_URL = "http://seva60plus.co.in/seva60PlusAndroidAPI/register/addSaathi";

	//Delete Saathi
	public static String DELETE_SAATHI_URL = "http://seva60plus.co.in/seva60PlusAndroidAPI/register/unlinkSaathi";

	//=================================================
	//============ FOR WELLBEING ======================

	public static final String EXERCISE_HEADER = "WELLBEING";
	public static final String MOOD_HEADER = "MOOD";
	public static final String SLEEP_HEADER = "SLEEP";
	public static final String EXERCISE = "exercise";
	public static final String MOOD = "mood";
	public static final String SLEEP = "sleep";
}
