package tk.rht0910.cheateye.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tk.rht0910.cheateye.CheatEye;

public class ConfigUtil {
	public static Object load(String path, String def) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File(CheatEye.getPlugin(CheatEye.class).getDataFolder(), "config.yml"));
		return config.get(path, def);
	}

	public static List<String> loadList(String path) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File(CheatEye.getPlugin(CheatEye.class).getDataFolder(), "config.yml"));
		List<String> list = new ArrayList<String>();
		for(Object obj : config.getList(path).toArray()){
			list.add(obj.toString());
		}
		return list;
	}

	public static boolean set(String path, Object obj) {
		CheatEye.getPlugin(CheatEye.class).getConfig().set(path, obj);
		return true;
	}

	public static boolean setList(String path, List<String> list) {
		try {
			CheatEye.getPlugin(CheatEye.class).getConfig().set(path, list);
			return true;
		} catch(Throwable e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean addToList(String path, String value) {
		try {
			List<String> configList = ConfigUtil.loadList(path);
			if(configList.contains(value)) {
				configList.remove(value);
			}
			configList.add(value);
			CheatEye.getPlugin(CheatEye.class).getConfig().set(path, configList);
			return true;
		} catch(Throwable e) {
			e.printStackTrace();
			return false;
		}
	}
}
