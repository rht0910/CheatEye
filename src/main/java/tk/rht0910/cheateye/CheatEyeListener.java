package tk.rht0910.cheateye;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import tk.rht0910.cheateye.util.ConfigUtil;
import tk.rht0910.cheateye.util.Utils;

public class CheatEyeListener implements Listener {
	public void kickPlayer(Player p, String r) {
		final Player player = p;
		final String reason = r;
		Bukkit.getScheduler().runTask(CheatEye.getPlugin(CheatEye.class), new Runnable() {
			public void run() {
				player.kickPlayer(reason);
			}
		});
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public boolean onPlayerChat(AsyncPlayerChatEvent event) {
		String m = event.getMessage();
		Player p = event.getPlayer();
		if(m.contains("fuck") || m.contains("f*ck")) {
			if(Data.searchbwkick(p.getName())) { // Is this a kick target?
				if(Data.searchbwban3(p.getName())) { // Is this a 3 left?
					if(Data.searchbwban2(p.getName())) { // Is this a 2 left?
						if(Data.searchbwban1(p.getName())) { // Is this a 1 left?
							m.replaceAll("fuck", "f*ck");
							if(Utils.isMCBansAvailable()) {
								Data.mcbansapi.localBan(p.getName(), p.getUniqueId().toString(), "Land_Crasher", "1865ab8c-700b-478b-9b52-a8c58739df1a", "bad language[auto] appeals in https://asyn.cf/appeal");
							} else {
								Bukkit.getBanList(Type.NAME).addBan(p.getName(), "bad language[auto] appeals in https://asyn.cf/appeal", null, null);
							}
							Bukkit.broadcastMessage(ChatColor.GRAY + p.getName() + " has banned by CheatEye.");
						} else { // No, kick
							m.replaceAll("fuck", "f*ck");
							kickPlayer(p, "You said with bad words. You will be banned when say again with bad words(1 left)"); // : asynchronous player kick!
							Data.bwban1count_add(p.getName());
							Bukkit.broadcastMessage(ChatColor.GRAY + p.getName() + " has kicked by CheatEye.");
						}
					} else { // No, kick.
						m.replaceAll("fuck", "f*ck");
						kickPlayer(p, "You said with bad words. You will be banned when say again with bad words(2 left)"); // Because it throws IllegalStateException
						Data.bwban2count_add(p.getName());
						Bukkit.broadcastMessage(ChatColor.GRAY + p.getName() + " has kicked by CheatEye.");
					}
				} else{ // No, kick.
					m.replaceAll("fuck", "f*ck");
					kickPlayer(p, "You said with bad words. You will be banned when say again with bad words(3 left)."); // Kick player in not async.
					Data.bwban3count_add(p.getName());
					Bukkit.broadcastMessage(ChatColor.GRAY + p.getName() + " has kicked by CheatEye.");
				}
			} else {
				m.replaceAll("fuck", "f*ck");
				p.sendMessage("You said with bad words. You will be kicked when say again with bad words.");
				Data.bwkickcount_add(p.getName());
			}
		}
		return true;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public boolean onPlayerJoin(PlayerJoinEvent e) {
		e.setJoinMessage(ChatColor.GRAY + e.getPlayer().getName() + " has joined the server");
		return true;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public boolean onPlayerQuit(PlayerQuitEvent e) {
		ConfigUtil.addToList("players", e.getPlayer().getName());
		ConfigUtil.addToList("ips", e.getPlayer().getAddress().getAddress().toString());
		e.setQuitMessage(ChatColor.GRAY + e.getPlayer().getName() + " has left the server");
		return true;
	}

	@EventHandler
	public boolean onPlayerLocaleChange(PlayerLocaleChangeEvent e) {
		if(e.getPlayer().isBlocking() || e.getPlayer().isSneaking() || e.getPlayer().isSprinting()) {
			if(Utils.isMCBansAvailable()) {
				Data.mcbansapi.kick(e.getPlayer().getName(), e.getPlayer().getUniqueId().toString(), "Land_Crasher", "1865ab8c-700b-478b-9b62-a8c58739df1a", ChatColor.RED + "Invalid action detected.");
			} else {
				e.getPlayer().kickPlayer(ChatColor.RED + "Invalid action detected.");
			}
			Bukkit.broadcastMessage(ChatColor.GRAY + e.getPlayer().getName() + " has kicked by CheatEye.");
		}
		return true;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(event.getPlayer().isSleeping()) {
			if(Utils.isMCBansAvailable()) {
				Data.mcbansapi.kick(event.getPlayer().getName(), event.getPlayer().getUniqueId().toString(), "Land_Crasher", "1865ab8c-700b-478b-9b62-a8c58739df1a", ChatColor.RED + "Invalid moving detected.");
			} else{
				event.getPlayer().kickPlayer(ChatColor.RED + "Invalid moving detected.");
			}
			Bukkit.broadcastMessage(ChatColor.GRAY + event.getPlayer().getName() + " has kicked by CheatEye.");
		}
	}
}
