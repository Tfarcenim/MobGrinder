package com.tfar.mobgrinder;

import com.tfar.mobgrinder.capability.CapabilityEntityHolder;
import com.tfar.mobgrinder.capability.EntityTypeHolder;
import com.tfar.mobgrinder.capability.ItemStackEntityHolder;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod {
	// Directly reference a log4j logger.

	public static final String MODID = "mobgrinder";

	private static final Logger LOGGER = LogManager.getLogger();

	public ExampleMod() {
		IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		// Register the setup method for modloading
		iEventBus.addListener(this::setup);
		// Register the doClientStuff method for modloading
		iEventBus.addListener(this::doClientStuff);
		RegistryObjects.ITEMS.register(iEventBus);
		RegistryObjects.BLOCKS.register(iEventBus);
		RegistryObjects.BLOCK_ENTITIES.register(iEventBus);
		RegistryObjects.MENUS.register(iEventBus);
	}

	@ObjectHolder("mobcatcher:net")
	public static Item net;
	@ObjectHolder("industrialforegoing:mob_imprisonment_tool")
	public static Item imprisonment_tool;

	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {
		ItemStack stack = evt.getObject();
		Item item = stack.getItem();

		if (item == net) {
			ItemStackEntityHolder itemStackEntityHolder = ItemStackEntityHolder.create(stack);
			evt.addCapability(new ResourceLocation(MODID,"entity_holder"), new ICapabilityProvider() {
				LazyOptional<EntityTypeHolder> holderLazyOptional = LazyOptional.of(() -> itemStackEntityHolder);

				@Nonnull
				@Override
				public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
																								 @Nullable Direction side) {
					return CapabilityEntityHolder.ENTITY_TYPE_HOLDER_CAPABILITY.orEmpty(cap, holderLazyOptional);
				}
			});
		}
	}

	private void setup(final FMLCommonSetupEvent event) {
		CapabilityEntityHolder.register();
		//MinecraftForge.EVENT_BUS.register(this);
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		ScreenManager.registerFactory(RegistryObjects.mob_grinder_menu.get(), MobGrinderScreen::new);
	}
}
