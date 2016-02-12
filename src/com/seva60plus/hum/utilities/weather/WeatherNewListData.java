package com.seva60plus.hum.utilities.weather;


public class WeatherNewListData {
	
	public String city,country;
	public String date;
	public String tempDay,tempMorn,tempEve,tempNight,tempMax,tempMin;
	
	public String pressure,humidity,clouds,deg,rain,speed;
	
	
	public String wName, wDescription, wIcon;
	/*
	public String place_id;
	public String distance_km;
	
	public String place_type;
*/
	public WeatherNewListData() {
	}
	
	public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    
    public String getTempDay() {
        return tempDay;
    }
    public void setTempDay(String tempDay) {
        this.tempDay = tempDay;
    }
    
    public String getTempMorn() {
        return tempMorn;
    }
    public void setTempMorn(String tempMorn) {
        this.tempMorn = tempMorn;
    }
    
    public String getTempEve() {
        return tempEve;
    }
    public void setTempEve(String tempEve) {
        this.tempEve = tempEve;
    }
    
    public String getTempNight() {
        return tempNight;
    }
    public void setTempNight(String tempNight) {
        this.tempNight = tempNight;
    }
    
    public String getTempMax() {
        return tempMax;
    }
    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }
    
    public String getTempMin() {
        return tempMin;
    }
    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }
    
    public String getPressure() {
        return pressure;
    }
    public void setPressure(String pressure) {
        this.pressure = pressure;
    }
    
    public String getHumidity() {
        return humidity;
    }
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
    
    public String getClouds() {
        return clouds;
    }
    public void setClouds(String clouds) {
        this.clouds = clouds;
    }
    
    public String getSpeed() {
        return speed;
    }
    public void setSpeed(String speed) {
        this.speed = speed;
    }
    
    public String getRain() {
        return rain;
    }
    public void setRain(String rain) {
        this.rain = rain;
    }
    
    public String getDeg() {
        return deg;
    }
    public void setDeg(String deg) {
        this.deg = deg;
    }
    
    public String getWname() {
        return wName;
    }
    public void setWname(String wName) {
        this.wName = wName;
    }
    
    public String getWdescription() {
        return wDescription;
    }
    public void setWdescription(String wDescription) {
        this.wDescription = wDescription;
    }
    
    public String getWicon() {
        return wIcon;
    }
    public void setWicon(String wIcon) {
        this.wIcon = wIcon;
    }
    
    
}
