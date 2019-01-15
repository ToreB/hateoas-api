package no.toreb.hateoasapi.api.v1.response.assembler;

import no.toreb.hateoasapi.api.common.response.HALResource;
import no.toreb.hateoasapi.api.v1.response.UserResponse;
import no.toreb.hateoasapi.domain.User;
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
