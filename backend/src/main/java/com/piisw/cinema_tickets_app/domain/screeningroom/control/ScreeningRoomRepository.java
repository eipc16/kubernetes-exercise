package com.piisw.cinema_tickets_app.domain.screeningroom.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.screeningroom.entity.ScreeningRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ScreeningRoomRepository extends JpaRepository<ScreeningRoom, Long>, JpaSpecificationExecutor<ScreeningRoom> {

    Optional<ScreeningRoom> findByIdAndObjectState(Long id, ObjectState objectState);

}
