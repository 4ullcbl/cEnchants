package su.trident.cenchants.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WorldGuardService
{
    private final RegionContainer regionContainer;

    public WorldGuardService(RegionContainer regionContainer)
    {
        this.regionContainer = regionContainer;
    }

    public boolean canBreakBlock(Player player, Block block)
    {
        try {
            if (player == null || block == null) return false;
            if (this.regionContainer == null || player.isOp()) return true;

            final RegionQuery query = this.regionContainer.createQuery();
            final LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

            if (localPlayer == null) return false;

            return query.testState(
                    BukkitAdapter.adapt(block.getLocation()),
                    localPlayer,
                    Flags.BLOCK_BREAK
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean canPlaceBlock(Player player, Block block)
    {
        try {
            if (player == null || block == null) return false;
            if (this.regionContainer == null || player.isOp()) return true;

            final RegionQuery query = this.regionContainer.createQuery();
            final LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

            if (localPlayer == null) return false;

            return query.testState(
                    BukkitAdapter.adapt(block.getLocation()),
                    localPlayer,
                    Flags.BLOCK_PLACE
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean canDoing(Player player, StateFlag flags, Location location)
    {
        try {
            if (player == null || location == null) return false;
            if (this.regionContainer == null || player.isOp()) return true;

            final RegionQuery query = this.regionContainer.createQuery();
            final LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

            if (localPlayer == null) return false;

            return query.testState(
                    BukkitAdapter.adapt(location),
                    localPlayer,
                    flags
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
