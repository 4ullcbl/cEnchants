package su.trident.cenchnats;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import su.trident.cenchnats.command.GiveEnchant;
import su.trident.cenchnats.context.blockbreak.BlockBreakListener;
import su.trident.cenchnats.enchant.EnchantRegister;
import su.trident.cenchnats.enchant.api.EnchantStorageAPI;
import su.trident.cenchnats.enchant.impl.EnchantStorage;
import su.trident.cenchnats.github.UpdateCheck;
import su.trident.cenchnats.listener.EnchantTableListener;
import su.trident.cenchnats.listener.PrepareAnvilListener;
import su.trident.cenchnats.util.worldguard.WorldGuardUtil;

import java.util.Objects;

public final class CEnchants extends JavaPlugin
{

    private long start;

    private EnchantRegister register;
    private EnchantStorageAPI storage;
    private WorldGuardUtil worldGuardUtil;

    @Override
    public void onLoad()
    {
        start = System.currentTimeMillis();
    }

    @Override
    public void onEnable()
    {
        final UpdateCheck uc = new UpdateCheck(this, "4ullcbl/cEnchants");
        uc.checkLast();

        final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        register = new EnchantRegister(this);
        storage = new EnchantStorage(this);
        worldGuardUtil = new WorldGuardUtil(container);

        Objects.requireNonNull(getCommand("ce")).setExecutor(new GiveEnchant(this));
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new PrepareAnvilListener(this), this);
        getServer().getPluginManager().registerEvents(new EnchantTableListener(this), this);

        final long launchTime = (System.currentTimeMillis() - start) / 1000;

        getLogger().info(getColorFromLaunch(launchTime) + "Запуск за " + launchTime + "сек.");
    }

    private ChatColor getColorFromLaunch(long launch)
    {
        if (launch <= 6) {
            return ChatColor.GREEN;
        } else if (launch <= 10) {
            return ChatColor.GOLD;
        }

        return ChatColor.RED;
    }

    public EnchantRegister getRegister()
    {
        return register;
    }

    public EnchantStorageAPI getStorage()
    {
        return storage;
    }

    public WorldGuardUtil getWorldGuardUtil()
    {
        return worldGuardUtil;
    }
}
