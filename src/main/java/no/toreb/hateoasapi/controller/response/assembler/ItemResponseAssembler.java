package no.toreb.hateoasapi.controller.response.assembler;

import no.toreb.hateoasapi.controller.response.HALResource;
import no.toreb.hateoasapi.controller.response.ItemResponse;
import no.toreb.hateoasapi.controller.response.UserResponse;
import no.toreb.hateoasapi.domain.Item;
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
