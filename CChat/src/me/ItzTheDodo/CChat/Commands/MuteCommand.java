package me.ItzTheDodo.CChat.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ItzTheDodo.CChat.CChat;
import me.ItzTheDodo.CChat.Commands.utils.CommandUtils;
import me.ItzTheDodo.CChat.api.Time;
import net.md_5.bungee.api.ChatColor;

public class MuteCommand implements CommandExecutor {
	
	private CChat plugin;

	public MuteCommand(CChat pl) {
		plugin = pl;
		plugin.getCommand("mute").setExecutor(this);
		plugin.getCommand("unmute").setExecutor(this);
		CommandUtils.registerCommand("mute <player> [time]", "mute a player");
		CommandUtils.registerCommand("unmute <player>", "unmute a player");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("mute")) {
			
			if (args.length == 0) {
				List<String> help = CommandUtils.generateGlobalHelp();
				for (int i = 0 ; i < help.size() ; i++) {
					sender.sendMessage(help.get(i));
				}
				return true;
			} else {
				
				if (!(sender.hasPermission("cchat.commands.mute.all.use"))) {
					sender.sendMessage(CommandUtils.getNoPermissionWarning());
					return true;
				}
				
				if (args[0].equalsIgnoreCase("all")) {
					
					String mutetime = "0:0:0~0/0/99";
					if (args.length > 1) {
						
						mutetime = CommandUtils.compileMuteTime(args[1]);
						
						if (mutetime == null) {
							sender.sendMessage(ChatColor.RED + "Invalid suffix to time value (s, m, h, d, mon, y)");
							return true;
						}
					
						for (String s : plugin.cfgm.getPlayers().getKeys(false)) {
							plugin.cfgm.getPlayers().set(s + ".ismuted", true);
							plugin.cfgm.getPlayers().set(s + ".mutetime", mutetime);
							plugin.cfgm.getPlayers().set(s + ".mutedate", Time.dateToTime(Time.getCurrentTime()).getFull());
							plugin.cfgm.savePlayers();
						}
						
						return true;
					
					}
					
					for (String s : plugin.cfgm.getPlayers().getKeys(false)) {
						plugin.cfgm.getPlayers().set(s + ".ismuted", true);
						plugin.cfgm.getPlayers().set(s + ".mutetime", mutetime);
						plugin.cfgm.getPlayers().set(s + ".mutedate", Time.dateToTime(Time.getCurrentTime()).getFull());
						plugin.cfgm.savePlayers();
					}
					return true;
					
				}
				
				if (!(sender.hasPermission("cchat.commands.mute.use"))) {
					sender.sendMessage(CommandUtils.getNoPermissionWarning());
					return true;
				}
				
				Player target = Bukkit.getServer().getPlayer(args[0]);
				
				if (target == null) {
					sender.sendMessage(ChatColor.RED + "Player not found!");
					return true;
				}
				
				if (plugin.cfgm.getPlayers().getBoolean(target.getUniqueId().toString() + ".ismuted")) {
					sender.sendMessage(ChatColor.RED + "This player is muted already!");
					return true;
				}
				
				String mutetime = "0:0:0~0/0/99";
				if (args.length > 1) {
					mutetime = CommandUtils.compileMuteTime(args[1]);
					
					if (mutetime == null) {
						sender.sendMessage(ChatColor.RED + "Invalid suffix to time value (s, m, h, d, mon, y)");
						return true;
					}
					
					plugin.cfgm.getPlayers().set(target.getUniqueId().toString() + ".ismuted", true);
					plugin.cfgm.getPlayers().set(target.getUniqueId().toString() + ".timesmuted", plugin.cfgm.getPlayers().getInt(target.getUniqueId().toString() + ".timesmuted") + 1);
					plugin.cfgm.getPlayers().set(target.getUniqueId().toString() + ".mutetime", mutetime);
					plugin.cfgm.getPlayers().set(target.getUniqueId().toString() + ".mutedate", Time.dateToTime(Time.getCurrentTime()).getFull());
					plugin.cfgm.savePlayers();
					sender.sendMessage(ChatColor.GREEN + "Player Muted!");
					
					return true;
					
				}
				
				plugin.cfgm.getPlayers().set(target.getUniqueId().toString() + ".ismuted", true);
				plugin.cfgm.getPlayers().set(target.getUniqueId().toString() + ".mutetime", mutetime);
				plugin.cfgm.getPlayers().set(target.getUniqueId().toString() + ".mutedate", Time.dateToTime(Time.getCurrentTime()).getFull());
				plugin.cfgm.savePlayers();
				
				return true;
			}
		
		}
		
		if (cmd.getName().equalsIgnoreCase("unmute")) {
			
			if (args.length == 0) {
				List<String> help = CommandUtils.generateGlobalHelp();
				for (int i = 0 ; i < help.size() ; i++) {
					sender.sendMessage(help.get(i));
				}
				return true;
			} else {
				
				if (args[0].equalsIgnoreCase("all")) {
					
					if (!(sender.hasPermission("cchat.commands.unmute.all.use"))) {
						sender.sendMessage(CommandUtils.getNoPermissionWarning());
						return true;
					}
					
					for (String s : plugin.cfgm.getPlayers().getKeys(false)) {
						plugin.cfgm.getPlayers().set(s + ".ismuted", false);
						plugin.cfgm.getPlayers().set(s + ".mutetime", "None");
						plugin.cfgm.getPlayers().set(s + ".mutedate", "None");
						plugin.cfgm.savePlayers();
					}
					return true;
					
				}
				
				if (!(sender.hasPermission("cchat.commands.unmute.use"))) {
					sender.sendMessage(CommandUtils.getNoPermissionWarning());
					return true;
				}
				
				Player target = Bukkit.getServer().getPlayer(args[0]);
				
				if (target == null) {
					sender.sendMessage(ChatColor.RED + "Player not found!");
					return true;
				}
				
				plugin.cfgm.getPlayers().set(target.getUniqueId().toString() + ".ismuted", false);
				plugin.cfgm.getPlayers().set(target.getUniqueId().toString() + ".mutetime", "None");
				plugin.cfgm.getPlayers().set(target.getUniqueId().toString() + ".mutedate", "None");
				plugin.cfgm.savePlayers();
				
				return true;
			}
		
		}
		
		return false;
		
	}

}
