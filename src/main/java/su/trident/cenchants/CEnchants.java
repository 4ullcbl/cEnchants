package su.trident.cenchants;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import su.trident.cenchants.command.GiveEnchant;
import su.trident.cenchants.context.blockbreak.BlockBreakListener;
import su.trident.cenchants.enchant.EnchantmentRegister;
import su.trident.cenchants.enchant.api.EnchantmentStorage;
import su.trident.cenchants.enchant.impl.NamespacedEnchantmentStorage;
import su.trident.cenchants.github.UpdateCheck;
import su.trident.cenchants.listener.EnchantTableListener;
import su.trident.cenchants.listener.PrepareAnvilListener;
import su.trident.cenchants.listener.RemoveCustomEnchant;
import su.trident.cenchants.util.worldguard.WorldGuardUtil;

import java.util.Arrays;

public final class CEnchants extends JavaPlugin
{
    private long start;

    private EnchantmentRegister register;
    private EnchantmentStorage storage;
    private WorldGuardUtil worldGuardUtil;

    @Override
    public void onLoad()
    {
        start = System.currentTimeMillis();
    }

    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        initializeUtilities();

        getCommand("ce").setExecutor(new GiveEnchant(this, storage));
        registerListeners();

        final long launchTime = (System.currentTimeMillis() - start);

        getLogger().info(getColorFromLaunch(launchTime) + "Запуск за " + launchTime + "мс.");
        startUpdateCheck();
    }

    private void registerListeners()
    {
        Listener[] listenersToRegister = {
                new BlockBreakListener(storage, worldGuardUtil),
                new PrepareAnvilListener(storage),
                new EnchantTableListener(storage),
                new RemoveCustomEnchant(storage)
        };

        Arrays.stream(listenersToRegister).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void initializeUtilities()
    {
        final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        register = new EnchantmentRegister(this);
        storage = new NamespacedEnchantmentStorage(this);
        worldGuardUtil = new WorldGuardUtil(container);
    }

    private ChatColor getColorFromLaunch(long launch)
    {
        if (launch <= 5000) {
            return ChatColor.GREEN;
        } else if (launch <= 10000) {
            return ChatColor.GOLD;
        }

        return ChatColor.RED;
    }

    private void startUpdateCheck()
    {
        if (this.getConfig().getBoolean("update-check")) {
            final UpdateCheck uc = new UpdateCheck(this, "4ullcbl/cEnchants");
            uc.checkLast();
        }
    }

    public EnchantmentRegister getRegister()
    {
        return register;
    }

    public EnchantmentStorage getStorage()
    {
        return storage;
    }

    public WorldGuardUtil getWorldGuardUtil()
    {
        return worldGuardUtil;
    }

}
