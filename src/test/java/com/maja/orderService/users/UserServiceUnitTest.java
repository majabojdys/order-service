package com.maja.orderService.users;

import com.maja.orderService.users.clients.DogAvatarClient;
import com.maja.orderService.users.exceptions.UserAlreadyExistsException;
import com.maja.orderService.users.repositories.Address;
import com.maja.orderService.users.repositories.User;
import com.maja.orderService.users.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceUnitTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private final DogAvatarClient dogAvatarClient = Mockito.mock(DogAvatarClient.class);
    private final UserService userService = new UserService(userRepository, passwordEncoder, dogAvatarClient);

    @Test
    void shouldCreateUser() {
        // given
        var userDto = UserFactory.getUserDtoWithAddress();
        var hashedPassword = "hashedPassword";
        var avatarUrl = "avatar";
        Mockito.when(passwordEncoder.encode(userDto.password())).thenReturn(hashedPassword);
        Mockito.when(dogAvatarClient.getDogAvatarUrl()).thenReturn(avatarUrl);
        Mockito.when(userRepository.existsByEmail(userDto.email())).thenReturn(false);

        // when
        userService.createUser(userDto);

        // then
        var address = new Address(userDto.address().street(), userDto.address().number(), userDto.address().city(), userDto.address().zipCode(), userDto.address().country());
        var user = new User(userDto.firstName(), userDto.lastName(), userDto.email(), hashedPassword, avatarUrl, address);

        Mockito.verify(userRepository).save(user);
    }

    @Test
    void shouldThrowUserAlreadyExistsException() {
        // given
        var userDto = UserFactory.getUserDtoWithAddress();
        Mockito.when(userRepository.existsByEmail(userDto.email())).thenReturn(true);

        // when
        var exception = Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userDto));

        // then
        Assertions.assertEquals(userDto.email(), exception.getMessage());
    }
}
