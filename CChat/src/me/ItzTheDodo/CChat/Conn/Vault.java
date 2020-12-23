package me.ItzTheDodo.CChat.Conn;

import org.bukkit.plugin.RegisteredServiceProvider;

import me.ItzTheDodo.CChat.CChat;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

public class Vault {
	
	private CChat plugin;
	
	public Vault(CChat pl) {
		this.plugin = pl;
		setupPermissions();
		setupChat();
	}
	
	public static Permission permission = null;
	public static Chat chat = null;
	
	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}
	
	private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp != null) {
        	chat = rsp.getProvider();
        }
        return (chat != null);
    }
	
	public Permission getPermissions() {
		return permission;
	}
	
	public Chat getChat() {
        return chat;
    }

}
