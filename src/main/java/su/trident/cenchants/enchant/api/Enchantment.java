package su.trident.cenchants.enchant.api;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import su.trident.cenchants.CEnchants;
import su.trident.cenchants.enchant.EnchantmentTarget;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class Enchantment<T extends Event> implements Listener
{
    private final String key;
    private final CEnchants plugin;

    private String name;
    private int chance;
    private int anvilCost;
    private int tableLevel;

    protected Enchantment(String key, CEnchants plugin)
    {
        this.key = key;
        this.plugin = plugin;
    }

    private String configPath;

    private static final Map<String, Enchantment<?>> byKey = new HashMap<>();

    public static Enchantment<?> getByKey(String key)
    {
        return byKey.get(key);
    }

    public static Set<String> keySet()
    {
        return byKey.keySet();
    }

    public static Map<String, Enchantment<?>> getKeys()
    {
        return byKey;
    }

    @EventHandler
    public abstract void usage(T event);

    public void register()
    {
        loadConfig();
        byKey.put(this.getKey(), this);
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);

        this.plugin.getLogger().info(ChatColor.GREEN + "Регистрация зачарования: " + this.getName() + " " + this.getStartLvl() + "-" + this.getMaxLvl());
        this.plugin.getLogger().info(ChatColor.DARK_GRAY + "DEBUG: шанс: " + this.getChance() + " | мин. уровень стола: " + this.getMinTableLevel() + " | цена чара: " + this.getAnvilCost());
    }

    public void loadConfig()
    {
        loadConfigPath();
        loadDefaultValue();
    }

    protected void loadDefaultName()
    {
        name = getConfig().getString(configPath + "name");
    }

    protected void loadDefaultValue()
    {
        loadConfigPath();
        loadDefaultName();
        loadDefaultChance();
        loadDefaultCost();
        loadDefaultMinLevel();
    }

    protected void loadDefaultChance()
    {
        chance = getConfig().getInt(configPath + "chance");
    }

    protected void loadDefaultCost()
    {
        anvilCost = getConfig().getInt(configPath + "anvil_cost");
    }

    protected void loadDefaultMinLevel()
    {
        tableLevel = getConfig().getInt(configPath + "min_table_level");
    }

    protected void loadConfigPath()
    {
        configPath = "enchants." + this.key + ".";
    }

    protected boolean hasEnchant(ItemStack stack)
    {
        if (stack == null) return false;
        if (stack.getItemMeta() == null) return false;

        return stack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(this.plugin, this.key), PersistentDataType.INTEGER);
    }

    protected boolean hasEnchant(ItemStack stack, Enchantment<?> enchantment)
    {
        if (stack == null) return false;
        if (stack.getItemMeta() == null) return false;

        return stack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(enchantment.plugin, enchantment.key), PersistentDataType.INTEGER);
    }

    protected boolean hasEnchant(ItemStack stack, int level)
    {
        if (stack == null || stack.getItemMeta() == null || !stack.hasItemMeta()) return false;

        int itemLevel = Optional.ofNullable(stack.getItemMeta()
                .getPersistentDataContainer()
                .get(new NamespacedKey(this.plugin, this.key), PersistentDataType.INTEGER)).orElse(0);
        return itemLevel == level;
    }

    protected int getLevel(ItemStack item, Enchantment<?> enchantment)
    {
        if (!hasEnchant(item, enchantment)) return 0;

        return Optional.ofNullable(item.getItemMeta()
                .getPersistentDataContainer()
                .get(new NamespacedKey(enchantment.getPlugin(), enchantment.getKey()), PersistentDataType.INTEGER)).orElse(0);
    }

    public boolean isCurse()
    {
        return false;
    }

    protected void setDefaultValue()
    {
        getConfig().set(getConfigPath() + "name", name);
        getConfig().set(getConfigPath() + "chance", chance);
        getConfig().set(getConfigPath() + "anvil_cost", anvilCost);
        getConfig().set(getConfigPath() + "min_table_level", tableLevel);
        plugin.saveConfig();
    }

    public abstract int getPriority();

    public String getName()
    {
        return getDefaultName();
    }

    public int getAnvilCost()
    {
        return getDefaultAnvilCost();
    }

    public int getMinTableLevel()
    {
        return getDefaultMinTableLevel();
    }

    public abstract int getStartLvl();

    public abstract int getMaxLvl();

    public abstract EnchantmentTarget getTarget();

    public int getChance()
    {
        return getDefaultChance();
    }

    public abstract String getKey();

    public JavaPlugin getPlugin()
    {
        return plugin;
    }

    public String getConfigPath()
    {
        return configPath;
    }

    public FileConfiguration getConfig()
    {
        return plugin.getConfig();
    }

    protected String getDefaultName()
    {
        return this.name;
    }

    protected int getDefaultChance()
    {
        return this.chance;
    }

    protected int getDefaultAnvilCost()
    {
        return this.anvilCost;
    }

    protected int getDefaultMinTableLevel()
    {
        return this.tableLevel;
    }


}
