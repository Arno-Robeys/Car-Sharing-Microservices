package be.ucll.reservationservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query("select r " +
            "from Reservation r " +
            "where " +
            "  r.carId = :carId AND " +
            "  ((r.startDate <= :endDate AND r.endDate >= :startDate) OR " +
            "  (r.endDate >= :startDate AND r.startDate <= :endDate))")
    List<Reservation> getReservationsForCarOverlapping(Integer carId, OffsetDateTime startDate, OffsetDateTime endDate);
}
