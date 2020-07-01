package com.tfar.mobgrinder;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class MobGrinderScreen extends ContainerScreen<MobGrinderMenu> {

	public static final ResourceLocation background = new ResourceLocation(MobGrinder.MODID,
					"textures/gui/mob_grinder.png");

	public MobGrinderScreen(MobGrinderMenu screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		renderBackground();
		super.render(p_render_1_, p_render_2_, p_render_3_);
		this.renderHoveredToolTip(p_render_1_, p_render_2_);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 *
	 * @param partialTicks
	 * @param mouseX
	 * @param mouseY
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		minecraft.getTextureManager().bindTexture(background);
		blit(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		this.font.drawString(this.playerInventory.getDisplayName().getUnformattedComponentText(), 8, this.ySize - 94, 0x404040);
		int size = font.getStringWidth(title.getFormattedText());
		int start = (this.xSize - size)/2;
		this.font.drawString(this.title.getUnformattedComponentText(), start, 8, 0x404040);
		if (!this.container.te.handler.getStackInSlot(0).isEmpty())
		this.font.drawString(I18n.format(Utils.getEntityTypeFromStack(this.container.te.handler.getStackInSlot(0)).get().getTranslationKey())
						, start + 27, 73, 0x404040);

	}
}
