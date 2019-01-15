package no.toreb.hateoasapi.api.v1.controller;

import no.toreb.hateoasapi.api.CustomMediaType;
import no.toreb.hateoasapi.api.common.response.CollectionResource;
import no.toreb.hateoasapi.api.common.response.HALResource;
import no.toreb.hateoasapi.api.v1.request.ItemRequest;
import no.toreb.hateoasapi.api.v1.response.ItemResponse;
import no.toreb.hateoasapi.api.v1.response.assembler.ItemResponseAssembler;
import no.toreb.hateoasapi.domain.Item;
import no.toreb.hateoasapi.domain.User;
import no.toreb.hateoasapi.exception.NotFoundException;
import no.toreb.hateoasapi.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/items", produces = {CustomMediaType.V1_VALUE})
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
        return new CollectionResource<>("items",
                                        items,
                                        List.of(linkTo(methodOn(ItemController.class).getAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public HALResource<ItemResponse> getById(@PathVariable("id") final UUID id) {
        final Item item = itemService.findById(id)
                                     .orElseThrow(() -> new NotFoundException("Item", id));

        return assembler.toResource(item);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HALResource<ItemResponse> create(@RequestBody final ItemRequest itemRequest) {
        final Item item = itemService.insert(new Item(null,
                                                      itemRequest.getName(),
                                                      itemRequest.getDescription(),
                                                      new User(itemRequest.getUser(), null)));
        return assembler.toResource(item);
    }

    @PatchMapping("/{id}")
    public HALResource<ItemResponse> update(@PathVariable("id") final UUID id,
                                            @RequestBody final ItemRequest itemRequest) {
        final Item item = itemService.update(new Item(id,
                                                      itemRequest.getName(),
                                                      itemRequest.getDescription(),
                                                      new User(itemRequest.getUser(), null)));

        return assembler.toResource(item);
    }

    @DeleteMapping("/{id}")
    public HALResource<ItemResponse> delete(@PathVariable("id") final UUID id) {
        itemService.delete(id);

        return new HALResource<>(null,
                                 List.of(linkTo(methodOn(ItemController.class).getAll()).withRel("items")),
                                 Map.of());
    }
}
