package com.piisw.cinema_tickets_app.domain.reservation.control;

import com.piisw.cinema_tickets_app.domain.reservation.entity.ReservationToSeatRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReservationToSeatRelationRepository extends JpaRepository<ReservationToSeatRelation, Long>, JpaSpecificationExecutor<ReservationToSeatRelation> {

}
