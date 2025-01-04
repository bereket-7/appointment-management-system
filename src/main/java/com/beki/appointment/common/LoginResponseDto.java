package com.beki.appointment.common;

import com.beki.appointment.security.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Data
public class LoginResponseDto {
    private String token;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;

    public LoginResponseDto(String token, CustomUserDetails userDetails) {
        this.token = token;
        this.userId = userDetails.getUser().getUserId();
        this.firstName = userDetails.getUser().getFirstName();
        this.lastName = userDetails.getUser().getLastName();
        this.email = userDetails.getUser().getEmail();
        this.roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
