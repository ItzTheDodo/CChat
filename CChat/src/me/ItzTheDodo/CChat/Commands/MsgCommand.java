package me.ItzTheDodo.CChat.Commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ItzTheDodo.CChat.CChat;
import me.ItzTheDodo.CChat.Commands.utils.CommandUtils;
import net.md_5.bungee.api.ChatColor;

public class MsgCommand implements CommandExecutor {
	
	private CChat plugin;
	public HashMap<Player, Player> recipients = new HashMap<Player, Player>();

	public MsgCommand(CChat pl) {
		plugin = pl;
		plugin.getCommand("message").setExecutor(this);
		plugin.getCommand("reply").setExecutor(this);
		CommandUtils.registerCommand("message", "message a player");
		CommandUtils.registerCommand("reply", "reply to your last message");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("message")) {
			
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Please enter a player and message!");
				return true;
			} else {
				if (args.length == 1) {
					sender.sendMessage(ChatColor.RED + "Please enter a message!");
					return true;
				}
				if (!(sender instanceof Player)) {
					sender.sendMessage(plugin.sendItem("Only a player may excecute this command"));
					return true;
				}
				
				Player p = (Player) sender;
				if (!(p.hasPermission("cchat.commands.msg"))) {
					sender.sendMessage(CommandUtils.getNoPermissionWarning());
					return true;
				}
				
				Player target = Bukkit.getServer().getPlayer(args[0]);
				
				if (target == null) {
					sender.sendMessage(ChatColor.RED + "Player not found!");
					return true;
				}
				if (IgnoreCommand.being_ignored.get((OfflinePlayer) p).contains((OfflinePlayer) target)) {
					sender.sendMessage(ChatColor.RED + "You are being ignored by this player!");
					return true;
				}
				
				target.sendMessage(ChatColor.YELLOW + "from [" + p.getDisplayName() + "] " + args[1]);
				p.sendMessage(ChatColor.YELLOW + "to [" + target.getDisplayName() + "] " + args[1]);
				recipients.put(target, p);
				recipients.put(p, target);
				return true;
			}
			
		}
		
		if (cmd.getName().equalsIgnoreCase("reply")) {
			
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Please enter a message!");
				return true;
			} else {
				
				if (!(sender instanceof Player)) {
					sender.sendMessage(plugin.sendItem("Only a player may excecute this command"));
					return true;
				}
				
				Player p = (Player) sender;
				
				if (!(p.hasPermission("cchat.commands.msg"))) {
					sender.sendMessage(CommandUtils.getNoPermissionWarning());
					return true;
				}
				if (!recipients.containsKey(p)) {
					sender.sendMessage(ChatColor.RED + "You haven't sent/recieved a message recently");
					return true;
				}
				
				Player target = recipients.get(p);
				if (IgnoreCommand.being_ignored.get((OfflinePlayer) p).contains((OfflinePlayer) target)) {
					sender.sendMessage(ChatColor.RED + "You are being ignored by this player!");
					return true;
				}
				
				target.sendMessage(ChatColor.YELLOW + "from [" + p.getDisplayName() + "] " + args[0]);
				p.sendMessage(ChatColor.YELLOW + "to [" + target.getDisplayName() + "] " + args[0]);
				recipients.put(target, p);
				recipients.put(p, target);
				return true;
				
			}
			
		}
		
		return false;
	}

}
