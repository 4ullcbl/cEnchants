package su.trident.cenchnats.enchant;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.api.Enchant;
import su.trident.cenchnats.enchant.impl.enchant.armor.Jumper;
import su.trident.cenchnats.enchant.impl.enchant.armor.LavaWalker;
import su.trident.cenchnats.enchant.impl.enchant.attack.Detection;
import su.trident.cenchnats.enchant.impl.enchant.attack.Poison;
import su.trident.cenchnats.enchant.impl.enchant.attack.Vampiring;
import su.trident.cenchnats.enchant.impl.enchant.blockbreak.*;
import su.trident.cenchnats.enchant.impl.enchant.interact.Greener;
import su.trident.cenchnats.enchant.impl.enchant.itembreak.Ping;
import su.trident.cenchnats.enchant.impl.enchant.projectilehit.*;

public class EnchantRegister
{

    private final Enchant<BlockBreakEvent> bulldozer;
    private final Enchant<BlockBreakEvent> melting;
    private final Enchant<BlockBreakEvent> magnet;
    private final Enchant<PlayerItemDamageEvent> pinger;
    private final Enchant<BlockBreakEvent> web;
    private final Enchant<BlockBreakEvent> megaBulldozer;
    private final Enchant<BlockBreakEvent> woodCutter;
    private final Enchant<PlayerArmorChangeEvent> jumper;
    private final Enchant<PlayerArmorChangeEvent> lavaWalker;
    private final Enchant<PlayerInteractEvent> greener;
    private final Enchant<ProjectileHitEvent> stupor;
    private final Enchant<ProjectileHitEvent> scout;
    private final Enchant<ProjectileHitEvent> pulling;
    private final Enchant<ProjectileHitEvent> comeback;
    private final Enchant<ProjectileHitEvent> boomber;
    private final Enchant<EntityShootBowEvent> sniper;
    private final Enchant<EntityDamageByEntityEvent> detection;
    private final Enchant<EntityDamageByEntityEvent> poison;
    private final Enchant<EntityDamageByEntityEvent> vamping;

    public EnchantRegister(CEnchants plugin)
    {

        this.bulldozer = new Bulldozer("bulldozer", plugin);
        this.bulldozer.register();

        this.melting = new Melting("melting", plugin);
        this.melting.register();

        this.magnet = new Magnet("magnet", plugin);
        this.magnet.register();

        this.pinger = new Ping("pinger", plugin);
        this.pinger.register();

        this.web = new Web("web", plugin);
        this.web.register();

        this.megaBulldozer = new MegaBulldozer("mega_bulldozer", plugin);
        this.megaBulldozer.register();

        this.woodCutter = new Woodcutter("woodcutter", plugin);
        this.woodCutter.register();

        this.jumper = new Jumper("jumper", plugin);
        this.jumper.register();

        this.lavaWalker = new LavaWalker("lavawalker", plugin);
        this.lavaWalker.register();

        this.greener = new Greener("greener", plugin);
        this.greener.register();

        this.stupor = new Stupor("stupor", plugin);
        this.stupor.register();

        this.scout = new Scout("scout", plugin);
        this.scout.register();

        this.pulling = new Pulling("pulling", plugin);
        this.pulling.register();

        this.comeback = new Comeback("comeback", plugin);
        this.comeback.register();

        this.boomber = new Boomber("bomber", plugin);
        this.boomber.register();

        this.sniper = new Sniper("sniper", plugin);
        this.sniper.register();

        this.detection = new Detection("detect", plugin);
        this.detection.register();

        this.poison = new Poison("poison", plugin);
        this.poison.register();

        this.vamping = new Vampiring("vamp", plugin);
        this.vamping.register();
    }

    public Enchant<PlayerArmorChangeEvent> getJumper()
    {
        return jumper;
    }

    public Enchant<BlockBreakEvent> getBulldozer()
    {
        return bulldozer;
    }

    public Enchant<BlockBreakEvent> getMelting()
    {
        return melting;
    }

    public Enchant<BlockBreakEvent> getMagnet()
    {
        return magnet;
    }

    public Enchant<PlayerItemDamageEvent> getPinger()
    {
        return pinger;
    }

    public Enchant<BlockBreakEvent> getMegaBulldozer()
    {
        return megaBulldozer;
    }

    public Enchant<BlockBreakEvent> getWeb()
    {
        return web;
    }

    public Enchant<BlockBreakEvent> getWoodCutter()
    {
        return woodCutter;
    }

    public Enchant<PlayerArmorChangeEvent> getLavaWalker()
    {
        return lavaWalker;
    }

    public Enchant<PlayerInteractEvent> getGreener()
    {
        return greener;
    }

    public Enchant<ProjectileHitEvent> getStupor()
    {
        return stupor;
    }

    public Enchant<ProjectileHitEvent> getScout()
    {
        return scout;
    }

    public Enchant<ProjectileHitEvent> getPulling()
    {
        return pulling;
    }

    public Enchant<ProjectileHitEvent> getComeback()
    {
        return comeback;
    }

    public Enchant<ProjectileHitEvent> getBoomber()
    {
        return boomber;
    }

    public Enchant<EntityShootBowEvent> getSniper()
    {
        return sniper;
    }

    public Enchant<EntityDamageByEntityEvent> getDetection()
    {
        return detection;
    }

    public Enchant<EntityDamageByEntityEvent> getPoison()
    {
        return poison;
    }

    public Enchant<EntityDamageByEntityEvent> getVamping()
    {
        return vamping;
    }
}
