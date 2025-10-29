package edu.infosys.lostAndFoundApplication.service;

import edu.infosys.lostAndFoundApplication.bean.FoundItem;
import edu.infosys.lostAndFoundApplication.bean.LostItem;
import edu.infosys.lostAndFoundApplication.dao.FoundItemRepository;
import edu.infosys.lostAndFoundApplication.dao.FuzzyLogicRepository;
import edu.infosys.lostAndFoundApplication.dao.LostItemRepository;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FuzzyLogic {

    @Autowired
    private LostItemRepository lostItemRepository;

    @Autowired
    private FoundItemRepository foundItemRepository;

    @Autowired
    private FuzzyLogicRepository fuzzyLogicRepository;

    private final JaroWinklerSimilarity jaro = new JaroWinklerSimilarity();

    private static final double ITEM_NAME_WEIGHT = 0.50;
    private static final double BRAND_WEIGHT = 0.25;
    private static final double COLOR_WEIGHT = 0.15;
    private static final double CATEGORY_WEIGHT = 0.10;

    private static final double MINIMUM_ITEM_NAME_SIMILARITY = 0.6;

    public List<FoundItem> findMatchingFoundItems(String lostItemId, double threshold) {
        Optional<LostItem> lostItemOptional = lostItemRepository.findById(lostItemId);

        if (lostItemOptional.isEmpty()) {
            return Collections.emptyList();
        }
        LostItem lostItem = lostItemOptional.get();
        String usernameOfLoser = lostItem.getUsername();

        List<FoundItem> potentialMatches = fuzzyLogicRepository.findPotentialFoundMatches(usernameOfLoser, lostItem.getCategory());

        List<FoundItem> matches = new ArrayList<>();

        for (FoundItem foundItem : potentialMatches) {
            String lostItemName = normalize(lostItem.getItemName());
            String foundItemName = normalize(foundItem.getItemName());

            double itemNameScore = jaro.apply(lostItemName, foundItemName);

            if (itemNameScore < MINIMUM_ITEM_NAME_SIMILARITY) {
                continue;
            }

            String lostBrand = normalize(lostItem.getBrand());
            String foundBrand = normalize(foundItem.getBrand());
            String lostColor = normalize(lostItem.getColor());
            String foundColor = normalize(foundItem.getColor());
            String lostCategory = normalize(lostItem.getCategory());
            String foundCategory = normalize(foundItem.getCategory());

            double brandScore = calculateSmartFieldScore(lostBrand, foundBrand);
            double colorScore = calculateSmartFieldScore(lostColor, foundColor);
            double categoryScore = jaro.apply(lostCategory, foundCategory);

            double averageScore = (itemNameScore * ITEM_NAME_WEIGHT) +
                    (brandScore * BRAND_WEIGHT) +
                    (colorScore * COLOR_WEIGHT) +
                    (categoryScore * CATEGORY_WEIGHT);

            if (averageScore >= threshold) {
                matches.add(foundItem);
            }
        }
        return matches;
    }

    public List<LostItem> findMatchingLostItems(String foundItemId, double threshold) {
        Optional<FoundItem> foundItemOptional = foundItemRepository.findById(foundItemId);

        if (foundItemOptional.isEmpty()) {
            return Collections.emptyList();
        }
        FoundItem foundItem = foundItemOptional.get();
        String usernameOfFinder = foundItem.getUsername();

        List<LostItem> potentialMatches = fuzzyLogicRepository.findPotentialLostMatches(usernameOfFinder, foundItem.getCategory());

        List<LostItem> matches = new ArrayList<>();

        for (LostItem lostItem : potentialMatches) {
            String foundItemName = normalize(foundItem.getItemName());
            String lostItemName = normalize(lostItem.getItemName());

            double itemNameScore = jaro.apply(foundItemName, lostItemName);

            if (itemNameScore < MINIMUM_ITEM_NAME_SIMILARITY) {
                continue;
            }

            String foundBrand = normalize(foundItem.getBrand());
            String lostBrand = normalize(lostItem.getBrand());
            String foundColor = normalize(foundItem.getColor());
            String lostColor = normalize(lostItem.getColor());
            String foundCategory = normalize(foundItem.getCategory());
            String lostCategory = normalize(lostItem.getCategory());

            double brandScore = calculateSmartFieldScore(foundBrand, lostBrand);
            double colorScore = calculateSmartFieldScore(foundColor, lostColor);
            double categoryScore = jaro.apply(foundCategory, lostCategory);

            double averageScore = (itemNameScore * ITEM_NAME_WEIGHT) +
                    (brandScore * BRAND_WEIGHT) +
                    (colorScore * COLOR_WEIGHT) +
                    (categoryScore * CATEGORY_WEIGHT);

            if (averageScore >= threshold) {
                matches.add(lostItem);
            }
        }
        return matches;
    }

    private String normalize(String input) {
        return (input == null) ? "" : input.toLowerCase().trim();
    }

    private double calculateSmartFieldScore(String s1, String s2) {
        if (s1.isEmpty() && s2.isEmpty()) {
            return 1.0;
        }
        if (s1.isEmpty() || s2.isEmpty()) {
            return 0.0;
        }
        return jaro.apply(s1, s2);
    }
}