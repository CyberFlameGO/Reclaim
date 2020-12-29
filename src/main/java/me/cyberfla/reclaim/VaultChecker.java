package net.cyberflame.reclaim;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultChecker {
	private static Permission permission;
	
	public static void checkVault() {
		if (Bukkit.getPluginManager().getPlugin("Vault").isEnabled()) {
			Bukkit.getConsoleSender().sendMessage("[Reclaim] §cVault is not enabled or installed. Aborting...");
			Bukkit.getPluginManager().disablePlugin((Plugin)Main.getPlugin());
		}
	}
	public static void setupVault() {
		RegisteredServiceProvider<Permission> rsp = Bukkit.getServicesManager().getRegistration(Permission.class);
		if (rsp != null) {
			permission = (Permission)rsp.getProvider();
		} else {
			Bukkit.getConsoleSender().sendMessage("[Reclaim] §cAn error occured whilst registering permissions with Vault...");
			checkVault();
			return;
		}
		Bukkit.getConsoleSender().sendMessage("[Reclaim] Successfully hooked into Vault.");
	}
	
	public static void unregisterVault() {
		if (permission != null)
			permission = null; 
	}
	
	public static Permission getVaultPermission() {
		if (permission != null)
			return permission;
		checkVault();
		return permission;
	}
}
