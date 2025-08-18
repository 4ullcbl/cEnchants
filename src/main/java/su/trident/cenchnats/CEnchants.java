package su.trident.cenchnats;

import org.bukkit.plugin.java.JavaPlugin;
import su.trident.cenchnats.command.GiveEnchant;
import su.trident.cenchnats.context.blockbreak.BlockBreakListener;
import su.trident.cenchnats.enchant.EnchantRegister;
import su.trident.cenchnats.enchant.api.EnchantStorageAPI;
import su.trident.cenchnats.enchant.impl.EnchantStorage;

public final class CEnchants extends JavaPlugin {

    private EnchantRegister register;
    private EnchantStorageAPI storage;

    @Override
    public void onEnable() {
        register = new EnchantRegister(this);
        storage = new EnchantStorage(this);

        getCommand("magnet").setExecutor(new GiveEnchant(this));
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
    }

    public EnchantRegister getRegister()
    {
        return register;
    }

    public EnchantStorageAPI getStorage()
    {
        return storage;
    }
}
