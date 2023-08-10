package com.example.demo.user.controller;

import com.example.demo.user.controller.form.SignUpRequestForm;
import com.example.demo.user.controller.form.UserResForm;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {
    final UserService userService;
    @PostMapping("/sign-up")
    public UserResForm signUp(@RequestBody SignUpRequestForm requestForm) {
        log.info("signup()");
        return userService.signUp(requestForm);
    }
}
