package no.toreb.hateoasapi.service;

import no.toreb.hateoasapi.db.repository.ItemRepository;
import no.toreb.hateoasapi.db.repository.UserRepository;
import no.toreb.hateoasapi.domain.Item;
import no.toreb.hateoasapi.domain.User;
import no.toreb.hateoasapi.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final IdGenerator idGenerator;

    @Autowired
    public ItemService(final ItemRepository itemRepository,
                       final UserRepository userRepository,
                       final IdGenerator idGenerator) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.idGenerator = idGenerator;
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> findById(final UUID id) {
        return itemRepository.findById(id);
    }

    public Item insert(final Item newItem) {
        final User user = getUser(newItem);

        final Item item = new Item(idGenerator.generateId(), newItem.getName(), newItem.getDescription(), user);

        itemRepository.insert(item);

        return item;
    }

    public Item update(final Item updatedItem) {
        final User user = getUser(updatedItem);

        final UUID itemId = updatedItem.getId();
        itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item", itemId));

        final Item item = new Item(itemId, updatedItem.getName(), updatedItem.getDescription(), user);

        itemRepository.update(item);

        return item;
    }

    private User getUser(final Item item) {
        final UUID userId = item.getUser().getId();
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User", userId));
    }

    public void delete(final UUID id) {
        findById(id).orElseThrow(() -> new NotFoundException("Item", id));

        itemRepository.delete(id);
    }
}
