package com.piisw.cinema_tickets_app.infrastructure.utils;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.Set;

@UtilityClass
public class ExceptionUtils {

    private static final String OBJECT_NOT_FOUND_MSG = "There is no {0} with id {1} and object state {2}";

    public IllegalArgumentException getObjectNotFoundException(Class<?> objectType, Long id, ObjectState objectState) {
        return new IllegalArgumentException(MessageFormat.format(OBJECT_NOT_FOUND_MSG, objectType.getSimpleName(), String.valueOf(id), objectState));
    }

    public IllegalArgumentException getObjectNotFoundException(Class<?> objectType, Long id, Set<ObjectState> objectStates) {
        return new IllegalArgumentException(MessageFormat.format(OBJECT_NOT_FOUND_MSG, objectType.getSimpleName(), String.valueOf(id), StringUtils.join(objectStates, " or ")));
    }

    public IllegalArgumentException getObjectNotFoundException(Class<?> objectType, Set<Long> ids, ObjectState objectState) {
        return new IllegalArgumentException(MessageFormat.format(OBJECT_NOT_FOUND_MSG, objectType.getSimpleName(), StringUtils.join(ids), StringUtils.join(objectState, " or ")));
    }

    public IllegalArgumentException getObjectNotFoundException(Class<?> objectType, Set<Long> ids, Set<ObjectState> objectStates) {
        return new IllegalArgumentException(MessageFormat.format(OBJECT_NOT_FOUND_MSG, objectType.getSimpleName(), StringUtils.join(ids), StringUtils.join(objectStates, " or ")));
    }

}
