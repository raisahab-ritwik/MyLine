package com.seva60plus.hum.pillreminder;


public class PillDay {
	  
	
	 String day = "";
	 boolean selected = false;
	  
	 public PillDay(String day, boolean selected) {
	  super();
	
	  this.day = day;
	  this.selected = selected;
	 }
	
	 public String getday() {
	  return day;
	 }
	 public void setday(String day) {
	  this.day = day;
	 }
	 
	 public boolean isSelected() {
	  return selected;
	 }
	 public void setSelected(boolean selected) {
	  this.selected = selected;
	 }
	  
	}
