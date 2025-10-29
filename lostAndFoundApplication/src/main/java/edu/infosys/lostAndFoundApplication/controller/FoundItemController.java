package edu.infosys.lostAndFoundApplication.controller;

import edu.infosys.lostAndFoundApplication.bean.FoundItem;
import edu.infosys.lostAndFoundApplication.service.CampusUserService;
import edu.infosys.lostAndFoundApplication.service.FoundItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/lost-found/found-items")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3939"})
public class FoundItemController {

    @Autowired
    private FoundItemService foundItemService;

    @Autowired
    private CampusUserService campusUserService;

    @PostMapping
    public FoundItem createFoundItem(@RequestBody FoundItem foundItem) {
        return foundItemService.addFoundItem(foundItem);
    }

    @GetMapping
    public List<FoundItem> getAllFoundItems() {
        return foundItemService.getAllFoundItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoundItem> getFoundItemById(@PathVariable String id) {
        return foundItemService.getFoundItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoundItem(@PathVariable String id) {
        foundItemService.deleteFoundItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    public List<FoundItem> getFoundItemsByUser() {
        String username = campusUserService.getUserId();
        return foundItemService.getFoundItemsByUsername(username);
    }
}
