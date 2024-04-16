package io.github.pantae35872.computers.utils;

import io.github.pantae35872.computers.Main;
import io.github.pantae35872.computers.registries.menutype.custom.widgets.ComputerWidget;

import java.util.ArrayList;

import static io.github.pantae35872.computers.registries.menutype.custom.widgets.ComputerWidget.*;

public class Editor {
    private final ArrayList<Byte> buffer;
    private final int row;
    private final int column;
    static final float WIDTH = 256.0f;
    private final int innerX;
    private final int innerY;
    private int cursor = 0;
    private int offset = 0;

    public Editor(int row, int column, int innerX, int innerY) {
        this.row = row;
        this.column = column;
        this.buffer = new ArrayList<>();
        this.innerX = innerX;
        this.innerY = innerY;
    }

    public void keyPress(byte character) {
        buffer.add(cursor,character);
        cursor++;
    }

    public void delete() {
        if (cursor - 1 >= 0) buffer.remove(cursor - 1);
        if (cursor > 0) {
            cursor--;
        }
    }

    public void forward_delete() {
        if (cursor < buffer.size()) buffer.remove(cursor);
    }

    public void pressNewLine() {
        buffer.add(cursor,(byte) '\n');
        cursor++;
    }

    public void render(ComputerWidget.QuadEmitter emitter) {
        renderText(emitter);
    }

    private record RenderStatus(int leftover, int readAmount){
    }

    private RenderStatus renderRow(ComputerWidget.QuadEmitter emitter,
                         int start, int leftover, int currentColumn, ArrayList<Byte> buffer, int offset, boolean simulate) {
        int currentRow = leftover;
        int i;
        for (i = 0; i < row; i++) {
            int readPos = start + i;
            if (readPos > buffer.size() - 1) break;
            if (currentRow >= row) break;

            Byte character = buffer.get(readPos);

            boolean renderCursor = false;
            if (cursor == readPos) {
                if ((Main.ForgeClientHooks.clientTick / 8) % 2 == 0) {
                    if (offset + currentColumn * FONT_HEIGHT > (column - 3) * FONT_HEIGHT) {
                        this.offset++;
                    } else if (offset + currentColumn * FONT_HEIGHT < 0) {
                        this.offset--;
                    }
                    renderCursor = true;
                }
            }
            if (character == '\n') {
                if (cursor == readPos && renderCursor) {
                    drawQuad(emitter, currentRow * FONT_WIDTH, offset + currentColumn * FONT_HEIGHT,
                            0, 1, FONT_HEIGHT, Color.ORANGE.rgba(), FULL_BRIGHT_LIGHTMAP);
                }
                return new RenderStatus(
                        -1, i);
            }
            if (character == 9) {
                if (!simulate && renderCursor) {
                    drawQuad(emitter, currentRow * FONT_WIDTH, offset + currentColumn * FONT_HEIGHT,
                            0, 1, FONT_HEIGHT, Color.ORANGE.rgba(), FULL_BRIGHT_LIGHTMAP);
                }
                currentRow+=4;
                if (currentRow >= row) {
                    return new RenderStatus(currentRow - row
                    , i + 1);
                }
            }

            if (!simulate) {
                renderChar(emitter, currentRow * FONT_WIDTH,
                        offset + currentColumn * FONT_HEIGHT, character, Color.WHITE.rgba());
            }

            if (!simulate && renderCursor && character != 9) {
                drawQuad(emitter, currentRow * FONT_WIDTH, offset + currentColumn * FONT_HEIGHT,
                        0, 1, FONT_HEIGHT, Color.ORANGE.rgba(), FULL_BRIGHT_LIGHTMAP);
            }


            if (character != 9) currentRow++;
        }
        if (start + i > buffer.size() - 1) {
            return new RenderStatus(
                    -2, i);
        } else {
            return new RenderStatus(
                    -1, i - 1);
        }
    }

    @SuppressWarnings("unchecked")
    public void renderText(ComputerWidget.QuadEmitter emitter) {

        int leftover = 0;
        int currentColumn = 0;
        int readPos = 0;
        ArrayList<Byte> buffer = (ArrayList<Byte>) this.buffer.clone();
        buffer.add((byte) 0);
        do {
            RenderStatus status;
            int offset = -FONT_HEIGHT * this.offset;
            int y_level = offset + currentColumn * FONT_HEIGHT;
            if (y_level < 0 || y_level > (column - 3) * FONT_HEIGHT) {
                status = renderRow(emitter, readPos,
                        leftover, currentColumn, buffer, offset, true);
            } else {
                status = renderRow(emitter, readPos,
                        leftover, currentColumn, buffer, offset, false);
            }
            if (status.leftover() == -1) {
                currentColumn++;
                leftover = 0;
                readPos++;
            } else {
                leftover = status.leftover();
            }
            if (status.leftover() != -1) currentColumn++;
            readPos += status.readAmount();
        } while (leftover != -2);
    }

    public void renderChar(ComputerWidget.QuadEmitter emitter, int x, int y, int index, byte[] rgba) {

        var column = index % 16;
        var row = index / 16;

        var xStart = 1 + column * (FONT_WIDTH + 2);
        var yStart = 1 + row * (FONT_HEIGHT + 2);

        quad(
                emitter, innerX + x, innerY + y, innerX + x + FONT_WIDTH, innerY + y + FONT_HEIGHT, 0, rgba,
                xStart / WIDTH, yStart / WIDTH, (xStart + FONT_WIDTH) / WIDTH, (yStart + FONT_HEIGHT) / WIDTH,
                FULL_BRIGHT_LIGHTMAP
        );
    }

    public void drawQuad(QuadEmitter emitter, float x, float y, float z, float width, float height, byte[] colour, int light) {
        quad(emitter, this.innerX + x, this.innerY+y, this.innerX + x + width, this.innerY+y + height, z, colour, BACKGROUND_START, BACKGROUND_START, BACKGROUND_END, BACKGROUND_END, light);
    }

    public void tab() {
        buffer.add(cursor, (byte) 9);
        cursor++;
    }


    public void move(int direction) {
        switch (direction) {
            //UP
            case 3 -> {
                int topRow = getTopRow();
                if (cursor - topRow >= 0) {
                    cursor -= topRow;
                }
            }
            //DOWN
            case 2 -> {
                int currentRow = getBelowRow();
                if (cursor + currentRow <= this.buffer.size()) {
                    cursor += currentRow;
                }
            }
            //LEFT
            case 1 -> {
                if (cursor > 0) {
                    cursor -= 1;
                }
            }
            //RIGHT
            case 0 -> {
                if (cursor < buffer.size()) {
                    cursor += 1;
                }
            }
            default -> {}
        }
    }


    //All code below have thousand and thousand of edge case

    private int first_char(int current) {
        int i;
        int currentRow = 0;
        for (i = 1; i < row; i++) {
            if (currentRow > row) {
                return currentRow;
            }

            try {
                byte character = buffer.get(current - i);
                switch (character) {
                    case 0xA -> {
                        return currentRow;
                    }
                    case 9 -> currentRow += 4;
                    default -> currentRow++;
                }
            } catch (Exception ignored) {
                return currentRow;
            }
        }
        return currentRow;
    }

    private int getTopRow() {
        int currentRow = 0;

        int i;
        try {
            if (buffer.get(cursor) == 9) {
                currentRow+=4;
            }
        } catch (Exception ignore) {

        }

        boolean flag = false;

        for (i = 1; i < row; i++) {
            try {
                byte character = buffer.get(cursor - i);
                if (currentRow >= (flag ? row - 1 : row) && buffer.get(cursor) != 9) {
                    return i - 1;
                } else if (currentRow > (flag ? row - 1 : row)){
                    character = buffer.get(cursor - i + 1);
                    if (character == 9) {
                        return i - 1;
                    }
                    return i + 2;
                }
                switch (character) {
                    case 0xA -> {
                        int first_char = first_char(cursor - i);
                        if (first_char == row - 1) {
                            flag = true;
                        }
                        currentRow += row - first_char;
                    }
                    case 9 -> currentRow += 4;
                    default -> currentRow++;
                }
            } catch (Exception ignore) {
                return i - 1;
            }
        }
        return i;
    }

    private int getBelowRow() {
        int currentRow = 0;

        int i;
        int first_char = first_char(this.cursor);
        for (i = 0; i < row; i++) {
            if (currentRow >= row) {
                return i;
            }

            try {
                byte character = buffer.get(cursor + i);
                switch (character) {
                    case 0xA -> currentRow += row - (first_char + currentRow);
                    case 9 -> currentRow += 4;
                    default -> currentRow++;
                }
            } catch (Exception ignored) {
                return i;
            }
        }
        return i;
    }
}
