package no.toreb.hateoasapi.controller.response.assembler;

import no.toreb.hateoasapi.controller.ItemController;
import no.toreb.hateoasapi.controller.response.ItemResponse;
import no.toreb.hateoasapi.domain.Item;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
class ItemResourceAssembler implements ResourceAssembler<Item, Resource<ItemResponse>> {

    @Override
    public Resource<ItemResponse> toResource(final Item item) {
        return new Resource<>(ItemResponse.of(item),
                              linkTo(methodOn(ItemController.class).getById(item.getId())).withSelfRel(),
                              linkTo(methodOn(ItemController.class).getAll()).withRel("items"));
    }
}
