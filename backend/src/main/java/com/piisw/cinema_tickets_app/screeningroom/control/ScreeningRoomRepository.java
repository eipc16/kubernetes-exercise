package com.piisw.cinema_tickets_app.screeningroom.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObjectState;
import com.piisw.cinema_tickets_app.screeningroom.entity.ScreeningRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ScreeningRoomRepository extends JpaRepository<ScreeningRoom, Long>, JpaSpecificationExecutor<ScreeningRoom> {

    Optional<ScreeningRoom> findByIdAndObjectState(Long id, AuditedObjectState objectState);

}
