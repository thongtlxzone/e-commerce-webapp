package com.project.shopapp.services;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.UserEntity;

public interface IUserService {
    UserEntity createUser(UserDTO userDTO) throws DataNotFoundException, Exception;
    String login(String phoneNumber, String password) throws DataNotFoundException,Exception;
}
