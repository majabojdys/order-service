package com.maja.orderService.users.clients;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DogAvatarClient {

    private final RestClient restClient;

    public DogAvatarClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public String getDogAvatarUrl() {
        return restClient.get()
                .uri("https://dog.ceo/api/breeds/image/random")
                .retrieve()
                .body(DogResponse.class)
                .message();
    }
}
