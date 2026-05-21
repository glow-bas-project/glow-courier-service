package com.glow.courier.application.api.model;

import com.glow.user.domain.model.CourierAvailability;

public record UpdateCourierAvailabilityRequest(
    CourierAvailability courierAvailability) {
}
