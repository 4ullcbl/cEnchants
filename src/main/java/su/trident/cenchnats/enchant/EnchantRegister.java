package su.trident.cenchnats.enchant;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.api.Enchant;
import su.trident.cenchnats.enchant.impl.enchant.*;
import su.trident.cenchnats.enchant.impl.enchant.blockbreak.*;

public class EnchantRegister
{

    private final CEnchants plugin;

    private final Enchant<BlockBreakEvent> bulldozer;
    private final Enchant<BlockBreakEvent> melting;
    private final Enchant<BlockBreakEvent> magnet;
    private final Enchant<PlayerItemDamageEvent> pinger;
    private final Enchant<BlockBreakEvent> web;
    private final Enchant<BlockBreakEvent> megaBulldozer;
    private final Enchant<BlockBreakEvent> woodCutter;

    public EnchantRegister(CEnchants plugin)
    {
        this.plugin = plugin;

        this.bulldozer = new Bulldozer("bulldozer", this.plugin);
        this.bulldozer.register();

        this.melting = new Melting("melting", this.plugin);
        this.melting.register();

        this.magnet = new Magnet("magnet", this.plugin);
        this.magnet.register();

        this.pinger = new Ping("pinger", this.plugin);
        this.pinger.register();

        this.web = new Web("web", this.plugin);
        this.web.register();

        this.megaBulldozer = new MegaBulldozer("mega_bulldozer", this.plugin);
        this.megaBulldozer.register();

        this.woodCutter = new Woodcutter("woodcutter", this.plugin);
        this.woodCutter.register();
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
}
