package com.maja.orderService.users;

import com.maja.orderService.IntegrationTest;
import com.maja.orderService.commons.Error;
import com.maja.orderService.users.dtos.AddressDtoCreateRequest;
import com.maja.orderService.users.dtos.UserDtoCreateRequest;
import com.maja.orderService.users.dtos.UserDtoResponse;
import com.maja.orderService.users.dtos.UserListDtoResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.List;

class GetUsersIntegrationTest extends IntegrationTest {

    @Test
    void shouldGetUsers() {
        // given
        var user1 = UserFactory.getUserWithAddress("user1@test.com");
        var user2 = UserFactory.getUserWithAddress("user2@test.com");
        userRepository.saveAll(List.of(user1, user2));

        // when
        var response = restTemplate.getForEntity(getLocalhost() + "/api/v1/users", UserListDtoResponse.class);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        var body = response.getBody();
        Assertions.assertEquals(2, body.users().size());
        Assertions.assertEquals(user1.getFirstName(), body.users().getFirst().firstName());
        Assertions.assertEquals(user1.getLastName(), body.users().getFirst().lastName());
        Assertions.assertEquals(user1.getEmail(), body.users().getFirst().email());
        Assertions.assertEquals(user1.getAvatarUrl(), body.users().getFirst().avatarUrl());

        Assertions.assertEquals(user2.getFirstName(), body.users().getLast().firstName());
        Assertions.assertEquals(user2.getLastName(), body.users().getLast().lastName());
        Assertions.assertEquals(user2.getEmail(), body.users().getLast().email());
        Assertions.assertEquals(user2.getAvatarUrl(), body.users().getLast().avatarUrl());
    }
}
