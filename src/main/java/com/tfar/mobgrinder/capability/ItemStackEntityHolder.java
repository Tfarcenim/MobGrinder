package com.tfar.mobgrinder.capability;

import com.tfar.mobgrinder.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class ItemStackEntityHolder implements EntityTypeHolder, INBTSerializable<CompoundNBT> {

	private final ItemStack holder;
	private EntityType<?> entity;

	protected ItemStackEntityHolder(ItemStack holder){
		this.holder = holder;
	}

	@Override
	public EntityType<?> getEntityType() {
		return entity;
	}

	@Override
	public void setEntityType(EntityType<?> entity) {
		this.entity = entity;
	}

	@Override
	public CompoundNBT serializeNBT() {
		return Utils.getNBTfromEntityType(this.entity);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.entity = Utils.getEntityTypeFromNBT(nbt);
	}

	public static ItemStackEntityHolder create(ItemStack stack) {
		ItemStackEntityHolder entityHolder = new ItemStackEntityHolder(stack);
		if (stack.hasTag())
		entityHolder.deserializeNBT(stack.getOrCreateChildTag(Utils.KEY));
		return entityHolder;
	}
}
