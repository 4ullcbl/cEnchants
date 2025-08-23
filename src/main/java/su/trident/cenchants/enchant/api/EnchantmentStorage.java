package su.trident.cenchants.enchant.api;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface EnchantmentStorage
{
    void addEnchant(ItemStack stack, Enchantment<?> enchantment, int lvl);

    void addEnchantSave(ItemStack stack, Enchantment<?> enchantment, int lvl);

    void removeEnchant(ItemStack stack, Enchantment<?> enchantment);

    void removeEnchantAll(ItemStack stack);

    void removeEnchantAll(ItemStack stack, boolean vanilla);

    boolean hasEnchant(ItemStack stack, Enchantment<?> enchantment);

    int getLevel(ItemStack item, Enchantment<?> enchantment);

    int getLevelSave(ItemStack item, Enchantment<?> enchantment);

    int getRandomLevel(Enchantment<?> enchantment);

    ItemStack book(Enchantment<?> enchantment, int level);

    List<Enchantment<?>> getAll(ItemStack itemStack);

    List<Enchantment<?>> getRandom();

    List<Enchantment<?>> getRandom(int count);;
}
