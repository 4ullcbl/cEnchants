package su.trident.cenchnats.command;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.api.Enchant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GiveEnchant implements CommandExecutor, TabExecutor
{
    private final CEnchants plugin;

    public GiveEnchant(CEnchants plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        final Player player = (Player) sender;

        final ItemStack item = player.getInventory().getItemInMainHand();

        final String arg = args[0];

        try {
            final int lvl = Integer.parseInt(args[1]);

            for (String key: Enchant.keySet()) {
                if (Objects.equals(key, arg)) {
                    plugin.getStorage().addEnchantSave(item, Enchant.getByKey(key), lvl);
                }
            }

            player.sendMessage("Вы зачаровали предмет в руке!");
            player.playSound(player.getEyeLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
            return true;
        } catch (Exception e) {
            player.sendMessage("usage: /ench <type> <level>");
            player.playSound(player.getEyeLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        final List<String> tab = new ArrayList<>();

        if (args.length == 1) {
            tab.addAll(Enchant.keySet());
        } else if (args.length == 2) {
            tab.add("[<level>]");
        }

        return tab;
    }
}
