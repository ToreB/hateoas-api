package no.toreb.hateoasapi.controller;

import no.toreb.hateoasapi.domain.Item;
import no.toreb.hateoasapi.dto.HALResource;
import no.toreb.hateoasapi.dto.ItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class ItemResourceAssembler implements ResourceAssembler<Item, HALResource<ItemResponse>> {

    private final UserResourceAssembler userResourceAssembler;

    @Autowired
    public ItemResourceAssembler(final UserResourceAssembler userResourceAssembler) {
        this.userResourceAssembler = userResourceAssembler;
    }

    @Override
    public HALResource<ItemResponse> toResource(final Item item) {
        return new HALResource<>(ItemResponse.of(item),
                                 Arrays.asList(
                                         linkTo(methodOn(ItemController.class).getById(item.getId())).withSelfRel(),
                                         linkTo(methodOn(ItemController.class).getAll()).withRel("items")),
                                 Collections.singletonMap("user", userResourceAssembler.toResource(item.getUser())));
    }
}
