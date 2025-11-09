package ru.itmo.se.is.feature.movie.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieLazyResponseDto implements Serializable {
    List<MovieResponseDto> data;
    Long totalRecords;
}
