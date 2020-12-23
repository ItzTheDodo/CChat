package me.ItzTheDodo.CChat.api.Interfaces;

import me.ItzTheDodo.CChat.api.Time;

public interface TimeInterface {

	public String getTime();

	public void setTime(String time);

	public String getDate();

	public void setDate(String date);
	
	public void add(int sec, int min, int hour, int day, int month, int year);
	
	public void addWithTime(Time ti);
	
	public String compare(int sec, int min, int hour, int day, int month, int year);
	
	public String compareWithTime(Time ti);
	
	public int getSec();
	
	public int getMin();
	
	public int getHour();
	
	public int getDay();
	
	public int getMonth();
	
	public int getYear();
	
	public void setSec(int sec);
	
	public void setMin(int min);
	
	public void setHour(int hour);
	
	public void setDay(int day);
	
	public void setMonth(int month);
	
	public void setYear(int year);
	
	public String getFull();

}
