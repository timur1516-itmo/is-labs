package ru.itmo.se.is.feature.person.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import ru.itmo.se.is.feature.person.api.dto.PersonLazyBeanParamDto;
import ru.itmo.se.is.feature.person.api.dto.PersonRequestDto;
import ru.itmo.se.is.feature.person.api.dto.PersonResponseDto;
import ru.itmo.se.is.feature.person.application.PersonService;
import ru.itmo.se.is.feature.person.infrastructure.mapper.PersonMapper;
import ru.itmo.se.is.shared.notification.NotificationMessageType;
import ru.itmo.se.is.shared.notification.NotificationService;

import java.net.URI;

@Path("/people")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonController {

    @Inject
    private PersonService service;
    @Inject
    private NotificationService notificationService;
    @Inject
    private PersonMapper personMapper;

    @GET
    public Response getAllPeople(@BeanParam PersonLazyBeanParamDto lazyBeanParamDto) {
        return Response.ok(service.lazyGet(lazyBeanParamDto)).build();
    }

    @POST
    public Response createPerson(@Context UriInfo uriInfo, PersonRequestDto dto) {
        PersonResponseDto createdPerson = personMapper.toDto(service.create(dto));

        URI location = uriInfo.getAbsolutePathBuilder()
                .path("{id}")
                .resolveTemplate("id", createdPerson.getId())
                .build();
        notificationService.notifyAll(NotificationMessageType.PERSON);
        return Response.created(location).entity(createdPerson).build();
    }

    @PATCH
    @Path("/{id}")
    public Response updatePerson(@PathParam("id") long id, PersonRequestDto dto) {
        service.update(id, dto);
        notificationService.notifyAll(NotificationMessageType.PERSON);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") long id) {
        service.delete(id);
        notificationService.notifyAll(NotificationMessageType.PERSON);
        return Response.noContent().build();
    }
}
