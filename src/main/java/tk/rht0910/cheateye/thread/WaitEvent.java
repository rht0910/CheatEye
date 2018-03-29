package tk.rht0910.cheateye.thread;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import tk.rht0910.cheateye.util.ConfigUtil;

public class WaitEvent extends Thread {
	@Override
	public void run() {
		Path dir = Paths.get(new File(ConfigUtil.load("watchDir", "/var/www/api/cheateye/v1/logs/").toString()).toURI());
		WatchService watcher = null;
		WatchKey watchkey = null;
		String filedata;
		String[] filedata2;
		String[] data;
		int i = 0;
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
						filedata = sb.toString().replaceAll("\n", "").toString().replaceAll("_", " ");
						filedata2 = filedata.split("&");
						data = filedata2[i].split("@");
						for(Player p : Bukkit.getOnlinePlayers()) {
							if(p.isOp()) {
								p.sendMessage(ChatColor.RED + String.format("Identified a cheater(hack or using illegally tool) suspect person: %s, Message: %s", data[1], data[0]));
								p.sendMessage(ChatColor.RED + String.format("%s はチーター(ハック、もしくは不正ツールの使用)の疑いがあります。メッセージ: %s", data[1], data[0]));
							}
						}
						URL url = new URL("https://api.rht0910.tk/cheateye/v1/clear");
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setAllowUserInteraction(false);
						conn.setInstanceFollowRedirects(true);
						conn.setRequestMethod("GET");
						conn.connect();
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}
				}
				watchkey.reset();
				i++;
				continue;
			}
		}
	}
}