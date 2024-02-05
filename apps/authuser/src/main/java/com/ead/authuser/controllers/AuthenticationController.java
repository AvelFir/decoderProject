package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.enuns.UserStatus;
import com.ead.authuser.enuns.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
@Log4j2
public class AuthenticationController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody
                                                   @Validated(UserDTO.UserView.RegistrationPost.class)
                                                   @JsonView(UserDTO.UserView.RegistrationPost.class
                                                   ) UserDTO userDTO){
        log.debug("POST registerUser userDto received {}", userDTO.toString());
        if(userService.existsByUserName(userDTO.getUsername())){
            log.warn("Username {} is Already Taken", userDTO.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already Taken!");
        }

        if(userService.existsByEmail(userDTO.getEmail())){
            log.warn("Email {} is Already Taken", userDTO.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is Already Taken!");
        }

        var userModel = new UserModel();
        BeanUtils.copyProperties(userDTO, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        log.debug("POST registerUser userId saved {}", userModel.getUserId());
        log.info("User saved successfully userId {}", userModel.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);

    }



}
