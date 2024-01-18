package com.ead.course.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonDTO {
    @NotBlank
    private String title;
    private String description;

    @NotBlank
    private String videoUrl;
}
