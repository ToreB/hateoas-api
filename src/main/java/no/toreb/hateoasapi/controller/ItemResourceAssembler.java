package no.toreb.hateoasapi.controller;

import no.toreb.hateoasapi.domain.Item;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class ItemResourceAssembler implements ResourceAssembler<Item, Resource<Item>> {

    @Override
    public Resource<Item> toResource(final Item item) {
        return new Resource<>(item,
                              linkTo(methodOn(ItemController.class).getById(item.getId())).withSelfRel(),
                              linkTo(methodOn(ItemController.class).all()).withRel("items"));
    }
}
