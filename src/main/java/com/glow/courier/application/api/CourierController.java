package com.glow.courier.application.api;

import com.glow.courier.application.api.model.CourierAvailabilityResponse;
import com.glow.courier.application.api.model.UpdateCourierAvailabilityRequest;
import com.glow.courier.application.mappers.CourierUserDtoMapper;
import com.glow.courier.application.model.CourierUserDto;
import com.glow.courier.application.services.CourierService;
import com.glow.user.application.api.model.CreateUserRequest;
import com.glow.user.application.api.model.UpdateUserRequest;
import com.glow.user.domain.shared.GlowRoles;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
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
    private final SecurityIdentity identity;

    public CourierController(CourierService courierService, CourierUserDtoMapper mapper,
                             JsonWebToken jwt, SecurityIdentity identity) {
        this.courierService = courierService;
        this.mapper = mapper;
        this.jwt = jwt;
        this.identity = identity;
    }

    @GET
    @PermitAll
    public RestResponse<String> root() {
        return RestResponse.ok("glow-courier-service is running");
    }

    @GET
    @Path("{id}")
    @RolesAllowed(GlowRoles.SYSADMIN)
    public RestResponse<CourierUserDto> findById(@PathParam("id") String id) {
        return RestResponse.ok(mapper.toDto(
            courierService.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException("Courier with id " + id + " not found"))));
    }

    @POST
    @RolesAllowed(GlowRoles.COURIER)
    public RestResponse<CourierUserDto> create(CreateUserRequest request) {
        var courierUser = courierService.createCourierUser(request, jwt.getSubject());
        return RestResponse.status(RestResponse.Status.CREATED, mapper.toDto(courierUser));
    }

    @PUT
    @Path("{id}")
    @Authenticated
    public RestResponse<CourierUserDto> update(@PathParam("id") String id, UpdateUserRequest request) {
        var courierUser = courierService.updateCourierUser(
            UUID.fromString(id), request, jwt.getSubject(), identity.hasRole(GlowRoles.SYSADMIN));
        return RestResponse.ok(mapper.toDto(courierUser));
    }

    @PUT
    @Path("{id}/availability")
    @Authenticated
    public RestResponse<CourierAvailabilityResponse> updateAvailability(
            @PathParam("id") String id, UpdateCourierAvailabilityRequest request) {
        return RestResponse.ok(courierService.updateCourierAvailability(
            UUID.fromString(id), request, jwt.getSubject(), identity.hasRole(GlowRoles.SYSADMIN)));
    }

    @DELETE
    @Path("{id}")
    @Authenticated
    public RestResponse<Void> deleteById(@PathParam("id") String id) {
        courierService.deleteCourierById(
            UUID.fromString(id), jwt.getSubject(), identity.hasRole(GlowRoles.SYSADMIN));
        return RestResponse.noContent();
    }
}
