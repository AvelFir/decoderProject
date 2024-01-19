package com.ead.course.controllers;

import com.ead.course.dtos.LessonDTO;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
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
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
public class LessonController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private LessonService lessonService;

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<?> getOneLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                          @PathVariable(value = "lessonId") UUID lessonId){
        Optional<LessonModel> lessonOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (lessonOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(lessonOptional.get());
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Page<LessonModel>> getAllLessons(@PathVariable(value = "moduleId") UUID moduleId,
                                                           SpecificationTemplate.LessonSpec spec,
                                                           @PageableDefault(page = 0,size = 10,sort = "lessonId",direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findAllByModule(SpecificationTemplate.lessonModuleId(moduleId).and(spec),pageable));
    }

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                             @RequestBody @Valid LessonDTO lessonDto){
        Optional<ModuleModel> optionalModule = moduleService.findById(moduleId);
        if (optionalModule.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found");
        }
        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(optionalModule.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel));
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                             @PathVariable(value = "lessonId") UUID lessonId,
                                             @RequestBody @Valid LessonDTO lessonDto){
        Optional<LessonModel> optionalLesson = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (optionalLesson.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found");
        }
        var lessonModel = optionalLesson.get();
        lessonModel.setTitle(lessonDto.getTitle());
        lessonModel.setDescription(lessonDto.getDescription());
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.save(lessonModel));
    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<?> deleteLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                          @PathVariable(value = "lessonId") UUID lessonId){

        Optional<LessonModel> optionalLesson = lessonService.findLessonIntoModule(moduleId, lessonId);
        if(optionalLesson.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not exists");
        }
        lessonService.delete(optionalLesson.get());
        return ResponseEntity.status(HttpStatus.OK).body("Module Deleted");
    }

}
