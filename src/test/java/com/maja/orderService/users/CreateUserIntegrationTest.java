package com.maja.orderService.users;

import com.maja.orderService.IntegrationTest;
import com.maja.orderService.commons.Error;
import com.maja.orderService.users.dtos.AddressDtoCreateRequest;
import com.maja.orderService.users.dtos.UserDtoCreateRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

class CreateUserIntegrationTest extends IntegrationTest {

    @Test
    void shouldCreateUser() {
        // given
        var userDto = UserFactory.getUserDtoWithAddress();
        var avatarUrl = "avatarUrl";
        Mockito.when(dogAvatarClient.getDogAvatarUrl()).thenReturn(avatarUrl);

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/users", userDto, Void.class);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        var users = userRepository.findAll();
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(userDto.firstName(), users.getFirst().getFirstName());
        Assertions.assertEquals(userDto.lastName(), users.getFirst().getLastName());
        Assertions.assertEquals(userDto.email(), users.getFirst().getEmail());
        Assertions.assertEquals(avatarUrl, users.getFirst().getAvatarUrl());
        Assertions.assertEquals(userDto.address().street(), users.getFirst().getAddress().getStreet());
        Assertions.assertEquals(userDto.address().number(), users.getFirst().getAddress().getNumber());
        Assertions.assertEquals(userDto.address().city(), users.getFirst().getAddress().getCity());
        Assertions.assertEquals(userDto.address().zipCode(), users.getFirst().getAddress().getZipCode());
        Assertions.assertEquals(userDto.address().country(), users.getFirst().getAddress().getCountry());

        Assertions.assertNotEquals(userDto.password(), users.getFirst().getPassword());
        Assertions.assertTrue(passwordEncoder.matches(userDto.password(), users.getFirst().getPassword()));
    }

    @Test
    void shouldValidateRequest() {
        // given
        var addressDto = new AddressDtoCreateRequest(null, null, null, null, null);
        var userDto = new UserDtoCreateRequest(null, null, "test", null, addressDto);

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/users", userDto, Error.class);

        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var body = response.getBody();
        Assertions.assertEquals("REQUEST_VALIDATION_ERRORS", body.errorCode());
        Assertions.assertEquals("[address.city must not be blank, address.country must not be blank, address.number must not be blank, address.street must not be blank, address.zipCode must not be blank, email must be a well-formed email address, firstName must not be blank, lastName must not be blank, password must not be blank]", body.errorDescription());
    }
}
