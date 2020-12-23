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

public class NickCommand implements CommandExecutor {
	
	private CChat plugin;

	public NickCommand(CChat pl) {
		plugin = pl;
		plugin.getCommand("nick").setExecutor(this);
		CommandUtils.registerCommand("nick", "disguises a users real IGN");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(plugin.sendItem("Only a player may excecute this command"));
			return true;
		}
		Player p = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("nick")) {
			
			if (args.length == 0) {
				List<String> help = CommandUtils.generateGlobalHelp();
				for (int i = 0 ; i < help.size() ; i++) {
					p.sendMessage(help.get(i));
				}
				return true;
			} else {
				
				if (args[0].equalsIgnoreCase("remove")) {
					p.setDisplayName(p.getName());
					plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".nick", " ");
					p.sendMessage(ChatColor.GREEN + "Nick removed!");
					return true;
				} else {
				
					Player target = Bukkit.getServer().getPlayer(args[0]);
					
					if (target == null) {
						if (p.hasPermission("cchat.commands.nick.use")) {
							p.setDisplayName(plugin.getConfig().getString("nickname-prefix") + args[0]);
							plugin.cfgm.getPlayers().set(p.getUniqueId().toString() + ".nick", plugin.getConfig().getString("nickname-prefix") + args[0]);
							p.sendMessage(ChatColor.GREEN + "You are now nicked!");
							return true;
						} else {
							p.sendMessage(CommandUtils.getNoPermissionWarning());
							return true;
						}
					} else {
						if (p.hasPermission("cchat.commands.nick.others")) {
							if (args.length == 1) {
								p.sendMessage(ChatColor.RED + "Please enter a nickname!");
								return true;
							} else {
								Player p_target = target;
								if (args[1].equalsIgnoreCase("remove")) {
									p_target.setDisplayName(p_target.getName());
									plugin.cfgm.getPlayers().set(p_target.getUniqueId().toString() + ".nick", " ");
									p.sendMessage(ChatColor.GREEN + p_target.getName() + "'s nick has been removed!");
									return true;
								} else {
									p_target.setDisplayName(plugin.getConfig().getString("nickname-prefix") + args[1]);
									plugin.cfgm.getPlayers().set(p_target.getUniqueId().toString() + ".nick", plugin.getConfig().getString("nickname-prefix") + args[1]);
									p.sendMessage(ChatColor.GREEN + p_target.getName() + " is now nicked");
									return true;
								}
							
							}
						} else {
							p.sendMessage(CommandUtils.getNoPermissionWarning());
							return true;
						}
					
					}
				
				}
				
			}
			
		}
		
		return false;
	}

}
