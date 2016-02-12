package com.seva60plus.hum.sharelocation;


public class MapListData {
	
	public String FimageUrl;
	public String Fname;
	public String FuID;
	public int image;
	
	public String destLate;
	public String destLang;
	
	public String place_id;
	public String distance_km;
	
	public String place_type;

	public MapListData() {
	}
	
	public String getFimageUrl() {
        return FimageUrl;
    }
    public void setFimageUrl(String FimageUrl) {
        this.FimageUrl = FimageUrl;
    }
    public String getFname() {
        return Fname;
    }
    public void setFname(String Fname) {
        this.Fname = Fname;
    }
    public String getFuID() {
        return FuID;
    }
    public void setFuID(String FuID) {
        this.FuID = FuID;
    }
    
    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }
    
    public String getDestLate() {
        return destLate;
    }
    public void setDestLate(String destLate) {
        this.destLate = destLate;
    }

    public String getDestLang() {
        return destLang;
    }
    public void setDestLang(String destLang) {
        this.destLang = destLang;
    }
    
    //-----
    
    public String getPlaceID() {
        return place_id;
    }
    public void setplaceID(String place_id) {
        this.place_id = place_id;
    }
    
    public String getDistanceKM() {
        return distance_km;
    }
    public void setDistanceKM(String distance_km) {
        this.distance_km = distance_km;
    }
    
    public String getPlaceType() {
        return place_type;
    }
    public void setplaceType(String place_type) {
        this.place_type = place_type;
    }
   
}
