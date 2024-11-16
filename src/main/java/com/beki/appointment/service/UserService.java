package com.beki.appointment.service;

import com.beki.appointment.common.UserRegistrationDto;
import com.beki.appointment.common.UserRole;
import com.beki.appointment.exception.GeneralException;
import com.beki.appointment.model.User;
import com.beki.appointment.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class UserService {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
        }

    @Transactional
    public User registerUser(UserRegistrationDto request) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new GeneralException("Email already taken");
            }

            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setPhone(request.getPhone());
            user.setVerified(false);
            user.setDeleted(false);
            user.setEnabled(true);
            user.setLoginOtpRequired(false);
            user.setUserRole(request.getUserRole());

            return userRepository.save(user);
        }

}
