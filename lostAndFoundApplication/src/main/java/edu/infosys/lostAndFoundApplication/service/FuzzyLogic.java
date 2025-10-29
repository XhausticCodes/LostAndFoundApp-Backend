package edu.infosys.lostAndFoundApplication.service;

import edu.infosys.lostAndFoundApplication.bean.FoundItem;
import edu.infosys.lostAndFoundApplication.bean.LostItem;
import edu.infosys.lostAndFoundApplication.dao.FoundItemRepository;
import edu.infosys.lostAndFoundApplication.dao.FuzzyLogicRepository;
import edu.infosys.lostAndFoundApplication.dao.LostItemRepository;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        if (lostItemOptional.isEmpty()) return Collections.emptyList();

        LostItem lostItem = lostItemOptional.get();
        String usernameOfLoser = lostItem.getUsername();
        List<FoundItem> potentialMatches = fuzzyLogicRepository.findPotentialFoundMatches(usernameOfLoser, lostItem.getCategory());
        List<FoundItem> matches = new ArrayList<>();

        for (FoundItem foundItem : potentialMatches) {
            String lostItemName = normalize(lostItem.getItemName());
            String foundItemName = normalize(foundItem.getItemName());
            double itemNameScore = calculateEnhancedSimilarity(lostItemName, foundItemName);
            if (itemNameScore < MINIMUM_ITEM_NAME_SIMILARITY) continue;

            double brandScore = calculateEnhancedSimilarity(normalize(lostItem.getBrand()), normalize(foundItem.getBrand()));
            double colorScore = calculateEnhancedSimilarity(normalize(lostItem.getColor()), normalize(foundItem.getColor()));
            double categoryScore = calculateEnhancedSimilarity(normalize(lostItem.getCategory()), normalize(foundItem.getCategory()));

            double averageScore = (itemNameScore * ITEM_NAME_WEIGHT) +
                    (brandScore * BRAND_WEIGHT) +
                    (colorScore * COLOR_WEIGHT) +
                    (categoryScore * CATEGORY_WEIGHT);

            if (averageScore >= threshold) matches.add(foundItem);
        }
        return matches;
    }

    public List<LostItem> findMatchingLostItems(String foundItemId, double threshold) {
        Optional<FoundItem> foundItemOptional = foundItemRepository.findById(foundItemId);
        if (foundItemOptional.isEmpty()) return Collections.emptyList();

        FoundItem foundItem = foundItemOptional.get();
        String usernameOfFinder = foundItem.getUsername();
        List<LostItem> potentialMatches = fuzzyLogicRepository.findPotentialLostMatches(usernameOfFinder, foundItem.getCategory());
        List<LostItem> matches = new ArrayList<>();

        for (LostItem lostItem : potentialMatches) {
            String foundItemName = normalize(foundItem.getItemName());
            String lostItemName = normalize(lostItem.getItemName());
            double itemNameScore = calculateEnhancedSimilarity(foundItemName, lostItemName);
            if (itemNameScore < MINIMUM_ITEM_NAME_SIMILARITY) continue;

            double brandScore = calculateEnhancedSimilarity(normalize(foundItem.getBrand()), normalize(lostItem.getBrand()));
            double colorScore = calculateEnhancedSimilarity(normalize(foundItem.getColor()), normalize(lostItem.getColor()));
            double categoryScore = calculateEnhancedSimilarity(normalize(foundItem.getCategory()), normalize(lostItem.getCategory()));

            double averageScore = (itemNameScore * ITEM_NAME_WEIGHT) +
                    (brandScore * BRAND_WEIGHT) +
                    (colorScore * COLOR_WEIGHT) +
                    (categoryScore * CATEGORY_WEIGHT);

            if (averageScore >= threshold) matches.add(lostItem);
        }
        return matches;
    }

    public List<FoundItem> searchFoundItems(String query, String username, double threshold) {
        List<FoundItem> potential = fuzzyLogicRepository.findAllFoundExcludingUser(username);
        Map<FoundItem, Double> scored = new HashMap<>();
        String q = normalize(query);

        final double ITEM_NAME_THRESHOLD = 0.6;
        final double ATTRIBUTE_THRESHOLD = 0.85;

        for (FoundItem f : potential) {
            double itemNameScore = calculateEnhancedSimilarity(q, normalize(f.getItemName()));
            double brandScore = calculateEnhancedSimilarity(q, normalize(f.getBrand()));
            double colorScore = calculateEnhancedSimilarity(q, normalize(f.getColor()));
            double categoryScore = calculateEnhancedSimilarity(q, normalize(f.getCategory()));
            double locationScore = calculateEnhancedSimilarity(q, normalize(f.getLocation()));
            double maxAttributeScore = Math.max(brandScore, Math.max(colorScore, Math.max(categoryScore, locationScore)));

            double finalScore = 0.0;
            boolean isMatch = false;

            if (itemNameScore >= ITEM_NAME_THRESHOLD) {
                finalScore = itemNameScore;
                isMatch = true;
            } else if (maxAttributeScore >= ATTRIBUTE_THRESHOLD) {
                finalScore = maxAttributeScore * 0.9;
                isMatch = true;
            }

            if (isMatch) scored.put(f, finalScore);
        }

        return scored.entrySet().stream()
                .sorted(Map.Entry.<FoundItem, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<LostItem> searchLostItems(String query, String username, double threshold) {
        List<LostItem> potential = fuzzyLogicRepository.findAllLostExcludingUser(username);
        Map<LostItem, Double> scored = new HashMap<>();
        String q = normalize(query);

        final double ITEM_NAME_THRESHOLD = 0.6;
        final double ATTRIBUTE_THRESHOLD = 0.85;

        for (LostItem l : potential) {
            double itemNameScore = calculateEnhancedSimilarity(q, normalize(l.getItemName()));
            double brandScore = calculateEnhancedSimilarity(q, normalize(l.getBrand()));
            double colorScore = calculateEnhancedSimilarity(q, normalize(l.getColor()));
            double categoryScore = calculateEnhancedSimilarity(q, normalize(l.getCategory()));
            double locationScore = calculateEnhancedSimilarity(q, normalize(l.getLocation()));
            double maxAttributeScore = Math.max(brandScore, Math.max(colorScore, Math.max(categoryScore, locationScore)));

            double finalScore = 0.0;
            boolean isMatch = false;

            if (itemNameScore >= ITEM_NAME_THRESHOLD) {
                finalScore = itemNameScore;
                isMatch = true;
            } else if (maxAttributeScore >= ATTRIBUTE_THRESHOLD) {
                finalScore = maxAttributeScore * 0.9;
                isMatch = true;
            }

            if (isMatch) scored.put(l, finalScore);
        }

        return scored.entrySet().stream()
                .sorted(Map.Entry.<LostItem, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private String normalize(String input) {
        return (input == null) ? "" : input.toLowerCase().trim();
    }

    private double calculateEnhancedSimilarity(String s1, String s2) {
        if (s1.isEmpty() && s2.isEmpty()) return 1.0;
        if (s1.isEmpty() || s2.isEmpty()) return 0.0;
        if (s1.contains(s2) || s2.contains(s1)) return 1.0;
        return jaro.apply(s1, s2);
    }
}
