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
        for (int i = 0; i<1000; i++ ){
            Card tempCard = new Card(i);
            assertEquals(i,tempCard.getValue()); //asserts the card created has equal value to i 
        }
    }
    
}
