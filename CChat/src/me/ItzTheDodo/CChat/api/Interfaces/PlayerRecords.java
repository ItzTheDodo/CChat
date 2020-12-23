package me.ItzTheDodo.CChat.api.Interfaces;

import java.util.Set;

import me.ItzTheDodo.CChat.api.Time;

public interface PlayerRecords {
	
	public Set<String> getPlayerChats();
	
	public boolean isMuted();
	
	public Time getMuteLength();
	
	public Time getBanDate();
	
	public Time getMuteTimeLeft();
	
	public int getMuteAmount();
	
	public Set<String> getChatHistory();
	
	public void clearChatHistory();
	
	public void setMuted(boolean mute);
	
	public String getNickname();
	
	public void setNickname(String nick);
	
	public int getChatOffences();
	
	public void setChatOffences(int i);
	
	public void wipeLogs();

}
