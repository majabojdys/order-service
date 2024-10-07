package com.maja.orderService.users;

import com.maja.orderService.users.dtos.UserDtoCreateRequest;
import com.maja.orderService.users.dtos.UserListDtoResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    UserListDtoResponse getUsers(Pageable pageable){
        return userService.getUsers(pageable);
    }
}
