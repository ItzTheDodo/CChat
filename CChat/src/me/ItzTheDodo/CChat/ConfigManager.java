package me.ItzTheDodo.CChat;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
	
	private CChat plugin;
	
	public ConfigManager(CChat pl) {
		plugin = pl;
	}
	
	
	public FileConfiguration playerscfg;
	public File playersfile;
	public FileConfiguration chatroomscfg;
	public File chatroomsfile;
	
	public void setupPlayers() {
		
		if (!(plugin.getDataFolder().exists())) {
			plugin.getDataFolder().mkdir();
			
		}
		
		playersfile = new File(plugin.getDataFolder(), "players.yml");
		
		if(!(playersfile.exists())) {
			try {
				playersfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}
		
		playerscfg = YamlConfiguration.loadConfiguration(playersfile);
		Reader defConfigStream = null;
	    try {
	        defConfigStream = new InputStreamReader(plugin.getResource("players.yml"), "UTF-8");
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        playerscfg.setDefaults(defConfig);
	        playerscfg.options().copyDefaults(true);
	        this.savePlayers();
	    }
		
	}
	
	public FileConfiguration getPlayers() {
		return playerscfg;
	}
	
	public void savePlayers() {
		try {
			playerscfg.save(playersfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public void reloadPlayers() {
		playerscfg = YamlConfiguration.loadConfiguration(playersfile);
	}
	
	
	public void setupChatRoomsConfig() {
		
		if (!(plugin.getDataFolder().exists())) {
			plugin.getDataFolder().mkdir();
			
		}
		
		chatroomsfile = new File(plugin.getDataFolder(), "chatrooms.yml");
		
		if(!(chatroomsfile.exists())) {
			try {
				chatroomsfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}
		
		chatroomscfg = YamlConfiguration.loadConfiguration(chatroomsfile);
		Reader defConfigStream = null;
	    try {
	        defConfigStream = new InputStreamReader(plugin.getResource("chatrooms.yml"), "UTF-8");
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        chatroomscfg.setDefaults(defConfig);
	        chatroomscfg.options().copyDefaults(true);
	        this.saveChatRoomsConfig();
	    }
		
	}
	
	public FileConfiguration getChatRoomsConfig() {
		return chatroomscfg;
	}
	
	public void saveChatRoomsConfig() {
		try {
			chatroomscfg.save(chatroomsfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public void reloadChatRoomsConfig() {
		chatroomscfg = YamlConfiguration.loadConfiguration(chatroomsfile);
	}
	
}
