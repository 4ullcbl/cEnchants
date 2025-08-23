package su.trident.cenchants.enchant;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import su.trident.cenchants.CEnchants;
import su.trident.cenchants.enchant.api.Enchantment;
import su.trident.cenchants.enchant.impl.enchant.armor.Jumper;
import su.trident.cenchants.enchant.impl.enchant.armor.LavaWalker;
import su.trident.cenchants.enchant.impl.enchant.attack.Detection;
import su.trident.cenchants.enchant.impl.enchant.attack.Poison;
import su.trident.cenchants.enchant.impl.enchant.attack.Vampiring;
import su.trident.cenchants.enchant.impl.enchant.blockbreak.*;
import su.trident.cenchants.enchant.impl.enchant.blockexp.Experienced;
import su.trident.cenchants.enchant.impl.enchant.interact.Greener;
import su.trident.cenchants.enchant.impl.enchant.itembreak.NotStable;
import su.trident.cenchants.enchant.impl.enchant.itembreak.Ping;
import su.trident.cenchants.enchant.impl.enchant.projectilehit.*;

public class EnchantmentRegister
{
    private final Enchantment<BlockBreakEvent> bulldozer;
    private final Enchantment<BlockBreakEvent> melting;
    private final Enchantment<BlockBreakEvent> magnet;
    private final Enchantment<PlayerItemDamageEvent> pinger;
    private final Enchantment<PlayerItemDamageEvent> notStable;
    private final Enchantment<BlockBreakEvent> web;
    private final Enchantment<BlockBreakEvent> megaBulldozer;
    private final Enchantment<BlockBreakEvent> woodCutter;
    private final Enchantment<PlayerArmorChangeEvent> jumper;
    private final Enchantment<PlayerArmorChangeEvent> lavaWalker;
    private final Enchantment<PlayerInteractEvent> greener;
    private final Enchantment<ProjectileHitEvent> stupor;
    private final Enchantment<ProjectileHitEvent> scout;
    private final Enchantment<ProjectileHitEvent> pulling;
    private final Enchantment<ProjectileHitEvent> comeback;
    private final Enchantment<ProjectileHitEvent> boomber;
    private final Enchantment<EntityShootBowEvent> sniper;
    private final Enchantment<EntityDamageByEntityEvent> detection;
    private final Enchantment<EntityDamageByEntityEvent> poison;
    private final Enchantment<EntityDamageByEntityEvent> vamping;
    private final Enchantment<BlockExpEvent> experienced;

    public EnchantmentRegister(CEnchants plugin)
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

        this.notStable = new NotStable("not_stable", plugin);
        this.notStable.register();

        this.experienced = new Experienced("experienced", plugin);
        this.experienced.register();
    }

    public Enchantment<PlayerArmorChangeEvent> getJumper()
    {
        return jumper;
    }

    public Enchantment<BlockBreakEvent> getBulldozer()
    {
        return bulldozer;
    }

    public Enchantment<BlockBreakEvent> getMelting()
    {
        return melting;
    }

    public Enchantment<BlockBreakEvent> getMagnet()
    {
        return magnet;
    }

    public Enchantment<PlayerItemDamageEvent> getPinger()
    {
        return pinger;
    }

    public Enchantment<BlockBreakEvent> getMegaBulldozer()
    {
        return megaBulldozer;
    }

    public Enchantment<BlockBreakEvent> getWeb()
    {
        return web;
    }

    public Enchantment<BlockBreakEvent> getWoodCutter()
    {
        return woodCutter;
    }

    public Enchantment<PlayerArmorChangeEvent> getLavaWalker()
    {
        return lavaWalker;
    }

    public Enchantment<PlayerInteractEvent> getGreener()
    {
        return greener;
    }

    public Enchantment<ProjectileHitEvent> getStupor()
    {
        return stupor;
    }

    public Enchantment<ProjectileHitEvent> getScout()
    {
        return scout;
    }

    public Enchantment<ProjectileHitEvent> getPulling()
    {
        return pulling;
    }

    public Enchantment<ProjectileHitEvent> getComeback()
    {
        return comeback;
    }

    public Enchantment<ProjectileHitEvent> getBoomber()
    {
        return boomber;
    }

    public Enchantment<EntityShootBowEvent> getSniper()
    {
        return sniper;
    }

    public Enchantment<EntityDamageByEntityEvent> getDetection()
    {
        return detection;
    }

    public Enchantment<EntityDamageByEntityEvent> getPoison()
    {
        return poison;
    }

    public Enchantment<EntityDamageByEntityEvent> getVamping()
    {
        return vamping;
    }

    public Enchantment<PlayerItemDamageEvent> getNotStable()
    {
        return notStable;
    }

    public Enchantment<BlockExpEvent> getExperienced()
    {
        return experienced;
    }
}
