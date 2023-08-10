package com.example.demo.user.service;

import com.example.demo.user.controller.form.SignUpRequestForm;
import com.example.demo.user.controller.form.UserResForm;
import com.example.demo.user.entity.User;

public interface UserService {
    public User loadUserByUserId(Long id);

    UserResForm signUp(SignUpRequestForm requestForm);
}
