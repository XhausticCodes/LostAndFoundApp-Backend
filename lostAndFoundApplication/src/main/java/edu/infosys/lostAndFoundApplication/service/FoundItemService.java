package edu.infosys.lostAndFoundApplication.service;

import edu.infosys.lostAndFoundApplication.bean.FoundItem;
import edu.infosys.lostAndFoundApplication.dao.FoundItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FoundItemService {

    @Autowired
    private FoundItemDao foundItemDao;

    public synchronized String generateNextFoundItemId() {
        Long maxId = foundItemDao.findMaxIdNumber();
        long nextId = (maxId == null) ? 1 : maxId + 1;
        return String.format("F%04d", nextId);
    }

    public FoundItem addFoundItem(FoundItem foundItem) {
        foundItem.setFoundItemId(generateNextFoundItemId());
        return foundItemDao.save(foundItem);
    }

    public List<FoundItem> getAllFoundItems() {
        return foundItemDao.findAll();
    }

    public Optional<FoundItem> getFoundItemById(String id) {
        return foundItemDao.findById(id);
    }

    public void deleteFoundItem(String id) {
        foundItemDao.deleteById(id);
    }

    public List<FoundItem> getFoundItemsByUsername(String username) {
        return foundItemDao.findByUsername(username);
    }
}