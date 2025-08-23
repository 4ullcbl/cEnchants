package su.trident.cenchants.enchant.impl.enchant.blockbreak;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchants.CEnchants;
import su.trident.cenchants.context.blockbreak.BlockBreakContext;
import su.trident.cenchants.enchant.EnchantmentTarget;
import su.trident.cenchants.enchant.api.BlockBreakableEnchantment;
import su.trident.cenchants.enchant.api.Enchantment;
import su.trident.cenchants.util.block.BlockUtil;

import java.util.ArrayList;
import java.util.List;

public class Web extends Enchantment<BlockBreakEvent> implements BlockBreakableEnchantment
{
    private final String key;
    private final CEnchants plugin;

    private Particle particleType;
    private int count;
    private double speed;
    private double deltaX;
    private double deltaY;
    private double deltaZ;

    private int maxOres;

    public Web(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.key = key;
        this.plugin = plugin;
    }

    @Override
    public void apply(BlockBreakContext context)
    {
        if (!hasEnchant(context.getTool())) return;
        if (!BlockUtil.getOres().contains(context.getOriginBlock().getType())) return;

        final List<Block> toBreak = BlockUtil.findNearestBlock(context.getOriginBlock(), 1, maxOres);

        if (toBreak == null) return;

        if (toBreak.size() <= 1) return;

        context.getAffectedBlocks().addAll(toBreak);

        final List<ItemStack> dropToAdd = new ArrayList<>();

        for (Block block : context.getAffectedBlocks()) {
            if (!this.plugin.getWorldGuardUtil().canBreakBlock(context.getPlayer(), block)) continue;

            if (block.equals(context.getOriginBlock())) continue;
            dropToAdd.addAll(block.getDrops(context.getTool()));
        }

        context.getDrops().addAll(dropToAdd);
        visualWeb(context);
    }

    private void visualWeb(BlockBreakContext context)
    {
        for (Block block : context.getAffectedBlocks()) {
            if (block.getType() == Material.AIR || block.isEmpty() || BlockUtil.getUnbreakable().contains(block.getType()) || block.getType().isEmpty() || block.getType().isAir())
                continue;
            block.getWorld().spawnParticle(particleType, BlockUtil.getCenter(block), count, speed, deltaX, deltaY, deltaZ);
        }
    }

    @Override
    public void loadConfig()
    {
        loadConfigPath();
        loadDefaultValue();

        maxOres = getConfig().getInt(getConfigPath() + "max_ores");

        particleType = Particle.valueOf(getConfig().getString(getConfigPath() + "particle.type"));
        count = getConfig().getInt(getConfigPath() + "particle.count");
        speed = getConfig().getDouble(getConfigPath() + "particle.speed");
        deltaX = getConfig().getDouble(getConfigPath() + "particle.delta_x");
        deltaY = getConfig().getDouble(getConfigPath() + "particle.delta_y");
        deltaZ = getConfig().getDouble(getConfigPath() + "particle.delta_z");
    }

    @Override
    public void usage(BlockBreakEvent event)
    {
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
