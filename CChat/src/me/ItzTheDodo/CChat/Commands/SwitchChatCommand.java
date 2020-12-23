package me.ItzTheDodo.CChat.Commands;

import java.util.HashMap;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ItzTheDodo.CChat.CChat;
import me.ItzTheDodo.CChat.ChatRooms.ChatRoom;
import me.ItzTheDodo.CChat.Commands.utils.CommandUtils;
import net.md_5.bungee.api.ChatColor;

public class SwitchChatCommand implements CommandExecutor {
	
	private CChat plugin;
	public static HashMap<Player, String> player_in_chat = new HashMap<Player, String>();

	public SwitchChatCommand(CChat pl) {
		plugin = pl;
		plugin.getCommand("switchchat").setExecutor(this);
		CommandUtils.registerCommand("switchchat global", "switch to global chat (in world)");
		CommandUtils.registerCommand("switchchat <chat>", "switch to a private chat");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("switchchat")) {
			
			if (args.length == 0) {
				List<String> help = CommandUtils.generateGlobalHelp();
				for (int i = 0 ; i < help.size() ; i++) {
					sender.sendMessage(help.get(i));
				}
				return true;
			} else {
				
				if (args[0].toLowerCase().equalsIgnoreCase("global")) {
					
					if (!(sender instanceof Player)) {
						sender.sendMessage(plugin.sendItem("Only a player may excecute this command"));
						return true;
					}
					
					Player p = (Player) sender;
					
					if (player_in_chat.get(p).equalsIgnoreCase("global")) {
						p.sendMessage(ChatColor.RED + "You are already in this chat");
						return true;
					}
					
					player_in_chat.put(p, "global");
					p.sendMessage(ChatColor.GREEN + "Chat switched To: global");
					
					return true;
				
				}
				
				if (!(sender instanceof Player)) {
					sender.sendMessage(plugin.sendItem("Only a player may excecute this command"));
					return true;
				}
				
				Player p = (Player) sender;
				
				String chat_name = args[0];
				ChatRoom chat = null;
				
				for (ChatRoom c : PrivateChatCommand.chatrooms) {
					if (c.getName_of_chat().equals(chat_name)) {
						chat = c;
					}
				}
				
				if (chat == null) {
					p.sendMessage(ChatColor.RED + "Chat not found!");
					return true;
				}
				
				if (player_in_chat.get(p).equalsIgnoreCase(chat_name)) {
					p.sendMessage(ChatColor.RED + "You are already in this chat");
					return true;
				}
				
				if (!chat.getPlayersInRoom().containsKey((OfflinePlayer) p)) {
					p.sendMessage(ChatColor.RED + "You are not part of this chat!");
					return true;
				}
				
				player_in_chat.put(p, chat_name);
				p.sendMessage(ChatColor.GREEN + "Chat switched To: " + chat_name);
				
				return true;
				
			}
		
		}
		
		return false;
		
	}

}
