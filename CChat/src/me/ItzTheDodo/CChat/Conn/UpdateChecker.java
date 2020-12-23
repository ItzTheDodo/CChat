package me.ItzTheDodo.CChat.Conn;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

import org.bukkit.Bukkit;

import me.ItzTheDodo.CChat.CChat;

public class UpdateChecker {
	
	private CChat plugin;
	private int resourceID;
	
	public UpdateChecker(CChat pl, int resID) {
		plugin = pl;
		resourceID = resID;
	}
	
	public void getVersion(final Consumer<String> consumer) {
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
			try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceID).openStream(); Scanner scanner = new Scanner(inputStream)) {
				if (scanner.hasNext()) {
					consumer.accept(scanner.next());
				}
			} catch (IOException e) {
				this.plugin.logEvent("Error while looking for updates: " + e.getMessage());
			}
		});
	}

}
