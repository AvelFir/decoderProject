package com.ead.course.controllers;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId") UUID courseId){
        Optional<CourseModel> optionalCourseModel = courseService.findById(courseId);
        if(optionalCourseModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course dont exists");
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(optionalCourseModel.get());
        }
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec,
                                                           @PageableDefault(page = 0,size = 10,sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CourseModel> courses = courseService.findAll(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDTO courseDTO){
        if(courseService.existsByCourseName(courseDTO.getName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: course name is Already Taken!");
        }
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDTO, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseModel));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable(value = "courseId") UUID courseId) {
        Optional<CourseModel> optionalCourseModel = courseService.findById(courseId);
        if (optionalCourseModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        courseService.delete(optionalCourseModel.get());
        return ResponseEntity.status(HttpStatus.OK).body("Course Deleted");
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId,
                                               @RequestBody @Valid CourseDTO courseDTO
    ){
        Optional<CourseModel> optionalCourseModel = courseService.findById(courseId);
        if (optionalCourseModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }

        var courseModel = new CourseModel();
        courseModel.setName(courseDTO.getName());
        courseModel.setDescription(courseDTO.getDescription());
        courseModel.setImageURL(courseDTO.getImageUrl());
        courseModel.setCourseStatus(courseDTO.getCourseStatus());
        courseModel.setCourseLevel(courseDTO.getCourseLevel());
        courseModel.setUserInstructor(courseDTO.getUserInstructor());
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel));
    }


}
