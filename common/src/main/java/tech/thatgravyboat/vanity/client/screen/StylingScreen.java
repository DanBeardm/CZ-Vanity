package tech.thatgravyboat.vanity.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.vanity.client.components.display.StyledItemWidget;
import tech.thatgravyboat.vanity.client.components.list.StylesListWidget;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.menu.StylingMenu;

public class StylingScreen extends AbstractContainerScreen<StylingMenu>{

    // Make this texture look like an anvil + keep the lower inventory area (176x220).
    private static final ResourceLocation BACKGROUND = Vanity.id("textures/gui/container/styling_anvil.png");


    public StylingScreen(StylingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.imageWidth = 176;
        this.imageHeight = 220;

        this.titleLabelX = 60;
        this.titleLabelY = 6;

        this.inventoryLabelX = 8;
        this.inventoryLabelY = 72;

    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, UIConstants.TITLE_COLOR, false);
        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, UIConstants.INVENTORY_COLOR, false);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}