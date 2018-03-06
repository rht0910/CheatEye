package tk.rht0910.cheateye;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import tk.rht0910.cheateye.executor.CheatEyeRootCommand;
import tk.rht0910.tomeito_core.utils.Log;

public class CheatEye extends JavaPlugin implements TabCompleter {
	@Override
	public void onEnable() {
		try {
			Log.color = ChatColor.GOLD;
			Log.info("Starting CheatEye");
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
}