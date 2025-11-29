package ru.itmo.se.is.feature.movie.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import ru.itmo.se.is.feature.movie.api.dto.MoviePagingAndSortingBeanParamDto;
import ru.itmo.se.is.feature.movie.api.dto.MovieRequestDto;
import ru.itmo.se.is.feature.movie.api.dto.MovieResponseDto;
import ru.itmo.se.is.feature.movie.application.MovieService;
import ru.itmo.se.is.feature.movie.infrastructure.mapper.MovieMapper;
import ru.itmo.se.is.shared.notification.NotificationMessageType;
import ru.itmo.se.is.shared.notification.NotificationService;

import java.net.URI;

@Path("/movies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MovieController {

    @Inject
    private MovieService service;
    @Inject
    private NotificationService notificationService;
    @Inject
    private MovieMapper movieMapper;

    @GET
    public Response getAllMovies(@BeanParam MoviePagingAndSortingBeanParamDto dto) {
        return Response.ok(service.getPagingAndSorting(dto)).build();
    }

    @POST
    public Response createMovie(@Context UriInfo uriInfo, MovieRequestDto dto) {
        MovieResponseDto createdMovie = service.create(dto);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path("{id}")
                .resolveTemplate("id", createdMovie.getId())
                .build();

        notificationService.notifyAll(NotificationMessageType.MOVIE);

        return Response.created(location).entity(createdMovie).build();
    }

    @PATCH
    @Path("/{id}")
    public Response updateMovie(@PathParam("id") long id, MovieRequestDto dto) {
        service.update(id, dto);
        notificationService.notifyAll(NotificationMessageType.MOVIE);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMovie(@PathParam("id") long id) {
        service.delete(id);
        notificationService.notifyAll(NotificationMessageType.MOVIE);
        return Response.noContent().build();
    }

    @GET
    @Path("/count-by-tagline/{tagline}")
    public Response countByTagline(@PathParam("tagline") String tagline) {
        return Response.ok(service.countByTagline(tagline)).build();
    }

    @GET
    @Path("/count-less-than-golden-palm/{count}")
    public Response countLessThanGoldenPalm(@PathParam("count") long count) {
        return Response.ok(service.countLessThanGoldenPalm(count)).build();
    }

    @GET
    @Path("/count-greater-than-golden-palm/{count}")
    public Response countGreaterThanGoldenPalm(@PathParam("count") long count) {
        return Response.ok(service.countGreaterThanGoldenPalm(count)).build();
    }

    @GET
    @Path("/directors-without-oscars")
    public Response getDirectorsWithoutOscars() {
        return Response.ok(service.getDirectorsWithoutOscars()).build();
    }

    @PATCH
    @Path("/add-oscar-to-r-rated")
    public Response addOscarToRated() {
        service.addOscarToRated();
        notificationService.notifyAll(NotificationMessageType.MOVIE);
        return Response.noContent().build();
    }
}
