package com.ead.course.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ModuleDTO {


    @NotBlank
    private String title;

    @NotBlank
    private String description;

}
