package com.ead.course.controllers;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.dtos.ModuleDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private CourseService courseService;

    @GetMapping("courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<?> getOneModule(@PathVariable(value = "courseId") UUID courseId,
                                          @PathVariable(value = "moduleId") UUID moduleId){
        Optional<ModuleModel> optionalModule = moduleService.findModuleIntoCourse(courseId, moduleId);
        if (optionalModule.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalModule.get());
    }

    @GetMapping("courses/{courseId}/modules")
    public ResponseEntity<Page<ModuleModel>> getAllModules(@PathVariable(value = "courseId") UUID courseId,
                                                           SpecificationTemplate.ModuleSpec spec,
                                                           @PageableDefault(page = 0,size = 10,sort = "moduleId",direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.findAllByCourse(SpecificationTemplate.moduleCourseId(courseId).and(spec), pageable));
    }

    @PostMapping("courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable(value = "courseId") UUID courseId,
                                             @RequestBody @Valid ModuleDTO moduleDto){
        Optional<CourseModel> optionalCourseModel = courseService.findById(courseId);
        if (optionalCourseModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found");
        }
        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDto, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(optionalCourseModel.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel));
    }

    @PutMapping("courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModel(@PathVariable(value = "courseId") UUID courseId,
                                             @PathVariable(value = "moduleId") UUID moduleId,
                                             @RequestBody @Valid ModuleDTO moduleDto){
        Optional<ModuleModel> optionalModule = moduleService.findModuleIntoCourse(courseId, moduleId);
        if (optionalModule.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found");
        }
        var moduleModel = optionalModule.get();
        moduleModel.setTitle(moduleDto.getTitle());
        moduleModel.setDescription(moduleDto.getDescription());
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.save(moduleModel));
    }

    @DeleteMapping("courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<?> deleteModule(@PathVariable(value = "courseId") UUID courseId,
                                          @PathVariable(value = "moduleId") UUID moduleId){

        Optional<ModuleModel> optionalModule = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(optionalModule.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not exists");
        }
        moduleService.delete(optionalModule.get());
        return ResponseEntity.status(HttpStatus.OK).body("Module Deleted");
    }

}
