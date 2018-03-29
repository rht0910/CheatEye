package tk.rht0910.cheateye.util;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tk.rht0910.cheateye.CheatEye;

public class ConfigUtil {
	public static Object load(String path, String def) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File(CheatEye.getPlugin(CheatEye.class).getDataFolder(), "config.yml"));
		return config.get(path, def);
	}

	public static boolean set(String path, Object obj) {
		CheatEye.getPlugin(CheatEye.class).getConfig().set(path, obj);
		return true;
	}
}
