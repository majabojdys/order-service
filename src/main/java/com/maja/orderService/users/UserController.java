package com.maja.orderService.users;

import com.maja.orderService.users.dtos.UserDtoCreateRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    void createUser(@Valid @RequestBody UserDtoCreateRequest userDto){
        userService.createUser(userDto);
    }
}
