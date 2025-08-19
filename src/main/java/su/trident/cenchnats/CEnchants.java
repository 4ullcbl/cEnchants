package su.trident.cenchnats;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.plugin.java.JavaPlugin;
import su.trident.cenchnats.command.GiveEnchant;
import su.trident.cenchnats.context.blockbreak.BlockBreakListener;
import su.trident.cenchnats.enchant.EnchantRegister;
import su.trident.cenchnats.enchant.api.EnchantStorageAPI;
import su.trident.cenchnats.enchant.impl.EnchantStorage;
import su.trident.cenchnats.listener.PrepareAnvilListener;
import su.trident.cenchnats.util.worldguard.WorldGuardUtil;

import java.util.Objects;

public final class CEnchants extends JavaPlugin {

    private EnchantRegister register;
    private EnchantStorageAPI storage;
    private WorldGuardUtil worldGuardUtil;

    @Override
    public void onEnable() {
        final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        register = new EnchantRegister(this);
        storage = new EnchantStorage(this);
        worldGuardUtil = new WorldGuardUtil(container);

        Objects.requireNonNull(getCommand("ce")).setExecutor(new GiveEnchant(this));
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new PrepareAnvilListener(this), this);
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
