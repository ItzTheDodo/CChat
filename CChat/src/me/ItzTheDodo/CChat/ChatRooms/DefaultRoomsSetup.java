package me.ItzTheDodo.CChat.ChatRooms;

import java.util.HashMap;

import org.bukkit.OfflinePlayer;

import me.ItzTheDodo.CChat.CChat;
import me.ItzTheDodo.CChat.ChatRooms.utils.RankLevel;
import me.ItzTheDodo.CChat.Commands.PrivateChatCommand;

public class DefaultRoomsSetup {
	
	private CChat plugin;

	public DefaultRoomsSetup(CChat pl) {
		plugin = pl;
	}
	
	public void setupDefaultRooms() {
		
		for (String chat_name : plugin.getConfig().getConfigurationSection("default-private-chats").getKeys(false)) {
			
			for (ChatRoom cr : PrivateChatCommand.chatrooms) {
				
				if (cr.getName_of_chat().equalsIgnoreCase(chat_name)) {
					plugin.logEvent("The default chat: " + chat_name + " has alredy been created in the config");
					return;
				}
				
			}
			
			String pre = plugin.getConfig().getString("default-private-chats." + chat_name + ".prefix");
			HashMap<String, RankLevel> ranks = new HashMap<String, RankLevel>();
			for (int i = 0 ; i < plugin.getConfig().getInt("default-private-chats." + chat_name + ".rank-amount") ; i++) {
				ranks.put(ChatRoom.rankLevelToRank(5 - i).name(), ChatRoom.rankLevelToRank(5 - i));
			}
			HashMap<String, String> rank_col = new HashMap<String, String>();
			int count = 0;
			for (String s : ranks.keySet()) {
				rank_col.put(s, plugin.getConfig().getString("default-private-chats." + chat_name + ".rank-colours").replace("[", "").replace("]", "").split(",")[count]);
				count++;
			}
			boolean nsfw = plugin.getConfig().getBoolean("default-private-chats." + chat_name + ".nsfw");
			String desc = plugin.getConfig().getString("default-private-chats." + chat_name + ".description");
			HashMap<OfflinePlayer, RankLevel> pir = new HashMap<OfflinePlayer, RankLevel>();
			
			ChatSettings set = new ChatSettings(pre, ranks, rank_col, nsfw);
			ChatRoom chat = new ChatRoom(plugin, chat_name, desc, pir, set);
			PrivateChatCommand.chatrooms.add(chat);
			
		}
		
	}
	
}
