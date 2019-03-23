package no.toreb.hateoasapi.api.v1.controller;

import no.toreb.hateoasapi.api.CustomMediaType;
import no.toreb.hateoasapi.api.common.response.CollectionResource;
import no.toreb.hateoasapi.api.common.response.HALResource;
import no.toreb.hateoasapi.api.v1.request.UserRequest;
import no.toreb.hateoasapi.api.v1.response.UserResponse;
import no.toreb.hateoasapi.api.v1.response.assembler.UserResponseAssembler;
import no.toreb.hateoasapi.domain.User;
import no.toreb.hateoasapi.exception.NotFoundException;
import no.toreb.hateoasapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/users", produces = {CustomMediaType.V1_VALUE})
@PreAuthorize("hasRole('SECURED')")
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
        return new CollectionResource<>("users",
                                        users,
                                        List.of(linkTo(methodOn(UserController.class).getAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public HALResource<UserResponse> getById(@PathVariable("id") final UUID id) {
        final User user = userService.findById(id)
                                     .orElseThrow(() -> new NotFoundException("User", id));

        return assembler.toResource(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HALResource<UserResponse> create(@RequestBody final UserRequest userRequest) {
        final User user = userService.insert(new User(null, userRequest.getName()));

        return assembler.toResource(user);
    }
}

