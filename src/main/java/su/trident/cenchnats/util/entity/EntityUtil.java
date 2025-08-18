package su.trident.cenchnats.util.entity;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class EntityUtil
{
    public static void smoothTeleport(Entity toTeleport, Entity target, double speed)
    {
        final Vector direction = target.getLocation().toVector().subtract(toTeleport.getLocation().toVector()).normalize().multiply(speed);

        toTeleport.setVelocity(direction);
    }

    public static void smoothTeleport(Entity toTeleport, Entity target)
    {
        Vector direction = target.getLocation().toVector().subtract(toTeleport.getLocation().toVector()).normalize().multiply(3);

        toTeleport.setVelocity(direction);
    }
}
