package com.piisw.cinema_tickets_app.infrastructure.bulk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class BulkOperationResult<T> {

    Map<OperationResultEnum, Set<T>> objectsByOperationResult;

    private BulkOperationResult(Builder<T> builder) {
        this.objectsByOperationResult = builder.objectsByOperationResult;
    }

    @JsonIgnore
    public Set<T> getByOperationResult(OperationResultEnum operationResult) {
        return objectsByOperationResult.getOrDefault(operationResult, Collections.emptySet());
    }

    public <S> BulkOperationResult<S> applyTransform(Function<T, S> transformFunction) {
        BulkOperationResult.Builder<S> builder = BulkOperationResult.builder();
        objectsByOperationResult.keySet()
                .forEach(key -> builder.addAllResults(key, mapObjectsByKey(key, transformFunction)));
        return builder.build();
    }

    private <S> Set<S> mapObjectsByKey(OperationResultEnum operationKey, Function<T, S> transformFunction) {
        return objectsByOperationResult.getOrDefault(operationKey, Collections.emptySet()).stream()
                .map(transformFunction)
                .collect(Collectors.toSet());
    }

    @JsonIgnore
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
