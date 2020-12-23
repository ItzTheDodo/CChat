package me.ItzTheDodo.CChat.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ItzTheDodo.CChat.CChat;
import me.ItzTheDodo.CChat.ChatRooms.ChatRoom;
import me.ItzTheDodo.CChat.ChatRooms.ChatSettings;
import me.ItzTheDodo.CChat.ChatRooms.utils.RankLevel;
import me.ItzTheDodo.CChat.Commands.utils.CommandUtils;
import me.ItzTheDodo.CChat.api.rayzr522.JSONMessage;
import net.md_5.bungee.api.ChatColor;

public class PrivateChatCommand implements CommandExecutor {
	
	// /pc create test testing true [&1,&2,&3,&4,&5,&6] >
	
	private CChat plugin;
	public static List<ChatRoom> chatrooms = new ArrayList<ChatRoom>();
	public static HashMap<Player, String> requests = new HashMap<Player, String>();

	public PrivateChatCommand(CChat pl) {
		plugin = pl;
		plugin.getCommand("privatechat").setExecutor(this);
		CommandUtils.registerCommand("privatechat create <chat-name> <chat-description> <nsfw> <rank colours in ascending order> <chat prefix> [min rank level (0-5)] [rank names in ascending order]", "create a private chat");
		CommandUtils.registerCommand("privatechat remove <chat-name>", "remove a private chat");
		CommandUtils.registerCommand("privatechat invite <chat-name> <player-name> [rank]", "invite a player to a private chat");
		CommandUtils.registerCommand("privatechat kick <chat-name> <player-name>", "kick a player in a private chat");
		CommandUtils.registerCommand("privatechat accept <sent-player-name> <chat>", "accept a private chat invite");
		CommandUtils.registerCommand("privatechat deny <sent-player-name>", "deny a private chat invite");
		CommandUtils.registerCommand("privatechat leave <chat>", "deny a private chat invite");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("privatechat")) {
			
			if (args.length == 0) {
				List<String> help = CommandUtils.generateGlobalHelp();
				for (int i = 0 ; i < help.size() ; i++) {
					sender.sendMessage(help.get(i));
				}
				return true;
			} else {
				
				if (args[0].equalsIgnoreCase("create")) {
					
					if (!(sender instanceof Player)) {
						sender.sendMessage(plugin.sendItem("Only a player may excecute this command"));
						return true;
					}
					if (args.length < 6) {
						sender.sendMessage(ChatColor.RED + "You do not have enough information in the command to create a private chat");
						return true;
					}
					
					Player p = (Player) sender;
					
					if (!(p.hasPermission("cchat.commands.privatechat.create"))) {
						p.sendMessage(CommandUtils.getNoPermissionWarning());
						return true;
					}
					
					String name = args[1];
					
					for (ChatRoom cr : chatrooms) {
						if (cr.getName_of_chat().equalsIgnoreCase(name)) {
							p.sendMessage(ChatColor.RED + "This chat name is alredy in use!");
							return true;
						}
					}
					
					String desc = args[2];
					String nsfw_prim = args[3];
					boolean nsfw;
					if (nsfw_prim.toLowerCase().equalsIgnoreCase("true")) {
						nsfw = true;
					} else {
						if (nsfw_prim.toLowerCase().equalsIgnoreCase("false")) {
							nsfw = false;
						} else {
							sender.sendMessage(plugin.sendItem("NSFW arg must represent a boolean (true, false)"));
							return true;
						}
					}
					String pre = args[5];
					int lowest_rank = 0;
					if (args.length > 6) {
						try {
							lowest_rank = Integer.parseInt(args[6]);
						} catch (NumberFormatException e) {
							sender.sendMessage("min rank level must be an integer");
							return true;
						}
						
					}
					if (lowest_rank > 4) {
						lowest_rank = 4;
					}
					HashMap<String, RankLevel> rank_names = new HashMap<String, RankLevel>();
					if (args.length > 7) {
						List<String> rank_names_prim = Arrays.asList(args[7].replace("[", "").replace("]", "").split(","));
						if (rank_names_prim.size() > 6) {
							int s = rank_names_prim.size();
							for (int i = 0 ; i < (s - 6) ; i++) {
								rank_names_prim.remove(s - i);
							}
						}
						int count = 0;
						for (String s : rank_names_prim) {
							rank_names.put(s, ChatRoom.rankLevelToRank(count));
							count++;
						}
					} else {
						if (lowest_rank == 0) {
							rank_names.put("New", RankLevel.NEW);
							rank_names.put("Member", RankLevel.MEMBER);
							rank_names.put("Trusted", RankLevel.TRUSTED);
							rank_names.put("Staff", RankLevel.STAFF);
							rank_names.put("Co-Owner", RankLevel.CO_OWNER);
							rank_names.put("Owner", RankLevel.CO_OWNER);
						}
						if (lowest_rank == 1) {
							rank_names.put("Member", RankLevel.MEMBER);
							rank_names.put("Trusted", RankLevel.TRUSTED);
							rank_names.put("Staff", RankLevel.STAFF);
							rank_names.put("Co-Owner", RankLevel.CO_OWNER);
							rank_names.put("Owner", RankLevel.CO_OWNER);
						}
						if (lowest_rank == 2) {
							rank_names.put("Trusted", RankLevel.TRUSTED);
							rank_names.put("Staff", RankLevel.STAFF);
							rank_names.put("Co-Owner", RankLevel.CO_OWNER);
							rank_names.put("Owner", RankLevel.CO_OWNER);
						}
						if (lowest_rank == 3) {
							rank_names.put("Staff", RankLevel.STAFF);
							rank_names.put("Co-Owner", RankLevel.CO_OWNER);
							rank_names.put("Owner", RankLevel.CO_OWNER);
						}
						if (lowest_rank == 4) {
							rank_names.put("Co-Owner", RankLevel.CO_OWNER);
							rank_names.put("Owner", RankLevel.CO_OWNER);
						}
					}
					String[] rank_col_prim = args[4].replace("[", "").replace("]", "").split(",");
					HashMap<String, String> rank_col = new HashMap<String, String>();
					
					List<String> names = new ArrayList<String>();
					for (String r : rank_names.keySet()) {
						names.add(r);
					}
					int count = 0;
					for (String s : rank_col_prim) {
						rank_col.put(names.get(count), s);
						count++;
					}
					
					HashMap<OfflinePlayer, RankLevel> pir = new HashMap<OfflinePlayer, RankLevel>();
					pir.put((OfflinePlayer) p, RankLevel.OWNER);
					
					ChatSettings sett = new ChatSettings(pre, rank_names, rank_col, nsfw);
					ChatRoom chat = new ChatRoom(plugin, name, desc, pir, sett);
					PrivateChatCommand.chatrooms.add(chat);
					p.sendMessage(ChatColor.GREEN + "Private Chat Created!");
					
					return true;
				}
				
				if (args[0].equalsIgnoreCase("remove")) {
					if (!(sender instanceof Player)) {
						sender.sendMessage(plugin.sendItem("Only a player may excecute this command"));
						return true;
					}
					if (args.length < 3) {
						sender.sendMessage(ChatColor.RED + "You do not have enough information in the command to remove the private chat");
						return true;
					}
					
					Player p = (Player) sender;
					
					String chat_name = args[1];
					ChatRoom chat = null;
					
					for (ChatRoom c : chatrooms) {
						if (c.getName_of_chat().equals(chat_name)) {
							chat = c;
						}
					}
					
					if (chat == null) {
						p.sendMessage(ChatColor.RED + "Chat not found!");
						return true;
					}
					
					
					if (!(p.hasPermission("cchat.commands.privatechat.remove"))) {
						
						if (!(chat.getRank(p).equals(RankLevel.OWNER) || chat.getRank(p).equals(RankLevel.CO_OWNER))) {
							p.sendMessage(ChatColor.RED + "You do not have permission to remove this chat");
							return true;
						}
						
						chat.close();
						chatrooms.remove(chat);
						p.sendMessage(ChatColor.GREEN + "Chat removed!");
						return true;
					}
					
					chat.close();
					chatrooms.remove(chat);
					p.sendMessage(ChatColor.GREEN + "Chat removed!");
					return true;
				}
				if (args[0].equalsIgnoreCase("invite")) {
					
					if (!(sender instanceof Player)) {
						sender.sendMessage(plugin.sendItem("Only a player may excecute this command"));
						return true;
					}
					if (args.length < 2) {
						sender.sendMessage(ChatColor.RED + "You do not have enough information in the command to invite a player to a private chat");
						return true;
					}
					
					Player p = (Player) sender;
					
					String chat_name = args[1];
					ChatRoom chat = null;
					
					for (ChatRoom c : chatrooms) {
						if (c.getName_of_chat().equals(chat_name)) {
							chat = c;
						}
					}
					
					if (chat == null) {
						p.sendMessage(ChatColor.RED + "Chat not found!");
						return true;
					}
					
					Player target = Bukkit.getServer().getPlayer(args[2]);
					
					if (target == null) {
						sender.sendMessage(ChatColor.RED + "Player not found!");
						return true;
					}
					
					if (chat.getPlayersInRoom().containsKey((OfflinePlayer) target)) {
						sender.sendMessage(ChatColor.RED + "This player is already in this private chat");
						return true;
					}
					
					if (!(p.hasPermission("cchat.commands.privatechat.add")) && !(chat.getRank(p).equals(RankLevel.OWNER) || chat.getRank(p).equals(RankLevel.CO_OWNER))) {
						p.sendMessage(ChatColor.RED + "You do not have permission to add a player to this chat");
						return true;
					}
					
					if (requests.containsKey(target)) {
						p.sendMessage(ChatColor.RED + "This player currently has a pending invite");
						return true;
					}
					
					if (args.length > 3) {
						requests.put(target, args[3]);
						JSONMessage.create(ChatColor.YELLOW + p.getDisplayName() + " has sent you an invite to join a private chat \n").then("Accept").color(org.bukkit.ChatColor.GREEN).style(org.bukkit.ChatColor.BOLD).tooltip("[Click to accept]").runCommand("/pc accept " + p.getName() + " " + chat.getName_of_chat()).then(" or ").color(org.bukkit.ChatColor.YELLOW).then("Deny").color(org.bukkit.ChatColor.RED).style(org.bukkit.ChatColor.BOLD).tooltip("[Click to deny]").runCommand("/pc deny " + p.getName()).send(target);
						p.sendMessage(ChatColor.GREEN + "Invite sent!");
						return true;
					}
					
					requests.put(target, ChatRoom.rankLevelToRank(5 - (chat.getSettings().getRanks().size() - 1)).name());
					JSONMessage.create(ChatColor.YELLOW + p.getDisplayName() + " has sent you an invite to join a private chat \n").then("Accept").color(org.bukkit.ChatColor.GREEN).style(org.bukkit.ChatColor.BOLD).tooltip("[Click to accept]").runCommand("/pc accept " + p.getName() + " " + chat.getName_of_chat()).then(" or ").color(org.bukkit.ChatColor.YELLOW).then("Deny").color(org.bukkit.ChatColor.RED).style(org.bukkit.ChatColor.BOLD).tooltip("[Click to deny]").runCommand("/pc deny " + p.getName()).send(target);
					p.sendMessage(ChatColor.GREEN + "Invite sent!");
					return true;
					
				}
				if (args[0].equalsIgnoreCase("kick")) {
					
					if (!(sender instanceof Player)) {
						sender.sendMessage(plugin.sendItem("Only a player may excecute this command"));
						return true;
					}
					if (args.length < 3) {
						sender.sendMessage(ChatColor.RED + "You do not have enough information in the command to kick a player from a private chat");
						return true;
					}
					
					Player p = (Player) sender;
					
					String chat_name = args[1];
					ChatRoom chat = null;
					
					for (ChatRoom c : chatrooms) {
						if (c.getName_of_chat().equals(chat_name)) {
							chat = c;
						}
					}
					
					if (chat == null) {
						p.sendMessage(ChatColor.RED + "Chat not found!");
						return true;
					}
					
					Player target = Bukkit.getServer().getPlayer(args[2]);
					
					if (target == null) {
						sender.sendMessage(ChatColor.RED + "Player not found!");
						return true;
					}
					
					if (!(chat.getPlayersInRoom().containsKey((OfflinePlayer) target))) {
						sender.sendMessage(ChatColor.RED + "This player is not in this private chat");
						return true;
					}
					
					if (!(p.hasPermission("cchat.commands.privatechat.kick")) && !(chat.getRank(p).equals(RankLevel.OWNER) || chat.getRank(p).equals(RankLevel.CO_OWNER))) {
						p.sendMessage(ChatColor.RED + "You do not have permission to kick a player from this chat");
						return true;	
					}
					
					chat.removePlayer(p);
					p.sendMessage(ChatColor.GREEN + "Player kicked!");
					return true;
					
				}
				if (args[0].equalsIgnoreCase("accept")) {
					
					if (!(sender instanceof Player)) {
						sender.sendMessage(plugin.sendItem("Only a player may excecute this command"));
						return true;
					}
					if (args.length < 3) {
						sender.sendMessage(ChatColor.RED + "You do not have enough information in the command to accept this invite");
						return true;
					}
					
					Player p = (Player) sender;
					
					if (!requests.containsKey(p)) {
						sender.sendMessage(ChatColor.RED + "You have alredy accepted or denied this invite");
						return true;
					}
					
					Player target = Bukkit.getServer().getPlayer(args[1]);
					
					if (target == null) {
						sender.sendMessage(ChatColor.RED + "Player not found!");
						return true;
					}
					
					String chat_name = args[2];
					ChatRoom chat = null;
					
					for (ChatRoom c : chatrooms) {
						if (c.getName_of_chat().equals(chat_name)) {
							chat = c;
						}
					}
					
					if (chat == null) {
						p.sendMessage(ChatColor.RED + "Chat not found!");
						return true;
					}
					
					chat.addPlayer(p, requests.get(p));
					requests.remove(p);
					p.sendMessage(ChatColor.GREEN + "Chat Joined!");
					target.sendMessage(ChatColor.GREEN + p.getName() + " has accepted your invite!");
					return true;
					
				}
				if (args[0].equalsIgnoreCase("deny")) {
					
					if (!(sender instanceof Player)) {
						sender.sendMessage(plugin.sendItem("Only a player may excecute this command"));
						return true;
					}
					if (args.length < 2) {
						sender.sendMessage(ChatColor.RED + "You do not have enough information in the command to accept this invite");
						return true;
					}
					
					Player p = (Player) sender;
					
					if (!requests.containsKey(p)) {
						sender.sendMessage(ChatColor.RED + "You have alredy accepted or denied this invite");
						return true;
					}
					
					Player target = Bukkit.getServer().getPlayer(args[1]);
					
					if (target == null) {
						sender.sendMessage(ChatColor.RED + "Player not found!");
						return true;
					}
					
					requests.remove(p);
					p.sendMessage(ChatColor.GREEN + "Chat invite denied!");
					target.sendMessage(ChatColor.GREEN + p.getName() + " has denied your invite!");
					return true;
					
				}
				if (args[0].equalsIgnoreCase("leave")) {
					
					if (!(sender instanceof Player)) {
						sender.sendMessage(plugin.sendItem("Only a player may excecute this command"));
						return true;
					}
					if (args.length < 2) {
						sender.sendMessage(ChatColor.RED + "You do not have enough information in the command to leave a chat");
						return true;
					}
					
					Player p = (Player) sender;
					
					String chat_name = args[1];
					ChatRoom chat = null;
					
					for (ChatRoom c : chatrooms) {
						if (c.getName_of_chat().equals(chat_name)) {
							chat = c;
						}
					}
					
					if (chat == null) {
						p.sendMessage(ChatColor.RED + "Chat not found!");
						return true;
					}
					
					chat.removePlayer(p);
					p.sendMessage(ChatColor.GREEN + "Chat left!");
					return true;
					
				}
				
				return true;
				
			}
		
		}
		
		return false;
	
	}

}
