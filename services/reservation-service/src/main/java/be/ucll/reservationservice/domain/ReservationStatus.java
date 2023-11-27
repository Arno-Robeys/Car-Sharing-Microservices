package be.ucll.reservationservice.domain;

public enum ReservationStatus {
    // Happy Flow
    REGISTERED,
    VALIDATING_USER,
    RESERVING_CAR,
    CONFIRMING_RESERVATION,
    BILLING_USER,
    NOTIFYING_USER,
    // Failure States
    NO_VALID_USER,
    NO_CAR,
    DOUBLE_BOOKING,
    OWNER_DECLINES,
    BILLING_USER_FAILED,
    NOTIFYING_USER_FAILED,
    // Final States
    COMPLETED,
    CANCELED,
    LISTING_REMOVED
    }
