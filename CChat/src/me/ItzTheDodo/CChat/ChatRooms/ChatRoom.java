package me.ItzTheDodo.CChat.ChatRooms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.ItzTheDodo.CChat.CChat;
import me.ItzTheDodo.CChat.ChatRooms.utils.ChatRoomInterface;
import me.ItzTheDodo.CChat.ChatRooms.utils.RankLevel;
import me.ItzTheDodo.CChat.api.rayzr522.JSONMessage;

public class ChatRoom implements ChatRoomInterface {
	
	private String name_of_chat;
	private String description;
	private HashMap<OfflinePlayer, RankLevel> playersInRoom;
	private ChatSettings settings;
	private CChat plugin;
	
	public ChatRoom(CChat pl, String name, String Description, HashMap<OfflinePlayer, RankLevel> players_in_room, ChatSettings setting) {
		name_of_chat = name;
		description = Description;
		playersInRoom = players_in_room;
		settings = setting;
		plugin = pl;
		
		if (!(plugin.cfgm.getChatRoomsConfig().getKeys(false).contains(name))) {
			plugin.cfgm.getChatRoomsConfig().set(name + ".description", this.description);
			plugin.cfgm.getChatRoomsConfig().createSection(name + ".players");
			for (OfflinePlayer player : playersInRoom.keySet()) {
				plugin.cfgm.getChatRoomsConfig().set(name + ".players." + player.getUniqueId().toString(), playersInRoom.get(player).name());
			}
			plugin.cfgm.getChatRoomsConfig().createSection(name + ".settings");
			plugin.cfgm.getChatRoomsConfig().set(name + ".settings.prefix", this.settings.getPrefix());
			plugin.cfgm.getChatRoomsConfig().set(name + ".settings.nsfw", this.settings.isNSFW());
			plugin.cfgm.getChatRoomsConfig().createSection(name + ".settings.ranks");
			for (String rank : this.settings.getRanks().keySet()) {
				plugin.cfgm.getChatRoomsConfig().set(name + ".settings.ranks." + rank, this.settings.getRanks().get(rank).name());
			}
			plugin.cfgm.getChatRoomsConfig().createSection(name + ".settings.ranks-colours");
			for (String rank : this.settings.getRank_colours().keySet()) {
				plugin.cfgm.getChatRoomsConfig().set(name + ".settings.ranks-colours." + rank, this.settings.getRank_colours().get(rank));
			}
			plugin.cfgm.saveChatRoomsConfig();
			
			for (OfflinePlayer p : players_in_room.keySet()) {
				plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".chatrooms." + name_of_chat, " ");
				plugin.cfgm.savePlayers();
			}
		
		}
	
	}
	
	public static int getRankLevel(RankLevel rank) {
		
		if (rank.equals(RankLevel.NEW)) {
			return 0;
		}
		if (rank.equals(RankLevel.MEMBER)) {
			return 1;
		}
		if (rank.equals(RankLevel.TRUSTED)) {
			return 2;
		}
		if (rank.equals(RankLevel.STAFF)) {
			return 3;
		}
		if (rank.equals(RankLevel.CO_OWNER)) {
			return 4;
		}
		if (rank.equals(RankLevel.OWNER)) {
			return 5;
		} else {
			return 0;
		}
		
	}
	
	public static RankLevel rankLevelToRank(int ranklevel) {
		
		if (ranklevel == 0) {
			return RankLevel.NEW;
		}
		if (ranklevel == 1) {
			return RankLevel.MEMBER;
		}
		if (ranklevel == 2) {
			return RankLevel.TRUSTED;
		}
		if (ranklevel == 3) {
			return RankLevel.STAFF;
		}
		if (ranklevel == 4) {
			return RankLevel.CO_OWNER;
		}
		if (ranklevel == 5) {
			return RankLevel.OWNER;
		} else {
			return RankLevel.NEW;
		}
		
	}
	
	public static ChatRoom getChatRoom(CChat pl, String name) {
		
		String desc = pl.cfgm.getChatRoomsConfig().getString(name + ".description");
		HashMap<OfflinePlayer, RankLevel> pir = new HashMap<OfflinePlayer, RankLevel>();
		for (String s : pl.cfgm.getChatRoomsConfig().getConfigurationSection(name + ".players").getKeys(false)) {
			pir.put(Bukkit.getServer().getOfflinePlayer(UUID.fromString(s)), ChatRoom.stringToRankLevel(pl.cfgm.getChatRoomsConfig().getString(name + ".players." + s)));
		}
		String pre = pl.cfgm.getChatRoomsConfig().getString(name + ".settings.prefix");
		boolean nsfw = pl.cfgm.getChatRoomsConfig().getBoolean(name + ".settings.nsfw");
		HashMap<String, RankLevel> ranks = new HashMap<String, RankLevel>();
		for (String s : pl.cfgm.getChatRoomsConfig().getConfigurationSection(name + ".settings.ranks").getKeys(false)) {
			ranks.put(s, ChatRoom.stringToRankLevel(pl.cfgm.getChatRoomsConfig().getString(name + ".settings.ranks." + s)));
		}
		HashMap<String, String> rank_col = new HashMap<String, String>();
		for (String s : pl.cfgm.getChatRoomsConfig().getConfigurationSection(name + ".settings.ranks-colours").getKeys(false)) {
			rank_col.put(s, pl.cfgm.getChatRoomsConfig().getString(name + ".settings.ranks-colours." + s));
		}
		ChatSettings set = new ChatSettings(pre, ranks, rank_col, nsfw);
		return new ChatRoom(pl, name, desc, pir, set);
		
	}
	
	public void promotePlayer(Player p) {
		RankLevel rank = playersInRoom.get((OfflinePlayer) p);
		if (rank.equals(RankLevel.OWNER)) {
			return;
		}
		playersInRoom.put((OfflinePlayer) p, ChatRoom.rankLevelToRank(ChatRoom.getRankLevel(rank) + 1));
		plugin.cfgm.getChatRoomsConfig().set(name_of_chat + ".players." + p.getUniqueId().toString(), ChatRoom.rankLevelToRank(ChatRoom.getRankLevel(rank) + 1).name());
		plugin.cfgm.saveChatRoomsConfig();
	}
	
	public void demotePlayer(Player p) {
		RankLevel rank = playersInRoom.get((OfflinePlayer) p);
		if (rank.equals(RankLevel.NEW)) {
			return;
		}
		playersInRoom.put((OfflinePlayer) p, ChatRoom.rankLevelToRank(ChatRoom.getRankLevel(rank) - 1));
		plugin.cfgm.getChatRoomsConfig().set(name_of_chat + ".players." + p.getUniqueId().toString(), ChatRoom.rankLevelToRank(ChatRoom.getRankLevel(rank) - 1));
		plugin.cfgm.saveChatRoomsConfig();
	}
	
	public void addPlayer(Player p, String r) {
		RankLevel rank = settings.getRanks().get(r);
		playersInRoom.put((OfflinePlayer) p, rank);
		plugin.cfgm.getChatRoomsConfig().set(name_of_chat + ".players." + p.getUniqueId().toString(), rank.name());
		plugin.cfgm.saveChatRoomsConfig();
		plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".chatrooms." + name_of_chat, " ");
		plugin.cfgm.savePlayers();
	}
	
	public void removePlayer(Player p) {
		playersInRoom.remove((OfflinePlayer) p);
		plugin.cfgm.getChatRoomsConfig().set(name_of_chat + ".players." + p.getUniqueId().toString(), null);
		plugin.cfgm.saveChatRoomsConfig();
		plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".chatrooms." + name_of_chat, null);
		plugin.cfgm.savePlayers();
	}
	
	public void close() {
		plugin.cfgm.getChatRoomsConfig().set(name_of_chat, null);
		plugin.cfgm.saveChatRoomsConfig();
	}
	
	public void sendMessage(String message, Player p) {
		
		String mod_message = message;
		
		if (!settings.isNSFW()) {
			
			List<String> arr_message = new ArrayList<String>();
			for (int i = 0 ; i < message.length() ; i++) {
				arr_message.add(Character.toString(message.charAt(i)));
			}
			boolean found = false;
			List<String> res_words = plugin.getConfig().getStringList("Restrictions.Restricted-Words");
			List<String> res_phrases = plugin.getConfig().getStringList("Restrictions.Restricted-Phrases");
			
			for (String word : res_words) {
				if (message.contains(word)) {
					found = true;
					if (plugin.getConfig().getBoolean("Restrictions.notify-player")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Restrictions.player-message")));
					}
					for (int i = 0 ; i < word.length() ; i++) {
						arr_message.set(message.indexOf(word) + i, plugin.getConfig().getString("Restrictions.replace-part-voilation-message-with"));
					}
				}
			}
			
			if (!found) {
				for (String phrase : res_phrases) {
					if (message.contains(phrase)) {
						if (plugin.getConfig().getBoolean("Restrictions.notify-player")) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Restrictions.player-message")));
						}
						for (int i = 0 ; i < phrase.length() ; i++) {
							arr_message.set(message.indexOf(phrase) + i, plugin.getConfig().getString("Restrictions.replace-part-voilation-message-with"));
						}
					}
				}
			}
		
			mod_message = "";
			for (String s : arr_message) {
				mod_message = mod_message + s;
			}
		
		}
		
		RankLevel rank_level = this.getRank(p);
		String player_rank_name = settings.getRank_colours().get(this.getRankName(rank_level)) + "[" + this.getRankName(rank_level) + "] " + p.getDisplayName() + ": ";
		mod_message = ChatColor.YELLOW + settings.getPrefix() + " [" + name_of_chat + "] " + ChatColor.translateAlternateColorCodes('&', player_rank_name) + ChatColor.WHITE + mod_message;
		
		Player p_online;
		for (OfflinePlayer player : playersInRoom.keySet()) {
			if (player.isOnline()) {
				p_online = player.getPlayer();
				if (p_online.equals(p)) {
					p_online.sendMessage(mod_message);
				} else {
					JSONMessage.create(mod_message).tooltip("[Report Player]").runCommand("/report " + p.getName()).send(p_online);
				}
			}
		}
		
	}
	
	public String getRankName(RankLevel r) {
		for (String rank : settings.getRanks().keySet()) {
			if (settings.getRanks().get(rank).equals(r)) {
				return rank;
			}
		}
		return null;
	}
	
	public RankLevel getRank(Player p) {
		return playersInRoom.get((OfflinePlayer) p);
	}
	
	public boolean playerHasRank(Player p, RankLevel rank) {
		return this.getRank(p).equals(rank);
	}

	public String getName_of_chat() {
		return name_of_chat;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		plugin.cfgm.getChatRoomsConfig().set(name_of_chat + "description", description);
		plugin.cfgm.saveChatRoomsConfig();
	}

	public HashMap<OfflinePlayer, RankLevel> getPlayersInRoom() {
		return playersInRoom;
	}

	public ChatSettings getSettings() {
		return settings;
	}
	
	public static RankLevel stringToRankLevel(String rank) {
		return Enum.valueOf(RankLevel.class, rank);
	}

}
