package com.piisw.cinema_tickets_app.domain.auditedobject.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuditedObjectService {

    public Set<Long> toSetOfIds(Collection<? extends AuditedObject> auditedObjects) {
        return auditedObjects.stream()
                .map(AuditedObject::getId)
                .collect(Collectors.toSet());
    }
    
}
