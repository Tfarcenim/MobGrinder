package com.tfar.mobgrinder.capability;

import net.minecraft.entity.EntityType;

public interface EntityTypeHolder {

	EntityType<?> getEntityType();

	void setEntityType(EntityType<?> entity);
}
