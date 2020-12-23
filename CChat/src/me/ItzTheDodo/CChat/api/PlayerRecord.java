package me.ItzTheDodo.CChat.api;

import java.util.Set;

import org.bukkit.entity.Player;

import me.ItzTheDodo.CChat.CChat;
import me.ItzTheDodo.CChat.api.Interfaces.PlayerRecords;

public class PlayerRecord implements PlayerRecords{
	
	private CChat plugin;
	private Player p;
	
	public PlayerRecord(CChat pl, Player player) {
		plugin = pl;
		p = player;
	}
	
	public Set<String> getPlayerChats() {
		return plugin.cfgm.getPlayers().getConfigurationSection(p.getUniqueId().toString() + ".chatrooms").getKeys(false);
	}
	
	public boolean isMuted() {
		return plugin.cfgm.getPlayers().getBoolean(p.getUniqueId().toString() + ".ismuted");
	}
	
	public Time getMuteLength() {
		if (plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutetime") == "None") {
			return null;
		}
		int sec = Integer.parseInt((plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutetime")).split("-")[0].split(":")[0]);
		int min = Integer.parseInt((plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutetime")).split("-")[0].split(":")[1]);
		int hour = Integer.parseInt((plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutetime")).split("-")[0].split(":")[2]);
		int day = Integer.parseInt((plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutetime")).split("-")[1].split("/")[0]);
		int month = Integer.parseInt((plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutetime")).split("-")[1].split("/")[1]);
		int year = Integer.parseInt((plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutetime")).split("-")[1].split("/")[2]);
		return new Time(sec, min, hour, day, month, year);
	}
	
	public Time getBanDate() {
		if (plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutetime") == "None") {
			return null;
		}
		int sec = Integer.parseInt((plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutedate")).split("-")[0].split(":")[0]);
		int min = Integer.parseInt((plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutedate")).split("-")[0].split(":")[1]);
		int hour = Integer.parseInt((plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutedate")).split("-")[0].split(":")[2]);
		int day = Integer.parseInt((plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutedate")).split("-")[1].split("/")[0]);
		int month = Integer.parseInt((plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutedate")).split("-")[1].split("/")[1]);
		int year = Integer.parseInt((plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutedate")).split("-")[1].split("/")[2]);
		return new Time(sec, min, hour, day, month, year);
	}
	
	public Time getMuteTimeLeft() {
		Time dayofmute = Time.parse(plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutedate"));
		dayofmute.addWithTime(Time.parse(plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutetime")));
		return Time.parse(dayofmute.compareWithTime(Time.dateToTime(Time.getCurrentTime())));
	}
	
	public int getMuteAmount() {
		return plugin.cfgm.getPlayers().getInt(p.getUniqueId().toString() + ".timesmuted");
	}
	
	public Set<String> getChatHistory() {
		return plugin.cfgm.getPlayers().getConfigurationSection(p.getUniqueId().toString() + ".history").getKeys(false);
	}
	
	public void clearChatHistory() {
		for (String s : plugin.cfgm.getPlayers().getConfigurationSection(p.getUniqueId().toString() + ".history").getKeys(false)) {
			plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".history." + s, null);
		}
	}
	
	public void setMuted(boolean mute) {
		plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".ismuted", mute);
	}
	
	public String getNickname() {
		return plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".nick");
	}
	
	public void setNickname(String nick) {
		plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".nick", nick);
	}
	
	public int getChatOffences() {
		return plugin.cfgm.getPlayers().getInt(p.getUniqueId().toString() + ".chat-offences");
	}
	
	public void setChatOffences(int i) {
		plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".chat-offences", i);
	}
	
	public void wipeLogs() {
		plugin.cfgm.getPlayers().set(p.getUniqueId().toString(), null);
	}

}
