package su.trident.cenchnats.enchant.api;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.EnchantTarget;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class Enchant<T extends Event> implements Listener
{

    private final String key;
    private final CEnchants plugin;

    protected Enchant(String key, CEnchants plugin)
    {
        this.key = key;
        this.plugin = plugin;
    }

    private static final Map<String, Enchant<?>> byKey = new HashMap<>();

    public static Enchant<?> getByKey(String key)
    {
        return byKey.get(key);
    }

    public static Set<String> keySet()
    {
        return byKey.keySet();
    }

    public static Map<String, Enchant<?>> getKeys()
    {
        return byKey;
    }

    @EventHandler
    public abstract void usage(T event);

    public void register()
    {
        byKey.put(this.getKey(), this);
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);

        this.plugin.getLogger().info("Register enchant: " + this.getName() + " " + this.getStartLvl() + "-" + this.getMaxLvl());
    }

    protected boolean hasEnchant(ItemStack stack)
    {
        if (stack == null) return false;
        if (stack.getItemMeta() == null) return false;

        return stack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(this.plugin, this.key), PersistentDataType.INTEGER);
    }

    protected boolean hasEnchant(ItemStack stack, Enchant<?> enchant)
    {
        if (stack == null) return false;
        if (stack.getItemMeta() == null) return false;

        return stack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(enchant.plugin, enchant.key), PersistentDataType.INTEGER);
    }

    protected boolean hasEnchant(ItemStack stack, int level)
    {
        if (stack == null || stack.getItemMeta() == null || !stack.hasItemMeta()) return false;

        int itemLevel = Optional.ofNullable(stack.getItemMeta()
                .getPersistentDataContainer()
                .get(new NamespacedKey(this.plugin, this.key), PersistentDataType.INTEGER)).orElse(0);
        return itemLevel == level;
    }

    protected int getLevel(ItemStack item, Enchant<?> enchant)
    {
        if (!hasEnchant(item, enchant)) return 0;

        return Optional.ofNullable(item.getItemMeta()
                .getPersistentDataContainer()
                .get(new NamespacedKey(enchant.getPlugin(), enchant.getKey()), PersistentDataType.INTEGER)).orElse(0);
    }

    public boolean isCurse() {
        return false;
    }

    public abstract int getPriority();

    public abstract String getName();

    public abstract int getStartLvl();

    public abstract int getMaxLvl();

    public abstract EnchantTarget getTarget();

    public abstract int getChance();

    public abstract String getKey();

    public JavaPlugin getPlugin()
    {
        return plugin;
    }
}
