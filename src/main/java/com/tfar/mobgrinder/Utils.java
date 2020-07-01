package com.tfar.mobgrinder;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;
import java.util.Optional;

public class Utils {

	public static final String KEY = "entity_holder";

	//helper methods

	public static boolean containsEntity(@Nonnull ItemStack stack) {
		return stack.hasTag() && stack.getTag().contains(KEY);
	}

	public static Optional<EntityType<?>> getEntityTypeFromNBT(CompoundNBT nbt) {
		return Registry.ENTITY_TYPE.getValue(new ResourceLocation(nbt.getString("id")));
	}

	public static Optional<EntityType<?>> getEntityTypeFromStack(ItemStack stack) {
		Item item = stack.getItem();
		if (item == MobGrinder.net)
		return getEntityTypeFromNBT(stack.getOrCreateTag().getCompound(KEY));
		else
			return Registry.ENTITY_TYPE.getValue(new ResourceLocation(stack.getOrCreateTag().getString("entity")));
	}

	//unused
	public static CompoundNBT getNBTfromEntityType(EntityType<?> entity){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("id", entity.getRegistryName().toString());
		return nbt;
	}
}
