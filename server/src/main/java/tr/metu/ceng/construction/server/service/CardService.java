package tr.metu.ceng.construction.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.metu.ceng.construction.server.model.Card;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CardService {

    /**
     * Selects a card from a given set of cards randomly.
     *
     * @param set this is the set of cards
     * @return random selected card
     */
    public Card getRandomCard(Set<Card> set) {
        Card randomCard = null;
        if (!set.isEmpty()) {
            Random random = new Random();

            // generate a random number using nextInt method.
            int randomIndex = random.nextInt(set.size());

            Iterator<Card> iterator = set.iterator();

            int currentIndex = 0;

            // iterate the HashSet
            while (iterator.hasNext()) {

                randomCard = iterator.next();
                // if current index is equal to random number
                if (currentIndex == randomIndex) {
                    break;
                }
                // increase the current index
                currentIndex++;
            }
        }
        return randomCard;
    }

    /**
     * Finds the highest scored card. If all cards are equally valued,
     * then return a random selected card.
     *
     * @param cards this is the set of cards
     * @return the card with highest score
     */
    public Card findHighestScoredCard(Set<Card> cards) {
        if (!cards.isEmpty()) {
            Map<Integer, Card> cardsAndScoresMap = new HashMap<>();
            cards.forEach(card -> cardsAndScoresMap.put(card.calculateExtraPoints(), card));

            Map.Entry<Integer, Card> entryWithMaxScore = null;

            // Iterate in the map to find the card with highest point yield
            for (Map.Entry<Integer, Card> currentEntry : cardsAndScoresMap.entrySet()) {

                if (entryWithMaxScore == null || currentEntry.getKey().compareTo(entryWithMaxScore.getKey()) > 0) {
                    entryWithMaxScore = currentEntry;
                }
            }

            // return the card with highest point yield
            if (entryWithMaxScore != null) {
                return entryWithMaxScore.getValue();
            }
        }
        return null;
    }
}
