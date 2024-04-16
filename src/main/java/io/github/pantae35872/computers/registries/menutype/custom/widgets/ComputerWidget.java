package io.github.pantae35872.computers.registries.menutype.custom.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.pantae35872.computers.Main;
import io.github.pantae35872.computers.utils.Color;
import io.github.pantae35872.computers.utils.Editor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
public class ComputerWidget extends AbstractWidget {
    public static final ResourceLocation FONT = new ResourceLocation(Main.MOD_ID, "textures/gui/term_font.png");
    private final int innerX;
    private final int innerY;
    private final Editor editor;
    public boolean isShifting = false;
    public boolean isCaps = false;
    public ComputerWidget(int pX, int pY, int pWidth, int pHeight, Component pMessage, Editor editor) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.innerX = pX + 2;
        this.innerY = pY + 2;
        this.editor = editor;
    }
    static final float WIDTH = 256.0f;
    public static final float BACKGROUND_START = (WIDTH - 6.0f) / WIDTH;
    public static final float BACKGROUND_END = (WIDTH - 4.0f) / WIDTH;
    public static final int FULL_BRIGHT_LIGHTMAP = (0xF << 4) | (0xF << 20);


    public record QuadEmitter(Matrix4f poseMatrix, VertexConsumer consumer) {
    }
    public static final int FONT_HEIGHT = 9;
    public static final int FONT_WIDTH = 6;

    @Override
    public void playDownSound(@NotNull SoundManager pHandler) {
        //super.playDownSound(pHandler);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode <= 265 && pKeyCode >= 262) {
            editor.move(pKeyCode - 262);
        }

        if (pKeyCode == 261) {
            editor.forward_delete();
        }

        if (pKeyCode == 340) {
            isShifting = true;
            return true;
        }

        if (pKeyCode == 257) {
            editor.pressNewLine();
            return true;
        }

        if (pKeyCode == 259) {
            editor.delete();
            return true;
        }

        if (pKeyCode == 258) {
            editor.tab();
            return true;
        }

        if (pKeyCode <= 127 && pKeyCode >= 32) {
            if (isShifting || isCaps) {
                if (pKeyCode == 96) {
                    editor.keyPress((byte) 126);
                    return true;
                }
                if (pKeyCode <= 90 && pKeyCode >= 64) {
                    editor.keyPress((byte) (pKeyCode & ~0b00100000));
                    return true;
                }
                if (pKeyCode <= 63 && pKeyCode >= 33) {
                    switch (pKeyCode) {
                        case 50 -> editor.keyPress((byte) 0b01000000);
                        case 54 -> editor.keyPress((byte) 0b01011110);
                        case 55 -> editor.keyPress((byte) 0b00100110);
                        case 48 -> editor.keyPress((byte) 0b00101001);
                        case 57 -> editor.keyPress((byte) 0b00101000);
                        case 56 -> editor.keyPress((byte) 0b00101010);
                        case 61 -> editor.keyPress((byte) 0b00101011);
                        case 45 -> editor.keyPress((byte) 0b01011111);
                        case 59 -> editor.keyPress((byte) 0b00111010);
                        case 39 -> editor.keyPress((byte) 0b00100010);
                        case 47 -> editor.keyPress((byte) 0b00111111);
                        case 46 -> editor.keyPress((byte) 0b00111110);
                        case 44 -> editor.keyPress((byte) 0b00111100);
                        default -> editor.keyPress((byte) (pKeyCode & ~0b00010000));
                    }

                    return true;
                }
                editor.keyPress((byte) (pKeyCode | 0b00100000));
                return true;
            }

            if (pKeyCode <= 90 && pKeyCode >= 65) {
                editor.keyPress((byte) (pKeyCode | 0b00100000));
            } else {
                editor.keyPress((byte) pKeyCode);
            }
            return true;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 340) {
            isShifting = false;
            return true;
        }
        if (pKeyCode == 280) {
            isCaps = !isCaps;
            return true;
        }

        return super.keyReleased(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        var bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

        QuadEmitter emitter = ComputerWidget.toVertexConsumer(pGuiGraphics.pose(), bufferSource.getBuffer(RenderType.text(FONT)));

        drawEmptyTerminal(emitter, innerX, innerY, this.width, this.height);
        editor.render(emitter);


        bufferSource.endBatch();
    }


    public static void drawEmptyTerminal(QuadEmitter emitter, float x, float y, float width, float height) {
        drawQuad(emitter, x, y, 0, width, height, Color.BLACK.rgba(), FULL_BRIGHT_LIGHTMAP);
    }


    public static void drawQuad(QuadEmitter emitter, float x, float y, float z, float width, float height, byte[] colour, int light) {
        quad(emitter, x, y, x + width, y + height, z, colour, BACKGROUND_START, BACKGROUND_START, BACKGROUND_END, BACKGROUND_END, light);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

    }


    public static QuadEmitter toVertexConsumer(PoseStack transform, VertexConsumer consumer) {
        return new QuadEmitter(transform.last().pose(), consumer);
    }

    public static void quad(QuadEmitter c, float x1, float y1, float x2, float y2, float z, byte[] rgba, float u1, float v1, float u2, float v2, int light) {
        var poseMatrix = c.poseMatrix();
        var consumer = c.consumer();
        byte r = rgba[0], g = rgba[1], b = rgba[2], a = rgba[3];

        consumer.vertex(poseMatrix, x1, y1, z).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
        consumer.vertex(poseMatrix, x1, y2, z).color(r, g, b, a).uv(u1, v2).uv2(light).endVertex();
        consumer.vertex(poseMatrix, x2, y2, z).color(r, g, b, a).uv(u2, v2).uv2(light).endVertex();
        consumer.vertex(poseMatrix, x2, y1, z).color(r, g, b, a).uv(u2, v1).uv2(light).endVertex();
    }
}
