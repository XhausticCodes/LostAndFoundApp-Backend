package edu.infosys.lostAndFoundApplication.dao;

import edu.infosys.lostAndFoundApplication.bean.FoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoundItemRepository extends JpaRepository<FoundItem, String> {

    @Query("SELECT f FROM FoundItem f WHERE f.username = ?1")
    List<FoundItem> findByUsername(String username);

    @Query(value = "SELECT MAX(CAST(SUBSTRING(found_item_id, 2) AS UNSIGNED)) FROM found_item", nativeQuery = true)
    Long findMaxIdNumber();
    
    }
