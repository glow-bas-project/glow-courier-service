package com.glow.courier.application.api.model;

import com.glow.user.domain.model.CourierAvailability;

import java.util.UUID;

public record CourierAvailabilityResponse(
    UUID courierId,
    CourierAvailability courierAvailability) {
}
