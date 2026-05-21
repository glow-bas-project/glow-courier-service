package com.glow.courier.application.services;

import com.glow.courier.application.api.model.CourierAvailabilityResponse;
import com.glow.courier.application.api.model.UpdateCourierAvailabilityRequest;
import com.glow.courier.domain.repository.CourierRepository;
import com.glow.courier.domain.shared.DomainPrecondition;
import com.glow.user.application.api.model.CreateUserRequest;
import com.glow.user.application.api.model.UpdateUserRequest;
import com.glow.user.domain.model.CourierUser;
import com.glow.user.domain.model.Permission;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CourierService {

    private final CourierRepository repository;

    public CourierService(CourierRepository repository) {
        this.repository = repository;
    }

    public Optional<CourierUser> findById(UUID id) {
        return repository.findById(id);
    }

    public void create(CourierUser courierUser) {
        repository.save(courierUser);
    }

    public CourierUser createCourierUser(CreateUserRequest request, String keycloakSubject) {
        var keycloakId = DomainPrecondition.requireValidUuid(
            keycloakSubject,
            "Courier keycloakId is required and cannot be blank",
            "Courier keycloakId must be a valid UUID");

        var permissions = request.permissions() == null || request.permissions().isEmpty()
            ? List.of(Permission.COURIER)
            : request.permissions();

        var courierUser = CourierUser.builder()
            .keycloakId(keycloakId)
            .name(request.name())
            .phoneNumber(request.phoneNumber())
            .email(request.email())
            .permissions(permissions)
            .vehicleType(request.vehicleType())
            .courierAvailability(request.courierAvailability())
            .courierTransferId(null)
            .stripeAccountId(null)
            .stripeOnboardingComplete(null)
            .build();

        repository.save(courierUser);
        return courierUser;
    }

    public CourierUser updateCourierUser(UUID id, UpdateUserRequest request) {
        var existing = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Courier with id " + id + " not found"));

        var permissions = request.permissions() == null || request.permissions().isEmpty()
            ? existing.getPermissions()
            : request.permissions();

        var updated = CourierUser.builder()
            .id(existing.getId())
            .createdAt(existing.getCreatedAt())
            .updatedAt(Instant.now())
            .keycloakId(existing.getKeycloakId())
            .name(request.name())
            .phoneNumber(request.phoneNumber())
            .email(request.email())
            .permissions(permissions)
            .vehicleType(request.vehicleType())
            .courierAvailability(request.courierAvailability())
            .courierTransferId(existing.getCourierTransferId())
            .stripeAccountId(existing.getStripeAccountId())
            .stripeOnboardingComplete(existing.getStripeOnboardingComplete())
            .build();

        repository.save(updated);
        return updated;
    }

    public CourierAvailabilityResponse updateCourierAvailability(UUID id, UpdateCourierAvailabilityRequest request) {
        var existing = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Courier with id " + id + " not found"));

        var updated = CourierUser.builder()
            .id(existing.getId())
            .createdAt(existing.getCreatedAt())
            .updatedAt(Instant.now())
            .keycloakId(existing.getKeycloakId())
            .name(existing.getName())
            .phoneNumber(existing.getPhoneNumber())
            .email(existing.getEmail())
            .permissions(existing.getPermissions())
            .vehicleType(existing.getVehicleType())
            .courierAvailability(request.courierAvailability())
            .courierTransferId(existing.getCourierTransferId())
            .stripeAccountId(existing.getStripeAccountId())
            .stripeOnboardingComplete(existing.getStripeOnboardingComplete())
            .build();

        repository.save(updated);
        return new CourierAvailabilityResponse(updated.getId(), updated.getCourierAvailability());
    }

    public boolean deleteCourierById(UUID id) {
        return repository.deleteById(id);
    }
}
