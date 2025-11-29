package ru.itmo.se.is.feature.movie.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.se.is.feature.movie.domain.value.MovieGenre;
import ru.itmo.se.is.feature.movie.domain.value.MpaaRating;
import ru.itmo.se.is.platform.validation.annotation.AllowedValues;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoviePagingAndSortingBeanParamDto implements Serializable {
    @Min(0)
    @DefaultValue("0")
    @QueryParam("first")
    private int first;

    @DefaultValue("10")
    @QueryParam("pageSize")
    private int pageSize;

    @AllowedValues({"id", "name", "creationDate", "oscarsCount", "mpaaRating", "budget", "totalBoxOffice", "length", "goldenPalmCount", "usaBoxOffice", "tagline", "genre"})
    @DefaultValue("id")
    @QueryParam("sortField")
    private String sortField;

    @AllowedValues({"-1", "1"})
    @DefaultValue("1")
    @QueryParam("sortOrder")
    private int sortOrder;

    @QueryParam("id")
    private Long idFilter;

    @QueryParam("name")
    private String nameFilter;

    @QueryParam("genre")
    private MovieGenre genreFilter;

    @QueryParam("mpaaRating")
    private MpaaRating mpaaRatingFilter;

    @QueryParam("tagline")
    private String taglineFilter;
}
