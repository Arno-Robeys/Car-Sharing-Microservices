package be.ucll.reservationservice.domain;

public enum ReservationStatus {
    // Happy Flow
    REGISTERED,
    VALIDATING_USER,
    RESERVING_CAR,
    CONFIRMING_RESERVATION,
    BILLING_USER,
    // Failure States
    NO_VALID_USER,
    NO_CAR,
    DOUBLE_BOOKING,
    OWNER_DECLINES,
    // Final States
    COMPLETED,
    CANCELED,
    LISTING_REMOVED
    }
