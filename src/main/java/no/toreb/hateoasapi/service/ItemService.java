package no.toreb.hateoasapi.service;

import no.toreb.hateoasapi.dao.ItemDao;
import no.toreb.hateoasapi.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemService {

    private final ItemDao itemDao;

    @Autowired
    public ItemService(final ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public List<Item> findAll() {
        return itemDao.findAll();
    }

    public Optional<Item> findById(final UUID id) {
        return itemDao.findById(id);
    }
}
