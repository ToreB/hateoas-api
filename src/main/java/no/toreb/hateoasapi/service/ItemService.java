package no.toreb.hateoasapi.service;

import no.toreb.hateoasapi.db.repository.ItemRepository;
import no.toreb.hateoasapi.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(final ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> findById(final UUID id) {
        return itemRepository.findById(id);
    }

    public void insert(final Item item) {
        itemRepository.insert(item);
    }
}
