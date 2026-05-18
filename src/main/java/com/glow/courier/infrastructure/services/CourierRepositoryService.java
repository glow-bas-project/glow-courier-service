package com.glow.courier.infrastructure.services;

import com.glow.user.domain.model.CourierUser;
import com.glow.courier.domain.repository.CourierRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CourierRepositoryService implements CourierRepository {

    private final Map<UUID, CourierUser> store = new HashMap<>();

    @Override
    public Optional<CourierUser> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void save(CourierUser courierUser) {
        store.put(courierUser.getId(), courierUser);
    }
}
