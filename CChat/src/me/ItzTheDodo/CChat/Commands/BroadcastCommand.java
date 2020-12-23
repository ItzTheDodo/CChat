package me.ItzTheDodo.CChat.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.ItzTheDodo.CChat.CChat;
import me.ItzTheDodo.CChat.Commands.utils.CommandUtils;
import net.md_5.bungee.api.ChatColor;

public class BroadcastCommand implements CommandExecutor {
	
	private CChat plugin;

	public BroadcastCommand(CChat pl) {
		plugin = pl;
		plugin.getCommand("broadcast").setExecutor(this);
		CommandUtils.registerCommand("broadcast", "broadcast a message to the whole server");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("broadcast")) {
			
			if (args.length == 0) {
				List<String> help = CommandUtils.generateGlobalHelp();
				for (int i = 0 ; i < help.size() ; i++) {
					sender.sendMessage(help.get(i));
				}
				return true;
			} else {
				
				if (!(sender.hasPermission("cchat.commands.broadcast.use"))) {
					sender.sendMessage(CommandUtils.getNoPermissionWarning());
					return true;
				}
				
				Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.YELLOW + "> Broadcast: " + args[0]));
				
				return true;
			}
		
		}
		
		return false;
	}

}
