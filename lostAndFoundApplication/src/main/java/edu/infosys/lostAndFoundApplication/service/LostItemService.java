package edu.infosys.lostAndFoundApplication.service;

import edu.infosys.lostAndFoundApplication.bean.LostItem;
import edu.infosys.lostAndFoundApplication.dao.LostItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LostItemService {

    @Autowired
    private LostItemDao lostItemDao;

    public synchronized String generateNextLostItemId() {
        Long maxId = lostItemDao.findMaxIdNumber();
        long nextId = (maxId == null) ? 1 : maxId + 1;
        return String.format("L%04d", nextId);
    }

    public LostItem addLostItem(LostItem lostItem) {
        lostItem.setLostItemId(generateNextLostItemId());
        return lostItemDao.save(lostItem);
    }

    public List<LostItem> getAllLostItems() {
        return lostItemDao.findAll();
    }



    public Optional<LostItem> getLostItemById(String id) {
        return lostItemDao.findById(id);
    }

    public void deleteLostItem(String id) {
        lostItemDao.deleteById(id);
    }

    public List<LostItem> getLostItemsByUsername(String username) {
        return lostItemDao.findByUsername(username);
    }
}