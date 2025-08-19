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
    private void safelyAddEnchant(ItemStack stack, ItemMeta meta, Enchant<?> enchant, int lvl)
    {
        if (hasEnchant(stack, enchant)) {
            upgradeEnchant(stack, enchant, lvl);
            return;
        }

        final List<String> lore = meta.getLore() == null || meta.getLore().isEmpty() ? new ArrayList<>() : meta.getLore();

        safeAddEnchantLore(enchant, lore, lvl);

        meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, enchant.getKey()), PersistentDataType.INTEGER, lvl);
        meta.setLore(lore);

        stack.setItemMeta(meta);

    }
    private void upgradeEnchant(ItemStack stack, Enchant<?> enchant, int lvl)
    {
        if (!isUpgradable(stack, enchant))  {
            System.out.println("not is upgradable");
            return;
        }

        int currentLevel = getLevel(stack, enchant);
        if (currentLevel > lvl) {
            System.out.println("clevel > lvl");
            return;
        }

        removeEnchant(stack, enchant);

        final ItemMeta upgradable = stack.getItemMeta();
        if (upgradable == null) return;

        int newLevel = Math.min(currentLevel + 1, enchant.getMaxLvl());

        upgradeEnchant(stack, enchant, upgradable, newLevel);
    }

    @SuppressWarnings("deprecation")
    private void upgradeEnchant(ItemStack stack, Enchant<?> enchant, ItemMeta upgradable, int newLevel)
    {
        final List<String> lore = upgradable.getLore() == null ? new ArrayList<>() : upgradable.getLore();
        safeAddEnchantLore(enchant, lore, newLevel);

        upgradable.getPersistentDataContainer().set(
                new NamespacedKey(this.plugin, enchant.getKey()),
                PersistentDataType.INTEGER,
                newLevel
        );
        upgradable.setLore(lore);

        stack.setItemMeta(upgradable);

        System.out.println("Upgraded " + enchant.getName() + " to level " + newLevel);
    }

    private boolean isUpgradable(ItemStack item, Enchant<?> enchant)
    {
        int level = getLevel(item, enchant);
        System.out.println("upg: " + level);

        System.out.println(level < enchant.getMaxLvl());

        return level < enchant.getMaxLvl();
    }

    private void safeAddEnchantLore(Enchant<?> enchant, List<String> lore, int lvl)
    {
        final String text = getLoreEnch(enchant, lvl);

        if (lore.contains(text)) return;

        lore.add(text);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void removeEnchant(ItemStack stack, Enchant<?> enchant)
    {
        if (!hasEnchant(stack, enchant)) return;

        if (stack.getItemMeta() == null || stack.getType().isEmpty() || stack.getLore() == null || stack.getLore().isEmpty())
            return;

        final ItemMeta meta = stack.getItemMeta();

        removePdcAndLore(enchant, stack, meta);
    }

    @SuppressWarnings("deprecation")
    private void removePdcAndLore(Enchant<?> enchant, ItemStack stack, ItemMeta meta)
    {
        final int level = getLevel(stack, enchant);

        if (level == 0) return;

        if (meta.getLore() == null) return;

        final List<String> lore = meta.getLore();

        lore.removeIf(s -> ChatColor.stripColor(s).startsWith(enchant.getName()));

        meta.getPersistentDataContainer().remove(new NamespacedKey(this.plugin, enchant.getKey()));
        meta.setLore(lore);
        stack.setItemMeta(meta);
    }

    private String getLoreEnch(Enchant<?> enchant, int level)
    {
        return ChatColor.translateAlternateColorCodes('&', "&r&7" + enchant.getName() + " " + NumberUtil.toRoman(level));
    }

    @Override
    public void removeAllEnchant()
    {
    }

    @Override
    public ItemStack giveBook()
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
