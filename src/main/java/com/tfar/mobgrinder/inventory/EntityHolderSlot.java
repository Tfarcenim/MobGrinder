package com.tfar.mobgrinder.inventory;

import com.tfar.mobgrinder.ExampleMod;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class EntityHolderSlot extends SlotItemHandler {
	public EntityHolderSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(@Nonnull ItemStack stack) {
		return stack.getItem() == ExampleMod.net || stack.getItem() == ExampleMod.imprisonment_tool;
	}
}
