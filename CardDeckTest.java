package game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *Series of tests for CardDeck class
 * @version 1.0
 */
public class CardDeckTest {
    CardDeck cardDeck1; 
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
     * Test of gameInterruption method, of class CardDeck.
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
     * Test of takeCard method, of class CardDeck.
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
     * Test of equals method, of class CardDeck.
     */
    @Test
    public void testEqualsTrue() {
        for (int i=0;i<4;i++){
            cardArray1[i] = new Card(i);
            cardArray2[i] = new Card(i);  

        }
        cardDeck1 = new CardDeck(cardArray1);
        cardDeck2 = new CardDeck(cardArray2);
        
        assertEquals(cardDeck1,cardDeck1);//deck must be equal to itself, equality is reflexive
        assertEquals(cardDeck1,cardDeck2);//deck will be equal to deck with same stats
    }
    
    @Test
    public void testEqualsFalse(){        
        for (int i=0;i<4;i++){
            cardArray1[i] = new Card(i);
            cardArray3[i] = new Card(i); 
    }

        for (int i=0;i<4;i++){cardArray4[i] = new Card(i+7);}
        int reverseCount = 4;
        for (int i=0;i<4;i++){
            cardArray5[i] = new Card(reverseCount);
            reverseCount --;
        }

        cardDeck3 = new CardDeck(cardArray3);
        cardDeck4 = new CardDeck(cardArray4);
        cardDeck5 = new CardDeck(cardArray5);
        cardDeck6 = new CardDeck(cardArray6);
        
        
        assertNotEquals(cardDeck1,cardDeck3);//rest will be false as each has a slight difference
        assertNotEquals(cardDeck1,cardDeck4);
        assertNotEquals(cardDeck1,cardDeck5);
        assertNotEquals(cardDeck1,cardDeck6);
    }
}