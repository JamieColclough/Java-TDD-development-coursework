package game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *Series of tests for CardDeck class
 * @version 1.2
 */
public class CardDeckTest {
    CardDeck cardDeck2;
    CardDeck cardDeck3;
    CardDeck cardDeck4;
    CardDeck cardDeck5;
    CardDeck cardDeck6;
    
    CardDeck testDeck;
    CardDeck expectedDeck;
    
    Card testCard;
    Card expectedCard;
    
    Card[] cardArray1 = new Card[4];
    Card[] cardArray2 = new Card[4];
    Card[] cardArray3 = new Card[5];
    Card[] cardArray4 = new Card[4];
    Card[] cardArray5 = new Card[4];
    Card[] cardArray6 = new Card[0];
    
    @Before
    public void setUp() {
        Card[] cardArray = new Card[28]; //28 highest possible size as this is 4n
        cardDeck2 = new CardDeck(cardArray);//initialises cardDeck2 as an empty deck
        for (int i=0;i<28;i++) cardArray[i] = new Card(i);
        expectedDeck = new CardDeck(cardArray);//initialises expectedDeck as a deck with cards with values from 0 to 27
        testDeck = new CardDeck(cardArray);

    }

    /**
     * Tests the gameInterruption method works successfully on an instance of CardDeck
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
     * Tests that the takeCard method works on the corner case of a full deck
     * @throws java.lang.InterruptedException
     */
    @Test
    public void takeCardFromFullDeck() throws InterruptedException{    
        cardArray1 = new Card[28];
        for(int i=1;i<28;i++) cardArray1[i-1] = new Card(i);
        expectedDeck = new CardDeck(cardArray1);          
        
        expectedCard = new Card(0);
        testCard = testDeck.takeCard();
        assertEquals(expectedCard,testCard);
        assertEquals(expectedDeck,testDeck);//asserts the hand taken is the same as the mock deck with same contents as expected result           
    }
    
    /**
     * Tests that the takeCard method works on a regular deck
     *@throws java.lang.InterruptedException
     */
    @Test
    public void takeCardFromRegularDeck() throws InterruptedException{    
        cardArray1 = new Card[28];
        cardArray2 = new Card[28];
        
        for (int i=0;i<15;i++) cardArray1[i] = new Card(i);
        testDeck = new CardDeck(cardArray1);
        
        for(int i=1;i<15;i++) cardArray2[i-1] = new Card(i);
        expectedDeck = new CardDeck(cardArray2);          
        
        expectedCard = new Card(0);
        testCard = testDeck.takeCard();
        assertEquals(expectedCard,testCard);
        assertEquals(expectedDeck,testDeck);//asserts the hand taken is the same as the mock deck with same contents as expected result           
    }
    /**
     * tests the takeCard method on the corner case of a deck with only 1 element remaining
     * @throws java.lang.InterruptedException
     */
    @Test
    public void takeCardFromDeckWithOneElement() throws InterruptedException{
        Card[] cardArray = new Card[1];
        cardArray[0] = new Card(0);
        testDeck = new CardDeck(cardArray); 
        
        Card[] emptyArray = new Card[1];
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
        Card[] testCardArray = new Card[2];
            
        testCardArray[0] = new Card(0);
            
        expectedDeck = new CardDeck(testCardArray);
        //decks created so the value will be similar to the queuing mechanism implemented by the cardDeck
        
        testDeck = new CardDeck(new Card[2]);
        testDeck.placeCard(new Card(0));  
        assertEquals(expectedDeck,testDeck);        
    }
    
    /**
     * Tests placeCard method on the regular case of an non-empty deck
     */
    @Test
    public void placeCardOnNonEmptyDeck(){
        Card[] testCardArray = new Card[2];
           
        testCardArray[0] = new Card(0);
        testCardArray[1] = new Card(1);
           
        expectedDeck = new CardDeck(testCardArray);
         
        Card[] cardArray = new Card[2];
        cardArray[0] = new Card(0);        
        testDeck = new CardDeck(cardArray);
        testDeck.placeCard(new Card(1));  
        assertEquals(expectedDeck,testDeck);      
    }

    /**
     * Test of equals method on instances of CardDeck in which true should be returned
     */
    @Test
    public void testEqualsTrue() {
        for (int i=0;i<4;i++){
            cardArray1[i] = new Card(i);
            cardArray2[i] = new Card(i);  

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
            cardArray1[i] = new Card(i);
            cardArray3[i] = new Card(i); 
    }
        expectedDeck = new CardDeck(cardArray1);

        for (int i=0;i<4;i++){cardArray4[i] = new Card(i+7);}
        int reverseCount = 4;
        for (int i=0;i<4;i++){
            cardArray5[i] = new Card(reverseCount);
            reverseCount --;
        }

        cardDeck3 = new CardDeck(cardArray3);//constructs cardDeck with same values as expectedDeck but a higher array number
        cardDeck4 = new CardDeck(cardArray4);//constructs cardDeck with each card 7 higher than expectedDeck
        cardDeck5 = new CardDeck(cardArray5);//constructs cardDeck with the same cards as expectedDeck but in reverse
        cardDeck6 = new CardDeck(cardArray6);//constucts cardDeck with an empty array
        
        
        assertNotEquals(expectedDeck,cardDeck3);//rest will be false as each has a slight difference
        assertNotEquals(expectedDeck,cardDeck4);
        assertNotEquals(expectedDeck,cardDeck5);
        assertNotEquals(expectedDeck,cardDeck6);
    }
}