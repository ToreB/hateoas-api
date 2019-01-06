package no.toreb.hateoasapi.controller;

import no.toreb.hateoasapi.controller.response.CollectionResource;
import no.toreb.hateoasapi.controller.response.HALResource;
import no.toreb.hateoasapi.controller.response.UserResponse;
import no.toreb.hateoasapi.controller.response.assembler.UserResponseAssembler;
import no.toreb.hateoasapi.domain.User;
import no.toreb.hateoasapi.exception.NotFoundException;
import no.toreb.hateoasapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserResponseAssembler assembler;

    @Autowired
    public UserController(final UserService userService, final UserResponseAssembler assembler) {
        this.userService = userService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionResource<UserResponse> getAll() {
        final List<HALResource<UserResponse>> users = userService.findAll()
                                                                 .stream()
                                                                 .map(assembler::toResource)
                                                                 .collect(Collectors.toList());
        return new CollectionResource<>("users", users, linkTo(methodOn(UserController.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public HALResource<UserResponse> getById(@PathVariable("id") final UUID id) {
        final User user = userService.findById(id)
                                     .orElseThrow(() -> new NotFoundException("User with id " + id + " not found!"));

        return assembler.toResource(user);
    }
}
