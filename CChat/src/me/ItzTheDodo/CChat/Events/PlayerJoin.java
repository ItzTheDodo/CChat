package me.ItzTheDodo.CChat.Events;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.ItzTheDodo.CChat.CChat;
import me.ItzTheDodo.CChat.Commands.IgnoreCommand;
import me.ItzTheDodo.CChat.Commands.SwitchChatCommand;
import me.ItzTheDodo.CChat.api.Time;
import net.md_5.bungee.api.ChatColor;

public class PlayerJoin implements Listener {
	
	private CChat plugin;
	public static HashMap<Player, Location> spawn_locations = new HashMap<Player, Location>();

	public PlayerJoin(CChat pl) {
		plugin = pl;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		
		Player p = e.getPlayer();
		PlayerJoin.spawn_locations.put(p, p.getLocation());
		
		if (plugin.getConfig().getBoolean("Join-leave-messages.use")) {
			String message = plugin.getConfig().getString("Join-leave-messages.join");
			message = ChatColor.translateAlternateColorCodes('&', message);
			message = message.replace("<Player>", p.getName());
			message = message.replace("<World>", p.getWorld().getName());
			message = ChatColor.translateAlternateColorCodes('&', message);
			e.setJoinMessage(message);
		} else {
			e.setJoinMessage("");
		}
		
		if (!p.hasPlayedBefore() || !plugin.cfgm.getPlayers().getKeys(false).contains(p.getUniqueId().toString())) {
			
			plugin.cfgm.getPlayers().createSection(p.getUniqueId().toString());
			plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".chat-offences", 0);
			plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".name", p.getName());
			plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".nick", " ");
			plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".ismuted", false);
			plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".timesmuted", 0);
			plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".mutetime", "None");
			plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".mutedate", "None");
			plugin.cfgm.getPlayers().createSection(p.getUniqueId().toString() + ".chatrooms");
			plugin.cfgm.getPlayers().createSection(p.getUniqueId().toString() + ".being-ignored-by");
			plugin.cfgm.getPlayers().createSection(p.getUniqueId().toString() + ".history");
			plugin.cfgm.savePlayers();
			
		}
		
		if (!(plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".nick").equalsIgnoreCase(" "))) {
			p.setDisplayName(plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".nick"));
		} else {
			p.setDisplayName(p.getName());
		}
		
		IgnoreCommand.being_ignored.put((OfflinePlayer) p, new ArrayList<OfflinePlayer>());
		for (String s : plugin.cfgm.getPlayers().getConfigurationSection(p.getUniqueId().toString() + ".being-ignored-by").getKeys(false)) {
			IgnoreCommand.being_ignored.get((OfflinePlayer) p).add(Bukkit.getServer().getOfflinePlayer(s));
		}
		
		if (plugin.cfgm.getPlayers().getBoolean(p.getUniqueId().toString() + ".ismuted")) {
			Time dayofmute = Time.parse(plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutedate"));
			dayofmute.addWithTime(Time.parse(plugin.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutetime")));
			Time timeleft = Time.parse(dayofmute.compareWithTime(Time.dateToTime(Time.getCurrentTime())));
			if (timeleft.getSec() <= 0 && timeleft.getMin() <= 0 && timeleft.getHour() <= 0 && timeleft.getDay() <= 0 && timeleft.getMonth() <= 0 && timeleft.getYear() <= 0) {
				plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".ismuted", false);
				plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".mutetime", "None");
				plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".mutedate", "None");
			}
		}
		
		Date now = new Date();
		OnChat.setPrev_msg(p, " ");
		OnChat.setPrev_msg_time(p, now);
		
		SwitchChatCommand.player_in_chat.put(p, "global");
		
		if (plugin.getConfig().getBoolean("override-present-Vault-chat-settings") && plugin.getConfig().getConfigurationSection("Connections").getBoolean("Vault")) {
			plugin.vault.getChat().setPlayerPrefix(p, plugin.getConfig().getString("Rank-Display." + plugin.vault.getPermissions().getPrimaryGroup(p) + ".prefix"));
			plugin.vault.getChat().setPlayerSuffix(p, plugin.getConfig().getString("Rank-Display." + plugin.vault.getPermissions().getPrimaryGroup(p) + ".suffix"));
		}
		
	}

}
