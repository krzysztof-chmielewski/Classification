package com.kchmielewski.ai.classifier.core;

public interface Trait<T extends Element> {
    double calculate(T value);
}
