package io.github.pantae35872.computers.registries.menutype.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.pantae35872.computers.Main;
import io.github.pantae35872.computers.networking.ModNetwork;
import io.github.pantae35872.computers.networking.packet.ComputerCloseC2SPacket;
import io.github.pantae35872.computers.registries.menutype.custom.widgets.ComputerWidget;
import io.github.pantae35872.computers.utils.Editor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import static io.github.pantae35872.computers.registries.menutype.custom.widgets.ComputerWidget.FONT_WIDTH;

public class ComputerScreen extends AbstractContainerScreen<ComputerMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Main.MOD_ID, "textures/gui/computer.png");

    public ComputerScreen(ComputerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    public static final int TERMINAL_WIDTH = 270;
    public static final int TERMINAL_HEIGHT = 210;

    @Override
    protected void init() {
        super.init();
        int x = (width - TERMINAL_WIDTH) / 2 - 65;
        int y = (height - TERMINAL_HEIGHT) / 2;
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
        ComputerWidget computerWidget = addRenderableWidget(new ComputerWidget(x, y, TERMINAL_WIDTH, TERMINAL_HEIGHT, Component.empty(),
                new Editor(TERMINAL_WIDTH / FONT_WIDTH, 25, x + 2, y + 2)));
        setFocused(computerWidget);
    }

    @Override
    public void onClose() {
        ModNetwork.sendToServer(new ComputerCloseC2SPacket((menu.blockEntity.getBlockPos())));
        super.onClose();
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        //int x = (width - imageWidth) / 2;
        //int y = (height - imageHeight) / 2;

        //pGuiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        //pGuiGraphics.flush();
    }


    @Override
    protected void containerTick() {
        super.containerTick();
        assert this.minecraft != null;
        assert this.minecraft.player != null;
        if (this.minecraft.player.position().distanceTo(menu.blockEntity.getBlockPos().getCenter()) > 5) {
            this.onClose();
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        //renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
