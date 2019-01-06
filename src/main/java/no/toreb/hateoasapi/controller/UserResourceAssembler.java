package no.toreb.hateoasapi.controller;

import no.toreb.hateoasapi.domain.User;
import no.toreb.hateoasapi.dto.UserResponse;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class UserResourceAssembler implements ResourceAssembler<User, Resource<UserResponse>> {

    @Override
    public Resource<UserResponse> toResource(final User user) {
        return new Resource<>(UserResponse.of(user),
                              linkTo(methodOn(UserController.class).getById(user.getId())).withSelfRel(),
                              linkTo(methodOn(UserController.class).getAll()).withRel("users"));
    }
}
