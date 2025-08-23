package su.trident.cenchants.enchant.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public interface EnchantmentStorage
{
    void addEnchantment(ItemStack stack, Enchantment<?> enchantment, int lvl);

    void addEnchantmentAll(ItemStack stack, Map<Enchantment<?>, Integer> enchantments);

    void removeEnchantment(ItemStack stack, Enchantment<?> enchantment);

    void removeEnchantmentAll(ItemStack stack);

    void removeEnchantmentAll(ItemStack stack, boolean vanilla);

    boolean hasEnchantment(ItemStack stack, Enchantment<?> enchantment);

    int getLevel(ItemStack item, Enchantment<?> enchantment);

    int getRandomLevel(Enchantment<?> enchantment);

    ItemStack book(Enchantment<?> enchantment, int level);

    List<Enchantment<?>> getEnchantmentList(ItemStack itemStack);

    Map<Enchantment<?>, Integer> getAll(ItemStack stack);

    List<Enchantment<?>> getRandom();

    List<Enchantment<?>> getRandom(int count);

    List<Enchantment<?>> getRandomForType(ItemStack stack);

    List<Enchantment<?>> getRandomForType(Material material);

    List<Enchantment<?>> getRandomForType(ItemStack stack, int count);

    List<Enchantment<?>> getRandomForType(Material material, int count);
}
