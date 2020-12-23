package me.ItzTheDodo.CChat;

import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.ItzTheDodo.CChat.ChatRooms.ChatRoom;
import me.ItzTheDodo.CChat.ChatRooms.DefaultRoomsSetup;
import me.ItzTheDodo.CChat.Commands.BroadcastCommand;
import me.ItzTheDodo.CChat.Commands.CChatCommand;
import me.ItzTheDodo.CChat.Commands.IgnoreCommand;
import me.ItzTheDodo.CChat.Commands.MsgCommand;
import me.ItzTheDodo.CChat.Commands.MuteCommand;
import me.ItzTheDodo.CChat.Commands.NickCommand;
import me.ItzTheDodo.CChat.Commands.PrivateChatCommand;
import me.ItzTheDodo.CChat.Commands.ReportCommand;
import me.ItzTheDodo.CChat.Commands.SwitchChatCommand;
import me.ItzTheDodo.CChat.Conn.UpdateChecker;
import me.ItzTheDodo.CChat.Conn.Vault;
import me.ItzTheDodo.CChat.Events.OnChat;
import me.ItzTheDodo.CChat.Events.PlayerJoin;
import me.ItzTheDodo.CChat.Events.PlayerKick;
import me.ItzTheDodo.CChat.Events.PlayerQuit;
import me.ItzTheDodo.CChat.api.Time;

public class CChat extends JavaPlugin {
	
	public final static String AUTHOR = "ItzTheDodo";
	private static final Logger log = Logger.getLogger("Minecraft");
	public boolean setup = true;
	public boolean res, rd, ch;
	public ConfigManager cfgm;
	public Vault vault;
	
	// TODO: chat games
	
	@Override
	public void onEnable() {
		  
		this.logEvent("CChat Plugin Enabled!");
		this.logEvent("Plugin By: ItzTheDodo");
		
		setup = isInitEnable();
		if (!setup) {
			this.getDataFolder().mkdir();
			this.logEvent("Seeing as this is the first run for my plugin, could you go through the config and customise the plugin to make it better for your needs and make your server a happier, healthier place!");
			this.logEvent("I am very happy that you chose this plugin and I hope it is sufficiant for you");
		}
		setupConfig();
		
		if (this.getConfig().getConfigurationSection("Connections").getBoolean("Vault")) {
			vault = new Vault(this);
		} else {
			logEvent("Vault not true in config");
			this.getConfig().set("Enable.Rank-Display", false);
			this.saveConfig();
			return;
		}
		
		registerEvents();
		loadConfig();
		registerCommands();
		
		for (String cs : this.cfgm.getChatRoomsConfig().getKeys(false)) {
			PrivateChatCommand.chatrooms.add(ChatRoom.getChatRoom(this, cs));
		}
		
		if (!setup) {
			DefaultRoomsSetup dr = new DefaultRoomsSetup(this);
			dr.setupDefaultRooms();
		}
		
		new UpdateChecker(this, 12345).getVersion(version -> {
			if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
				this.logEvent("Plugin up to date");
			} else {
				this.logEvent("There is a new update for cchat available");
			}
		});
		
	}
	
	@Override
	public void onDisable() {
		logEvent("CChat Disabling!");
		log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
	}
	
	public void logEvent(String log) {
		Bukkit.getServer().getLogger().info("[CChat] " + log);
	}
	
	public String sendItem(String msg) {
		return "[CChat] " + msg;
	}
	
	private boolean isInitEnable() {
		if (this.getDataFolder().exists()) {
			return true;
		}
		return false;
	}
	
	private void registerCommands() {
		new NickCommand(this);
		new CChatCommand(this);
		new MsgCommand(this);
		new IgnoreCommand(this);
		new MuteCommand(this);
		new BroadcastCommand(this);
		new PrivateChatCommand(this);
		new SwitchChatCommand(this);
		new ReportCommand(this);
	}
	
	private void setupConfig() {
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		cfgm = new ConfigManager(this);
		cfgm.setupPlayers();
		cfgm.reloadPlayers();
		cfgm.savePlayers();
		cfgm.setupChatRoomsConfig();
		cfgm.reloadChatRoomsConfig();
		cfgm.saveChatRoomsConfig();
	}
	
	public void loadConfig() {
		res = this.getConfig().getConfigurationSection("Enable").getBoolean("Restrictions");
		rd = this.getConfig().getConfigurationSection("Enable").getBoolean("Rank-Display");
		ch = this.getConfig().getConfigurationSection("Enable").getBoolean("Chat-History");
	}
	
	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new OnChat(this), this);
		pm.registerEvents(new PlayerJoin(this), this);
		pm.registerEvents(new PlayerQuit(this), this);
		pm.registerEvents(new PlayerKick(this), this);
	}
	
	public void asyncUpdate() {
		for (String str_UUID : cfgm.getPlayers().getKeys(false)) {
			OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(UUID.fromString(str_UUID));
			if (p.isBanned() && this.getConfig().getBoolean("Restrictions.Ban-Refresh")) {
				cfgm.getPlayers().set(str_UUID, null);
			}
			
			if (this.cfgm.getPlayers().getBoolean(p.getUniqueId().toString() + ".ismuted")) {
				Time dayofmute = Time.parse(this.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutedate"));
				dayofmute.addWithTime(Time.parse(this.cfgm.getPlayers().getString(p.getUniqueId().toString() + ".mutetime")));
				Time timeleft = Time.parse(dayofmute.compareWithTime(Time.dateToTime(Time.getCurrentTime())));
				if (timeleft.getSec() <= 0 && timeleft.getMin() <= 0 && timeleft.getHour() <= 0 && timeleft.getDay() <= 0 && timeleft.getMonth() <= 0 && timeleft.getYear() <= 0) {
					this.cfgm.getPlayers().set(p.getUniqueId().toString() + ".ismuted", false);
					this.cfgm.getPlayers().set(p.getUniqueId().toString() + ".mutetime", "None");
					this.cfgm.getPlayers().set(p.getUniqueId().toString() + ".mutedate", "None");
				}
			}
		}
	
	}
	
}
