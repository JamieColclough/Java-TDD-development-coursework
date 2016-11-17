package game;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *Series of tests for generic CardDeck class
 * @version 2.0
 */
public class CardDeckTest {
    CardDeck cardDeck2;
    CardDeck cardDeck4;
    CardDeck cardDeck5;
    CardDeck cardDeck6;
    
    CardDeck testDeck;
    CardDeck expectedDeck;
    
    Card testCard;
    Card expectedCard;
    
    ArrayList<Card> cardArray1 = new ArrayList<>();
    ArrayList<Card> cardArray2 = new ArrayList<>();
    ArrayList<Card> cardArray3 = new ArrayList<>();
    ArrayList<Card> cardArray4 = new ArrayList<>();
    ArrayList<Card> cardArray5 = new ArrayList<>();
    ArrayList<Card> cardArray6 = new ArrayList<>();
    
    @Before
    public void setUp() {
        ArrayList<Card> cardArray = new ArrayList<>();
        cardDeck2 = new CardDeck(cardArray);//initialises cardDeck2 as an empty deck
        for (int i=0;i<28;i++) cardArray.add(new Card(i));
        expectedDeck = new CardDeck(cardArray);//initialises expectedDeck as a deck with cards with values from 0 to 27
        testDeck = new CardDeck(cardArray);

    }

    /**
     * Tests the gameInterruption method works successfully
     */
    @Test
    public void testGameInterruption() {
        testDeck.gameInterruption();
        try{
            testDeck.takeCard();
            fail();//if the card was taken without raising an InterruptedException, the method didn't work
        }catch(InterruptedException e){}
    }

    /**
     * Tests that the takeCardmethod works on the corner case of a full deck
     * @throws java.lang.InterruptedException
     */
    @Test
    public void takeCardFromFullDeck() throws InterruptedException{    
        cardArray1 = new ArrayList<>();
        for(int i=1;i<28;i++) cardArray1.add(new Card(i));
        expectedDeck = new CardDeck(cardArray1);          
        
        expectedCard = new Card(0);
        testCard = testDeck.takeCard();
        assertEquals(expectedCard,testCard);
        assertEquals(expectedDeck,testDeck);//asserts the hand taken is the same as the mock deck with same contents as expected result   
    }
    
    /**
     * Tests that the takeCard method works on a regular deck
     * @throws InterruptedException 
     */
    @Test
    public void takeCardFromRegularDeck() throws InterruptedException{    
        cardArray1 = new ArrayList<>();
        cardArray2 = new ArrayList<>();
        
        for (int i=0;i<15;i++) cardArray1.add(new Card(i));
        testDeck = new CardDeck(cardArray1);
        
        for(int i=1;i<15;i++) cardArray2.add(new Card(i));
        expectedDeck = new CardDeck(cardArray2);          
        
        expectedCard = new Card(0);
        testCard = testDeck.takeCard();
        assertEquals(expectedCard,testCard);
        assertEquals(expectedDeck,testDeck);//asserts the hand taken is the same as the mock deck with same contents as expected result           
    }        
    
    /**
     * tests the takeCard method on the corner case of a deck with only 1 element remaining
     * @throws InterruptedException 
     */
    @Test
    public void takeCardFromDeckWithOneElement() throws InterruptedException{
        ArrayList<Card> cardArray = new ArrayList<>();
        cardArray.add(new Card(0));
        testDeck = new CardDeck(cardArray); 
        
        ArrayList<Card> emptyArray = new ArrayList<>();
        expectedDeck = new CardDeck(emptyArray);
        
        expectedCard = new Card(0);
        testCard = testDeck.takeCard();
        assertEquals(expectedCard,testCard);
        assertEquals(expectedDeck,testDeck);
    }

    /**
     * Tests the placeCard method on the corner case of an empty deck
     */
    @Test
    public void placeCardOnEmptyDeck(){
        ArrayList<Card> testCardArray = new ArrayList<>();
            
        testCardArray.add(new Card(0));
            
        expectedDeck = new CardDeck(testCardArray);
        //decks created so the value will be similar to the queuing mechanism implemented by the cardDeck
        
        testDeck = new CardDeck(new ArrayList<>());
        testDeck.placeCard(new Card(0));  
        assertEquals(expectedDeck,testDeck);        
    }
    
    /**
     * Tests placeCard method on the regular case of a non-empty deck
     */
    @Test
    public void placeCardOnNonEmptyDeck(){
        ArrayList<Card> testCardArray = new ArrayList<>();
           
        testCardArray.add(new Card(0));
        testCardArray.add(new Card(1));
           
        expectedDeck = new CardDeck(testCardArray);
         
        ArrayList<Card> cardArray = new ArrayList<>();
        cardArray.add(new Card(0));        
        testDeck = new CardDeck(cardArray);
        testDeck.placeCard(new Card(1));  
        assertEquals(expectedDeck,testDeck);      
    }

    /**
     * Test of equals method on instances of cardDeck win which true should be returned
     */
    @Test
    public void testEqualsTrue() {
        for (int i=0;i<4;i++){
            cardArray1.add(new Card(i));
            cardArray2.add(new Card(i));  

        }
        expectedDeck = new CardDeck(cardArray1);
        cardDeck2 = new CardDeck(cardArray2);
        
        assertEquals(expectedDeck,expectedDeck);//deck must be equal to itself, equality is reflexive
        assertEquals(expectedDeck,cardDeck2);//deck will be equal to deck with same stats
    }
    
    /**
     * Test of equals method on instances of CardDeck in which false should be returned
     */
    @Test
    public void testEqualsFalse(){        
        for (int i=0;i<4;i++){
            cardArray1.add(new Card(i));
    }

        for (int i=0;i<4;i++){cardArray4.add(new Card(i+7));}
        int reverseCount = 4;
        for (int i=0;i<4;i++){
            cardArray5.add(new Card(reverseCount));
            reverseCount --;
        }
        expectedDeck = new CardDeck(cardArray1);

        //cardDeck 3 is not tested for equality here as the resizing of the arrayLists means that it would be equal to expectedDeck
        cardDeck4 = new CardDeck(cardArray4);
        cardDeck5 = new CardDeck(cardArray5);
        cardDeck6 = new CardDeck(cardArray6);
        
        
        assertNotEquals(expectedDeck,cardDeck4);
        assertNotEquals(expectedDeck,cardDeck5);
        assertNotEquals(expectedDeck,cardDeck6);
    }
}