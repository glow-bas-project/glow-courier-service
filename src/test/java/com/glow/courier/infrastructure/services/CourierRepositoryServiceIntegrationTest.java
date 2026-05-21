package com.glow.courier.infrastructure.services;

import com.glow.user.domain.model.CourierUser;
import com.glow.user.domain.model.VehicleType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourierRepositoryServiceIntegrationTest {

	@Test
	public void saveAndFind() {
		// Arrange
		CourierRepositoryService repo = new CourierRepositoryService();
		CourierUser user = CourierUser.builder()
			.keycloakId(java.util.UUID.randomUUID())
			.name("Courier")
			.email("c@example.com")
			.vehicleType(VehicleType.CAR)
			.build();

		// Act
		repo.save(user);

		// Assert
		assertTrue(repo.findById(user.getId()).isPresent());
	}
}
