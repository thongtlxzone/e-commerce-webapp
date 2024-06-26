package com.project.shopapp.services;

import com.project.shopapp.components.JwtTokenUtils;
import com.project.shopapp.dtos.UpdateUserDTO;
import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.PermissionDenyExeption;
import com.project.shopapp.models.RoleEntity;
import com.project.shopapp.models.UserEntity;
import com.project.shopapp.repositories.RoleRepository;
import com.project.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository; // "final" bat buoc phai thuc thi ham khoi tao ngay khi UserService duoc tao ra
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    @Transactional
    public UserEntity createUser(UserDTO userDTO) throws Exception {
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        RoleEntity role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        if (role.getName().toUpperCase().equals(RoleEntity.ADMIN)){
            throw new PermissionDenyExeption("You cannot register a ADMIN account");
        }
        UserEntity newUser = UserEntity
                .builder()
                .fullname(userDTO.getFullname())
                .password(userDTO.getPassword())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();
        newUser.setRole(role);
        //Kiem tra xem neu co accountId, khong yeu cau password
        if (userDTO.getFacebookAccountId()==0 && userDTO.getGoogleAccountId()==0){
            String password = userDTO.getPassword();
            String encodePassword = passwordEncoder.encode(password);
            newUser.setPassword(encodePassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password, Long roleId) throws DataNotFoundException,Exception {
        Optional<UserEntity> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()){
            throw new DataNotFoundException("Invalid phonenumber / password");
        }
        UserEntity existingUser = optionalUser.get();
        //check password
        if(existingUser.getFacebookAccountId()==0 && existingUser.getGoogleAccountId()==0){
            if (!passwordEncoder.matches(password, existingUser.getPassword())
            ) {
                throw new BadCredentialsException("Wrong phone number/password");
            }
        }
        Optional<RoleEntity> optionalRole = roleRepository.findById(roleId);
        if(optionalRole.isEmpty() || !roleId.equals(existingUser.getRole().getId())){
            throw new DataNotFoundException("Role does not exists");
        }
        if (!optionalUser.get().isActive()){
            throw new DataNotFoundException("User was locked");
        }
        //authenticate with java spring security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber,password,
                existingUser.getAuthorities()
        );
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser); //tra ve json web token (jwt)
    }

    @Override
    public UserEntity getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new Exception("Token is expired");
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        Optional<UserEntity> user = userRepository.findByPhoneNumber(phoneNumber);

        if (user.isPresent()){
            return user.get();
        }else {
            throw new Exception("User not found");
        }
    }

    @Transactional
    @Override
    public UserEntity updateUser(Long userId, UpdateUserDTO updatedUserDto) throws Exception{
        UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(()-> new DataNotFoundException("User not found"));

        String newPhoneNumber = updatedUserDto.getPhoneNumber();
        if(!existingUser.getPhoneNumber().equals(newPhoneNumber)&&
            userRepository.existsByPhoneNumber(newPhoneNumber)){
                throw new DataIntegrityViolationException("Phone number already exists");
        }

        if(updatedUserDto.getFullname()!=null){
            existingUser.setFullname(updatedUserDto.getFullname());
        }
        if(updatedUserDto.getPhoneNumber()!=null){
            existingUser.setPhoneNumber(newPhoneNumber);
        }
        if(updatedUserDto.getAddress()!=null){
            existingUser.setAddress(updatedUserDto.getAddress());
        }
        if(updatedUserDto.getDateOfBirth()!=null){
            existingUser.setDateOfBirth(updatedUserDto.getDateOfBirth());
        }
        if (updatedUserDto.getFacebookAccountId()>0){
            existingUser.setFacebookAccountId(updatedUserDto.getFacebookAccountId());
        }
        if (updatedUserDto.getGoogleAccountId()>0){
            existingUser.setGoogleAccountId(updatedUserDto.getGoogleAccountId());
        }
        if(updatedUserDto.getPassword()!= null && !updatedUserDto.getPassword().isEmpty()){
            if(!updatedUserDto.getPassword().equals(updatedUserDto.getRetypePassword())){
                throw new DataNotFoundException("Password and retype password not the same");
            }
            String newPassword = updatedUserDto.getPassword();
            String encodePassword = passwordEncoder.encode(newPassword);
            existingUser.setPassword(encodePassword);
        }
        return userRepository.save(existingUser);
    }
}
