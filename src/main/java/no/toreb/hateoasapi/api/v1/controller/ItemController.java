package no.toreb.hateoasapi.api.v1.controller;

import no.toreb.hateoasapi.api.Version;
import no.toreb.hateoasapi.api.common.response.CollectionResource;
import no.toreb.hateoasapi.api.common.response.HALResource;
import no.toreb.hateoasapi.api.common.response.ItemResponse;
import no.toreb.hateoasapi.api.v1.response.assembler.ItemResponseAssembler;
import no.toreb.hateoasapi.domain.Item;
import no.toreb.hateoasapi.exception.NotFoundException;
import no.toreb.hateoasapi.service.ItemService;
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
@RequestMapping(path = "/items", produces = {Version.V1})
public class ItemController {

    private final ItemService itemService;
    private final ItemResponseAssembler assembler;

    @Autowired
    public ItemController(final ItemService itemService,
                          final ItemResponseAssembler assembler) {
        this.itemService = itemService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionResource<ItemResponse> getAll() {
        final List<HALResource<ItemResponse>> items = itemService.findAll()
                                                                 .stream()
                                                                 .map(assembler::toResource)
                                                                 .collect(Collectors.toList());
        return new CollectionResource<>("items", items, linkTo(methodOn(ItemController.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public HALResource<ItemResponse> getById(@PathVariable("id") final UUID id) {
        final Item item = itemService.findById(id)
                                     .orElseThrow(() -> new NotFoundException("Item with id " + id + " not found!"));

        return assembler.toResource(item);
    }
}
