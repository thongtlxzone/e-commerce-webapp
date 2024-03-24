package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginDTO {
    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;
    @NotBlank(message = "Password cannot be blank")
    private String password;
    @JsonProperty("role_id")
    @Min(value = 1, message = "You must enter role's Id")
    private Long roleId;
}
