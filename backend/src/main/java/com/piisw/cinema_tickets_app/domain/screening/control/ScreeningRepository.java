package com.piisw.cinema_tickets_app.domain.screening.control;

import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long>, JpaSpecificationExecutor<Screening> {

}
