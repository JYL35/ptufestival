// controller/UserController.java
package com.capstone7.ptufestival.controller;

import com.capstone7.ptufestival.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public String getCurrentUser(@AuthenticationPrincipal User user) {
        return "현재 로그인한 유저: " + user.getUsername();
    }
}
