package tk.rht0910.cheateye.executor;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.rht0910.cheateye.Data;
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
		}
		return true;
	}

}
