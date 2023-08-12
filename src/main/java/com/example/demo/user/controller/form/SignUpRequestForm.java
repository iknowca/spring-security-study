package com.example.demo.user.controller.form;

import lombok.Data;

@Data
public class SignUpRequestForm {
    private String email;
    private String pw;
    private String role;
}
