package com.gabrielsoule.lighttree;

import java.util.List;

public class ColorProvider{
    private int[] colors;
    public ColorProvider(int[] colors) {
        this.colors = colors;
    }

    public ColorProvider(List<Integer> colors) {
        this(colors.stream().mapToInt(i->i).toArray());
    }
    public int getColor(int index) {
        int c = colors[index];
        if(c == -1) {
            c = LightTree.getInstance().color(LightTree.getInstance().random(0, 255), 255, 255);
        }
        return c;
    }

    public int[] getColors() {
        return colors;
    }
}
