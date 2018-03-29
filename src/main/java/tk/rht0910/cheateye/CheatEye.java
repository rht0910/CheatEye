package tk.rht0910.cheateye;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import tk.rht0910.cheateye.executor.CheatEyeRootCommand;
import tk.rht0910.cheateye.util.ConfigUtil;
import tk.rht0910.tomeito_core.utils.Log;

public class CheatEye extends JavaPlugin implements TabCompleter {
	@Override
	public void onEnable() {
		try {
			Log.color = ChatColor.GOLD;
			Log.info("Starting CheatEye");

			this.getConfig().options().copyDefaults(true);
			this.saveConfig();
			long startTime = System.nanoTime();
			if(this.getServer().getAllowFlight()) {
				Log.warn("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
				Log.warn("Allowed flight in server.properties(allow-flight: true)!");
				Log.warn("For best performance, please set it to false.");
				Log.warn("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
			}
			Log.info("Events Registering");
			try {
				Bukkit.getPluginManager().registerEvents(new CheatEyeListener(), this);
			} catch(Throwable e) {
				Log.error("An error occurred during registering events.");
				Log.error("Stack trace dumped below:");
				e.printStackTrace();
				Log.error("Caused by:");
				e.getCause().printStackTrace();
			}
			Log.info("Commands Registering");
			try {
				Bukkit.getPluginCommand("ce").setExecutor(new CheatEyeRootCommand());
				Bukkit.getPluginCommand("cheateye").setExecutor(new CheatEyeRootCommand());
			} catch(Throwable e) {
				Log.error("An error occurred during registering commands.");
				Log.error("Stack trace dumped below:");
				e.printStackTrace();
				Log.error("Caused by");
				e.getCause().printStackTrace();
				Log.error("Failed to register commands.");
				Log.error("CheatEye is may not be stable!");
			}
			Log.info("Initializing MCBans");
			Data data = new Data();
			data.getAPI();
			long estimatedTime = System.nanoTime() - startTime;
			Log.info("CheatEye v1.0 has fully startup @" + TimeUnit.MILLISECONDS.convert(estimatedTime, TimeUnit.NANOSECONDS) + "ms");
			waitEvent();
		} catch(Throwable e) {
			Log.error("Please contact to the developers!");
			Log.error("Stack trace dumped below:");
			e.printStackTrace();
			Log.error("Caused by:");
			e.getCause().printStackTrace();
			Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(this.getName()));
			Log.info("CheatEye is disabled.");
			// Disable Plugin When failed loading
		}
	}

	@Override
	public void onDisable() {
		Log.info("Disabling CheatEye");
		HandlerList.unregisterAll(Bukkit.getPluginManager().getPlugin(this.getName()));
		Log.info("Unregistered listeners");
		Log.info("Disabled CheatEye");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(!command.getName().equalsIgnoreCase("pman")) return super.onTabComplete(sender, command, alias, args);
		if(args.length == 1) {
			if(args[0].length() == 0) { // Until /cheateye
				// /cheateye <TAB>
				return Arrays.asList("initmcbans", "checkmcbans");
				} else {
				// Correct first input string

				// /cheateye <HERE>
				// Example: /cheateye [space] <TAB> -> /pman load (Auto tab completed)
				if ("initmcbans".startsWith(args[0])) {
					return Collections.singletonList("initmcbans");
				} else if ("checkmcbans".startsWith(args[0])) {
					return Collections.singletonList("checkmcbans");
				}
			}
		}
		return super.onTabComplete(sender, command, alias, args);
	}

	public void waitEvent() {
		Path dir = Paths.get(new File(ConfigUtil.load("watchDir", "/var/www/api/cheateye/v1/logs/").toString()).toURI());
		WatchService watcher = null;
		WatchKey watchkey = null;
		String filedata;
		String[] data;
		try {
			watcher = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			dir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(;;) {
			try {
				watchkey = watcher.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(WatchEvent<?> event1 : watchkey.pollEvents()) {
				if(event1.kind() == StandardWatchEventKinds.OVERFLOW) continue;
				Path name = null;
				name = (Path) event1.context();
				if(event1.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
					File file = name.toFile();
					FileReader fr = null;
					try {
						fr = new FileReader(file);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					int output;
					StringBuffer sb = new StringBuffer();
					try {
						while((output = fr.read()) != -1) {
							sb.append((char)output);
						}
						filedata = sb.toString();
						data = filedata.split("|");
						data[0] = data[0].replaceAll("_", " ");
						for(Player p : Bukkit.getOnlinePlayers()) {
							if(p.isOp()) {
								p.sendMessage(ChatColor.RED + String.format("Identified a cheater(hack or using illegally tool) suspect person: %s, Message: ", data[1], data[0]));
								p.sendMessage(ChatColor.RED + String.format("%s はチーター(ハック、もしくは不正ツールの使用)の疑いがあります。メッセージ: %s", data[1], data[0]));
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				watchkey.reset();
			}
		}
	}
}
