package com.maja.orderService.users;

import com.maja.orderService.users.clients.DogAvatarClient;
import com.maja.orderService.users.dtos.UserDtoCreateRequest;
import com.maja.orderService.users.repositories.Address;
import com.maja.orderService.users.repositories.User;
import com.maja.orderService.users.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DogAvatarClient dogAvatarClient;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, DogAvatarClient dogAvatarClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.dogAvatarClient = dogAvatarClient;
    }

    void createUser(UserDtoCreateRequest userDto) {
        var addressDto = userDto.address();
        var address = new Address(addressDto.street(), addressDto.number(), addressDto.city(), addressDto.zipCode(), addressDto.country());
        var avatarUrl = dogAvatarClient.getDogAvatarUrl();
        var user = new User(userDto.firstName(), userDto.lastName(), userDto.email(), passwordEncoder.encode(userDto.password()), avatarUrl, address);
        userRepository.save(user);
    }
}
