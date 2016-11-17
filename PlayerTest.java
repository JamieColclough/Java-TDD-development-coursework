package game2;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * series of unit tests for generic version of player class
 * @version 2.0
 */
public class PlayerTest {
    Player expectedPlayer;
    Player testPlayer;
    ArrayList<Card> genericHand;
    ArrayList<Card> cardArrayl = new ArrayList<>();
    ArrayList<Card> cardArrayr = new ArrayList<>();
    CardDeck cardDeckl;
    CardDeck cardDeckr;
    
    CardDeck differentDeck = new CardDeck(cardArrayl);//made just so it is different to the others
    
    Card testCard;
    Card expectedCard;
    
    @Before
    public void setUp() {
        genericHand = new ArrayList<>();
        for(int i=0;i<4;i++){
            genericHand.add(new Card(i+1));}
        
        cardDeckl = new CardDeck(cardArrayl);
        cardDeckr = new CardDeck(cardArrayl);
        cardDeckl.placeCard(new Card(1));
        cardDeckr.placeCard(new Card(2));//done just to make both decks original
    }

    /**
     * Tests for addCard method removed as the method itself was removed 
     */
    
    
    /**
     * Test to show winningHand method returns true on a winning hand
     */
    @Test
    public void testWinningHandTrue() {
        ArrayList<Card> winningHand = new ArrayList<>();
        for(int i=0;i<4;i++){
        winningHand.add(new Card(1));
        
        testPlayer = new Player(1,winningHand,cardDeckl,cardDeckr);
        assertTrue(testPlayer.winningHand());//asserts that if the player has a winning hand, will return true
        }
    }
    /**
     * Test to show winningHand method returns false on a non-winning hand
     */
    @Test
    public void testWinningHandFalse(){
        testPlayer = new Player(1,genericHand,cardDeckl,cardDeckr);
        assertFalse(testPlayer.winningHand());//converse for this assertion

    }

    /**
     * Test that when the hand is a winning one, nothing is changed
     */
    @Test
    public void noNonPreferedCardInHand() {        
        ArrayList<Card> winningHand = new ArrayList<>();
        for(int i=0;i<4;i++){
            winningHand.add(new Card(1));
        //if the player has no non-prefered cards, will return null
        testPlayer = new Player(1,winningHand,cardDeckl,cardDeckr);
        expectedPlayer = new Player(1,winningHand,cardDeckl,cardDeckr);
        assertNull(testPlayer.nonPreferedCard());
        assertEquals(expectedPlayer,testPlayer);
        }
    }
    /**
     * Test to see whether the nonPreferedCard method works on a hand where
     * the first non-preferred card is first in the hand
     */
    @Test
    public void nonPreferedCardIsFirst() {  
        genericHand = new ArrayList<>();
        ArrayList<Card> testHand = new ArrayList<>();
        for(int i=0;i<4;i++){
            genericHand.add(new Card(i+1));
        }
        testHand.add(new Card(1));
        testHand.add(new Card(3));
        testHand.add(new Card(4));
        testPlayer = new Player(1,genericHand,cardDeckl,cardDeckr);
        expectedPlayer = new Player(1,testHand,cardDeckl,cardDeckr);
        assertEquals(new Card(2),testPlayer.nonPreferedCard());//1 is the preferred card, will return next non-preferred one,
        assertEquals(expectedPlayer,testPlayer);       
    }
    
    /**
     * Test to see whether the nonPreferedCard method works on a hand where the first 
     * non-preferred card is last in the hand
     */
    @Test
    public void nonPreferedCardIsLast() {  
        genericHand = new ArrayList<>();
        ArrayList<Card> testHand = new ArrayList<>();
        for(int i=0;i<3;i++){
            genericHand.add(new Card(1));
            testHand.add(new Card(1));
        }
        genericHand.add(new Card(2));
        testPlayer = new Player(1,genericHand,cardDeckl,cardDeckr);
        expectedPlayer = new Player(1,testHand,cardDeckl,cardDeckr);
        expectedCard = new Card(2);
        assertEquals(expectedCard,testPlayer.nonPreferedCard());//1 is the preferred card, will return next non-preferred one,
        assertEquals(expectedPlayer,testPlayer);
    }

    /**
     * Tests equals method on instances of player in which the method should return true
     */
    @Test
    public void testEqualsTrue() {        
        //sets up instances of players to test equals() on
        expectedPlayer = new Player(1,genericHand,cardDeckl,cardDeckr);
        testPlayer = new Player(1,genericHand,cardDeckl,cardDeckr);
        
        assertEquals(expectedPlayer,expectedPlayer); //player should be equal to itself
        assertEquals(expectedPlayer,testPlayer); //should be equal by defined method       
    }
    
    /**
     * Tests equals method on instances of Player in which the method should return false
     */
    @Test
    public void testEqualsFalse(){
        
        Player differentPreferencePlayer = new Player(2,genericHand,cardDeckl,cardDeckr);
        Player differentHandPlayer = new Player(1,new ArrayList<>(),cardDeckl,cardDeckr);
        Player differentLDeckPlayer = new Player(1,genericHand,differentDeck,cardDeckr);
        Player differentRDeckPlayer = new Player(1,genericHand,cardDeckl,differentDeck);
        
        assertNotEquals(expectedPlayer,differentPreferencePlayer);
        assertNotEquals(expectedPlayer,differentHandPlayer);
        assertNotEquals(expectedPlayer,differentLDeckPlayer);
        assertNotEquals(expectedPlayer,differentRDeckPlayer);
        
    }
}
