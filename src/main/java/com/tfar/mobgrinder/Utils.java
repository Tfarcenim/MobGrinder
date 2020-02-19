package com.tfar.mobgrinder;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;

public class Utils {

	public static final String KEY = "entity_holder";

	//helper methods

	public static boolean containsEntity(@Nonnull ItemStack stack) {
		return stack.hasTag() && stack.getTag().contains(KEY);
	}

	public static EntityType<?> getEntityTypeFromNBT(CompoundNBT nbt) {
		return Registry.ENTITY_TYPE.getOrDefault(new ResourceLocation(nbt.getString("id")));
	}

	public static EntityType<?> getEntityTypeFromStack(ItemStack stack) {
		Item item = stack.getItem();
		if (item == ExampleMod.net)
		return getEntityTypeFromNBT(stack.getOrCreateTag().getCompound(KEY));
		else
			return Registry.ENTITY_TYPE.getOrDefault(
							new ResourceLocation(stack.getOrCreateTag().getString("entity")));
	}

	//unused
	public static CompoundNBT getNBTfromEntityType(EntityType<?> entity){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("id", entity.getRegistryName().toString());
		return nbt;
	}
}
