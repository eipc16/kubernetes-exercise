package com.piisw.cinema_tickets_app.domain.reservation.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.authentication.control.AuthenticationService;
import com.piisw.cinema_tickets_app.domain.reservation.entity.Reservation;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.domain.seat.entity.Seat;
import com.piisw.cinema_tickets_app.domain.user.control.UserService;
import com.piisw.cinema_tickets_app.infrastructure.security.UserInfo;
import com.piisw.cinema_tickets_app.infrastructure.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class ReservationService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationSpecification specification;

    private static final String CANNOT_MAKE_RESERVATION_FOR_USER = "Cannot make reservation for user with id {0}. User doesn't exist or has inactive account.";
    private static final String NO_PERMISSION_TO_MAKE_RESERVATION = "You don't have sufficient privileges to make reservation for different user than currently logged!";

    public void validateReservation(Reservation reservation, Screening screening, List<Seat> seats, UserInfo userInfo) {
        validateReservationUser(reservation, userInfo);
        validateScreeningTime(reservation, screening);

    }

    private void validateReservationUser(Reservation reservation, UserInfo userInfo) {
        validateIfUserFromReservationExists(reservation.getReservedByUser());
        validateIfCanReserveOnBehalfOfSuppliedUser(reservation, userInfo);
    }

    private void validateIfUserFromReservationExists(Long userId) {
        if (!userService.isExistingAndActiveUser(userId)) {
            throw new IllegalArgumentException(MessageFormat.format(CANNOT_MAKE_RESERVATION_FOR_USER, String.valueOf(userId)));
        }
    }

    private void validateIfCanReserveOnBehalfOfSuppliedUser(Reservation reservation, UserInfo currentUser) {
        if (!isModeratorOrAdmin(currentUser) && !currentUserIsUserFromReservation(reservation.getReservedByUser(), currentUser.getId())) {
            throw new IllegalArgumentException(NO_PERMISSION_TO_MAKE_RESERVATION);
        }
    }

    private boolean isModeratorOrAdmin(UserInfo userInfo) {
        return authenticationService.hasRole(userInfo, UserRole.ROLE_MODERATOR)
                || authenticationService.hasRole(userInfo, UserRole.ROLE_ADMIN);
    }

    private boolean currentUserIsUserFromReservation(Long userFromReservation, Long currentUser) {
        return userFromReservation.equals(currentUser);
    }

    private void validateScreeningTime(Reservation reservation, Screening screening) {
        if (isReservedAfterScreeningStartTime(screening.getStartTime())) {
            throw new IllegalArgumentException("Cannot make reservation after screening start time.");
        }
    }

    private boolean isReservedAfterScreeningStartTime(LocalDateTime screeningStartTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.compareTo(screeningStartTime) > 0;
    }

    public List<Reservation> getReservationsForScreening(Screening screening, Set<ObjectState> objectStates) {
        return reservationRepository.findAll(specification.whereScreeningIdEqualsAndObjectStateIn(screening.getId(), objectStates));
    }

}
