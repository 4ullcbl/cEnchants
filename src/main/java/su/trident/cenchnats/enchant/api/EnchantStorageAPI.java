package su.trident.cenchnats.enchant.api;

import org.bukkit.inventory.ItemStack;

import java.util.List;


public interface EnchantStorageAPI
{

    void addEnchant(ItemStack stack, Enchant<?> enchant, int lvl);

    void addEnchantSave(ItemStack stack, Enchant<?> enchant, int lvl);

    void removeEnchant(ItemStack stack, Enchant<?> enchant);

    void removeEnchantAll(ItemStack stack);

    void removeEnchantAll(ItemStack stack, boolean vanilla);

    boolean hasEnchant(ItemStack stack, Enchant<?> enchant);

    int getLevel(ItemStack item, Enchant<?> enchant);

    int getLevelSave(ItemStack item, Enchant<?> enchant);

    int getRandomLevel(Enchant<?> enchant);

    ItemStack book(Enchant<?> enchant, int level);

    List<Enchant<?>> getAll(ItemStack itemStack);

    List<Enchant<?>> getRandom();

    List<Enchant<?>> getRandom(int count);;
}
