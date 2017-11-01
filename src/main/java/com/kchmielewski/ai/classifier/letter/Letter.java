package com.kchmielewski.ai.classifier.letter;

import com.kchmielewski.ai.classifier.core.Element;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Letter implements Element {
    private final String name;
    private final String group;
    private final boolean[][] image;

    public Letter(String name, String path)  {
        this.name = name;
        this.group = name == null ? null : name.substring(0, 1);
        this.image = determineImage(path);
    }
    public Letter(String path)  {
        this(null, path);
    }

    private boolean[][] determineImage(String path) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        int black = Color.BLACK.getRGB();
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if (image.getRGB(i, j) == black) {
                    if (i < minX) {
                        minX = i;
                    }
                    if (j < minY) {
                        minY = j;
                    }
                    if (i > maxX) {
                        maxX = i;
                    }
                    if (j > maxY) {
                        maxY = j;
                    }
                }
            }
        }

        boolean result[][] = new boolean[maxY - minY][maxX - minX];

        for (int i = minY; i < maxY; i++) {
            for (int j = minX; j < maxX; j++) {
                result[i - minY][j - minX] = image.getRGB(j, i) == black;
            }
        }

        return result;
    }

    public String getName() {
        return name;
    }

    public boolean[][] getImage() {
        return image;
    }

    @Override
    public Optional<String> group() {
        return Optional.of(group);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(name + "\n");
        for (boolean[] column : image) {
            for (int j = 0; j < image[0].length; j++) {
                if (column[j]) {
                    builder.append('*');
                } else {
                    builder.append(' ');
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
