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

public class ReportCommand implements CommandExecutor {
	
	private CChat plugin;

	public ReportCommand(CChat pl) {
		plugin = pl;
		plugin.getCommand("report").setExecutor(this);
		CommandUtils.registerCommand("report <player>", "reports a player");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("report")) {
			
			if (args.length == 0) {
				List<String> help = CommandUtils.generateGlobalHelp();
				for (int i = 0 ; i < help.size() ; i++) {
					sender.sendMessage(help.get(i));
				}
				return true;
			} else {
				
				if (!(sender instanceof Player)) {
					sender.sendMessage(plugin.sendItem("Only a player may excecute this command"));
					return true;
				}
				
				Player p = (Player) sender;
				Player target = Bukkit.getServer().getPlayer(args[0]);
				
				if (target == null) {
					sender.sendMessage(ChatColor.RED + "Player not found!");
					return true;
				}
				
				String message = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("report-message").replace("<Player>", p.getName()).replace("<Target>", target.getName()));
				
				for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
					
					if (staff.hasPermission("cchat.admin.reports.receive")) {
						staff.sendMessage(message);
					}
				
				}
				
				p.sendMessage(ChatColor.GREEN + "Player reported!");
				
			}
			
		}
		
		return true;
	
	}

}
