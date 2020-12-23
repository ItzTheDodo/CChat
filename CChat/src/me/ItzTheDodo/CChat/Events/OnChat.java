package me.ItzTheDodo.CChat.Events;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.ItzTheDodo.CChat.CChat;
import me.ItzTheDodo.CChat.ChatRooms.ChatRoom;
import me.ItzTheDodo.CChat.Commands.IgnoreCommand;
import me.ItzTheDodo.CChat.Commands.PrivateChatCommand;
import me.ItzTheDodo.CChat.Commands.SwitchChatCommand;
import me.ItzTheDodo.CChat.Events.ChatUtils.Utils;
import me.ItzTheDodo.CChat.api.rayzr522.JSONMessage;
import net.milkbowl.vault.chat.Chat;

public class OnChat implements Listener {
	
	private CChat plugin;
	public static HashMap<Player, String> prev_msg = new HashMap<Player, String>();
	public static HashMap<Player, Date> prev_msg_time = new HashMap<Player, Date>();
	
	public OnChat(CChat pl) {
		plugin = pl;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		plugin.asyncUpdate();
		Player p = e.getPlayer();
		String message = e.getMessage();
		e.setCancelled(true);
		
		if (PlayerJoin.spawn_locations.containsKey(p)) {
			if (PlayerJoin.spawn_locations.get(p).equals(p.getLocation())) {
				p.sendMessage(ChatColor.RED + "Please move to use chat!");
			} else {
				PlayerJoin.spawn_locations.remove(p);
			}
		}
		
		if (plugin.cfgm.getPlayers().getBoolean(p.getUniqueId().toString() + ".ismuted")) {
			p.sendMessage(ChatColor.RED + "You are muted!");
			return;
		}
		
		if (!SwitchChatCommand.player_in_chat.get(p).equalsIgnoreCase("global")) {
			
			String chat_name = SwitchChatCommand.player_in_chat.get(p);
			ChatRoom chat = null;
			
			for (ChatRoom c : PrivateChatCommand.chatrooms) {
				if (c.getName_of_chat().equals(chat_name)) {
					chat = c;
				}
			}
			
			if (chat == null) {
				p.sendMessage(ChatColor.RED + "Error: switched chat not found! - reverting to global");
				SwitchChatCommand.player_in_chat.put(p, "global");
			} else {
				if (!chat.getPlayersInRoom().containsKey((OfflinePlayer) p)) {
					p.sendMessage(ChatColor.RED + "You are no longer in this chat - reverting to global");
					SwitchChatCommand.player_in_chat.put(p, "global");
				} else {
					chat.sendMessage(message, p);
					return;
				}
			}
		
		}
		
		if (OnChat.prev_msg.get(p).equalsIgnoreCase(message) && plugin.getConfig().getBoolean("Enable.anti-spam")) {
			p.sendMessage(ChatColor.RED + "Please do not repeat the same message twice");
			return;
		} else {
			OnChat.prev_msg.put(p, message);
		}
		
		Date now = new Date();
		SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
		
		if (plugin.getConfig().getBoolean("Chat-Delay.enabled")) {
			int delay = plugin.getConfig().getInt("Chat-Delay.time");
			if (Integer.parseInt(time.format(OnChat.prev_msg_time.get(p)).split(":")[2]) + delay >= Integer.parseInt(time.format(now).split(":")[2]) && Integer.parseInt(time.format(OnChat.prev_msg_time.get(p)).split(":")[1]) == Integer.parseInt(time.format(now).split(":")[1]) && Integer.parseInt(time.format(OnChat.prev_msg_time.get(p)).split(":")[0]) == Integer.parseInt(time.format(now).split(":")[0])) {
				p.sendMessage(ChatColor.RED + "Chat cooldown, please don't send messages \"" + Integer.toString(delay) + " seconds\" apart");
				return;
			} else {
				OnChat.prev_msg_time.put(p, now);
			}
			
		}
		
		String permission = null;
		try {
			permission = plugin.vault.getPermissions().getPrimaryGroup(p);
		} catch (UnsupportedOperationException e1) {
			plugin.logEvent("Vault has not connected to a permissions plugins, reverting to default!");
			permission = plugin.getConfig().getString("Rank-Display.default");
		}
		
		message = message.replaceAll("%player%", p.getName());
		message = message.replaceAll("%health%", Double.toString(p.getHealth()));
		message = message.replaceAll("%world%", p.getLocation().getWorld().getName());
		message = message.replaceAll("%uuid%", p.getUniqueId().toString());
		
		message = message.replaceAll("%smile%", ":)");
		message = message.replaceAll("%derp%", ":p");
		message = message.replaceAll("%laugh%", "XD");
		
		if (plugin.res) {
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
					plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".chat-offences", plugin.cfgm.getPlayers().getInt(p.getUniqueId() + ".chat-offences") + 1);
					if (plugin.getConfig().getString("Restrictions.Ban-After") != "null") {
						if (plugin.cfgm.getPlayers().getInt(p.getUniqueId() + ".chat-offences") == plugin.getConfig().getInt("Ban-After")) {
							p.setBanned(true);
							return;
						}
					}
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
						plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".chat-offences", plugin.cfgm.getPlayers().getInt(p.getUniqueId() + ".chat-offences") + 1);
						if (plugin.getConfig().getString("Restrictions.Ban-After") != "null") {
							if (plugin.cfgm.getPlayers().getInt(p.getUniqueId() + ".chat-offences") == plugin.getConfig().getInt("Ban-After")) {
								p.setBanned(true);
								return;
							}
						}
						if (plugin.getConfig().getBoolean("Restrictions.notify-player")) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Restrictions.player-message")));
						}
						for (int i = 0 ; i < phrase.length() ; i++) {
							arr_message.set(message.indexOf(phrase) + i, plugin.getConfig().getString("Restrictions.replace-part-voilation-message-with"));
						}
					}
				}
			}
			
			plugin.cfgm.savePlayers();
			
			message = "";
			for (String s : arr_message) {
				message = message + s;
			}
			
		}
		
		if (plugin.getConfig().getBoolean("Fliter.unicode")) {
			message = Utils.unicodeFilter(message, p);
		}
		if (plugin.getConfig().getBoolean("Fliter.url")) {
			message = Utils.urlFilter(message, p);
		}
		if (plugin.getConfig().getBoolean("Fliter.ip")) {
			message = Utils.ipFilter(message, p);
		}
		if (plugin.getConfig().getBoolean("Fliter.caps")) {
			message = Utils.excessiveCapsFilter(message, p);
			if (message == null) {
				return;
			}
		}
		
		if (!plugin.getConfig().getBoolean("use-preset-Vault-chat-settings")) {
			if (plugin.rd && plugin.getConfig().getConfigurationSection("Connections").getBoolean("Vault") && plugin.getConfig().getConfigurationSection("Rank-Display").getKeys(false).contains(permission)) {
				
				if (plugin.getConfig().getBoolean("Rank-Display.use-op-colour") && p.isOp()) {
					message = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Rank-Display.op-colour") + p.getDisplayName()) + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Rank-Display." + permission + ".chat-colour") + ": " + message);
				} else {
					message = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Rank-Display." + permission + ".name-colour") + p.getDisplayName()) + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Rank-Display." + permission + ".chat-colour") + ": " + message);
				}
				
				message = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Rank-Display." + permission + ".prefix")) + " " + message;
				message = message + " " + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Rank-Display." + permission + ".suffix"));
					
				if (plugin.getConfig().getBoolean("Rank-Display." + permission + ".world.show-world")) {
					if (plugin.getConfig().getString("Rank-Display." + permission + ".world.prefix-suffix") == "suffix") {
						message = message + " " + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Rank-Display." + permission + ".world.world-colour") + "[" + p.getWorld().getName() + "]");
					} else {
						message = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Rank-Display." + permission + ".world.world-colour") + "[" + p.getWorld().getName() + "]") + " " +  message;
					}
				}
			}
		} else {
			Chat chat = plugin.vault.getChat();
			message = ChatColor.translateAlternateColorCodes('&', chat.getPlayerPrefix(p) + " " + p.getDisplayName() + ": " + message + chat.getPlayerSuffix(p));
		}
				
			
		if (plugin.ch) {
			now = new Date();
			SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			plugin.cfgm.getPlayers().set(p.getUniqueId() + ".history." + dateformat.format(now), message);
			plugin.cfgm.savePlayers();
		}
		
		World w = p.getWorld();
		for (Player pl : e.getRecipients()) {
			if (plugin.getConfig().getBoolean("Enable.world-specific-chats")) {
				if (!(IgnoreCommand.being_ignored.get((OfflinePlayer) p).contains((OfflinePlayer) pl)) && pl.getWorld().equals(w)) {
					if (pl.equals(p)) {
						pl.sendMessage(message);
					} else {
						JSONMessage.create(message).tooltip("[Report Player]").runCommand("/report " + p.getName()).send(pl);
					}
				}
			} else {
				if (!(IgnoreCommand.being_ignored.get((OfflinePlayer) p).contains((OfflinePlayer) pl))) {
					if (pl.equals(p)) {
						pl.sendMessage(message);
					} else {
						JSONMessage.create(message).tooltip("[Report Player]").runCommand("/report " + p.getName()).send(pl);
					}
				}
			}
			
		}
		
	}

	public static String getPrev_msg(Player p) {
		return prev_msg.get(p);
	}

	public static void setPrev_msg(Player p, String data) {
		OnChat.prev_msg.put(p, data);
	}

	public static Date getPrev_msg_time(Player p) {
		return prev_msg_time.get(p);
	}

	public static void setPrev_msg_time(Player p, Date data) {
		OnChat.prev_msg_time.put(p, data);
	}

}
