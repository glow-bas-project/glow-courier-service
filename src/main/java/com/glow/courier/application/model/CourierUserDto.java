package com.glow.courier.application.model;

import com.glow.user.domain.model.CourierAvailability;
import com.glow.user.domain.model.Permission;
import com.glow.user.domain.model.VehicleType;

import java.util.List;

public record CourierUserDto(
    String id,
    String name,
    String phoneNumber,
    String email,
    List<Permission> permissions,
    VehicleType vehicleType,
    CourierAvailability courierAvailability,
    String stripeAccountId,
    Boolean stripeOnboardingComplete) {
}
