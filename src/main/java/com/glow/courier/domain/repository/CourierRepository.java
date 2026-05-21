package com.glow.courier.domain.repository;

import com.glow.user.domain.model.CourierUser;
import java.util.Optional;
import java.util.UUID;

public interface CourierRepository {
    Optional<CourierUser> findById(UUID id);
    void save(CourierUser courierUser);
    boolean deleteById(UUID id);
}
