package no.toreb.hateoasapi.db.repository;

import no.toreb.hateoasapi.db.dao.ItemDao;
import no.toreb.hateoasapi.db.dao.UserDao;
import no.toreb.hateoasapi.db.model.ItemRecord;
import no.toreb.hateoasapi.domain.Item;
import no.toreb.hateoasapi.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ItemRepository {

    private final ItemDao itemDao;
    private final UserDao userDao;

    @Autowired
    public ItemRepository(final ItemDao itemDao, final UserDao userDao) {
        this.itemDao = itemDao;
        this.userDao = userDao;
    }

    @Transactional(readOnly = true)
    public List<Item> findAll() {
        return itemDao.findAll()
                      .stream()
                      .map(itemRecord -> Item.of(itemRecord, getUser(itemRecord.getUserId())))
                      .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<Item> findById(final UUID id) {
        return itemDao.findById(id).map(itemRecord -> Item.of(itemRecord, getUser(itemRecord.getUserId())));
    }

    @Transactional
    public void insert(final Item item) {
        itemDao.insert(ItemRecord.of(item));
    }

    @Transactional
    public void update(final Item item) {
        itemDao.update(ItemRecord.of(item));
    }

    private User getUser(final UUID userId) {
        return userDao.findById(userId)
                      .map(User::of)
                      .orElse(null);
    }

    @Transactional
    public void delete(final UUID id) {
        itemDao.delete(id);
    }
}
