package com.kchmielewski.ai.classifier.example;

import com.kchmielewski.ai.classifier.core.Model;
import com.kchmielewski.ai.classifier.core.Trait;
import com.kchmielewski.ai.classifier.core.TraitData;
import com.kchmielewski.ai.classifier.letter.Letter;
import com.kchmielewski.ai.classifier.letter.traits.HigherQuarterToAll;
import com.kchmielewski.ai.classifier.letter.traits.HigherToAll;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        String directoryName = "src/main/resources";
        String[] files = new File(directoryName).list();
        if (files == null) {
            throw new RuntimeException(String.format("Directory %s is empty or IO error occurred.", directoryName));
        }
        List<String> fileNames = Arrays.asList(files);
        List<Letter> learningSet = fileNames.stream()
                .filter(f -> !f.startsWith("0"))
                .map(f -> new Letter(f, "src/main/resources/" + f))
                .collect(Collectors.toList());
        List<Letter> testSet = fileNames.stream()
                .filter(f -> f.startsWith("0"))
                .map(f -> new Letter(f, "src/main/resources/" + f))
                .collect(Collectors.toList());

        List<TraitData<Letter>> traitData = Arrays.asList(
                new TraitData<>(new HigherToAll(), learningSet),
                new TraitData<>(new HigherQuarterToAll(), learningSet));
        Model<Letter> model = new Model<>(traitData);

        testSet.forEach(l -> System.out.println("Determined " + l.getName() + " as " + model.determineGroup(l)));


    }
}
