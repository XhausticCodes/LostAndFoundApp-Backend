package edu.infosys.lostAndFoundApplication.dao;

import edu.infosys.lostAndFoundApplication.bean.FoundItem;
import java.util.List;
import java.util.Optional;

public interface FoundItemDao {

    Long findMaxIdNumber();

    FoundItem save(FoundItem foundItem);

    List<FoundItem> findAll();

    Optional<FoundItem> findById(String id);

    void deleteById(String id);

    List<FoundItem> findByUsername(String username);
}