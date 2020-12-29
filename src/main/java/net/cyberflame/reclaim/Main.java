package net.cyberflame.reclaim;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private static Main plugin;
	
	List<String> used = new ArrayList<>();
	
	public static Main getPlugin() {
		return plugin;
	}
	
	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player)sender;
			FileConfiguration config = getConfig();
			String groupName = VaultChecker.getVaultPermission().getPlayerGroups(p)[0];
			String redeemedPlayers = "Reclaim.redeemedPlayers.";
			String groups = "Reclaim.groups.";
			if (commandLabel.equalsIgnoreCase("reclaim"))
				if (config.getString(String.valueOf(groups) + groupName) == null) {
					p.sendMessage(ChatColor.RED + "You dont have anything to reclaim!");
				} else {
					if (config.getStringList(String.valueOf(redeemedPlayers) + groupName) != null)
						this.used.addAll(config.getStringList(String.valueOf(redeemedPlayers) + groupName));
					if (this.used.contains(p.getName().toLowerCase())) {
						p.sendMessage(ChatColor.RED + "You have already reclaimed your rewards.");
					} else {
						this.used.add(p.getName().toLowerCase());
						config.set(String.valueOf(redeemedPlayers) + groupName, this.used);
						saveConfig();
						for (String reclaimCommand : config.getStringList(String.valueOf(groups) + groupName))
							Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), reclaimCommand.replace("{PLAYER}", p.getName()));
						if (config.getBoolean("Broadcast"))
							Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Message").replace("{PLAYER}", p.getName()).replace("{GROUP}", groupName)));
					}
				}
			if (commandLabel.equalsIgnoreCase("resetreclaim"))
				if (args.length == 0) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&m----------&6[&eReclaim&6]&6&m----------"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aDeveloper: &cCyberFlame"));
				} else if (args.length >= 1) {
					if (sender.hasPermission("reclaim.reload")) {
						if (args[0].equalsIgnoreCase("reload")) {
							reloadConfig();
							sender.sendMessage(ChatColor.GREEN + "You have reloaded the config.");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "You do not have permission to perform this action.");
					}
						}
					}
		return false;
  }
}
