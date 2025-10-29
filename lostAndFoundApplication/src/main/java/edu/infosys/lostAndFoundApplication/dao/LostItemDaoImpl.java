package edu.infosys.lostAndFoundApplication.dao;

import edu.infosys.lostAndFoundApplication.bean.LostItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class LostItemDaoImpl implements LostItemDao {

    @Autowired
    private LostItemRepository lostItemRepository;

    @Override
    public Long findMaxIdNumber() {
        return lostItemRepository.findMaxIdNumber();
    }

    @Override
    public LostItem save(LostItem lostItem) {
        return lostItemRepository.save(lostItem);
    }

    @Override
    public List<LostItem> findAll() {
        return lostItemRepository.findAll();
    }

    @Override
    public Optional<LostItem> findById(String id) {
        return lostItemRepository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        lostItemRepository.deleteById(id);
    }

    @Override
    public List<LostItem> findByUsername(String username) {
        return lostItemRepository.findByUsername(username);
    }
}