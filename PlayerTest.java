package game;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * series of unit tests for player class
 * @version 1.1
 */
public class PlayerTest {
    Player expectedPlayer;
    Player testPlayer;
    Card[] genericHand;
    Card[] cardArrayl = new Card[28];
    Card[] cardArrayr = new Card[28];
    CardDeck cardDeckl;
    CardDeck cardDeckr;
    
    CardDeck differentDeck = new CardDeck(cardArrayl);//made just so it is different to the others
    
    Card testCard;
    Card expectedCard;
    
    @Before
    public void setUp() {
        genericHand = new Card[4];
        for(int i=0;i<4;i++){
            genericHand[i] = new Card(i+1);}
        
        cardDeckl = new CardDeck(cardArrayl);
        cardDeckr = new CardDeck(cardArrayl);
        cardDeckl.placeCard(new Card(1));
        cardDeckr.placeCard(new Card(2));//done just to make both CardDecks original
    }

    /**
     * Test to analyse behaviour of addCard() on a player whose hand has 4 cards already in it
     */
    @Test
    public void addCardToHandWith4Cards() {
        Card[] testHand = new Card[4];
        for(int i=0;i<4;i++){ 
            testHand[i] = new Card(i);
        }
        
        testPlayer = new Player(1,testHand,cardDeckl,cardDeckr);
        assertTrue(testPlayer.addCard(new Card(4)));//
    }
    
    /**
     * Test to analyse behaviour of addCard() on a player whose hand has 5 cards in it
     */
    @Test
    public void addCardToHandWith5Cards(){
         Card[] testHand = new Card[4];
        for(int i=0;i<4;i++){ 
            testHand[i] = new Card(i);
        }        
        testPlayer = new Player(1,testHand,cardDeckl,cardDeckr);  // player hand has 4 cards
        testPlayer.addCard(new Card(5));                          // players hand should now have 5 cards
        assertFalse(testPlayer.addCard(new Card(6)));             // should return false as you cant add anymore cards to a player who already has 5 cards in hand
    }

    /**
     * Test to show winningHand() returns true on a winning hand
     */
    @Test
    public void testWinningHandTrue() {
        Card[] winningHand = new Card[4];
        for(int i=0;i<4;i++){
        winningHand[i] = new Card(1);
        
        testPlayer = new Player(1,winningHand,cardDeckl,cardDeckr);
        assertTrue(testPlayer.winningHand());//asserts that if the player has a winning hand, will return true
        }
    }
    /**
     * Test to show winningHand() returns false on a non-winning hand
     */
    @Test
    public void testWinningHandFalse(){
        testPlayer = new Player(1,genericHand,cardDeckl,cardDeckr);
        assertFalse(testPlayer.winningHand());//converse for this assertion

    }

    /**
     * Test that the method nonPrederredCard returns null and does not remove any cards from the players hand
     * when there are no non-preferred cards in the players hand
     */
    @Test
    public void noNonPreferedCardInHand() {        
        Card[] winningHand = new Card[4];
        for(int i=0;i<4;i++){
            winningHand[i] = new Card(1);
        //if the player has no non-prefered cards, will return null
        testPlayer = new Player(1,winningHand,cardDeckl,cardDeckr);
        expectedPlayer = new Player(1,winningHand,cardDeckl,cardDeckr);
        assertNull(testPlayer.nonPreferedCard());
        assertEquals(expectedPlayer,testPlayer);
        }
    }
    /**
     * Test to see whether the nonPreferredCard method works on a hand where the first non-preferred card is first in the hand
     */
    @Test
    public void nonPreferedCardIsFirst() {  
        genericHand = new Card[4];
        Card[] testHand = new Card[4];
        for(int i=0;i<4;i++){
            genericHand[i] = new Card(i+1);
        }
        testHand[0] = new Card(1);
        testHand[1] = new Card(3);
        testHand[2] = new Card(4);
        testPlayer = new Player(1,genericHand,cardDeckl,cardDeckr);
        expectedPlayer = new Player(1,testHand,cardDeckl,cardDeckr);
        assertEquals(new Card(2),testPlayer.nonPreferedCard());//1 is the preferred card, will return next non-preferred one,
        assertEquals(expectedPlayer,testPlayer);       
    }
    
    /**
     * Test to see whether the nonPrefferedCard method works on a hand where the first non-preferred card is last in the hand
     */
    @Test
    public void nonPreferedCardIsLast() {  
        genericHand = new Card[4];
        Card[] testHand = new Card[4];
        for(int i=0;i<3;i++){
            genericHand[i] = new Card(1);
            testHand[i] = new Card(1);
        }
        genericHand[3] = new Card(2);
        testPlayer = new Player(1,genericHand,cardDeckl,cardDeckr);
        expectedPlayer = new Player(1,testHand,cardDeckl,cardDeckr);
        expectedCard = new Card(2);
        assertEquals(expectedCard,testPlayer.nonPreferedCard());//1 is the preferred card, will return next non-preferred one,
        assertEquals(expectedPlayer,testPlayer);
    }

    /**
     * Tests equals method on instances of Player in which the method should return true
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
        Player differentHandPlayer = new Player(1,new Card[4],cardDeckl,cardDeckr);
        Player differentLDeckPlayer = new Player(1,genericHand,differentDeck,cardDeckr);
        Player differentRDeckPlayer = new Player(1,genericHand,cardDeckl,differentDeck);
        
        assertNotEquals(expectedPlayer,differentPreferencePlayer);
        assertNotEquals(expectedPlayer,differentHandPlayer);
        assertNotEquals(expectedPlayer,differentLDeckPlayer);
        assertNotEquals(expectedPlayer,differentRDeckPlayer);
        
    }
}
