package com.piisw.cinema_tickets_app.domain.reservation.control;

import com.google.common.collect.Sets;
import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectService;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.authentication.control.AuthenticationService;
import com.piisw.cinema_tickets_app.domain.reservation.entity.Reservation;
import com.piisw.cinema_tickets_app.domain.screening.control.ScreeningService;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.domain.seat.entity.Seat;
import com.piisw.cinema_tickets_app.domain.user.control.UserService;
import com.piisw.cinema_tickets_app.infrastructure.security.UserInfo;
import com.piisw.cinema_tickets_app.infrastructure.security.UserRole;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private static final String CANNOT_MAKE_RESERVATION_FOR_USER = "Cannot make reservation for user with id {0}. User doesn't exist or has inactive account.";
    public static final String NO_PERMISSION_TO_MAKE_OR_REMOVE_RESERVATION = "You don't have sufficient privileges to make/remove reservation for different user than currently logged!";
    public static final String SEATS_ALREADY_RESERVED = "Cannot make reservation for seats {0}. They are already reserved.";
    public static final String CANNOT_RESERVE_AFTER_SCREENING_START = "Cannot make reservation after screening start time.";

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final ReservationRepository reservationRepository;
    private final ReservationSpecification specification;
    private final ReservationToSeatRelationService reservationToSeatRelationService;
    private final AuditedObjectService auditedObjectService;
    private final ScreeningService screeningService;

    public List<Reservation> getReservationsByIds(Set<Long> ids, Set<ObjectState> objectStates) {
        return reservationRepository.findAll(specification.whereIdAndObjectStateIn(ids, objectStates));
    }

    public Reservation createReservation(Reservation reservation, Collection<Seat> seatsToReserve, UserInfo userInfo) {
        Reservation reservationToCreate = reservation.toBuilder()
                .id(null)
                .objectState(ObjectState.ACTIVE)
                .build();
        Screening screening = screeningService.getScreeningById(reservationToCreate.getScreeningId(), ObjectState.ACTIVE);
        validateReservationOnCreate(reservationToCreate, screening, seatsToReserve, userInfo);
        Reservation createdReservation = reservationRepository.save(reservationToCreate);
        reservationToSeatRelationService.createReservationToSeatRelation(createdReservation, seatsToReserve);
        return createdReservation;
    }

    private void validateReservationOnCreate(Reservation reservation, Screening screening, Collection<Seat> seats, UserInfo userInfo) {
        validateReservationUser(reservation, userInfo);
        validateScreeningTime(screening);
        validateSeatsAvailability(screening, seats);
    }

    private void validateReservationUser(Reservation reservation, UserInfo userInfo) {
        validateIfUserFromReservationExists(reservation.getReservedByUser());
        validateIfCanPerformOnBehalfOfSuppliedUser(reservation, userInfo);
    }

    private void validateIfUserFromReservationExists(Long userId) {
        if (!userService.isExistingAndActiveUser(userId)) {
            throw new IllegalArgumentException(MessageFormat.format(CANNOT_MAKE_RESERVATION_FOR_USER, String.valueOf(userId)));
        }
    }

    private void validateIfCanPerformOnBehalfOfSuppliedUser(Reservation reservation, UserInfo currentUser) {
        if (!isModeratorOrAdmin(currentUser) && !currentUserIsUserFromReservation(reservation.getReservedByUser(), currentUser.getId())) {
            throw new IllegalArgumentException(NO_PERMISSION_TO_MAKE_OR_REMOVE_RESERVATION);
        }
    }

    private boolean isModeratorOrAdmin(UserInfo userInfo) {
        return authenticationService.hasRole(userInfo, UserRole.ROLE_MODERATOR)
                || authenticationService.hasRole(userInfo, UserRole.ROLE_ADMIN);
    }

    private boolean currentUserIsUserFromReservation(Long userFromReservation, Long currentUser) {
        return userFromReservation.equals(currentUser);
    }

    private void validateScreeningTime(Screening screening) {
        if (isReservedAfterScreeningStartTime(screening.getStartTime())) {
            throw new IllegalArgumentException(CANNOT_RESERVE_AFTER_SCREENING_START);
        }
    }

    private boolean isReservedAfterScreeningStartTime(LocalDateTime screeningStartTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.compareTo(screeningStartTime) > 0;
    }

    public List<Reservation> getReservationsForScreening(Screening screening, Set<ObjectState> objectStates) {
        return reservationRepository.findAll(specification.whereScreeningIdEqualsAndObjectStateIn(screening.getId(), objectStates));
    }

    private void validateSeatsAvailability(Screening screening, Collection<Seat> seatsToReserve) {
        List<Reservation> alreadyExistingReservations = getReservationsForScreening(screening, Set.of(ObjectState.ACTIVE));
        List<Seat> reservedSeats = getReservedSeats(alreadyExistingReservations, Set.of(ObjectState.ACTIVE));
        Set<Long> alreadyReservedSeatsIds = getAlreadyReservedSeatsIds(reservedSeats, seatsToReserve);
        if (!alreadyReservedSeatsIds.isEmpty()) {
            throw new IllegalArgumentException(MessageFormat.format(SEATS_ALREADY_RESERVED, StringUtils.join(alreadyReservedSeatsIds)));
        }
    }

    public List<Seat> getReservedSeats(List<Reservation> reservations, Set<ObjectState> objectStates) {
        return reservationToSeatRelationService.getReservedSeats(reservations, objectStates);
    }

    public Map<Seat, Long> getReservedSeatsWithUserId(List<Reservation> reservations, Set<ObjectState> objectStates) {
        return reservationToSeatRelationService.getReservedSeatsWithUserId(reservations, objectStates);
    }

    private Set<Long> getAlreadyReservedSeatsIds(Collection<Seat> alreadyReserved, Collection<Seat> seatsToReserve) {
        Set<Long> alreadyReservedIds = auditedObjectService.toSetOfIds(alreadyReserved);
        Set<Long> seatsToReserveIds = auditedObjectService.toSetOfIds(seatsToReserve);
        return Sets.intersection(alreadyReservedIds, seatsToReserveIds);
    }

    public List<Reservation> removeReservationsByIds(Collection<Long> ids, UserInfo userInfo) {
        List<Reservation> reservationsToRemove = reservationRepository
                .findAll(specification.whereIdInAndObjectStateEquals(ids, ObjectState.ACTIVE));
        reservationToSeatRelationService.removeRelationsForReservations(reservationsToRemove);
        reservationsToRemove.forEach(reservation -> validateReservationOnRemove(reservation, userInfo));
        reservationsToRemove.forEach(reservation -> reservation.setObjectState(ObjectState.REMOVED));
        return reservationRepository.saveAll(reservationsToRemove);
    }

    private void validateReservationOnRemove(Reservation reservation, UserInfo userInfo) {
        validateIfCanPerformOnBehalfOfSuppliedUser(reservation, userInfo);
    }
}
