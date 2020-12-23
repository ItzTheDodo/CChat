package me.ItzTheDodo.CChat.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ItzTheDodo.CChat.CChat;
import me.ItzTheDodo.CChat.Commands.utils.CommandUtils;
import net.md_5.bungee.api.ChatColor;

public class CChatCommand implements CommandExecutor {
	
	private CChat plugin;

	public CChatCommand(CChat pl) {
		plugin = pl;
		plugin.getCommand("cchat").setExecutor(this);
		CommandUtils.registerCommand("cchat", "main command root");
		CommandUtils.registerCommand("cchat reload", "reloads configs after change");
		CommandUtils.registerCommand("cchat help", "displays this help screen");
		CommandUtils.registerCommand("cchat clear", "clears your chat");
		CommandUtils.registerCommand("cchat clear all", "clears everyone's chat");
		CommandUtils.registerCommand("cchat clear <player>", "clears a player's chat");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("cchat")) {
			
			if (args.length == 0) {
				List<String> help = CommandUtils.generateGlobalHelp();
				for (int i = 0 ; i < help.size() ; i++) {
					sender.sendMessage(help.get(i));
				}
				return true;
			} else {
				
				if (args[0].equalsIgnoreCase("help")) {
					List<String> help = CommandUtils.generateGlobalHelp();
					for (int i = 0 ; i < help.size() ; i++) {
						sender.sendMessage(help.get(i));
					}
					return true;
				}
				
				if (args[0].equalsIgnoreCase("reload")) {
					if (!(sender.hasPermission("cchat.commands.cchat.reload"))) {
						sender.sendMessage(CommandUtils.getNoPermissionWarning());
						return true;
					}
					plugin.reloadConfig();
					plugin.loadConfig();
					plugin.cfgm.reloadPlayers();
					return true;
				}
				if (args[0].equalsIgnoreCase("clear")) {
					if (args.length == 1) {
						if (!(sender instanceof Player)) {
							sender.sendMessage(plugin.sendItem("Only a player may excecute this command"));
							return true;
						}
						if (!(sender.hasPermission("cchat.commands.cchat.clear.use"))) {
							sender.sendMessage(CommandUtils.getNoPermissionWarning());
							return true;
						}
						Player p = (Player) sender;
						for (int i = 0 ; i < 100 ; i++) {
							p.sendMessage(" ");
						}
						return true;
					}
					
					if (args[1].equalsIgnoreCase("all")) {
						if (!(sender.hasPermission("cchat.commands.cchat.clear.all"))) {
							sender.sendMessage(CommandUtils.getNoPermissionWarning());
							return true;
						}
						for (int i = 0 ; i < 100 ; i++) {
							Bukkit.getServer().broadcastMessage(" ");
						}
						return true;
					}
					
					if (!(sender.hasPermission("cchat.commands.cchat.clear.others"))) {
						sender.sendMessage(CommandUtils.getNoPermissionWarning());
						return true;
					}
					
					Player target = Bukkit.getServer().getPlayer(args[1]);
					
					if (target == null) {
						sender.sendMessage(ChatColor.RED + "Player not found!");
						return true;
					}
					
					for (int i = 0 ; i < 100 ; i++) {
						target.sendMessage(" ");
					}
					sender.sendMessage(ChatColor.GREEN + "Player chat cleared!");
					return true;
				
				}
				
			}
			
		}
		
		return false;
	}
		

}
