package mekanism.common.item.block;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.api.text.EnumColor;
import mekanism.common.MekanismLang;
import mekanism.common.block.basic.BlockBin;
import mekanism.common.inventory.BinMekanismInventory;
import mekanism.common.inventory.slot.BinInventorySlot;
import mekanism.common.item.IItemSustainedInventory;
import mekanism.common.item.ITieredItem;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.tier.BinTier;
import mekanism.common.util.text.TextComponentUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemBlockBin extends ItemBlockTooltip<BlockBin> implements ITieredItem<BinTier>, IItemSustainedInventory {

    public ItemBlockBin(BlockBin block) {
        super(block, ItemDeferredRegister.getMekBaseProperties().maxStackSize(1));
    }

    @Nullable
    @Override
    public BinTier getTier(@Nonnull ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ItemBlockBin ? ((ItemBlockBin) item).getTier() : null;
    }

    @Nonnull
    @Override
    public BinTier getTier() {
        return getBlock().getTier();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addStats(@Nonnull ItemStack stack, World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag) {
        BinMekanismInventory inventory = BinMekanismInventory.create(stack);
        BinTier tier = getTier(stack);
        if (inventory != null && tier != null) {
            BinInventorySlot slot = inventory.getBinSlot();
            if (slot.isEmpty()) {
                tooltip.add(MekanismLang.EMPTY.translateColored(EnumColor.DARK_RED));
            } else {
                tooltip.add(TextComponentUtil.build(EnumColor.BRIGHT_GREEN, slot.getStack().getDisplayName()));
                tooltip.add(MekanismLang.ITEM_AMOUNT.translateColored(EnumColor.PURPLE, EnumColor.GRAY,
                      slot.getCount() == Integer.MAX_VALUE ? MekanismLang.INFINITE : slot.getCount()));
            }
            int cap = tier.getStorage();
            if (cap == Integer.MAX_VALUE) {
                tooltip.add(MekanismLang.CAPACITY.translateColored(EnumColor.INDIGO, EnumColor.GRAY, MekanismLang.INFINITE));
            } else {
                tooltip.add(MekanismLang.CAPACITY_ITEMS.translateColored(EnumColor.INDIGO, EnumColor.GRAY, cap));
            }
        }
    }
}