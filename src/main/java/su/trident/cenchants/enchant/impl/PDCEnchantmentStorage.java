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

import java.util.*;
import java.util.stream.Collectors;

public class PDCEnchantmentStorage implements EnchantmentStorage
{
    private static final Random random = new Random();
    private final JavaPlugin plugin;

    public PDCEnchantmentStorage(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void addEnchantment(ItemStack stack, Enchantment<?> enchantment, int level)
    {
        if (!isEnchantable(stack, enchantment, level)) return;
        applyEnchantment(stack, stack.getItemMeta(), enchantment, level);
    }

    @Override
    public void addEnchantmentAll(ItemStack stack, Map<Enchantment<?>, Integer> enchantments)
    {
        enchantments.keySet().forEach(e -> addEnchantment(stack, e, enchantments.get(e)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void removeEnchantment(ItemStack stack, Enchantment<?> enchantment)
    {
        if (!hasEnchantment(stack, enchantment)) return;
        if (enchantment.isCurse()) return;

        if (stack.getItemMeta() == null || stack.getType().isEmpty() || stack.getLore() == null || stack.getLore().isEmpty())
            return;

        final ItemMeta meta = stack.getItemMeta();

        removePdcAndLore(enchantment, stack, meta);
    }

    @Override
    public void removeEnchantmentAll(ItemStack stack)
    {
        if (stack == null || stack.getItemMeta() == null || stack.getType().isAir()) return;

        final List<Enchantment<?>> toRemove = this.getEnchantmentList(stack).stream()
                .filter(e -> !e.isCurse())
                .filter(e -> hasEnchantment(stack, e))
                .toList();

        toRemove.forEach(e -> removeEnchantment(stack, e));
    }

    @Override
    public void removeEnchantmentAll(ItemStack stack, boolean vanillaRemove)
    {
        removeEnchantmentAll(stack);
        if (!vanillaRemove) return;

        stack.getEnchantments().keySet().forEach(stack::removeEnchantment);
    }

    @Override
    public ItemStack book(Enchantment<?> enchantment, int level)
    {
        final ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        addEnchantment(book, enchantment, level);

        return book;
    }

    @Override
    public boolean hasEnchantment(ItemStack stack, Enchantment<?> enchantment)
    {
        final ItemMeta meta = stack.getItemMeta();

        return meta.getPersistentDataContainer().has(new NamespacedKey(enchantment.getPlugin(), enchantment.getKey()), PersistentDataType.INTEGER);
    }

    @Override
    public List<Enchantment<?>> getEnchantmentList(ItemStack stack)
    {
        final List<Enchantment<?>> enchantments = new ArrayList<>();

        if (stack == null || stack.getItemMeta() == null) return enchantments;

        Enchantment.getKeys().values().stream()
                .filter(e -> hasEnchantment(stack, e))
                .forEach(enchantments::add);

        return enchantments;
    }

    @Override
    public Map<Enchantment<?>, Integer> getAll(ItemStack stack)
    {
        final Map<Enchantment<?>, Integer> enchantments = new HashMap<>();

        Enchantment.getKeys().values().stream()
                .filter(e -> hasEnchantment(stack, e))
                .forEach(e -> enchantments.put(e, getLevel(stack, e)));

        return enchantments;
    }

    @Override
    public List<Enchantment<?>> getRandom()
    {
        final List<Enchantment<?>> result = new ArrayList<>();

        Enchantment.keySet().stream()
                .map(Enchantment::getByKey)
                .filter(e -> {
                    int roll = random.nextInt(100);
                    return roll < e.getChance();
                }).forEach(result::add);

        return result;
    }

    @Override
    public List<Enchantment<?>> getRandom(int count)
    {
        final List<Enchantment<?>> eligibleEnchantments = Enchantment.keySet().stream()
                .map(Enchantment::getByKey)
                .filter(e -> {
                    int roll = random.nextInt(100);
                    return roll < e.getChance();
                })
                .collect(Collectors.toList());

        Collections.shuffle(eligibleEnchantments, random);
        return eligibleEnchantments.subList(0, Math.min(count, eligibleEnchantments.size()));
    }


    //TODO доделать методы в EnchantmentStorage
    @Override
    public List<Enchantment<?>> getRandomForType(ItemStack stack)
    {
        return List.of();
    }

    @Override
    public List<Enchantment<?>> getRandomForType(Material material)
    {
        return List.of();
    }

    @Override
    public List<Enchantment<?>> getRandomForType(ItemStack stack, int count)
    {
        return List.of();
    }

    @Override
    public List<Enchantment<?>> getRandomForType(Material material, int count)
    {
        return List.of();
    }

    @Override
    public int getLevel(ItemStack item, Enchantment<?> e)
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
        if (getLevel(stack, enchantment) == 0) return;
        if (meta.getLore() == null) return;
        if (enchantment.isCurse()) return;

        final List<String> lore = meta.getLore();
        lore.removeIf(s -> ChatColor.stripColor(s).startsWith(enchantment.getName()));

        meta.getPersistentDataContainer().remove(new NamespacedKey(enchantment.getPlugin(), enchantment.getKey()));
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
    private void applyEnchantment(ItemStack stack, ItemMeta meta, Enchantment<?> enchantment, int lvl)
    {
        if (hasEnchantment(stack, enchantment)) {
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

        removeEnchantment(stack, enchantment);

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

    private boolean isEnchantable(ItemStack stack, Enchantment<?> enchantment, int level)
    {
        if (level > enchantment.getMaxLvl() || level < enchantment.getStartLvl() || stack.getItemMeta() == null)
            return false;

        return enchantment.getTarget().isTarget(stack.getType());
    }
}
