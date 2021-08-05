package tr.metu.ceng.construction.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.metu.ceng.construction.server.DTO.TableStateDTO;
import tr.metu.ceng.construction.server.enums.EventType;
import tr.metu.ceng.construction.server.exception.CouldNotPickACardException;
import tr.metu.ceng.construction.server.exception.GameNotFoundException;
import tr.metu.ceng.construction.server.model.Card;
import tr.metu.ceng.construction.server.model.Game;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static tr.metu.ceng.construction.server.common.CommonConstants.DOUBLE_PISTI_POINT;
import static tr.metu.ceng.construction.server.common.CommonConstants.PISTI_POINT;
import static tr.metu.ceng.construction.server.common.Messages.COULD_NOT_PICK_CARD;
import static tr.metu.ceng.construction.server.common.Messages.GAME_NOT_EXIST;
import static tr.metu.ceng.construction.server.enums.Rank.JACK;
import static tr.metu.ceng.construction.server.utils.GameUtilities.calculatePointToAdd;

@Service
@RequiredArgsConstructor
public class ComputerMoveService {

    private final CardService cardService;

    /**
     * Makes computer move for the three difficulty levels respectively.
     * For easy level, picks a random card and plays it.
     * For normal level, tries to throw the card with the same rank
     * as the top one in the face-up cards. If it does not have one ,and has JACK,
     * it will throw it. Otherwise, throws a random card.
     * For hard level, checks how many cards the pile consists of and applies different
     * strategies accordingly.
     *
     * @param game this is the game being played
     * @param tableState this is the current table state
     * @throws GameNotFoundException if the passed game parameter is null
     * @throws CouldNotPickACardException if the computer could not pick a card
     */
    public void makeComputerMove(Game game, TableStateDTO tableState) throws GameNotFoundException, CouldNotPickACardException {
        if (game != null) {
            Card pickedCard = null;
            Set<Card> computerCards = tableState.getComputerCards();
            List<Card> faceUpCards = tableState.getFaceUpCards();

            int level = game.getLevel();
            if (level == 1) {
                /* easy level: computer picks a random card and plays it */
                pickedCard = cardService.getRandomCard(computerCards);

            } else if (level == 2) {
            /* normal level: computer tries to throw the card with the same rank as the top one in the face-up cards.
             If it does not have one ,and has JACK, it will throw it. Otherwise, it will throw a random card */

                if (faceUpCards.size() > 0) {
                    Card lastCardOnMiddle = faceUpCards.get(faceUpCards.size() - 1);
                    pickedCard = pickMatchingCardOrJack(lastCardOnMiddle, computerCards);
                }

                if (pickedCard == null) {
                    pickedCard = cardService.getRandomCard(computerCards);
                }

            } else if (level == 3) {
             /* hard level: check whether the pile consists of exactly one card, zero card or more than one card
            and apply different strategies accordingly*/
                pickedCard = playHardLevel(tableState);
            }

            if (pickedCard == null) {
                // in case there is a problem in picking a card
                throw new CouldNotPickACardException(COULD_NOT_PICK_CARD);
            }

            computerCards.remove(pickedCard);

            // calculate computer score and set how many cards captured by computer
            executeComputerMove(pickedCard, tableState);
        }
        else {
            throw new GameNotFoundException(GAME_NOT_EXIST);
        }
    }

    /* checks if computer captures the play pile with its move, if so, update computer score and
   total number of cards captured by computer */
    private void executeComputerMove(Card playedCard, TableStateDTO tableState) {
        List<Card> faceUpCards = tableState.getFaceUpCards();

        if (faceUpCards.size() != 0)  {
            Card lastCardOnMiddle = faceUpCards.get(faceUpCards.size() - 1);
            if (playedCard.canCapture(lastCardOnMiddle)) {
                //capture all the cards in the play pile
                tableState.setEventType(EventType.COMPUTER_CAPTURED);

                Set<Card> computerCapturedCards = new HashSet<>(faceUpCards);
                computerCapturedCards.add(playedCard);

                // update computer level score
                if (faceUpCards.size() == 1 && lastCardOnMiddle.getRank().equals(JACK)){
                    tableState.setComputerLevelScore(tableState.getComputerLevelScore() + DOUBLE_PISTI_POINT);
                } else if (faceUpCards.size() == 1) {
                    tableState.setComputerLevelScore(tableState.getComputerLevelScore() + PISTI_POINT);
                }
                tableState.setComputerLevelScore(tableState.getComputerLevelScore() + calculatePointToAdd(faceUpCards, playedCard));

                int updatedCapturedCardsNumberByComputer = tableState.getCapturedCardsNumberByComputer() + computerCapturedCards.size();
                tableState.setCapturedCardsNumberByComputer(updatedCapturedCardsNumberByComputer);

                //make face-up cards empty
                tableState.setFaceUpCards(new ArrayList<>());
            }
            else {
                // cannot capture
                faceUpCards.add(playedCard);
            }
        }
        else {
            faceUpCards.add(playedCard);
        }
    }

    private Card pickMatchingCardOrJack(Card lastCardOnMiddle, Set<Card> computerCards) {
        Card pickedCard = null;

        Card matchingCard = computerCards.stream()
                .filter(card-> card.compareRank(lastCardOnMiddle))
                .findFirst()
                .orElse(null);

        if(matchingCard != null){
           pickedCard = matchingCard;
        } else {
            Card jack = computerCards.stream()
                    .filter(card-> card.getRank().equals(JACK))
                    .findFirst().orElse(null);
            if (jack != null) {
                pickedCard = jack;
            }
        }

        return pickedCard;
    }

    private Card findMatchingCardWithHighestPoint(Card lastCardOnMiddle, Set<Card> computerCards) {
        Card pickedCard = null;

        Set<Card> matchingCards = computerCards.stream()
                .filter(card-> card.compareRank(lastCardOnMiddle))
                .collect(Collectors.toSet());

        if (matchingCards.size() > 1) {
            //there are more than one matching card, find the highest scored one if any
            pickedCard = cardService.findHighestScoredCard(matchingCards);

        }
        else if (matchingCards.size() == 1) {
            pickedCard = matchingCards.iterator().next();
        }

        return pickedCard;
    }

    private Card playHardLevel(TableStateDTO tableState) {
        Card pickedCard = null;
        Set<Card> computerCards = tableState.getComputerCards();
        List<Card> faceUpCards = tableState.getFaceUpCards();

        // check if computer has a card with rank JACK
        Card jack = computerCards.stream()
                .filter(card-> card.getRank().equals(JACK))
                .findFirst().orElse(null);

        if (faceUpCards.size() == 0) {
            // do not pick a card whose rank is JACK
            if (jack != null) {
                Set<Card> cardSet = new HashSet<>(computerCards);
                cardSet.remove(jack);
                if (!cardSet.isEmpty()) {
                    pickedCard = cardService.getRandomCard(cardSet);
                } else {
                    // computer has just one card, then throw it anyway
                    pickedCard = jack;
                }
            }
        } else {
            // there are cards in the center of table
            Card lastCardOnMiddle = faceUpCards.get(faceUpCards.size() - 1);

            // choose the card whose rank is JACK if computer has one
            if (faceUpCards.size() == 1) {
                if (jack != null && lastCardOnMiddle.getRank().equals(JACK)) {
                    // choose the card whose rank is JACK if computer has one
                    pickedCard = jack;
                } else {
                    // find the matching card with highest point yield if any
                    pickedCard = findMatchingCardWithHighestPoint(lastCardOnMiddle, computerCards);
                }

            } else {
                //there are more than one cards at center

                // try to find the matching card with highest point yield if any
                pickedCard = findMatchingCardWithHighestPoint(lastCardOnMiddle, computerCards);

            }
        }

        if (pickedCard == null && jack != null) {
            pickedCard = jack;
        }

        if (pickedCard == null ) {
            pickedCard = cardService.getRandomCard(computerCards);
        }
        return pickedCard;
    }

}
