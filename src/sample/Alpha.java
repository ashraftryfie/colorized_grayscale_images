package sample;

import javafx.scene.paint.Color;

public class Alpha {

    public boolean[][] pixels;
    public Color color;
    public Color lastColor;

    public Alpha(int x, int y, Color color) {
        this.pixels = new boolean[x][y];
        this.color = color;
    }

}
