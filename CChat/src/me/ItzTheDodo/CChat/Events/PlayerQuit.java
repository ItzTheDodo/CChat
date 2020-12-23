package me.ItzTheDodo.CChat.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.ItzTheDodo.CChat.CChat;
import me.ItzTheDodo.CChat.Commands.SwitchChatCommand;
import net.md_5.bungee.api.ChatColor;

public class PlayerQuit implements Listener {
	
	private CChat plugin;

	public PlayerQuit(CChat pl) {
		plugin = pl;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		
		Player p = e.getPlayer();
		
		if (plugin.getConfig().getBoolean("Join-leave-messages.use")) {
			String message = plugin.getConfig().getString("Join-leave-messages.leave");
			message = ChatColor.translateAlternateColorCodes('&', message);
			message = message.replace("<Player>", p.getName());
			message = message.replace("<World>", p.getWorld().getName());
			message = ChatColor.translateAlternateColorCodes('&', message);
			e.setQuitMessage(message);
		} else {
			e.setQuitMessage("");
		}
		
		SwitchChatCommand.player_in_chat.remove(p);
	
	}
		
}
