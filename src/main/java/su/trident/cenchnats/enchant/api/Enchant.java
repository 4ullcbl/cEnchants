package su.trident.cenchnats.enchant.api;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class Enchant<T extends Event> implements Listener
{

    private final String key;
    private final JavaPlugin plugin;

    protected Enchant(String key, JavaPlugin plugin)
    {
        this.key = key;
        this.plugin = plugin;
    }

    private static final Map<String, Enchant<?>> byKey = new HashMap<>();

    public static Map<String, Enchant<?>> getByKey() {
        return byKey;
    }

    @EventHandler
    public abstract void usage(T event);

    public void register() {
        byKey.put(this.getKey(), this);
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);

        this.plugin.getLogger().info("Register enchant: " + this.getName() + " " + this.getStartLvl() + "-" + this.getMaxLvl());
    }

    protected boolean hasEnchant(ItemStack stack) {
        if (stack == null) return false;
        if (stack.getItemMeta() == null) return false;

        return stack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(this.plugin, this.key), PersistentDataType.INTEGER);
    }

    protected boolean hasEnchant(ItemStack stack, Enchant<?> enchant) {
        if (stack == null) return false;
        if (stack.getItemMeta() == null) return false;

        return stack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(enchant.plugin, enchant.key), PersistentDataType.INTEGER);
    }

    protected boolean hasEnchant(ItemStack stack, int level) {
        if (stack == null || stack.getItemMeta() == null || !stack.hasItemMeta()) return false;

        int itemLevel = Optional.ofNullable(stack.getItemMeta()
                        .getPersistentDataContainer()
                        .get(new NamespacedKey(this.plugin, this.key), PersistentDataType.INTEGER)).orElse(-1337);
        return itemLevel == level;
    }

    public abstract int getPriority();

    public abstract String getName();

    public abstract int getStartLvl();

    public abstract int getMaxLvl();

    public abstract String getKey();

    public JavaPlugin getPlugin()
    {
        return plugin;
    }
}
