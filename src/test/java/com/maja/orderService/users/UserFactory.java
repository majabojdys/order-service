package com.maja.orderService.users;

import com.maja.orderService.users.dtos.AddressDtoCreateRequest;
import com.maja.orderService.users.dtos.UserDtoCreateRequest;
import com.maja.orderService.users.repositories.Address;
import com.maja.orderService.users.repositories.User;

public class UserFactory {

    public static User getUserWithAddress(){
        var address = new Address("ulica", "31A", "miasto", "12-123", "PL");
        return new User("Maja", "Bojdys", "email@gmail.com", "password", "avatarUrl", address);
    }

    public static UserDtoCreateRequest getUserDtoWithAddress(){
        var addressDto = new AddressDtoCreateRequest("ulica", "31A", "miasto", "12-123", "PL");
        return new UserDtoCreateRequest("Maja", "Bojdys", "email@gmail.com", "password", addressDto);
    }
}
