package me.ItzTheDodo.CChat.ChatRooms.utils;

import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.ItzTheDodo.CChat.ChatRooms.ChatSettings;

public interface ChatRoomInterface {
	
	public void promotePlayer(Player p);
	
	public void demotePlayer(Player p);
	
	public void addPlayer(Player p, String r);
	
	public void removePlayer(Player p);
	
	public void close();
	
	public void sendMessage(String message, Player p);
	
	public String getRankName(RankLevel r);
	
	public RankLevel getRank(Player p);
	
	public boolean playerHasRank(Player p, RankLevel rank);

	public String getName_of_chat();

	public String getDescription();

	public void setDescription(String description);

	public HashMap<OfflinePlayer, RankLevel> getPlayersInRoom();

	public ChatSettings getSettings();

}
