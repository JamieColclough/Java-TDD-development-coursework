package game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *Game application class
 *@ version 1.3
 */
public class Game {
    
    /** Function to deal out cards in a round robin fashion to either form all hands or all decks
     * 
     * 
     * @param pack the initial pack that deals out all cards
     * @param isHand boolean to indicate whether or not to deal with respect to a hand or deck
     * @param n amount of players
     * @param placeInPack indexing starts from different place depending on deck or card
     * @return Array of either decks or hands
     */
    static Card[][] deal(Card[] pack, boolean isHand, int n, int placeInPack){
        Card[][] cardsArray = isHand? new Card[n][4]: new Card[n][4*n]; //if hand: each array is size 4 else size 4n
                
        int addCount = placeInPack; //Starting index to iterate through deck array
        for(int cIndex=0; cIndex<4; cIndex++){ //deals to hand or deck in a round robin fashion
            for (int pIndex=0; pIndex<n; pIndex++ ){
                cardsArray[cIndex][pIndex] = pack[addCount];
                addCount++; //increments count so next card added is next one in the deck
            }
        }      
        return cardsArray;
    }
  
    /**Static function to return all hands that will be used in the game
     * 
     * @param pack the initial pack that deals out all cards
     * @param n the amount of players
     * @return an array of hands
     */
    static Card[][] dealToHands(Card[] pack, int n){
        return deal(pack, true, n, 0); //sets parameters to specify hand in the deal function
    }

    /** Static function to return all decks that will be used in the game
     * 
     * @param pack the initial pack that deals out all cards
     * @param n the amount of players
     * @return an array of decks
     */
    static Card[][] dealToDecks(Card[] pack, int n){
        return deal(pack, false, n, 4*n);
    }
    
    /**Static Main method to ask user for input and simulate the game
     * @param args the command line arguments
     */   
    public static void main(String[] args){
        Scanner cmdScan = new Scanner(new InputStreamReader(System.in));
        
        //While loop guard to repeatedly query user until correct file given
        int n = 0;
        while ((n < 2) || (n > 7)){
            System.out.println("Enter Value of n (Must be Integer between 2 and 7):");
            try {
                 n = Integer.parseInt(cmdScan.next()); //if n isn't an integer will catch and ask again
            }catch(NumberFormatException e){System.out.println("Invalid type of n, Must be int");}
        
        }
        
        //guarded while loop to query user for a legitimate text file
        String textFile;
        FileReader fr = null;
        while (fr == null) {       
            System.out.println("text file:  "); 
            try{
                //Must be in full path form i.e. C:\sample_deck.txt
                textFile = cmdScan.next();       
                fr = new FileReader(textFile);
            }catch(FileNotFoundException e){System.out.println("File Not Found");}
        }
        
        Scanner fileScan = new Scanner(fr);
        Card[] initialPack = new Card[8*n];
        System.out.println(8*n);
        int addCount = 0;
        while (fileScan.hasNextLine()) {            
            Card nextCard = new Card(fileScan.nextInt());
            initialPack[addCount] = nextCard;
            addCount++;
        } 
        
        //iterates over handArray, distributing each card to it in a round robin fashion
        Card[][] handArray = dealToHands(initialPack, n);
        
        //performs same iterations but this time on the decks in a round robin fashion
        Card[][] deckArray =  dealToDecks(initialPack, n);
    }}        