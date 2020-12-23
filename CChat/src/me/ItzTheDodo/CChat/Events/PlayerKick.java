package me.ItzTheDodo.CChat.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import me.ItzTheDodo.CChat.CChat;
import me.ItzTheDodo.CChat.Commands.SwitchChatCommand;
import net.md_5.bungee.api.ChatColor;

public class PlayerKick implements Listener {
	
	private CChat plugin;

	public PlayerKick(CChat pl) {
		plugin = pl;
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent e) {
		
		Player p = e.getPlayer();
		
		if (plugin.getConfig().getBoolean("Join-leave-messages.use")) {
			String message = plugin.getConfig().getString("Join-leave-messages.kick");
			message = ChatColor.translateAlternateColorCodes('&', message);
			message = message.replace("<Player>", p.getName());
			message = message.replace("<World>", p.getWorld().getName());
			message = ChatColor.translateAlternateColorCodes('&', message);
			e.setLeaveMessage(message);
		} else {
			e.setLeaveMessage("");
		}
		
		SwitchChatCommand.player_in_chat.remove(p);
	
	}

}
