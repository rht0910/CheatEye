package tk.rht0910.cheateye.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public class Utils {
    public static boolean isFood(Material m) {
    	return (m == Material.COOKED_BEEF || m == Material.COOKED_CHICKEN || m == Material.COOKED_FISH
    			|| m == Material.COOKED_MUTTON || m == Material.COOKED_RABBIT || m == Material.GRILLED_PORK
    			|| m == Material.GOLDEN_APPLE || m == Material.RAW_BEEF || m == Material.RAW_CHICKEN
    			|| m == Material.RAW_FISH || m == Material.ROTTEN_FLESH || m == Material.POTATO_ITEM
    			|| m == Material.BAKED_POTATO || m == Material.POISONOUS_POTATO || m == Material.MELON
    			|| m == Material.PORK || m == Material.APPLE || m == Material.BREAD || m == Material.COOKIE
    			|| m == Material.MUSHROOM_SOUP || m == Material.SPIDER_EYE || m == Material.RABBIT_STEW
    			|| m == Material.MILK_BUCKET || m == Material.CHORUS_FRUIT || m == Material.GOLDEN_CARROT
    			|| m == Material.PUMPKIN_PIE );
    }

    /*public static long calcSurvivalFastBreak(ItemStack tool, Material block) {
        if (isInstantBreak(block) || (tool.getType() == Material.SHEARS && block == Material.LEAVES)) {
            return 0;
        }
        double bhardness = BlockHardness.getBlockHardness(block);
        double thardness = ToolHardness.getToolHardness(tool.getType());
        long enchantlvl = (long) tool.getEnchantmentLevel(Enchantment.DIG_SPEED);

        long result = Math.round((bhardness * thardness) * 0.10 * 10000);

        if (enchantlvl > 0) {
            result /= enchantlvl * enchantlvl + 1L;
        }

        result = result > 25000 ? 25000 : result < 0 ? 0 : result;

        if (isQuickCombo(tool, block)) {
            result = result / 2;
        }

        return result;
    }

    private static boolean isQuickCombo(ItemStack tool, Material m) {

        if (tool.getType() == Material.DIAMOND_SWORD || tool.getType() == Material.IRON_SWORD || tool.getType() == Material.STONE_SWORD || tool.getType() == Material.GOLD_SWORD || tool.getType() == Material.WOOD_SWORD) {

            return m == Material.WEB;

        } else if (tool.getType() == Material.SHEARS) {

            return m == Material.WOOL;

        }

        return false;

    }*/

    public static boolean isInstantBreak(Material m) {

        return m == Material.TORCH || m == Material.FLOWER_POT || m == Material.RED_ROSE || m == Material.YELLOW_FLOWER || m == Material.LONG_GRASS
                || m == Material.RED_MUSHROOM || m == Material.BROWN_MUSHROOM || m == Material.TRIPWIRE || m == Material.TRIPWIRE_HOOK ||
                m == Material.DEAD_BUSH || m == Material.DIODE_BLOCK_OFF || m == Material.DIODE_BLOCK_ON || m == Material.REDSTONE_COMPARATOR_OFF
                || m == Material.REDSTONE_COMPARATOR_OFF || m == Material.REDSTONE_WIRE || m == Material.REDSTONE_TORCH_OFF ||
                m == Material.REDSTONE_TORCH_ON || m == Material.DOUBLE_PLANT || m == Material.SUGAR_CANE_BLOCK;
    }

    public static boolean isMCBansAvailable() {
    	return Bukkit.getServer().getPluginManager().getPlugin("MCBans") != null;
    }
}
