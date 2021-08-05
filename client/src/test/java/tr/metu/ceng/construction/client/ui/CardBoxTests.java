package tr.metu.ceng.construction.client.ui;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import tr.metu.ceng.construction.client.DTO.CardDTO;
import tr.metu.ceng.construction.client.component.FaceDownCardBox;
import tr.metu.ceng.construction.client.component.MiddleCardBox;
import tr.metu.ceng.construction.client.component.OpponentCardBox;
import tr.metu.ceng.construction.client.component.PlayerCardBox;
import tr.metu.ceng.construction.client.enums.Rank;
import tr.metu.ceng.construction.client.enums.Suit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardBoxTests extends ApplicationTest {

    @Test
    public void testPlayerCardBox() {
        CardDTO card = new CardDTO(Suit.CLUBS, Rank.ACE);
        Set<CardDTO> cardDTOS = new HashSet<>();

        PlayerCardBox playerCardBox = new PlayerCardBox(cardDTOS);
        cardDTOS.add(card);
        playerCardBox.setCards(cardDTOS, 1);
        assertEquals(1, playerCardBox.getChildren().size());

        playerCardBox.playCard(new CardDTO(Suit.CLUBS, Rank.ACE));

        assertEquals(0, playerCardBox.getChildren().size());
    }

    @Test
    public void testOpponentCardBox() {
        OpponentCardBox opponentCardBox = new OpponentCardBox(0);
        assertEquals(0, opponentCardBox.getChildren().size());

        opponentCardBox.increment();
        assertEquals(1, opponentCardBox.getChildren().size());

        opponentCardBox.decrement();
        assertEquals(0, opponentCardBox.getChildren().size());

        opponentCardBox.setNOfCards(4);
        assertEquals(4, opponentCardBox.getChildren().size());
    }

    @Test
    public void testMiddleCardBox() {
        CardDTO card = new CardDTO(Suit.CLUBS, Rank.ACE);
        List<CardDTO> cardDTOS = new ArrayList<>();
        List<CardDTO> anotherDeck = new ArrayList<>();
        anotherDeck.add(card);

        MiddleCardBox middleCardBox = new MiddleCardBox(cardDTOS);
        assertEquals(0, middleCardBox.getChildren().size());

        middleCardBox.setCards(anotherDeck);
        assertEquals(1, middleCardBox.getChildren().size());
    }

    @Test
    public void testFaceDownCardBox() {
        FaceDownCardBox faceDownCardBox = new FaceDownCardBox(20);
        faceDownCardBox.setNumberOfCards(30);

        assertEquals(3, faceDownCardBox.getChildren().size());
    }

}
