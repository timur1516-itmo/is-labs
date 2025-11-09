package ru.itmo.se.is.shared.dto.exception;

import jakarta.annotation.Nullable;
import lombok.*;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemDetail implements Serializable {

    private String title;

    private int status;

    private String detail;

    private String code;

    @Nullable
    private Map<String, Object> properties;

    public void setProperty(String name, @Nullable Object value) {
        this.properties = (this.properties != null ? this.properties : new LinkedHashMap<>());
        this.properties.put(name, value);
    }
}
