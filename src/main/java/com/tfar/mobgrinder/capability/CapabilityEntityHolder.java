package com.tfar.mobgrinder.capability;

import com.tfar.mobgrinder.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityEntityHolder {
	@CapabilityInject(EntityTypeHolder.class)
	public static Capability<EntityTypeHolder> ENTITY_TYPE_HOLDER_CAPABILITY = null;

	public static void register() {
		CapabilityManager.INSTANCE.register(EntityTypeHolder.class, new Capability.IStorage<EntityTypeHolder>() {
			@Override
			public INBT writeNBT(Capability<EntityTypeHolder> capability, EntityTypeHolder instance, Direction side) {
				EntityType<?> entity = instance.getEntityType();
				return Utils.getNBTfromEntityType(entity);
			}

			@Override
			public void readNBT(Capability<EntityTypeHolder> capability, EntityTypeHolder instance, Direction side, INBT base) {
				CompoundNBT nbt = (CompoundNBT)base;
				EntityType<?> entity = Utils.getEntityTypeFromNBT(nbt);
				instance.setEntityType(entity);
			}
		}, SingleEntityHolder::new);
	}
}
