package com.ead.course.specifications;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {
    @And({
            @Spec(path = "courseLevel", spec = Equal.class),
            @Spec(path = "courseStatus", spec = Equal.class),
            @Spec(path = "name", spec = Like.class)
    })
    public interface CourseSpec extends Specification<CourseModel> {}


    @Spec(path = "title", spec = Like.class)
    public interface ModuleSpec extends Specification<ModuleModel> {}

    @Spec(path = "title", spec = Like.class)
    public interface LessonSpec extends Specification<LessonModel> {}

    public static Specification<ModuleModel> moduleCourseId(final UUID courseId){
        return ((root, critQuery, critBuild) -> {
            critQuery.distinct(true);
            Root<ModuleModel> module = root;
            Root<CourseModel> course = critQuery.from(CourseModel.class);
            Expression<Collection<ModuleModel>> coursesModules = course.get("modules");
            return critBuild.and(critBuild.equal(course.get("courseId"),courseId),critBuild.isMember(module, coursesModules));
        });
    };

    public static Specification<LessonModel> lessonModuleId(final UUID moduleId){
        return ((root, critQuery, critBuild) -> {
            critQuery.distinct(true);
            Root<LessonModel> lesson = root;
            Root<ModuleModel> module = critQuery.from(ModuleModel.class);
            Expression<Collection<LessonModel>> lessonModules = module.get("lessons");
            return critBuild.and(critBuild.equal(module.get("moduleId"),moduleId),critBuild.isMember(lesson, lessonModules));
        });
    };
}
