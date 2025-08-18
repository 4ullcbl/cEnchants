package su.trident.cenchnats.enchant.api;

import org.bukkit.inventory.ItemStack;

import java.util.List;


public interface EnchantStorageAPI
{

    void addEnchant(ItemStack stack, Enchant<?> enchant, int lvl);

    void addEnchantSave(ItemStack stack, Enchant<?> enchant, int lvl);

    ItemStack remove(ItemStack stack, Enchant<?> enchant);

    ItemStack removeAll();

    boolean hasEnchant(ItemStack stack, Enchant<?> enchant);

    List<Enchant<?>> getAll(ItemStack itemStack);

    int getLevel(ItemStack item, Enchant<?> e);

    int getLevelSave(ItemStack item, Enchant<?> e);
}
