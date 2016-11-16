package game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 * series of tests for Game application class
 * @version 1.2
 */
public class GameTest {  
    File file1;
    File file2;
    File file3;
    
    BufferedWriter fw1;
    BufferedWriter fw2;
    BufferedWriter fw3;
    
    FileReader fr1;
    FileReader fr2;
    FileReader fr3;
    
    int testHandValue;
    int testDeckValue;
    
   @Rule
   public TemporaryFolder tempFolder = new TemporaryFolder(); //sets up temporary folder, holds temp files and deletes itself after end of test

    
    /**
     * sets up various files and readers for tests
     * @throws IOException
     */
    @Before
    public void setUp() throws IOException {
        file1 = tempFolder.newFile("file1.txt");
        file2 = tempFolder.newFile("file2.txt");
        file3 = tempFolder.newFile("file3.txt");
        
        fw1 = new BufferedWriter(new FileWriter(file1));
        //fw2 not here as we are leaving file2 as an empty file
        fw3 = new BufferedWriter(new FileWriter(file3));
        
        for (int i=0; i<32;i++){
            fw1.write(Integer.toString(i));            
            fw1.newLine();
            //test by writing a non int to the file 
            fw3.write("fdsdtt");
            fw3.newLine();
        }        
        
      
        fw1.close();
        fw3.close();
        fr1 = new FileReader(file1);
        fr2 = new FileReader(file2);
        fr3 = new FileReader(file3);
    }

    //first 3 tests based on file size, tests for when file is exactly right,too small and too big
    //The deck we are using is exactly correct for game with 4 players
   
    @Test
    public void testCorrectFileTooBig() throws FileNotFoundException{
        assertTrue(Game.correctFile(fr1,3));       
    }
    
    @Test
    public void testCorrectFileCorrectSize() throws FileNotFoundException{
        assertTrue(Game.correctFile(fr1,4));
    }
    
    @Test
    public void testCorrectFileTooSmall() throws FileNotFoundException{
        assertFalse(Game.correctFile(fr1,5));
    } 
    
    /**
     * Tests that correctFile works when the file is empty
     * @throws FileNotFoundException 
     */
    @Test
    public void testCorrectFileEmpty() throws FileNotFoundException{
        //covers corner cases of an empty file 
        assertFalse(Game.correctFile(fr2, 2));            
        fr2 = new FileReader(file2);
                
    }
    
    /**
     * Tests that correctFile works when file has non-integers present
     * @throws FileNotFoundException 
     */
    @Test
    public void testCorrectFileInvalid() throws FileNotFoundException{
        //covers corner case of a file not containing integers
        assertFalse(Game.correctFile(fr3, 2)); 
        fr3 = new FileReader(file3);
    }
    
    /**
     * Tests that when when a file is valid, winnableGame returns false
     */
    @Test
    public void testWinnableGameValid(){
        Card[] validCardArray = new Card[24];        
        
        int cardCount = 0;
        for (int i=0; i<4; i++){
            for (int j=0;j<6;j++){
                validCardArray[cardCount] = new Card(j);
                cardCount++;
            }
        }        
        assertTrue(Game.winnableGame(validCardArray, 3));
        
    }
    /**
     * Tests that when a game isn't winnable, winnableGame returns false
     */
    @Test
    public void testWinnableGameInvalid(){
        Card[] invalidCardArray = new Card[24];
        int cardCount = 0;
        for (int i=0; i<3; i++){
            for (int j=0;j<8;j++){
                invalidCardArray[cardCount] = new Card(j);
                cardCount++;
            }
        }
        assertFalse(Game.winnableGame(invalidCardArray,3));
    }
    
    /**
     * Checks the deal function splits the decks in a round robin fashion
     */
    @Test
    public void testDeal() {
        int x = 1;
        //n=4 chosen for this test
            Card[] deck = new Card[32];
               for (int i=1; i<=32; i++){       
                   if (x>4) x=1;
                   deck[i-1] = new Card(x);
                   x++;
                }
               
            //tests both dealHand and Deal deck so testing those functions seperately is unecessary
            Card[][] dealedHand = Game.deal(deck, true,4,0);
            Card[][] dealedDeck = Game.deal(deck, false, 4, 16);
                
            for (int j=0;j<4;j++){                    
                for (int m=0; m<4; m++){
                    testHandValue = dealedHand[j][m].getValue();
                    testDeckValue = dealedDeck[j][m].getValue();
                    assertEquals(j+1,testHandValue);
                    assertEquals(j+1,testDeckValue);
                }
            }
        }
                
    

    /**
     * Tests that fillPack fills a pack in the correct fashion
     */
    @Test
    public void testFillPack() {
        Card[] testPack = Game.fillPack(fr1,4);        
         
        for(int i=0;i<32;i++){
            Card testCard = new Card(i);
            assertEquals(testPack[i],testCard); //checks every card in filled pack is present and in the correct order 
        }
    }
    
}
