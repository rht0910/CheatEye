package tk.rht0910.cheateye;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.mcbans.firestar.mcbans.MCBans;
import com.mcbans.firestar.mcbans.api.MCBansAPI;

import tk.rht0910.tomeito_core.utils.Log;

public class Data {
	public static MCBansAPI mcbansapi;
	private static Object[] badword_kick = {};
	private static Object[] badword_ban3left = {};
	private static Object[] badword_ban2left = {};
	private static Object[] badword_ban1left = {};

	public void getAPI(){
		try {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("MCBans");
		if (plugin == null){
			Log.warn("MCBans not found. You can initialize in '/cheateye initmcbans' when MCBans loaded.");
		}else{
			mcbansapi = ((MCBans) plugin).getAPI(plugin);
			Log.info("MCBans Initialized.");
		}
		} catch(Throwable e) { /* Ignore it */ }
	}

	public static void bwkickcount_add(String e) {
		ArrayList<Object> aslist = new ArrayList<>(Arrays.asList(badword_kick));
		aslist.add(new ArrayList<>(Arrays.asList(e)));
		badword_kick = Arrays.asList(aslist).toArray();
	}

	public static boolean searchbwkick(String s) {
		Log.info("Searched for kick lists.");
		for(Object e : Data.badword_kick) {
			String m = ""; try { m = e.toString(); }catch(Throwable ignored){}
			Log.info(" * " + m);
			if(m.contains(s)) {
				return true;
			}
		}
		return Arrays.asList(badword_kick).contains(s);
	}

	public static void bwban3count_add(String e) {
		ArrayList<Object> aslist = new ArrayList<>(Arrays.asList(badword_ban3left));
		aslist.add(e);
		badword_ban3left = Arrays.asList(aslist).toArray();
	}

	public static boolean searchbwban3(String s) {
		Log.info("Searched for kick lists.");
		for(Object e : Data.badword_ban3left) {
			String m = ""; try { m = e.toString(); }catch(Throwable ignored){}
			Log.info(" * " + m);
			if(m.contains(s)) {
				return true;
			}
		}
		return Arrays.asList(badword_ban3left).contains(s);
	}

	public static void bwban2count_add(String e) {
		ArrayList<Object> aslist = new ArrayList<>(Arrays.asList(badword_ban2left));
		aslist.add(e);
		badword_ban2left = Arrays.asList(aslist).toArray();
	}

	public static boolean searchbwban2(String s) {
		Log.info("Searched for kick lists.");
		for(Object e : Data.badword_ban2left) {
			String m = ""; try { m = e.toString(); }catch(Throwable ignored){}
			Log.info(" * " + m);
			if(m.contains(s)) {
				return true;
			}
		}
		return Arrays.asList(badword_ban2left).contains(s);
	}

	public static void bwban1count_add(String e) {
		ArrayList<Object> aslist = new ArrayList<>(Arrays.asList(badword_ban1left));
		aslist.add(e);
		badword_ban1left = Arrays.asList(aslist).toArray();
	}

	public static boolean searchbwban1(String s) {
		Log.info("Searched for kick lists.");
		for(Object e : Data.badword_ban1left) {
			String m = ""; try { m = e.toString(); }catch(Throwable ignored){}
			Log.info(" * " + m);
			if(m.contains(s)) {
				return true;
			}
		}
		return Arrays.asList(badword_ban1left).contains(s);
	}
}
