package com.glow.courier.application.services;

import com.glow.courier.application.api.model.UpdateCourierAvailabilityRequest;
import com.glow.courier.infrastructure.services.CourierRepositoryService;
import com.glow.user.application.api.model.CreateUserRequest;
import com.glow.user.application.api.model.UpdateUserRequest;
import com.glow.user.domain.model.CourierAvailability;
import com.glow.user.domain.model.CourierUser;
import com.glow.user.domain.model.Permission;
import com.glow.user.domain.model.VehicleType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourierServiceTest {

	@Test
	public void builderProducesCourierUser() {
		// Arrange
		var keycloakId = java.util.UUID.randomUUID();

		// Act
		CourierUser user = CourierUser.builder()
			.keycloakId(keycloakId)
			.name("Test Courier")
			.email("courier@example.com")
			.vehicleType(VehicleType.CAR)
			.build();

		// Assert
		assertEquals("Test Courier", user.getName());
		assertEquals(VehicleType.CAR, user.getVehicleType());
	}

	@Test
	public void updateCourierAvailability_returnsUpdatedStatus() {
		var repository = new CourierRepositoryService();
		var service = new CourierService(repository);
		var keycloakSubject = java.util.UUID.randomUUID().toString();
		// Arrange
		var created = service.createCourierUser(
			new CreateUserRequest(
				"Test Courier",
				"+1234567890",
				"courier@example.com",
				List.of(Permission.COURIER),
				List.of(),
				VehicleType.BIKE,
				CourierAvailability.BUSY,
				null),
			keycloakSubject);

		// Act
		var response = service.updateCourierAvailability(
			created.getId(),
			new UpdateCourierAvailabilityRequest(CourierAvailability.AVAILABLE));

		// Assert
		assertEquals(created.getId(), response.courierId());
		assertEquals(CourierAvailability.AVAILABLE, response.courierAvailability());
	}

	@Test
	public void updateCourierUser_usesSharedUpdateRequest() {
		var repository = new CourierRepositoryService();
		var service = new CourierService(repository);
		var keycloakSubject = java.util.UUID.randomUUID().toString();
		// Arrange
		var created = service.createCourierUser(
			new CreateUserRequest(
				"Test Courier",
				"+1234567890",
				"courier@example.com",
				List.of(Permission.COURIER),
				List.of(),
				VehicleType.BIKE,
				CourierAvailability.BUSY,
				null),
			keycloakSubject);

		// Act
		var updated = service.updateCourierUser(
			created.getId(),
			new UpdateUserRequest(
				"Updated Courier",
				"+1987654321",
				"updated@example.com",
				List.of(Permission.COURIER),
				List.of(),
				VehicleType.CAR,
				CourierAvailability.OFFLINE,
				null));

		// Assert
		assertEquals("Updated Courier", updated.getName());
		assertEquals(CourierAvailability.OFFLINE, updated.getCourierAvailability());
	}
}
