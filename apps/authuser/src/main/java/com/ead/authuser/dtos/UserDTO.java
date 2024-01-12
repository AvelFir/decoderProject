package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserDTO {

    public interface UserView{
        public static interface RegistrationPost{}
        public static interface UserPut{}
        public static interface PasswordPut{}
        public static interface ImagePut{}
    }

    @JsonView(UserView.RegistrationPost.class)
    private String username;

    @JsonView(UserView.RegistrationPost.class)
    private String email;

    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String password;

    @JsonView(UserView.PasswordPut.class)
    private String oldPassword;

    @JsonView(UserView.UserPut.class)
    private String fullName;

    @JsonView(UserView.UserPut.class)
    private String phoneNumber;

    @JsonView(UserView.UserPut.class)
    private String cpf;

    @JsonView(UserView.ImagePut.class)
    private String imageURL;
}
