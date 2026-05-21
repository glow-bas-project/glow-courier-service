package com.glow.courier.application.mappers;

import com.glow.courier.application.model.CourierUserDto;
import jakarta.enterprise.context.ApplicationScoped;
import com.glow.user.domain.model.CourierUser;

import java.util.UUID;

@ApplicationScoped
public class CourierUserDtoMapper {

    public CourierUserDto toDto(CourierUser courierUser) {
        if (courierUser == null) {
            return null;
        }

        return new CourierUserDto(
            courierUser.getId().toString(),
            courierUser.getName(),
            courierUser.getPhoneNumber(),
            courierUser.getEmail(),
            courierUser.getPermissions(),
            courierUser.getVehicleType(),
            courierUser.getCourierAvailability(),
            courierUser.getCourierTransferId(),
            courierUser.getStripeAccountId(),
            courierUser.getStripeOnboardingComplete());
    }

    public CourierUser toDomain(CourierUserDto dto) {
        if (dto == null) {
            return null;
        }

        CourierUser.Builder builder = CourierUser.builder()
            .vehicleType(dto.vehicleType())
            .courierAvailability(dto.courierAvailability())
            .stripeAccountId(dto.stripeAccountId())
            .stripeOnboardingComplete(dto.stripeOnboardingComplete());

        builder.id(dto.id() == null ? null : UUID.fromString(dto.id()));
        builder.name(dto.name());
        builder.phoneNumber(dto.phoneNumber());
        builder.email(dto.email());
        builder.permissions(dto.permissions());

        return (CourierUser) builder.build();
    }
}
