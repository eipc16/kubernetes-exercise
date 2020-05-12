package com.piisw.cinema_tickets_app.domain.reservation.control;

import com.piisw.cinema_tickets_app.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
