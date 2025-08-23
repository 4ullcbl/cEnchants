package su.trident.cenchants.enchant.impl.enchant.interact;

import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import su.trident.cenchants.CEnchants;
import su.trident.cenchants.enchant.EnchantmentTarget;
import su.trident.cenchants.enchant.api.Enchantment;
import su.trident.cenchants.util.block.BlockUtil;

public class Greener extends Enchantment<PlayerInteractEvent>
{

    private final String key;
    private final CEnchants plugin;

    private Particle particleType;
    private int count;
    private double speed;
    private double deltaX;
    private double deltaY;
    private double deltaZ;
    private int upgradeStep;

    public Greener(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.key = key;
        this.plugin = plugin;
    }

    @Override
    @EventHandler
    public void usage(PlayerInteractEvent event)
    {
        if (!hasEnchant(event.getPlayer().getInventory().getItemInMainHand()))
            return;

        if (event.getClickedBlock() == null || event.getClickedBlock().getType().isAir() || (!(event.getClickedBlock().getBlockData() instanceof Ageable ageable)))
            return;

        if (ageable.getAge() >= ageable.getMaximumAge())
            return;

        if (!plugin.getWorldGuardUtil().canPlaceBlock(event.getPlayer(), event.getClickedBlock()))
            return;

        ageable.setAge(ageable.getAge() + upgradeStep);
        event.getClickedBlock().setBlockData(ageable);

        greenerVisual(event.getPlayer(), event.getClickedBlock());
    }

    private void greenerVisual(Player player, Block block)
    {
        player.getWorld().spawnParticle(
                particleType,
                BlockUtil.getCenter(block),
                count,
                speed,
                deltaX, deltaY, deltaZ
        );
        player.swingMainHand();
    }

    @Override
    public void loadConfig()
    {
        loadConfigPath();
        loadDefaultValue();

        particleType = Particle.valueOf(getConfig().getString(getConfigPath() + "particle.type"));
        count = getConfig().getInt(getConfigPath() + "particle.count");
        speed = getConfig().getDouble(getConfigPath() + "particle.speed");
        deltaX = getConfig().getDouble(getConfigPath() + "particle.delta_x");
        deltaY = getConfig().getDouble(getConfigPath() + "particle.delta_y");
        deltaZ = getConfig().getDouble(getConfigPath() + "particle.delta_z");

        upgradeStep = getConfig().getInt(getConfigPath() + "upgrade_step");

    }

    @Override
    public int getPriority()
    {
        return 0;
    }

    @Override
    public int getStartLvl()
    {
        return 1;
    }

    @Override
    public int getMaxLvl()
    {
        return 1;
    }

    @Override
    public String getKey()
    {
        return this.key;
    }

    @Override
    public EnchantmentTarget getTarget()
    {
        return EnchantmentTarget.TOOLS;
    }
}
