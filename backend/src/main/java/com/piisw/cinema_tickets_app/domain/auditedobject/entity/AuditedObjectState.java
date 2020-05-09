package com.piisw.cinema_tickets_app.domain.auditedobject.entity;

import java.util.Set;

public enum AuditedObjectState {

    ACTIVE,
    INACTIVE,
    REMOVED;

    public static final Set<AuditedObjectState> existingStates() {
        return Set.of(ACTIVE, INACTIVE);
    }

}
