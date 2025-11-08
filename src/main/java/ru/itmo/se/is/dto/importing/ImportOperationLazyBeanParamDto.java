package ru.itmo.se.is.dto.importing;

import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.se.is.annotation.AllowedValues;
import ru.itmo.se.is.entity.value.Color;
import ru.itmo.se.is.entity.value.Country;
import ru.itmo.se.is.entity.value.ImportStatus;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportOperationLazyBeanParamDto implements Serializable {
    @Min(0)
    @DefaultValue("0")
    @QueryParam("first")
    private int first;

    @DefaultValue("10")
    @QueryParam("pageSize")
    private int pageSize;

    @AllowedValues({"id", "status", "startDt", "endDt", "importCnt", "errorMessage"})
    @DefaultValue("id")
    @QueryParam("sortField")
    private String sortField;

    @AllowedValues({"-1", "1"})
    @DefaultValue("1")
    @QueryParam("sortOrder")
    private int sortOrder;

    @QueryParam("id")
    private String idFilter;

    @QueryParam("status")
    private ImportStatus statusFilter;
}
