package game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *Game application class
 *@ version 1.6
 */
public class Game {
    
    /**Function to query whether the file is correct for the specified number of n
     * 
     * @param fr fileReader of specified text file
     * @param n number of players
     * @return boolean saying whether the file can be used in the game
     */
    static boolean correctFile(FileReader fr, int n){
        Scanner checker = new Scanner(fr);
        int fileLength = 0;
        while (checker.hasNextLine()) {            
            try {
                Integer.parseInt(checker.nextLine());
                fileLength++;
            }catch(NumberFormatException e){System.out.println("Invalid file layout");checker.close();return false;}
        }
            checker.close();
            if (fileLength < 8*n){System.out.println("File too small");return false;}
            else if (fileLength > 8*n){System.out.println("File too large, using first " + 8*n + " Cards");return true;}
            else {return true;}
        }
    /**
     * Method to fill initialPack from text file
     * @param fr fileReader of text file that is proven to already obey the rules of the game
     * @param n the amount of players in the game
     * @return the pack filled with card instances
     */
    static Card[] fillPack(FileReader fr,int n ){
        Scanner fileScan = new Scanner(fr);
        Card[] initialPack = new Card[8*n];
        for (int i=0; i<8*n;i++) {
            Card nextCard = new Card(Integer.parseInt(fileScan.nextLine()) );
            initialPack[i] = nextCard;
        }
        return initialPack;
    }
    /**
     * Function to see if game is able to be won i.e. the game will not stagnate indefinitely
     * @param pack the initial pack
     * @param n the number of players
     * @return boolean corresponding to whether the game can be won
     */
    static boolean winnableGame(Card[] pack,int n){
        int counter;
        for (int i=0; i<8*n;i++){
            counter = 0;
            for(int j=0; j<8*n;j++){
                if (pack[i].getValue() == pack[j].getValue()) counter++; 
            }//counter done for each member of the list
            if (counter >= 4) return true;//if there is no member that appears more than 4 times,game can't be won
        }
        System.out.println("Deck doesn't have more than 3 of a single card, impossible to win");
        return false;   
    }
    
    
    
    
    /** Function to deal out cards in a round robin fashion to either form all hands or all decks     * 
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
                cardsArray[pIndex][cIndex] = pack[addCount];
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
     * @throws java.io.IOException
     */   
    public static void main(String[] args) throws IOException{        
       
        Scanner cmdScan = new Scanner(new InputStreamReader(System.in));
        
            //While loop guard to repeatedly query user until correct file given
            int n = 0;
            while ((n < 2) || (n > 7)){
                System.out.println("Enter Value of n (Must be Integer between 2 and 7):");
                try {
                    n = Integer.parseInt(cmdScan.next()); //if n isn't an integer will catch and ask again
                }catch(NumberFormatException e){System.out.println("Invalid type of n, Must be int");}
                
            }   //guarded do-while loop to query user for a legitimate text file
            
        boolean gameReady = false;
        Card[] initialPack = new Card[8*n];
        while (gameReady == false){    
            FileReader fr;
            String textFile = "";
            do{
                fr = null;
                while (fr == null) {
                    System.out.println("text file:  ");
                    try{
                        //Must be in full path form i.e. C:\sample_deck.txt
                        textFile = cmdScan.next();
                        fr = new FileReader(textFile);
                    }catch(FileNotFoundException e){System.out.println("File Not Found");}
                }
            }while(!correctFile(fr,n));
      
            //now file is legitimate, can fill the initial pack     
        try {fr = new FileReader(textFile);} catch (FileNotFoundException e) {}
        
        initialPack = fillPack(fr,n);
            
        gameReady = winnableGame(initialPack,n); //once pack created,checks to see if the game is able to finish
        //closes scanners 
        fr.close();     
        
    }
        
        cmdScan.close();  //closes fileReaders and scanners to avoid inteference with the threading
        Thread[] playerArray = new Thread[n];
        
        //iterates over handArray, distributing each card to it in a round robin fashion
        Card[][] handArray = dealToHands(initialPack, n);
        
        //performs same iterations but this time on the decks in a round robin fashion
        Card[][] deckArray =  dealToDecks(initialPack, n);
        
        CardDeck[] cardDecks = new CardDeck[n];        
        for (int decks=0;decks<n;decks++){cardDecks[decks] = new CardDeck(deckArray[decks]);}
        for (int q=0; q<n;q++){
            //z made to find 0th member of array for right deck when lightDeck is n-1
            int z = (q == n-1)?0:q+1;
            playerArray[q] = new Thread (new Player((q+1),handArray[q],cardDecks[q],cardDecks[z]));
            playerArray[q].setName("Player " + (q+1));
        }
        
        for (Thread player: playerArray){player.start();}

    }
    }