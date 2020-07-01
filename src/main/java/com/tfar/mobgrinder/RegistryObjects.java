package com.tfar.mobgrinder;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryObjects {

	public static final String name = "mob_grinder";
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MobGrinder.MODID);
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MobGrinder.MODID);
	public static final DeferredRegister<ContainerType<?>> MENUS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, MobGrinder.MODID);
	public static final DeferredRegister<TileEntityType<?>> BLOCK_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, MobGrinder.MODID);

	public static final RegistryObject<Block> mob_grinder = BLOCKS.register(name, () -> new MobGrinderBlock(Block.Properties.from(Blocks.IRON_BLOCK)));
	public static final RegistryObject<Item> mob_grinder_item = ITEMS.register(name, () -> new BlockItem(mob_grinder.get(),new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<ContainerType<MobGrinderMenu>> mob_grinder_menu = MENUS.register(name, () -> IForgeContainerType.create((windowId, inv, data) ->
					new MobGrinderMenu(windowId, inv, inv.player.world, data.readBlockPos())));
	public static final RegistryObject<TileEntityType<?>> mob_grinder_block_entity =
					BLOCK_ENTITIES.register(name, () -> TileEntityType.Builder.create(MobGrinderBlockEntity::new,BLOCKS.getEntries().stream()
									.map(RegistryObject::get).toArray(Block[]::new)).build(null));

}
