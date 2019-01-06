package no.toreb.hateoasapi.controller;

import no.toreb.hateoasapi.domain.User;
import no.toreb.hateoasapi.dto.HALResource;
import no.toreb.hateoasapi.dto.UserResponse;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class UserResourceAssembler implements ResourceAssembler<User, HALResource<UserResponse>> {

    @Override
    public HALResource<UserResponse> toResource(final User user) {
        return new HALResource<>(UserResponse.of(user),
                                 linkTo(methodOn(UserController.class).getById(user.getId())).withSelfRel(),
                                 linkTo(methodOn(UserController.class).getAll()).withRel("users"));
    }
}
