package tk.rht0910.cheateye.thread;

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

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import tk.rht0910.cheateye.Data;
import tk.rht0910.cheateye.util.ConfigUtil;
import tk.rht0910.cheateye.util.Utils;
import tk.rht0910.tomeito_core.utils.Log;

public class WaitEvent extends Thread {
	public static int i = 0;

	@Override
	public void run() {
		Path dir = Paths.get(new File(ConfigUtil.load("watchDir", "/var/www/api/cheateye/v1/logs/").toString()).toURI());
		WatchService watcher = null;
		WatchKey watchkey = null;
		String filedata;
		String[] filedata2;
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
				Log.info("Event hooked");
				if(event1.kind() == StandardWatchEventKinds.OVERFLOW) continue;
				Path name = null;
				name = (Path) event1.context();
				if(event1.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
					File file = new File(ConfigUtil.load("watchDir", "/var/www/api/cheateye/v1/logs/").toString() + name.toFile());
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
						filedata = sb.toString().replaceAll("_", " ");
						filedata2 = filedata.split("&");
						data = filedata2[0].split("@");
						String data1 = data[i];
						String data2 = data[i+1];
						new WaitEvent().start();
						for(Player p : Bukkit.getOnlinePlayers()) {
							if(p.isOp()) {
								String id = getID(data2);
								if(id != "") {
									i++;
									i++;
										p.sendMessage(ChatColor.RED + String.format("Identified a cheater(hack or using illegally tool) suspect person: %s [ %s ], Message: %s", id, data2, data1));
										p.sendMessage(ChatColor.RED + String.format("%s [ %s ] はチーター(ハック、もしくは不正ツールの使用)の疑いがあります。メッセージ: %s", id, data2, data1));
										Log.error("Cheater: " + id + ", Message: " + data1);
									if(p.getName() != ConfigUtil.load("excludePlayer", "land_crasher")) {
										if(Utils.isMCBansAvailable()) {
											Data.mcbansapi.localBan(id, Bukkit.getPlayer(id).getUniqueId().toString(), "land_crasher", Bukkit.getPlayer("land_crasher").getUniqueId().toString(), data1);
										} else {
											Bukkit.getBanList(Type.NAME).addBan(id, data1, null, "land_crasher");
										}
										Log.info("Banned: " + id);
										Bukkit.broadcastMessage(ChatColor.GRAY + id + "(" + data2 + ") has banned(ID) by CheatEye.");
										break;
									}
								} else {
									i++;
									i++;
									p.sendMessage(ChatColor.RED + String.format("Identified a cheater(hack or using illegally tool) suspect person: %s , Message: %s", data2, data1));
									p.sendMessage(ChatColor.RED + String.format("%s はチーター(ハック、もしくは不正ツールの使用)の疑いがあります。メッセージ: %s", data2, data1));
									Log.error("Cheater: " + data2 + ", Message: " + data1);
									if(p.getName() != ConfigUtil.load("excludePlayer", "land_crasher")) {
										Bukkit.getBanList(Type.IP).addBan(data2, data1, null, "land_crasher");
										Log.info("Banned: " + data2);
										for(Player allp : Bukkit.getOnlinePlayers()) {
											allp.sendMessage(ChatColor.GRAY + data2 + " has banned by CheatEye.");
										}
									}
								}
							}
						}
						//URL url = new URL("https://api.rht0910.tk/cheateye/v1/clear");
						//HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						//conn.setAllowUserInteraction(false);
						//conn.setInstanceFollowRedirects(true);
						//conn.setRequestMethod("GET");
						//conn.connect();
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}
				}
				watchkey.reset();
			}
		}
	}

	public String getID(String ipaddress) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			Log.info(p.getName() + ": " + p.getAddress().getAddress().toString().replaceFirst("/", "") + ", selected: " + ipaddress);
			if(p.getAddress().getAddress().toString().replaceFirst("/", "") == ipaddress) {
				Log.info("Selected: " + p.getName());
				return p.getName();
			}
		}
		/*Log.info("Searching for offline players");
		//for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
		List<String> ips = new ArrayList<String>();
		List<String> players = new ArrayList<String>();
		try {
			ips = ConfigUtil.loadList("ips");
		} catch(Throwable e){ Log.error("Can't get ips!");}
			try{players = ConfigUtil.loadList("players");}catch(Throwable e){Log.error("Can't get players!");}
			try {
			for(int c=0;c<=players.size();i++) {
				if(ipaddress == ips.toArray()[c].toString()) {
					return players.toArray()[c].toString();
				}
			}
			} catch(Throwable e){}
		//}*/
		return "";
	}
}
