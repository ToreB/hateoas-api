package no.toreb.hateoasapi.controller;

import no.toreb.hateoasapi.domain.User;
import no.toreb.hateoasapi.dto.HALResource;
import no.toreb.hateoasapi.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserResponseAssembler {

    private final UserResourceAssembler userResourceAssembler;

    @Autowired
    public UserResponseAssembler(final UserResourceAssembler userResourceAssembler) {
        this.userResourceAssembler = userResourceAssembler;
    }

    public HALResource<UserResponse> toResource(final User user) {
        return new HALResource<>(userResourceAssembler.toResource(user));
    }
}
