package com.beki.appointment.interfaces.mapper;

import com.beki.appointment.domain.user.User;
import com.beki.appointment.interfaces.dto.UserRegistrationDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserRegistrationDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setUserRole(user.getUserRole());

        return dto;
    }

    public User toEntity(UserRegistrationDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setUserRole(dto.getUserRole());

        return user;
    }

    public void updateEntity(User user, UserRegistrationDto dto) {
        if (dto == null || user == null) {
            return;
        }

        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
        if (dto.getUserRole() != null) {
            user.setUserRole(dto.getUserRole());
        }
    }
}
