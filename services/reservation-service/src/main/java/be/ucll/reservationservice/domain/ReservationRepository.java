package be.ucll.reservationservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query("SELECT r " +
            "FROM Reservation r " +
            "WHERE " +
            "  r.id <> :reservationId AND " +
            "  r.carId = :carId AND " +
            "  r.status NOT IN (:failureStates) AND " +
            "  ((r.startDate <= :endDate AND r.endDate >= :startDate) OR " +
            "  (r.endDate >= :startDate AND r.startDate <= :endDate))")
    List<Reservation> getReservationsForCarOverlapping(Integer reservationId, Integer carId, OffsetDateTime startDate, OffsetDateTime endDate, List<ReservationStatus> failureStates);
}
