package com.kchmielewski.ai.classifier.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Model<T extends Element> {
    private final List<TraitData<T>> model;

    public Model(List<TraitData<T>> model) {
        this.model = model;
    }

    public String determineGroup(T element) {
        List<TraitData.NearestGroup> distances = new ArrayList<>();
        for (TraitData<T> traitData : model) {
            distances.addAll(traitData.calculateDistances(element));
        }
        Map<String, List<TraitData.NearestGroup>> map = distances.stream()
                .collect(Collectors.groupingBy(TraitData.NearestGroup::getGroupName));
        double min = Double.MAX_VALUE;
        String bestGroup = "";
        for (Map.Entry<String, List<TraitData.NearestGroup>> entry : map.entrySet()) {
            double sum = entry.getValue().stream().collect(Collectors.averagingDouble(TraitData.NearestGroup::getDistance));
            if (sum < min) {
                min = sum;
                bestGroup = entry.getKey();
            }
        }

        return bestGroup;
    }
}
