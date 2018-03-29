package tk.rht0910.cheateye.executor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.rht0910.cheateye.Data;
import tk.rht0910.cheateye.thread.WaitEvent;
import tk.rht0910.cheateye.util.Utils;

public class CheatEyeRootCommand implements CommandExecutor {

	private char altColorChar = '&';

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			if(!sender.isOp()) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes(altColorChar, "&cYou are not a operator!"));
				return false;
			}
		}
		if(args[0].equalsIgnoreCase("initmcbans")) {
			sender.sendMessage(ChatColor.GREEN + "Initializing MCBans");
			Data data = new Data();
			data.getAPI();
			sender.sendMessage(ChatColor.GREEN + "Maybe MCBans initialized!(See console)");
		} else if(args[0].equalsIgnoreCase("checkmcbans")) {
			String message = "";
			if(Utils.isMCBansAvailable()) {
				message = ChatColor.GREEN + "MCBans is available!";
			} else {
				message = ChatColor.RED + "MCBans is not initialized. Initialize -> /cheateye initmcbans";
			}
			sender.sendMessage(message);
		} else if(args[0].equalsIgnoreCase("resetcounter")) {
			sender.sendMessage(ChatColor.GREEN + "Counter will be reset to 0");
			WaitEvent.i = 0;
			sender.sendMessage(ChatColor.GREEN + "Counter reset: 0");
		} else if(args[0].equalsIgnoreCase("clearLog")) {
			URL url = null;
			try {
				url = new URL("https://api.rht0910.tk/cheateye/v1/clear");
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection) url.openConnection();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			conn.setAllowUserInteraction(false);
			conn.setInstanceFollowRedirects(true);
			try {
				conn.setRequestMethod("GET");
			} catch (ProtocolException e) {
				e.printStackTrace();
			}
			try {
				conn.connect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			sender.sendMessage(ChatColor.GREEN + "Commands:");
			sender.sendMessage(ChatColor.BLUE + " - /ce initmcbans");
			sender.sendMessage(ChatColor.BLUE + " - /ce checkmcbans");
			sender.sendMessage(ChatColor.BLUE + " - /ce resetcounter");
			sender.sendMessage(ChatColor.BLUE + " - /ce clearLog");
		}
		return true;
	}

}
