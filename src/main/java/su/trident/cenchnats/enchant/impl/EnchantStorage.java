package su.trident.cenchnats.enchant.impl;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import su.trident.cenchnats.enchant.api.Enchant;
import su.trident.cenchnats.enchant.api.EnchantStorageAPI;
import su.trident.cenchnats.util.numbers.NumberUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnchantStorage implements EnchantStorageAPI
{
    private final JavaPlugin plugin;

    public EnchantStorage(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void addEnchant(ItemStack stack, Enchant<?> enchant, int lvl)
    {
        safelyAddEnchant(stack, stack.getItemMeta(), enchant, lvl);
    }

    @Override
    public void addEnchantSave(ItemStack stack, Enchant<?> enchant, int lvl)
    {
        if (lvl > enchant.getMaxLvl() || lvl < enchant.getStartLvl()) return;
        if (stack.getItemMeta() == null) return;

        safelyAddEnchant(stack, stack.getItemMeta(), enchant, lvl);
    }

    @SuppressWarnings("deprecation")
    private void safelyAddEnchant(ItemStack clone, ItemMeta meta, Enchant enchant, int lvl)
    {
        if (hasEnchant(clone, enchant)) return;

        final List<String> lore = meta.getLore() == null || meta.getLore().isEmpty() ? new ArrayList<>() : meta.getLore();

        safeAddEnchantLore(enchant, lore, lvl);

        meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, enchant.getKey()), PersistentDataType.INTEGER, lvl);
        meta.setLore(lore);

        clone.setItemMeta(meta);

    }

    private void safeAddEnchantLore(Enchant<?> enchant, List<String> lore, int lvl)
    {

        final String text = ChatColor.translateAlternateColorCodes('&', "&r&7" + enchant.getName() + " " + NumberUtil.toRoman(lvl));

        if (lore.contains(text)) return;

        lore.add(text);
    }

    @Override
    public ItemStack remove(ItemStack stack, Enchant<?> enchant)
    {
        return null;
    }

    @Override
    public ItemStack removeAll()
    {
        return null;
    }

    @Override
    public boolean hasEnchant(ItemStack stack, Enchant<?> enchant)
    {
        final ItemMeta meta = stack.getItemMeta();

        return meta.getPersistentDataContainer().has(new NamespacedKey(this.plugin, enchant.getKey()), PersistentDataType.INTEGER);
    }

    @Override
    public List<Enchant<?>> getAll(ItemStack itemStack)
    {
        final List<Enchant<?>> enchants = new ArrayList<>();

        if (itemStack == null || itemStack.getItemMeta() == null) return enchants;

        for (Enchant<?> e : Enchant.getKeys().values()) {
            if (itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(e.getPlugin(), e.getKey()), PersistentDataType.INTEGER))
                enchants.add(e);
        }

        return enchants;
    }

    @Override
    public int getLevel(ItemStack item, Enchant<?> e)
    {
        final NamespacedKey key = new NamespacedKey(e.getPlugin(), e.getKey());

        return Optional.ofNullable(item.getItemMeta()
                        .getPersistentDataContainer()
                        .get(key, PersistentDataType.INTEGER))
                .orElse(0);
    }

    @Override
    public int getLevelSave(ItemStack item, Enchant<?> e)
    {
        if (item == null || item.getItemMeta() == null) return 0;

        final NamespacedKey key = new NamespacedKey(e.getPlugin(), e.getKey());

        return Optional.ofNullable(item.getItemMeta()
                        .getPersistentDataContainer()
                        .get(key, PersistentDataType.INTEGER))
                .orElse(0);
    }
}
