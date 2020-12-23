package me.ItzTheDodo.CChat.Commands.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.ItzTheDodo.CChat.CChat;
import net.md_5.bungee.api.ChatColor;

public class CommandUtils {
	
	@SuppressWarnings("unused")
	private CChat plugin;
	private static HashMap<String, String> commands = new HashMap<String, String>();
	private static List<String> command_names = new ArrayList<String>();

	public CommandUtils(CChat pl) {
		plugin = pl;
	}
	
	public static void registerCommand(String name, String info) {
		commands.put(name, info);
		command_names.add(name);
	}

	public static HashMap<String, String> getCommands() {
		return commands;
	}
	
	public static List<String> getCommandNames() {
		return command_names;
	}
	
	public static String getCommandInfoByName(String name) {
		return commands.get(name);
	}
	
	public static List<String> generateGlobalHelp() {
		List<String> out = new ArrayList<String>();
		
		out.add("");
		out.add(ChatColor.GREEN + "------------- CChat -------------");
		for (int i = 0 ; i < commands.size() ; i++) {
			out.add(ChatColor.AQUA + "> /" + command_names.get(i) + " - " + commands.get(command_names.get(i)));
		}
		out.add(ChatColor.GREEN + "---------------------------------");
		out.add("");
		
		return out;
	}
	
	public static String getNoPermissionWarning() {
		return ChatColor.RED + "You do not have permission to execute this command!";
	}
	
	public static String compileMuteTime(String arg) {
		String mutetime;
		
		int time = Integer.parseInt(arg.replaceAll("\\D", ""));
		
		if (arg.toLowerCase().contains("s")) {
			mutetime = Integer.toString(time) + ":0:0~0/0/0";
		} else {
			if (arg.toLowerCase().contains("m")) {
				mutetime = "0:" + Integer.toString(time) + ":0~0/0/0";
			} else {
				if (arg.toLowerCase().contains("h")) {
					mutetime = "0:0:" + Integer.toString(time) + "~0/0/0";
				} else {
					if (arg.toLowerCase().contains("d")) {
						mutetime = "0:0:0~" + Integer.toString(time) + "/0/0";
					} else {
						if (arg.toLowerCase().contains("mon")) {
							mutetime = "0:0:0~0/" + Integer.toString(time) + "/0";
						} else {
							if (arg.toLowerCase().contains("y")) {
								mutetime = "0:0:0~0/0/" + Integer.toString(time);
							} else {
								return null;
							}
						}
					}
				}
			}
		}
		return mutetime;
	}

}
