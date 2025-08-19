package su.trident.cenchnats.util.block;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BlockUtil
{
    private static final List<Material> UNBREAKABLE = Arrays.asList(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR, Material.BARRIER, Material.BEDROCK, Material.END_PORTAL, Material.END_PORTAL_FRAME, Material.NETHER_PORTAL, Material.COMMAND_BLOCK, Material.CHAIN_COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK, Material.STRUCTURE_BLOCK, Material.JIGSAW, Material.STRUCTURE_VOID, Material.PISTON_HEAD, Material.MOVING_PISTON, Material.FIRE, Material.SOUL_FIRE, Material.WATER, Material.LAVA, Material.BUBBLE_COLUMN);
    private static final List<Material> ORES = Arrays.asList(Material.COAL_ORE, Material.IRON_ORE, Material.GOLD_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.REDSTONE_ORE, Material.LAPIS_ORE, Material.NETHER_QUARTZ_ORE, Material.NETHER_GOLD_ORE, Material.ANCIENT_DEBRIS);
    private static final List<Material> WOOD = Arrays.asList(Material.OAK_LOG, Material.SPRUCE_LOG);

    public static Location getCenter(Block block)
    {
        return block.getLocation().add(0.5, 0.5, 0.5);
    }

    public static Location getButtomCenter(Block block)
    {
        return block.getLocation().add(0.5, 0, 0.5);
    }

    public static boolean isPassible(@NotNull Block block)
    {
        return block.isPassable() || block.isEmpty();
    }

    public static List<Block> getBlockBetween(Location loc1, Location loc2)
    {
        final List<Block> cube = new ArrayList<>();

        final int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        final int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        final int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        final int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        final int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        final int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    cube.add(loc1.getWorld().getBlockAt(x, y, z));
                }
            }
        }

        return cube;
    }

    public static boolean isBetweenY(Block block, int minY, int maxY)
    {
        int y = block.getY();
        return y >= minY && y <= maxY;
    }

    public static void spawnBreakParticle(Player player, Block block)
    {
        player.spawnParticle(
                Particle.BLOCK_CRACK,
                BlockUtil.getCenter(block),
                30,
                0.5, 0.5, 0.5,
                0.1,
                block.getBlockData()
        );
    }

    public static void spawnBreakParticle(Block block)
    {
        block.getWorld().spawnParticle(
                Particle.BLOCK_CRACK,
                BlockUtil.getCenter(block),
                30,
                0.5, 0.5, 0.5,
                0.1,
                block.getBlockData()
        );
    }

    /**
     * Находит ближайших блоков схожих с добытой
     *
     * @param start   Блок, от которого ищем руды
     * @param radius  Радиус поиска (в блоках)
     * @param maxOres Максимальное количество руд для возврата
     * @return Список ближайших руд
     */
    public static List<Block> findNearestBlock(Block start, int radius, int maxOres)
    {
        final Queue<Block> toCheck = new LinkedList<>();
        final Set<Block> visited = new HashSet<>();

        toCheck.add(start);
        visited.add(start);

        while (!toCheck.isEmpty()) {
            final Block block = toCheck.poll();
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) continue;
                        final Block neighbor = block.getRelative(dx, dy, dz);
                        if (neighbor.getType() == start.getType() && visited.add(neighbor)) {
                            toCheck.add(neighbor);

                            if (toCheck.size() >= maxOres) {
                                visited.remove(start);
                                return new ArrayList<>(visited);
                            }
                        }
                    }
                }
            }
        }

        visited.remove(start);

        return new ArrayList<>(visited);
    }

    public static List<Material> getOres()
    {
        return ORES;
    }

    public static List<Material> getUnbreakable()
    {
        return UNBREAKABLE;
    }
}
