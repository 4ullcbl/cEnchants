package su.trident.cenchnats.enchant.api;

import org.bukkit.inventory.ItemStack;

import java.util.List;


public interface EnchantStorageAPI
{

    void addEnchant(ItemStack stack, Enchant<?> enchant, int lvl);

    void addEnchantSave(ItemStack stack, Enchant<?> enchant, int lvl);

    void removeEnchant(ItemStack stack, Enchant<?> enchant);

    void removeAllEnchant();

    ItemStack giveBook();

    boolean hasEnchant(ItemStack stack, Enchant<?> enchant);

    List<Enchant<?>> getAll(ItemStack itemStack);

    int getLevel(ItemStack item, Enchant<?> e);

    int getLevelSave(ItemStack item, Enchant<?> e);
}
