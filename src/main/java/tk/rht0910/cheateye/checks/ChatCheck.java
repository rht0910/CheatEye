package tk.rht0910.cheateye.checks;

import org.bukkit.entity.Player;

public class ChatCheck {
	public int runChatChecks(Player p, String message) {
		if(p.isSneaking()) {
			return 1;
		}
		return 0;
	}
}
