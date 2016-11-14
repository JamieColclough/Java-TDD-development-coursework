package game;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * series of unit tests for player class
 * @version 1.0
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
        cardDeckr.placeCard(new Card(2));//done just to make both decks original
    }

    /**
     * Test of addCard method, of class Player.
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
    
    @Test
    public void addCardToHandWith5Cards(){
         Card[] testHand = new Card[4];
        for(int i=0;i<4;i++){ 
            testHand[i] = new Card(i);
        }        
        testPlayer = new Player(1,testHand,cardDeckl,cardDeckr);
        testPlayer.addCard(new Card(5));
        assertFalse(testPlayer.addCard(new Card(6)));//can't testEquals for this one as cannot create player with array of 5
    }

    /**
     * Test of winningHand method, of class Player.
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
    @Test
    public void testWinningHandFalse(){
        testPlayer = new Player(1,genericHand,cardDeckl,cardDeckr);
        assertFalse(testPlayer.winningHand());//converse for this assertion

    }

    /**
     * Test of nonPreferedCard method, of class Player.
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
     * Test of equals method, of class Player.
     */
    @Test
    public void testEqualsTrue() {        
        //sets up instances of players to test equals() on
        expectedPlayer = new Player(1,genericHand,cardDeckl,cardDeckr);
        testPlayer = new Player(1,genericHand,cardDeckl,cardDeckr);
        
        assertEquals(expectedPlayer,expectedPlayer); //player should be equal to itself
        assertEquals(expectedPlayer,testPlayer); //should be equal by defined method       
    }
    
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
