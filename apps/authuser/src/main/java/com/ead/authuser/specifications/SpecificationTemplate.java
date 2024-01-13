package com.ead.authuser.specifications;

import com.ead.authuser.models.UserModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationTemplate {

    //AND soma as specs
    //OR pega um ou outro
    @And({
          @Spec(path = "userType", spec = Equal.class),
          @Spec(path = "userStatus", spec = Equal.class),
          @Spec(path = "email", spec = Like.class)
    })
    public interface UserSpec extends Specification<UserModel> {

    }
}
