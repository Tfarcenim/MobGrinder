package com.tfar.mobgrinder.capability;

import net.minecraft.entity.EntityType;

import javax.annotation.Nullable;

public class SingleEntityHolder implements EntityTypeHolder {

	public EntityType<?> storedEntity;

	public SingleEntityHolder(){
		this(null);
	}

	public SingleEntityHolder(EntityType<?> entity){
		this.storedEntity = entity;
	}

	@Nullable
	@Override
	public EntityType<?> getEntityType() {
		return storedEntity;
	}

	@Override
	public void setEntityType(EntityType<?> entity) {
		this.storedEntity = entity;
	}
}
