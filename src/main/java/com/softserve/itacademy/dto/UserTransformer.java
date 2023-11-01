package com.softserve.itacademy.dto;

import com.softserve.itacademy.model.*;
public class UserTransformer {



    public static UserDto convertUserToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole().getId()
        );
    }

    public static User convertUserDtoToEntity(UserDto userDto, Role role) {
        User user = new User();
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(role);
        return user;
    }
}