package com.example.demo.user.controller;

import com.example.demo.user.controller.form.SignUpRequestForm;
import com.example.demo.user.controller.form.UserResForm;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/{userId}")
    public UserResForm getUserInfo(@PathVariable Long userId) {
        log.info("getUserInfo()");
        return userService.getUserInfo(userId);
    }
}
