package com.project.shopapp.controllers;

import com.project.shopapp.dtos.UpdateUserDTO;
import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserLoginDTO;
import com.project.shopapp.models.UserEntity;
import com.project.shopapp.responses.LoginResponse;
import com.project.shopapp.responses.UserResponse;
import com.project.shopapp.services.IUserService;
import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO,
                                        BindingResult result){
        try {
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_PASSWORD_NOT_MATCH));
            }
            userService.createUser(userDTO);
//            return new ResponseEntity<>("Register successfully", HttpStatus.OK);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO){
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword(),
                                                userLoginDTO.getRoleId() == null ? 1 : userLoginDTO.getRoleId());
            return new ResponseEntity<>(LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                            .token(token)
                        .build()
                    , HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED, e.getMessage()))
                            .build()
            );
        }
    }
    @PostMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String authorizationHeader){
        try{
            String extractedToken = authorizationHeader.substring(7);
            UserEntity user = userService.getUserDetailsFromToken(extractedToken);
            return ResponseEntity.ok(UserResponse.fromUser(user));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/details/{userId}")
    public ResponseEntity<?> updateUserDetails(
            @PathVariable Long userId,
            @RequestBody UpdateUserDTO updatedUserDto,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            String extractedToken = authorizationHeader.substring(7);
            UserEntity user = userService.getUserDetailsFromToken(extractedToken);

            if(user.getId() != userId){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            UserEntity updatedUser = userService.updateUser(userId,updatedUserDto);

            return new ResponseEntity<>(UserResponse.fromUser(updatedUser),HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
