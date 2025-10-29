package edu.infosys.lostAndFoundApplication.dao;

import edu.infosys.lostAndFoundApplication.bean.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LostItemRepository extends JpaRepository<LostItem, String> {

    @Query("SELECT l FROM LostItem l WHERE l.username = ?1")
    List<LostItem> findByUsername(String username);

    @Query(value = "SELECT MAX(CAST(SUBSTRING(lost_item_id, 2) AS UNSIGNED)) FROM lost_item", nativeQuery = true)
    Long findMaxIdNumber();
}