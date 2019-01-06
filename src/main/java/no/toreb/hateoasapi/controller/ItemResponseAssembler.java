package no.toreb.hateoasapi.controller;

import no.toreb.hateoasapi.domain.Item;
import no.toreb.hateoasapi.dto.HALResource;
import no.toreb.hateoasapi.dto.ItemResponse;
import no.toreb.hateoasapi.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ItemResponseAssembler {

    private final ItemResourceAssembler itemResourceAssembler;
    private final UserResourceAssembler userResourceAssembler;

    @Autowired
    public ItemResponseAssembler(final ItemResourceAssembler itemResourceAssembler,
                                 final UserResourceAssembler userResourceAssembler) {
        this.itemResourceAssembler = itemResourceAssembler;
        this.userResourceAssembler = userResourceAssembler;
    }

    public HALResource<ItemResponse> toResource(final Item item) {
        final Resource<UserResponse> userResource = userResourceAssembler.toResource(item.getUser());
        final Resource<ItemResponse> itemResource = itemResourceAssembler.toResource(item);

        return new HALResource<>(itemResource, Collections.singletonMap("user", userResource));
    }
}
