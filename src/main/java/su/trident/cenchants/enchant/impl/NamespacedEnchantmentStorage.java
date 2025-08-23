package su.trident.cenchants.enchant.impl;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import su.trident.cenchants.enchant.api.Enchantment;
import su.trident.cenchants.enchant.api.EnchantmentStorage;
import su.trident.cenchants.util.numbers.NumberUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class NamespacedEnchantmentStorage implements EnchantmentStorage
{
    private static final Random random = new Random();

    private final JavaPlugin plugin;

    public NamespacedEnchantmentStorage(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void addEnchant(ItemStack stack, Enchantment<?> enchantment, int lvl)
    {
        safelyAddEnchant(stack, stack.getItemMeta(), enchantment, lvl);
    }

    @Override
    public void addEnchantSave(ItemStack stack, Enchantment<?> enchantment, int lvl)
    {
        if (lvl > enchantment.getMaxLvl() || lvl < enchantment.getStartLvl()) return;
        if (stack.getItemMeta() == null) return;
        if (!(enchantment.getTarget().isTarget(stack.getType()))) return;

        safelyAddEnchant(stack, stack.getItemMeta(), enchantment, lvl);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void removeEnchant(ItemStack stack, Enchantment<?> enchantment)
    {
        if (!hasEnchant(stack, enchantment)) return;
        if (enchantment.isCurse()) return;

        if (stack.getItemMeta() == null || stack.getType().isEmpty() || stack.getLore() == null || stack.getLore().isEmpty())
            return;

        final ItemMeta meta = stack.getItemMeta();

        removePdcAndLore(enchantment, stack, meta);
    }

    @Override
    public void removeEnchantAll(ItemStack stack)
    {
        if (stack == null || stack.getItemMeta() == null || stack.getType().isAir()) return;

        for (Enchantment<?> e : getAll(stack)) {
            if (e.isCurse()) continue;
            if (!hasEnchant(stack, e)) continue;

            removeEnchant(stack, e);
        }
    }

    @Override
    public void removeEnchantAll(ItemStack stack, boolean vanilla)
    {
        removeEnchantAll(stack);

        if (vanilla) {
            for (org.bukkit.enchantments.Enchantment e : stack.getEnchantments().keySet()) {
                stack.removeEnchantment(e);
            }
        }
    }

    @Override
    public ItemStack book(Enchantment<?> enchantment, int level)
    {
        final ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);

        addEnchantSave(book, enchantment, level);

        return book;
    }

    @Override
    public boolean hasEnchant(ItemStack stack, Enchantment<?> enchantment)
    {
        final ItemMeta meta = stack.getItemMeta();

        return meta.getPersistentDataContainer().has(new NamespacedKey(this.plugin, enchantment.getKey()), PersistentDataType.INTEGER);
    }

    @Override
    public List<Enchantment<?>> getAll(ItemStack stack)
    {
        final List<Enchantment<?>> enchantments = new ArrayList<>();

        if (stack == null || stack.getItemMeta() == null) return enchantments;

        for (Enchantment<?> e : Enchantment.getKeys().values()) {
            if (stack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(e.getPlugin(), e.getKey()), PersistentDataType.INTEGER))
                enchantments.add(e);
        }

        return enchantments;
    }

    @Override
    public List<Enchantment<?>> getRandom()
    {
        final List<Enchantment<?>> result = new ArrayList<>();

        for (String key : Enchantment.keySet()) {
            if (random.nextInt(100) > Enchantment.getByKey(key).getChance()) continue;
            result.add(Enchantment.getByKey(key));
        }

        return result;
    }

    @Override
    public List<Enchantment<?>> getRandom(int count)
    {
        final List<Enchantment<?>> result = new ArrayList<>();

        for (String key : Enchantment.keySet()) {
            if (random.nextInt(100) > Enchantment.getByKey(key).getChance()) continue;

            if (result.size() <= count) {
                result.add(Enchantment.getByKey(key));
                continue;
            }
            return result;
        }

        return result;
    }

    @Override
    public int getLevel(ItemStack item, Enchantment<?> e)
    {
        final NamespacedKey key = new NamespacedKey(e.getPlugin(), e.getKey());

        return Optional.ofNullable(item.getItemMeta()
                        .getPersistentDataContainer()
                        .get(key, PersistentDataType.INTEGER))
                .orElse(0);
    }

    @Override
    public int getLevelSave(ItemStack item, Enchantment<?> e)
    {
        if (item == null || item.getItemMeta() == null || item.getType().isEmpty()) return 0;

        final NamespacedKey key = new NamespacedKey(e.getPlugin(), e.getKey());

        return Optional.ofNullable(item.getItemMeta()
                        .getPersistentDataContainer()
                        .get(key, PersistentDataType.INTEGER))
                .orElse(0);
    }

    @Override
    public int getRandomLevel(Enchantment<?> enchantment)
    {
        if (enchantment.getStartLvl() == enchantment.getMaxLvl()) return enchantment.getStartLvl();

        int range = enchantment.getMaxLvl() - enchantment.getStartLvl() + 1;
        return random.nextInt(range) + enchantment.getStartLvl();
    }

    @SuppressWarnings("deprecation")
    private void removePdcAndLore(Enchantment<?> enchantment, ItemStack stack, ItemMeta meta)
    {
        final int level = getLevel(stack, enchantment);

        if (level == 0) return;

        if (meta.getLore() == null) return;

        final List<String> lore = meta.getLore();

        if (enchantment.isCurse()) return;

        lore.removeIf(s -> ChatColor.stripColor(s).startsWith(enchantment.getName()));

        meta.getPersistentDataContainer().remove(new NamespacedKey(this.plugin, enchantment.getKey()));
        meta.setLore(lore);
        stack.setItemMeta(meta);
    }

    private String getLoreEnch(Enchantment<?> enchantment, int level)
    {
        return (!enchantment.isCurse())
                ? ChatColor.translateAlternateColorCodes(
                '&', "&r&7" + enchantment.getName() + " " + NumberUtil.toRoman(level))
                : ChatColor.translateAlternateColorCodes(
                '&', "&r&c" + enchantment.getName() + " " + NumberUtil.toRoman(level));
    }

    @SuppressWarnings("deprecation")
    private void safelyAddEnchant(ItemStack stack, ItemMeta meta, Enchantment<?> enchantment, int lvl)
    {
        if (hasEnchant(stack, enchantment)) {
            upgradeEnchant(stack, enchantment, lvl);
            return;
        }

        final List<String> lore = meta.getLore() == null || meta.getLore().isEmpty() ? new ArrayList<>() : meta.getLore();

        safeAddEnchantLore(enchantment, lore, lvl);

        meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, enchantment.getKey()), PersistentDataType.INTEGER, lvl);
        meta.setLore(lore);

        stack.setItemMeta(meta);

    }

    private void upgradeEnchant(ItemStack stack, Enchantment<?> enchantment, int lvl)
    {
        if (!isUpgradable(stack, enchantment))
            return;


        int currentLevel = getLevel(stack, enchantment);
        if (currentLevel > lvl)
            return;

        removeEnchant(stack, enchantment);

        final ItemMeta upgradable = stack.getItemMeta();
        if (upgradable == null) return;

        int newLevel = Math.min(currentLevel + 1, enchantment.getMaxLvl());

        upgradeEnchant(stack, enchantment, upgradable, newLevel);
    }

    @SuppressWarnings("deprecation")
    private void upgradeEnchant(ItemStack stack, Enchantment<?> enchantment, ItemMeta upgradable, int newLevel)
    {
        final List<String> lore = upgradable.getLore() == null ? new ArrayList<>() : upgradable.getLore();
        safeAddEnchantLore(enchantment, lore, newLevel);

        upgradable.getPersistentDataContainer().set(
                new NamespacedKey(this.plugin, enchantment.getKey()),
                PersistentDataType.INTEGER,
                newLevel
        );
        upgradable.setLore(lore);

        stack.setItemMeta(upgradable);
    }

    private boolean isUpgradable(ItemStack item, Enchantment<?> enchantment)
    {
        int level = getLevel(item, enchantment);

        return level < enchantment.getMaxLvl();
    }

    private void safeAddEnchantLore(Enchantment<?> enchantment, List<String> lore, int lvl)
    {
        final String text = getLoreEnch(enchantment, lvl);

        if (lore.contains(text)) return;

        lore.add(text);
    }
}
