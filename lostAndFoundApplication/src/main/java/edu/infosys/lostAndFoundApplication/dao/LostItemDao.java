package edu.infosys.lostAndFoundApplication.dao;

import edu.infosys.lostAndFoundApplication.bean.LostItem;
import java.util.List;
import java.util.Optional;

public interface LostItemDao {

    Long findMaxIdNumber();

    LostItem save(LostItem lostItem);

    List<LostItem> findAll();

    Optional<LostItem> findById(String id);

    void deleteById(String id);

    List<LostItem> findByUsername(String username);
}