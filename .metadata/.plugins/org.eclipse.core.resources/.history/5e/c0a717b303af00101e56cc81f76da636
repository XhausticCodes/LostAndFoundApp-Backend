package edu.infosys.lostAndFoundApplication.controller;

import edu.infosys.lostAndFoundApplication.bean.FoundItem;
import edu.infosys.lostAndFoundApplication.bean.LostItem;
import edu.infosys.lostAndFoundApplication.service.FuzzyLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lost-found/fuzzy")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3939"})
public class FuzzyLogicController {

    @Autowired
    private FuzzyLogic fuzzyLogic;

    @GetMapping("/match/found/{lostItemId}")
    public List<FoundItem> getMatchingFoundItems(
            @PathVariable String lostItemId,
            @RequestParam(defaultValue = "0.7") double threshold) {
        return fuzzyLogic.findMatchingFoundItems(lostItemId, threshold);
    }

    @GetMapping("/match/lost/{foundItemId}")
    public List<LostItem> getMatchingLostItems(
            @PathVariable String foundItemId,
            @RequestParam(defaultValue = "0.7") double threshold) {
        return fuzzyLogic.findMatchingLostItems(foundItemId, threshold);
    }
}