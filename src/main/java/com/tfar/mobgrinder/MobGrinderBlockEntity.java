package com.tfar.mobgrinder;

import com.mojang.authlib.GameProfile;
import com.tfar.mobgrinder.inventory.NoInsertIItemHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.UUID;

public class MobGrinderBlockEntity extends TileEntity implements INamedContainerProvider, ITickableTileEntity {

	private static GameProfile PROFILE = new GameProfile(UUID.fromString("a42ac406-c797-4e0e-b147-f01ac5551be6"), "[MobGrinder]");

	public static final Method droploot;

	public static final Method dropSpecialItems;

	static {
		//entity.dropLoot(source,true);
		droploot = ObfuscationReflectionHelper.findMethod(LivingEntity.class, "func_213354_a",
						DamageSource.class, boolean.class);
		//entity.dropSpecialItems(source, i, true);
		dropSpecialItems = ObfuscationReflectionHelper.findMethod(LivingEntity.class, "func_213333_a",
						DamageSource.class, int.class, boolean.class);
	}

	public ItemStackHandler handler = new ItemStackHandler(2);

	public NoInsertIItemHandler storage = new NoInsertIItemHandler(9);

	public LazyOptional<IItemHandler> optional = LazyOptional.of(() -> storage);

	public MobGrinderBlockEntity() {
		super(RegistryObjects.mob_grinder_block_entity.get());
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName() {
		return new StringTextComponent("grinder");
	}

	@Nullable
	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
		return new MobGrinderMenu(p_createMenu_1_, p_createMenu_2_, world, pos);
	}

	@Override
	public void tick() {
		if (!world.isRemote) {
			if (world.getWorldInfo().getGameTime() % MobGrinderConfigs.ServerConfig.timeBetweenKills.get() != 0) return;
			ItemStack holder = handler.getStackInSlot(0);
			if (!holder.isEmpty()) {
				if (holder.hasTag() && !storage.full()) {
					Utils.getEntityTypeFromStack(holder).ifPresent(entityType -> {
						LivingEntity entity = (LivingEntity) entityType.create(world);
						if (entity instanceof MobEntity) {
							((MobEntity) entity).onInitialSpawn(world, world.getDifficultyForLocation(pos)
											, SpawnReason.NATURAL, null, null);
						}
						FakePlayer fakePlayer = FakePlayerFactory.get((ServerWorld) world, PROFILE);
						fakePlayer.setHeldItem(Hand.MAIN_HAND, handler.getStackInSlot(1));

						DamageSource source = DamageSource.causePlayerDamage(fakePlayer);

						ObfuscationReflectionHelper.setPrivateValue(LivingEntity.class, entity, fakePlayer, "field_70717_bb");

						entity.captureDrops(new java.util.ArrayList<>());
						int i = net.minecraftforge.common.ForgeHooks.getLootingLevel(entity, fakePlayer, source);

						try {
							//entity.dropLoot(source,true);
							droploot.invoke(entity, source, true);
							//entity.dropSpecialItems(source, i, true);
							dropSpecialItems.invoke(entity, source, i, true);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}

						Collection<ItemEntity> mobDrops = entity.captureDrops(null);
						LivingDropsEvent event = new LivingDropsEvent(entity, source, mobDrops, i, true);
						if (!MinecraftForge.EVENT_BUS.post(event)) {
							event.getDrops().stream()
											.map(ItemEntity::getItem)
											.forEach(stack -> storage.addItem(stack, false));
						}
						if (MobGrinderConfigs.ServerConfig.damagePerKill.get() > 0)
							handler.getStackInSlot(1).damageItem(MobGrinderConfigs.ServerConfig.damagePerKill.get()
											, fakePlayer, p -> {
											});
					});
				}
			}
		}
	}

	@Nonnull
	@Override
	public CompoundNBT write(CompoundNBT tag) {
		CompoundNBT inv = handler.serializeNBT();
		tag.put("inv", inv);
		CompoundNBT storage = this.storage.serializeNBT();
		tag.put("storage", storage);
		//if (this.customName != null) {
		//	tag.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
		//}

		return super.write(tag);
	}

	@Override
	public void read(CompoundNBT tag) {
		CompoundNBT invTag = tag.getCompound("inv");
		handler.deserializeNBT(invTag);
		CompoundNBT storage = tag.getCompound("storage");
		this.storage.deserializeNBT(storage);
		//if (tag.contains("CustomName", 8)) {
		//	this.customName = ITextComponent.Serializer.fromJson(tag.getString("CustomName"));
		//}

		super.read(tag);
	}

	@Override
	public void remove() {
		super.remove();
		this.optional.invalidate();
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? optional.cast() : super.getCapability(cap, side);
	}
}
