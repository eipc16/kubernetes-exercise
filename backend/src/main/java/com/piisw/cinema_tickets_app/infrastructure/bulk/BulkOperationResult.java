package com.piisw.cinema_tickets_app.infrastructure.bulk;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BulkOperationResult<T> {

    Map<OperationResultEnum, Set<T>> objectsByOperationResult;

    private BulkOperationResult(Builder<T> builder) {
        this.objectsByOperationResult = builder.objectsByOperationResult;
    }

    public Set<T> getByOperationResult(OperationResultEnum operationResult) {
        return objectsByOperationResult.getOrDefault(operationResult, Collections.emptySet());
    }

    public Set<T> getAll() {
        return objectsByOperationResult.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        Map<OperationResultEnum, Set<T>> objectsByOperationResult;

        private Builder() {
            objectsByOperationResult = new HashMap<>();
        }

        public Builder<T> addResult(OperationResultEnum operationResult, T object) {
            return addAllResults(operationResult, Collections.singleton(object));
        }

        public Builder<T> addAllResults(OperationResultEnum operationResult, Collection<T> objects) {
            if (!objectsByOperationResult.containsKey(operationResult)) {
                objectsByOperationResult.put(operationResult, new HashSet<>());
            }
            objectsByOperationResult.get(operationResult).addAll(objects);
            return this;
        }

        public BulkOperationResult<T> build() {
            return new BulkOperationResult<>(this);
        }
    }
}
