package su.trident.cenchants.util.entity;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class PlayerUtil
{
    public static List<Player> getNearbyPlayers(Player player, double radius)
    {
        return player.getNearbyEntities(radius, radius, radius)
                .stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity).toList();
    }

    @Deprecated
    public static int getEmptySlot(Player player)
    {
        final Inventory inventory = player.getInventory();

        int slot = -1;

        for (int i = 0; i < inventory.getSize(); i++) {

            if (inventory.getItem(i) == null) {
                slot = i;
                break;
            }
            if (Objects.requireNonNull(inventory.getItem(i)).getType() == Material.AIR) {
                slot = i;
                break;
            }
        }

        return slot;
    }

    public static boolean hasEmptySlot(Player player) {
        final Inventory inventory = player.getInventory();

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null)
                return true;
            if (Objects.requireNonNull(inventory.getItem(i)).getType() == Material.AIR)
                return true;
        }

        return false;
    }


    public static ItemStack getItemMainHand(Player player)
    {
        return player.getInventory().getItemInMainHand();
    }

    public static ItemStack getItemInOfHand(Player player)
    {
        return player.getInventory().getItemInOffHand();
    }


}
