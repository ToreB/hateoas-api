package no.toreb.hateoasapi.controller;

import no.toreb.hateoasapi.domain.Item;
import no.toreb.hateoasapi.exception.NotFoundException;
import no.toreb.hateoasapi.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
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
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemResourceAssembler assembler;

    @Autowired
    public ItemController(final ItemService itemService,
                          final ItemResourceAssembler assembler) {
        this.itemService = itemService;
        this.assembler = assembler;
    }

    @GetMapping
    public Resources<Resource<Item>> all() {
        final List<Resource<Item>> items = itemService.findAll()
                                                      .stream()
                                                      .map(assembler::toResource)
                                                      .collect(Collectors.toList());
        return new Resources<>(items, linkTo(methodOn(ItemController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public Resource<Item> getById(@PathVariable("id") final UUID id) {
        final Item item = itemService.findById(id)
                                     .orElseThrow(() -> new NotFoundException("Item with id " + id + " not found!"));

        return assembler.toResource(item);
    }
}
