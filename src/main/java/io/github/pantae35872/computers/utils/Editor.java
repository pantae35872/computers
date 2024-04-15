package io.github.pantae35872.computers.utils;

import io.github.pantae35872.computers.Main;
import io.github.pantae35872.computers.registries.menutype.custom.widgets.ComputerWidget;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static io.github.pantae35872.computers.registries.menutype.custom.widgets.ComputerWidget.*;

public class Editor {
    private final List<Byte> buffer;
    private final int row;
    private final int column;
    static final float WIDTH = 256.0f;
    private final int innerX;
    private final int innerY;
    private final Queue<Integer> moveQueue;
    private int cursor = 0;

    public Editor(int row, int column, int innerX, int innerY) {
        this.row = row;
        this.column = column;
        this.buffer = new ArrayList<>();
        this.innerX = innerX;
        this.innerY = innerY;
        moveQueue = new LinkedList<>();
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

    public void pressNewLine() {
        buffer.add(cursor,(byte) '\n');
        cursor++;
    }

    public void render(ComputerWidget.QuadEmitter emitter) {
        renderText(emitter);
    }

    public void checkColumn(int column) {
        if (this.column > column) {

        }
    }

    public void renderText(ComputerWidget.QuadEmitter emitter) {
        int currentColumn = 0;
        int currentRow = 0;

        // TODO: Add text scrolling

        for (int i = 0; i <= buffer.size(); i++) {
            Byte character = (byte)255;
            if (i < buffer.size()) {
                character = buffer.get(i);
            }
            if (currentRow >= row) {
                currentRow = 0;
                currentColumn++;
            }
            if (character == 9) {
                currentRow += 4;
                int leftover = currentRow - row;
                if (leftover > 0) {
                    currentRow = leftover - 1;
                    currentColumn++;
                }
            }
            renderChar(emitter, currentRow * FONT_WIDTH, currentColumn * FONT_HEIGHT, character);

            if (i == cursor) {
                if ((Main.ForgeClientHooks.clientTick / 8) % 2 == 0) {
                    renderChar(emitter, currentRow * FONT_WIDTH, currentColumn * FONT_HEIGHT, '_');
                }
            }

            if (character == 0xA) {
                currentRow = 0;
                currentColumn++;
            }
            if (character != 0xA) currentRow++;
        }
    }

    public void renderChar(ComputerWidget.QuadEmitter emitter, int x, int y, int index) {

        var column = index % 16;
        var row = index / 16;

        var xStart = 1 + column * (FONT_WIDTH + 2);
        var yStart = 1 + row * (FONT_HEIGHT + 2);

        quad(
                emitter, innerX + x, innerY + y, innerX + x + FONT_WIDTH, innerY + y + FONT_HEIGHT, 0, WHITE,
                xStart / WIDTH, yStart / WIDTH, (xStart + FONT_WIDTH) / WIDTH, (yStart + FONT_HEIGHT) / WIDTH,
                FULL_BRIGHT_LIGHTMAP
        );
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
                if (cursor - topRow > 0) {
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
                return currentRow - 1;
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
                if (currentRow >= (flag ? row - 1 : row) && buffer.get(cursor) != 9) {
                    return i - 1;
                } else if (currentRow > row) {
                    return i - 1;
                }
                byte character = buffer.get(cursor - i);
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
