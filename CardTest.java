/*
 * Test class for Card class
 * version 1.0
 */
package game;

import org.junit.Test;
import static org.junit.Assert.*;

public class CardTest {
    
    /**
     * Test of getValue method, of class Card.
     */
    @Test
    public void testGetValue() {
            Card tempCard = new Card(1);
            assertEquals(1,tempCard.getValue()); //asserts the card created has equal value to i 
        }
    
    /**
     * Tests for the various possibilities in the equals method 
     */
    @Test
    public void testEqualsTrue() {
        Card expectedCard = new Card(1);
        Card sameValueCard = new Card(1);
        
        assertEquals(expectedCard,expectedCard);//reflexive
        assertEquals(expectedCard,sameValueCard);//same details
        
    }
    
    @Test
    public void testEqualsFalse() {
        Card expectedCard = new Card(1);
        Card differentValueCard = new Card(2);
        assertNotEquals(expectedCard,differentValueCard);
    }
    
}
