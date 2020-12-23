package me.ItzTheDodo.CChat.Commands;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ItzTheDodo.CChat.CChat;
import me.ItzTheDodo.CChat.Commands.utils.CommandUtils;
import net.md_5.bungee.api.ChatColor;

public class IgnoreCommand implements CommandExecutor {
	
	private CChat plugin;
	public static HashMap<OfflinePlayer, List<OfflinePlayer>> being_ignored = new HashMap<OfflinePlayer, List<OfflinePlayer>>();

	public IgnoreCommand(CChat pl) {
		plugin = pl;
		plugin.getCommand("ignore").setExecutor(this);
		CommandUtils.registerCommand("ignore <player>", "ignore a player, don't recieve messages from them");
		CommandUtils.registerCommand("ignore remove <player>", "un-ignore a player, recieve messages from them again");
		CommandUtils.registerCommand("ignore list", "lists the people ignoring you");
		CommandUtils.registerCommand("ignore list <player>", "lists the people ignoring a player");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("ignore")) {
			
			if (args.length == 0) {
				List<String> help = CommandUtils.generateGlobalHelp();
				for (int i = 0 ; i < help.size() ; i++) {
					sender.sendMessage(help.get(i));
				}
				return true;
			} else {
				
				if (args[0].equalsIgnoreCase("remove")) {
					if (!(sender instanceof Player)) {
						sender.sendMessage(plugin.sendItem("Only a player may excecute this command"));
						return true;
					}
					if (!(sender.hasPermission("cchat.commands.ignore.use"))) {
						sender.sendMessage(CommandUtils.getNoPermissionWarning());
						return true;
					}
					Player p = (Player) sender;
					
					Player target = Bukkit.getServer().getPlayer(args[1]);
					
					if (target == null) {
						sender.sendMessage(ChatColor.RED + "Player not found!");
						return true;
					}
					
					if (!(IgnoreCommand.being_ignored.get((OfflinePlayer) p).contains((OfflinePlayer) target))) {
						sender.sendMessage(ChatColor.RED + "You are not ignoring this player");
						return true;
					}
					
					IgnoreCommand.being_ignored.get((OfflinePlayer) p).remove((OfflinePlayer) target);
					plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".being-ignored-by." + p.getName(), null);
					plugin.cfgm.savePlayers();
					p.sendMessage(ChatColor.GREEN + "You have now un-ignored " + target.getName());
					return true;
				}
				if (args[0].equalsIgnoreCase("list")) {
					
					if (args.length > 1) {
						if (!(sender.hasPermission("cchat.commands.ignore.list.others"))) {
							sender.sendMessage(CommandUtils.getNoPermissionWarning());
							return true;
						}
						
						Player target = Bukkit.getServer().getPlayer(args[1]);
						
						if (target == null) {
							sender.sendMessage(ChatColor.RED + "Player not found!");
							return true;
						}
						
						if (!(IgnoreCommand.being_ignored.containsKey((OfflinePlayer) target))) {
							target.sendMessage(ChatColor.RED + "They are not being ignored by anyone!");
							return true;
						}
						
						target.sendMessage(ChatColor.AQUA + "----------- + " + target.getName() + "'s Ignored List -----------");
						for (OfflinePlayer op : IgnoreCommand.being_ignored.get(target)) {
							target.sendMessage(ChatColor.GREEN + "- " + op.getName());
						}
						
						return true;
						
					}
					
					if (!(sender instanceof Player)) {
						sender.sendMessage(plugin.sendItem("Only a player may excecute this command"));
						return true;
					}
					if (!(sender.hasPermission("cchat.commands.ignore.list.use"))) {
						sender.sendMessage(CommandUtils.getNoPermissionWarning());
						return true;
					}
					Player p = (Player) sender;
					
					if (!(IgnoreCommand.being_ignored.containsKey((OfflinePlayer) p))) {
						p.sendMessage(ChatColor.RED + "You are not being ignored by anyone!");
						return true;
					}
					
					p.sendMessage(ChatColor.AQUA + "----------- Your Ignored List -----------");
					for (OfflinePlayer op : IgnoreCommand.being_ignored.get(p)) {
						p.sendMessage(ChatColor.GREEN + "- " + op.getName());
					}
					return true;
					
				}
				
				if (!(sender instanceof Player)) {
					sender.sendMessage(plugin.sendItem("Only a player may excecute this command"));
					return true;
				}
				if (!(sender.hasPermission("cchat.commands.ignore.use"))) {
					sender.sendMessage(CommandUtils.getNoPermissionWarning());
					return true;
				}
				Player p = (Player) sender;
				
				Player target = Bukkit.getServer().getPlayer(args[0]);
				
				if (target == null) {
					sender.sendMessage(ChatColor.RED + "Player not found!");
					return true;
				}
				
				IgnoreCommand.being_ignored.get((OfflinePlayer) p).add((OfflinePlayer) target);
				plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".being-ignored-by." + p.getName(), " ");
				plugin.cfgm.savePlayers();
				p.sendMessage(ChatColor.GREEN + "You are now ignoring " + target.getName());
				return true;
				
			}
		
		}
		
		return false;
	}

}
