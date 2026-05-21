package com.glow.courier.domain.model;

import com.glow.user.domain.model.Address;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CourierUserTest {

	@Test
	public void createAddress() {
		// Arrange / Act
		Address a = new Address("Street 1", "City", "Country", 1.0, 2.0);

		// Assert
		assertNotNull(a);
	}
}
