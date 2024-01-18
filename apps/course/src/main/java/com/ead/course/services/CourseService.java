package com.ead.course.services;

import com.ead.course.models.CourseModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseService {

    Optional<CourseModel> findById(UUID courseId);
    void delete(CourseModel courseModel);

    List<CourseModel> findAll();

    CourseModel save(CourseModel courseModel);

    boolean existsByCourseName(String name);
}
