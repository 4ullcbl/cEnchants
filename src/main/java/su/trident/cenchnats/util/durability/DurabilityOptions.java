package su.trident.cenchnats.util.durability;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class DurabilityOptions
{

    public static void damageItem(@NotNull ItemStack item, int damage)
    {
        if (!isDamageable(item)) return;

        final Damageable damageableMeta = (Damageable) item.getItemMeta();

        damageableMeta.setDamage(damageableMeta.getDamage() + damage);

        if (damageableMeta.getDamage() >= item.getType().getMaxDurability()) {
            damageableMeta.setDamage(item.getType().getMaxDurability());
            item.setAmount(0);
        } else {
            item.setItemMeta((ItemMeta) damageableMeta);
        }
    }

    public static void damageItem(Player player, @NotNull ItemStack item, int damage)
    {
        if (!isDamageable(item)) return;

        final PlayerItemDamageEvent event = new PlayerItemDamageEvent(player, item, damage);
        event.callEvent();

        if (event.isCancelled()) return;

        final Damageable damageableMeta = (Damageable) item.getItemMeta();

        int newDamage = damageableMeta.getDamage() + damage;
        damageableMeta.setDamage(newDamage);

        if (newDamage >= item.getType().getMaxDurability()) {
            item.setAmount(0);
            player.playSound(player.getEyeLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
        } else {
            item.setItemMeta((ItemMeta) damageableMeta);
        }
    }


    public static void damageNoBreak(@NotNull ItemStack item, int damage)
    {
        if (isDamageable(item)) return;

        final Damageable damageableMeta = (Damageable) item.getItemMeta();

        damageableMeta.setDamage(damageableMeta.getDamage() + damage);

        item.setItemMeta((ItemMeta) damageableMeta);
    }

    public static boolean isDamageable(@NotNull ItemStack item)
    {
        if (item.getItemMeta() == null)
            return false;

        if (item.getItemMeta().isUnbreakable())
            return false;

        return item.getItemMeta() instanceof Damageable;
    }

    public static boolean isLowDurability(ItemStack item)
    {
        if (item == null || !item.getType().isItem() || !(item.getType().getMaxDurability() > 0)) {
            return false;
        }

        short maxDurability = item.getType().getMaxDurability();
        short currentDamage = item.getDurability();
        short remainingDurability = (short) (maxDurability - currentDamage);

        double durabilityPercentage = ((double) remainingDurability / maxDurability) * 100;
        return durabilityPercentage <= 20;
    }

    public static boolean isLowDurability(ItemStack item, double percent)
    {
        if (item == null || !item.getType().isItem() || !(item.getType().getMaxDurability() > 0)) {
            return false;
        }

        short maxDurability = item.getType().getMaxDurability();
        short currentDamage = item.getDurability();
        short remainingDurability = (short) (maxDurability - currentDamage);

        double durabilityPercentage = ((double) remainingDurability / maxDurability) * 100;
        return durabilityPercentage <= percent;
    }
}
