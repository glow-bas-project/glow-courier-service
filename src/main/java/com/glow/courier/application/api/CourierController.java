package com.glow.courier.application.api;

import com.glow.courier.application.api.model.CourierAvailabilityResponse;
import com.glow.courier.application.api.model.UpdateCourierAvailabilityRequest;
import com.glow.courier.application.mappers.CourierUserDtoMapper;
import com.glow.courier.application.model.CourierUserDto;
import com.glow.courier.application.services.CourierService;
import com.glow.user.application.api.model.CreateUserRequest;
import com.glow.user.application.api.model.UpdateUserRequest;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.UUID;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CourierController {

    private final CourierService courierService;
    private final CourierUserDtoMapper mapper;
    private final JsonWebToken jwt;

    public CourierController(CourierService courierService, CourierUserDtoMapper mapper, JsonWebToken jwt) {
        this.courierService = courierService;
        this.mapper = mapper;
        this.jwt = jwt;
    }

    @GET
    @PermitAll
    public RestResponse<String> root() {
        return RestResponse.ok("glow-courier-service is running");
    }

    @GET
    @Path("{id:[0-9a-fA-F\\-]{36}}")
    public RestResponse<CourierUserDto> findById(@PathParam("id") String id) {
        return RestResponse.ok(mapper.toDto(
            courierService.findById(UUID.fromString(id))
                .orElseThrow(() -> new jakarta.ws.rs.NotFoundException("Courier with id " + id + " not found"))));
    }

    @POST
    public RestResponse<CourierUserDto> create(CreateUserRequest request) {
        var courierUser = courierService.createCourierUser(request, jwt.getSubject());
        return RestResponse.status(RestResponse.Status.CREATED, mapper.toDto(courierUser));
    }

    @PUT
    @Path("{id:[0-9a-fA-F\\-]{36}}")
    public RestResponse<CourierUserDto> update(@PathParam("id") String id, UpdateUserRequest request) {
        var courierUser = courierService.updateCourierUser(UUID.fromString(id), request);
        return RestResponse.ok(mapper.toDto(courierUser));
    }

    @PUT
    @Path("{id:[0-9a-fA-F\\-]{36}}/availability")
    public RestResponse<CourierAvailabilityResponse> updateAvailability(@PathParam("id") String id,
                                                                         UpdateCourierAvailabilityRequest request) {
        return RestResponse.ok(courierService.updateCourierAvailability(UUID.fromString(id), request));
    }
}
