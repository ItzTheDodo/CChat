package me.ItzTheDodo.CChat.api;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;

import me.ItzTheDodo.CChat.api.Interfaces.TimeInterface;

public class Time implements TimeInterface {
	
	private String time;
	private String date;
	
	public Time(int sec, int min, int hour, int day, int month, int year) {
		time = Integer.toString(sec) + ":" + Integer.toString(min) + ":" + Integer.toString(hour);
		date = Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year);
	}
	
	public static Date getCurrentTime() {
		return new Date();
	}
	
	public static Time dateToTime(Date d) {
		
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String[] form = dateformat.format(d).split(" ");
		
		int sec = Integer.parseInt(form[1].split(":")[2]);
		int min = Integer.parseInt(form[1].split(":")[1]);
		int hour = Integer.parseInt(form[1].split(":")[0]);
		int day = Integer.parseInt(form[0].split("-")[0]);
		int month = Integer.parseInt(form[0].split("-")[1]);
		int year = Integer.parseInt(form[0].split("-")[2]);
		return new Time(sec, min, hour, day, month, year);
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public void add(int sec, int min, int hour, int day, int month, int year) {
		time = Integer.toString(Integer.parseInt(time.split(":")[0]) + sec) + ":" + Integer.toString(Integer.parseInt(time.split(":")[1]) + min) + ":" + Integer.toString(Integer.parseInt(time.split(":")[2]) + hour);
		date = Integer.toString(Integer.parseInt(date.split("/")[0]) + day) + "/" + Integer.toString(Integer.parseInt(date.split("/")[1]) + month) + "/" + Integer.toString(Integer.parseInt(date.split("/")[2]) + year);
		if (this.getSec() >= 60) {
			this.setSec(this.getSec() - 60);
			this.setMin(this.getMin() + 1);
		}
		if (this.getMin() >= 60) {
			this.setMin(this.getMin() - 60);
			this.setHour(this.getHour() + 1);
		}
		if (this.getHour() > 24) {
			this.setHour(this.getHour() - 24);
			this.setDay(this.getDay() + 1);
		}
		if (this.getDay() > Time.getDaysInMonth(this.getYear(), this.getMonth())) {
			this.setDay(this.getDay() - Time.getDaysInMonth(this.getYear(), this.getMonth()));
			this.setMonth(this.getMonth() + 1);
		}
		if (this.getMonth() > 12) {
			this.setMonth(this.getMonth() - 12);
			this.setYear(this.getYear() + 1);
		}
	}
	
	public void addWithTime(Time ti) {
		int sec = ti.getSec();
		int min = ti.getMin();
		int hour = ti.getHour();
		int day = ti.getDay();
		int month = ti.getMonth();
		int year = ti.getYear();
		time = Integer.toString(Integer.parseInt(time.split(":")[0]) + sec) + ":" + Integer.toString(Integer.parseInt(time.split(":")[1]) + min) + ":" + Integer.toString(Integer.parseInt(time.split(":")[2]) + hour);
		date = Integer.toString(Integer.parseInt(date.split("/")[0]) + day) + "/" + Integer.toString(Integer.parseInt(date.split("/")[1]) + month) + "/" + Integer.toString(Integer.parseInt(date.split("/")[2]) + year);
		if (this.getSec() >= 60) {
			this.setSec(this.getSec() - 60);
			this.setMin(this.getMin() + 1);
		}
		if (this.getMin() >= 60) {
			this.setMin(this.getMin() - 60);
			this.setHour(this.getHour() + 1);
		}
		if (this.getHour() > 24) {
			this.setHour(this.getHour() - 24);
			this.setDay(this.getDay() + 1);
		}
		if (this.getDay() > Time.getDaysInMonth(this.getYear(), this.getMonth())) {
			this.setDay(this.getDay() - Time.getDaysInMonth(this.getYear(), this.getMonth()));
			this.setMonth(this.getMonth() + 1);
		}
		if (this.getMonth() > 12) {
			this.setMonth(this.getMonth() - 12);
			this.setYear(this.getYear() + 1);
		}
	}
	
	public String compare(int sec, int min, int hour, int day, int month, int year) {
		String t = Integer.toString(Integer.parseInt(time.split(":")[0]) - sec) + ":" + Integer.toString(Integer.parseInt(time.split(":")[1]) - min) + ":" + Integer.toString(Integer.parseInt(time.split(":")[2]) - hour);
		String d = Integer.toString(Integer.parseInt(date.split("/")[0]) - day) + "/" + Integer.toString(Integer.parseInt(date.split("/")[1]) - month) + "/" + Integer.toString(Integer.parseInt(date.split("/")[2]) - year);
		if (t.contains("-")) {
			String[] ind = t.split(":");
			for (int i = 0 ; i < ind.length ; i++) {
				if (!(i == 0)) {
					if (ind[i - 1].contains("-")) {
						ind[i - 1] = "0";
					}
				}
			}
			t = "";
			for (int i = 0 ; i < ind.length ; i++) {
				if (!(i == 0)) {
					t = t + ":" + ind[i];
				} else {
					t = ind[i];
				}
			}
		}
		if (d.contains("-")) {
			String[] ind = d.split("/");
			for (int i = 0 ; i < ind.length ; i++) {
				if (!(i == 0)) {
					if (ind[i - 1].contains("-")) {
						ind[i - 1] = "0";
					}
				}
			}
			d = "";
			for (int i = 0 ; i < ind.length ; i++) {
				if (!(i == 0)) {
					d = d + ":" + ind[i];
				} else {
					d = ind[i];
				}
			}
		}
		return t + "~" + d;
	}
	
	public String compareWithTime(Time ti) {
		int sec = ti.getSec();
		int min = ti.getMin();
		int hour = ti.getHour();
		int day = ti.getDay();
		int month = ti.getMonth();
		int year = ti.getYear();
		String t = Integer.toString(Integer.parseInt(time.split(":")[0]) - sec) + ":" + Integer.toString(Integer.parseInt(time.split(":")[1]) - min) + ":" + Integer.toString(Integer.parseInt(time.split(":")[2]) - hour);
		String d = Integer.toString(Integer.parseInt(date.split("/")[0]) - day) + "/" + Integer.toString(Integer.parseInt(date.split("/")[1]) - month) + "/" + Integer.toString(Integer.parseInt(date.split("/")[2]) - year);
		if (t.contains("-")) {
			String[] ind = t.split(":");
			for (int i = 0 ; i < ind.length ; i++) {
				if (!(i == 0)) {
					if (ind[i - 1].contains("-")) {
						ind[i - 1] = "0";
					}
				}
			}
			t = "";
			for (int i = 0 ; i < ind.length ; i++) {
				if (!(i == 0)) {
					t = t + ":" + ind[i];
				} else {
					t = ind[i];
				}
			}
		}
		if (d.contains("-")) {
			String[] ind = d.split("/");
			for (int i = 0 ; i < ind.length ; i++) {
				if (!(i == 0)) {
					if (ind[i - 1].contains("-")) {
						ind[i - 1] = "0";
					}
				}
			}
			d = "";
			for (int i = 0 ; i < ind.length ; i++) {
				if (!(i == 0)) {
					d = d + ":" + ind[i];
				} else {
					d = ind[i];
				}
			}
		}
		return t + "~" + d;
	}
	
	public int getSec() {
		return Integer.parseInt(time.split(":")[0]);
	}
	
	public int getMin() {
		return Integer.parseInt(time.split(":")[1]);
	}
	
	public int getHour() {
		return Integer.parseInt(time.split(":")[2]);
	}
	
	public int getDay() {
		return Integer.parseInt(date.split("/")[0]);
	}
	
	public int getMonth() {
		return Integer.parseInt(date.split("/")[1]);
	}
	
	public int getYear() {
		return Integer.parseInt(date.split("/")[2]);
	}
	
	public void setSec(int sec) {
		time = Integer.toString(sec) + ":" + time.split(":")[1] + ":" + time.split(":")[2];
	}
	
	public void setMin(int min) {
		time = time.split(":")[0] + ":" + Integer.toString(min) + ":" + time.split(":")[2];
	}
	
	public void setHour(int hour) {
		time = time.split(":")[0] + ":" + time.split(":")[1] + ":" + Integer.toString(hour);
	}
	
	public void setDay(int day) {
		date = Integer.toString(day) + "/" + date.split("/")[1] + "/" + time.split("/")[2];
	}
	
	public void setMonth(int month) {
		date = time.split("/")[0] + "/" + Integer.toString(month) + "/" + time.split("/")[2];
	}
	
	public void setYear(int year) {
		date = time.split("/")[0] + "/" + time.split("/")[1] + "/" + Integer.toString(year);
	}
	
	public String getFull() {
		return time + "~" + date;
	}
	
	public static int getDaysInMonth(int year, int month) {
		return YearMonth.of(year, month).lengthOfMonth();
	}
	
	public static Time parse(String iter) {
		int sec = Integer.parseInt(iter.split("~")[0].split(":")[0]);
		int min = Integer.parseInt(iter.split("~")[0].split(":")[1]);
		int hour = Integer.parseInt(iter.split("~")[0].split(":")[2]);
		int day = Integer.parseInt(iter.split("~")[1].split("/")[0]);
		int month = Integer.parseInt(iter.split("~")[1].split("/")[1]);
		int year = Integer.parseInt(iter.split("~")[1].split("/")[2]);
		return new Time(sec, min, hour, day, month, year);
	}

}
