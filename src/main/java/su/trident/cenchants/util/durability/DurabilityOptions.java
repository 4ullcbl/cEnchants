package su.trident.cenchants.util.durability;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.trident.cenchants.enchant.EnchantmentTarget;

public class DurabilityOptions
{

    public static void damageItem(@NotNull ItemStack item, int damage)
    {
        if (!isDamageable(item)) return;

        final Damageable damageableMeta = (Damageable) item.getItemMeta();

        damageableMeta.setDamage(damageableMeta.getDamage() + damage);

        if (damageableMeta.getDamage() >= item.getType().getMaxDurability()) {
            damageableMeta.setDamage(item.getType().getMaxDurability());
            item.setType(Material.AIR);
        } else if (isDamageable(item)) {
            item.setItemMeta((ItemMeta) damageableMeta);
        }
    }

    public static void damageItem(Player player, @NotNull ItemStack item, int damage)
    {
        if (!(item.getItemMeta() instanceof Damageable damageableMeta)) return;

        if (!isDamageable(item)) return;
        if (item.getType() == Material.ENCHANTED_BOOK && !EnchantmentTarget.ALL.isTarget(item.getType())) return;

        final PlayerItemDamageEvent event = new PlayerItemDamageEvent(player, item, damage);
        event.callEvent();

        damage = getDamageConsideringEnchant(item, damage);

        if (event.isCancelled()) return;

        int newDamage = damageableMeta.getDamage() + damage;
        damageableMeta.setDamage(newDamage);

        if (newDamage >= item.getType().getMaxDurability() && isDamageable(item)) {
            item.setAmount(0);
            player.playSound(player.getEyeLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
        } else if (isDamageable(item)) {
            item.setItemMeta((ItemMeta) damageableMeta);
        }
    }

    private static int getDamageConsideringEnchant(@NotNull ItemStack item, int damage)
    {
        if (hasEnchantmentLevel(item, Enchantment.DURABILITY, 1)) {
            damage = (int) (damage / 1.5);
        } else if (hasEnchantmentLevel(item, Enchantment.DURABILITY, 2)) {
            damage = (int) (damage / 1.7);
        } else if (hasEnchantmentLevel(item, Enchantment.DURABILITY, 3)) {
            damage = damage / 2;
        } else if (hasEnchantmentLevel(item, Enchantment.DURABILITY, 4)) {
            damage = (int) (damage / 2.5);
        } else if (hasEnchantmentLevel(item, Enchantment.DURABILITY, 5)) {
            damage = (damage / 3);
        }

        return damage;
    }


    public static boolean hasEnchantmentLevel(ItemStack item, Enchantment enchantment, int level)
    {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        return item.getEnchantmentLevel(enchantment) == level;
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
