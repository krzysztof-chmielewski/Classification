package com.kchmielewski.ai.classifier.core;

import java.util.*;
import java.util.stream.Collectors;

public class TraitData<T extends Element> {
    private final Trait<T> trait;
    private final Map<String, Double> averageValues;

    public TraitData(Trait<T> trait, List<T> elements) {
        this.trait = trait;
        this.averageValues = calculateAverageValues(trait, elements);
    }

    private Map<String, Double> calculateAverageValues(Trait<T> trait, List<T> elements) {
        Map<String, List<Double>> results = new HashMap<>();
        for (T element : elements) {
            assert element.group().isPresent();
            String name = element.group().get();
            double value = trait.calculate(element);
            if (!results.containsKey(name)) {
                results.put(name, new ArrayList<>());
            }
            results.get(element.group().get()).add(value);
        }

        Map<String, Double> result = new HashMap<>(results.size());
        for (Map.Entry<String, List<Double>> entry : results.entrySet()) {
            result.put(entry.getKey(), average(entry.getValue()));
        }

        return result;
    }

    private double average(List<Double> numbers) {
        double sum = 0;
        for (Double number : numbers) {
            sum += number;
        }

        return sum / numbers.size();
    }

    public Trait<T> getTrait() {
        return trait;
    }

    public Map<String, Double> getAverageValues() {
        return averageValues;
    }

    public SortedSet<NearestGroup> calculateDistances(T element) {
        SortedSet<NearestGroup> distances = new TreeSet<>();
        for (Map.Entry<String, Double> value: averageValues.entrySet()) {
            distances.add(new NearestGroup(value.getKey(), Math.abs(value.getValue() - trait.calculate(element))));
        }

        return distances;
    }

    public static class NearestGroup implements Comparable<NearestGroup> {
        private final String groupName;
        private final double distance;

        public NearestGroup(String groupName, double distance) {
            this.groupName = groupName;
            this.distance = distance;
        }

        public String getGroupName() {
            return groupName;
        }

        public double getDistance() {
            return distance;
        }

        @Override
        public String toString() {
            return "NearestGroup{" +
                    "groupName='" + groupName + '\'' +
                    ", distance=" + distance +
                    '}';
        }

        @Override
        public int compareTo(NearestGroup o) {
            return Double.compare(distance, o.distance);
        }
    }
}
