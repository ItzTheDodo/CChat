package me.ItzTheDodo.CChat.Events.ChatUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Utils {
	
	public static String unicodeFilter(String message, Player p) {
		String mod_message = message;
		boolean isFound = false;
		
		for (char c : mod_message.toCharArray()) {
			if (Character.isUnicodeIdentifierPart(c)) {
				mod_message = mod_message.replace(c, '*');
				isFound = true;
			}
		}
		
		if (isFound) {
			p.sendMessage(ChatColor.RED + "Please do not use unicode characters");
		}
		
		return mod_message;
	}
	
	public static String urlFilter(String message, Player p) {
		String mod_message = message;
		List<String> words = Arrays.asList(mod_message.split(" "));
		mod_message = "";
		boolean isFound = false;
		int count = 0;
		
		for (String s : words) {
			if (s.contains("https://") || s.contains("http://")  || s.contains("www.")  || s.contains(".co")  || s.contains(".org")) {
				words.remove(count);
				words.add(count, "********");
				isFound = true;
			}
			mod_message = mod_message + " " + words.get(count);
			count++;
		}
		
		if (isFound) {
			p.sendMessage(ChatColor.RED + "Please do put urls in a chat message");
		}
		
		return mod_message;
	}
	
	public static String ipFilter(String message, Player p) {
		String mod_message = message;
		List<String> words = Arrays.asList(mod_message.split(" "));
		mod_message = "";
		boolean isFound = false;
		int count = 0;
		
		for (String s : words) {
			String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
			Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
			Matcher matcher = pattern.matcher(s);
			if (matcher.find()) {
				isFound = true;
				words.remove(count);
				words.add(count, "********");
			} else {
				IPADDRESS_PATTERN = "([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}";
				pattern = Pattern.compile(IPADDRESS_PATTERN);
				matcher = pattern.matcher(s);
				if (matcher.find()) {
					isFound = true;
					words.remove(count);
					words.add(count, "********");
				}
			}
			mod_message = mod_message + " " + words.get(count);
			count++;
		}
		
		if (isFound) {
			p.sendMessage(ChatColor.RED + "Please do put ips in a chat message");
		}
		
		return mod_message;
	}
	
	public static String excessiveCapsFilter(String message, Player p) {
		String mod_message = message;
		List<Character> words = mod_message.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
		mod_message = "";
		boolean isFound = false;
		int count = 0;
		
		for (char s : words) {
			
			if (!(count == 0 && count < (words.size() - 1))) {
				if (Character.isUpperCase(s) && Character.isUpperCase(words.get(count - 1)) && Character.isUpperCase(words.get(count + 1))) {
					isFound = true;
				}
			
			}
			mod_message = mod_message + " " + words.get(count);
			count++;
		}
		
		if (isFound) {
			p.sendMessage(ChatColor.RED + "Please use excessive caps (caps lock) in chat messages");
			return null;
		}
		
		return mod_message;
		
	}

}
