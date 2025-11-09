package ru.itmo.se.is.feature.movie.api.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.se.is.feature.movie.api.dto.coordinates.CoordinatesRequestDto;
import ru.itmo.se.is.feature.movie.domain.value.MovieGenre;
import ru.itmo.se.is.feature.movie.domain.value.MpaaRating;
import ru.itmo.se.is.feature.person.api.dto.PersonRequestDto;
import ru.itmo.se.is.platform.validation.annotation.ValidEmbedded;
import ru.itmo.se.is.shared.dto.common.EmbeddedObjectDto;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequestDto implements Serializable {
    @NotBlank
    private String name;

    @NotNull
    @Valid
    private CoordinatesRequestDto coordinates;

    @NotNull
    @Positive
    private Integer oscarsCount;

    @NotNull
    @Positive
    private Float budget;

    @NotNull
    @Positive
    private Integer totalBoxOffice;

    @Nullable
    private MpaaRating mpaaRating;

    @ValidEmbedded
    @Valid
    private EmbeddedObjectDto<Long, PersonRequestDto> directorReference;

    @ValidEmbedded(nullable = true)
    @Valid
    private EmbeddedObjectDto<Long, PersonRequestDto> screenwriterReference;

    @ValidEmbedded(nullable = true)
    @Valid
    private EmbeddedObjectDto<Long, PersonRequestDto> operatorReference;

    @Nullable
    @Positive
    private Integer length;

    @NotNull
    @Positive
    private Integer goldenPalmCount;

    @NotNull
    @Positive
    private Integer usaBoxOffice;

    @NotNull
    private String tagline;

    @Nullable
    private MovieGenre genre;
}
