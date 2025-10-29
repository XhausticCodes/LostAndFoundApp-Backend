package edu.infosys.lostAndFoundApplication.controller;

import edu.infosys.lostAndFoundApplication.bean.LostItem;
import edu.infosys.lostAndFoundApplication.service.CampusUserService;
import edu.infosys.lostAndFoundApplication.service.LostItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/lost-found/lost-items")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3939"})
public class LostItemController {

    @Autowired
    private LostItemService lostItemService;

    @Autowired
    private CampusUserService campusUserService;

    @PostMapping
    public LostItem createLostItem(@RequestBody LostItem lostItem) {
        return lostItemService.addLostItem(lostItem);
    }

    @GetMapping
    public List<LostItem> getAllLostItems() {
        return lostItemService.getAllLostItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LostItem> getLostItemById(@PathVariable String id) {
        return lostItemService.getLostItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLostItem(@PathVariable String id) {
        lostItemService.deleteLostItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    public List<LostItem> getLostItemsByUser() {
        String username = campusUserService.getUserId();
        return lostItemService.getLostItemsByUsername(username);
    }
}
