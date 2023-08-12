package com.example.demo.user.service;

import com.example.demo.user.controller.form.SignUpRequestForm;
import com.example.demo.user.controller.form.UserResForm;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User loadUserByUserId(Long id) throws UsernameNotFoundException {
        Optional<User> maybeUser = userRepository.findById(id);
        if (maybeUser.isEmpty()) {
            throw new UsernameNotFoundException("can not find user");
        } else {
            return maybeUser.get();
        }
    }

    @Override
    public UserResForm signUp(SignUpRequestForm requestForm) {
        String email = requestForm.getEmail();
        String pw = passwordEncoder.encode(requestForm.getPw());

        User user = User.builder().email(email).pw(pw).role(requestForm.getRole()).build();
        userRepository.save(user);
        return user.toUserRestForm();
    }

    @Override
    public UserResForm getUserInfo(Long userId) {
        Optional<User> maybeUser = userRepository.findById(userId);
        UserResForm userResForm =  maybeUser.map(User::toUserRestForm).orElse(null);
        log.info(String.valueOf(userResForm));
        return userResForm;
    }
}
