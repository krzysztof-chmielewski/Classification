package com.kchmielewski.ai.classifier.letter.traits;

import com.kchmielewski.ai.classifier.core.Trait;
import com.kchmielewski.ai.classifier.letter.Letter;

public class HigherQuarterToAll implements Trait<Letter> {
    @Override
    public double calculate(Letter value) {
        boolean[][]  image = value.getImage();
        int center = image[0].length/4;
        int blackPixels = 0;
        int whitePixels = 0;
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                if (image[i][j]) {
                    if (i < center) {
                        blackPixels++;
                    } else {
                        whitePixels++;
                    }
                }
            }
        }
        return (double) blackPixels / (blackPixels + whitePixels);
    }
}
