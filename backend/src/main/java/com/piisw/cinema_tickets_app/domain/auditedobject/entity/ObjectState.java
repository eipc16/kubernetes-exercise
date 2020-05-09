package com.piisw.cinema_tickets_app.domain.auditedobject.entity;

import java.util.Set;

public enum ObjectState {

    ACTIVE,
    INACTIVE,
    REMOVED;

    public static final Set<ObjectState> existingStates() {
        return Set.of(ACTIVE, INACTIVE);
    }

}
