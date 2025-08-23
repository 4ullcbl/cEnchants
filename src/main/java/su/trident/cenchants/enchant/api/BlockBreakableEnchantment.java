package su.trident.cenchants.enchant.api;

import su.trident.cenchants.context.blockbreak.BlockBreakContext;

public interface BlockBreakableEnchantment
{
    void apply(BlockBreakContext context);
}
