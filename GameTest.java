package game;

import java.io.BufferedWriter;
import java.io.File;
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
 * @version 1.0
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

    /**
     * Test of correctFile method, of class ThreadingCA.
     * @throws java.io.IOException
     */
    @Test
    public void testCorrectFile() throws IOException{
        //first 3 tests based on file size, tests for when file is exactly right,too small and too big
        assertTrue(Game.correctFile(fr1,3));
        fr1= new FileReader(file1);
        assertTrue(Game.correctFile(fr1,4));
        fr1 = new FileReader(file1);
        assertFalse(Game.correctFile(fr1,5));
        
        //covers corner cases of an empty file and a file not containing integers
            assertFalse(Game.correctFile(fr2, 2));
            assertFalse(Game.correctFile(fr3, 2));
            fr2 = new FileReader(file2);
            fr3 = new FileReader(file3);
        
        
    }
    
    @Test
    public void testWinnableGame(){
        Card[] invalidCardArray = new Card[24];
        Card[] validCardArray = new Card[24];
        
        
        int cardCount = 0;
        for (int i=0; i<4; i++){
            for (int j=0;j<6;j++){
                validCardArray[cardCount] = new Card(j);
                cardCount++;
            }
        }
        
        cardCount = 0;
        for (int i=0; i<3; i++){
            for (int j=0;j<8;j++){
                invalidCardArray[cardCount] = new Card(j);
                cardCount++;
            }
        }
        assertTrue(Game.winnableGame(validCardArray, 3));
        assertFalse(Game.winnableGame(invalidCardArray,3));
    }
    
    /**
     * Checks the deal function splits the decks in a round robin fashion
     */
    @Test
    public void testDeal() {        
        for(int n = 2; n<8;n++){
        int x = 1;
        //creates the deck to be tested on the method, in the form 1,2,....n n times over
            Card[] deck = new Card[8*n];
               for (int i=1; i<=8*n; i++){       
                   if (x>n) x=1;
                   deck[i-1] = new Card(x);
                   x++;
                }
               
                //tests both dealHand and Deal deck so testing those functions seperately is unecessary
                Card[][] dealedHand = Game.deal(deck, true,n,0);
                Card[][] dealedDeck = Game.deal(deck, false, n, 4*n);
                
                for (int j=0;j<n;j++){                    
                    for (int m=0; m<4; m++){
                        assertEquals(dealedHand[j][m].getValue(),j+1);
                        assertEquals(dealedDeck [j][m].getValue(),j+1);
                    }
                }
        }
                
    }

    /**
     * Test of fillPack method, of class Game.
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
