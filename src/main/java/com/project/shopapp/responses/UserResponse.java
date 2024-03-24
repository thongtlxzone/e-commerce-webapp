package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.RoleEntity;
import com.project.shopapp.models.UserEntity;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    @JsonProperty("fullname")
    private String fullname;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("address")
    private String address;
    @JsonProperty("is_active")
    private boolean active;
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    @JsonProperty("facebook_account_id")
    private int facebookAccountId;
    @JsonProperty("google_account_id")
    private int googleAccountId;
    @JsonProperty("role")
    private RoleEntity role;

    public static UserResponse fromUser(UserEntity user){
        return UserResponse.builder()
                .id(user.getId())
                .fullname(user.getFullname())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .active(user.isActive())
                .dateOfBirth(user.getDateOfBirth())
                .facebookAccountId(user.getFacebookAccountId())
                .googleAccountId(user.getGoogleAccountId())
                .role(user.getRole())
                .build();
    }
}
