package game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Game2 class with generics
 * @version 2.0 
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
    static ArrayList<Card> fillPack(FileReader fr,int n ){
        Scanner fileScan = new Scanner(fr);
        ArrayList<Card> initialPack = new ArrayList<>();
        for (int i=0; i<8*n;i++) {
            Card nextCard = new Card(Integer.parseInt(fileScan.nextLine()));
            initialPack.add(nextCard);
        }
        return initialPack;
    }
    /**
     * Function to see if game is able to be won i.e. the game will not stagnate indefinitely
     * @param pack the initial pack
     * @param n the number of players
     * @return boolean corresponding to whether the game can be won
     */
    static boolean winnableGame(ArrayList<Card> pack,int n){
        int counter;
        for (int i=0; i<8*n;i++){
            counter = 0;
            for(int j=0; j<8*n;j++){
                if (pack.get(i).getValue() == pack.get(j).getValue()) counter++; //replacement of [i] with .get(i)
            }
            if (counter >= 4) return true;
        }
        System.out.println("Deck doesn't have more than 3 of a single card, impossible to win");
        return false;   
    }
    
    
    
    
    /** Function to deal out cards in a round robin fashion to either form all hands or all decks
     * 
     * 
     * @param pack the initial pack that deals out all cards
     * @param isHand boolean to indicate whether or not to deal with respect to a hand or deck
     * @param n amount of players
     * @param placeInPack indexing starts from different place depending on deck or card
     * @return ArrayList of either decks or hands
     */
    static ArrayList<ArrayList<Card>> deal(ArrayList<Card> pack, int n){//isHand boolean and placeInPack no longer required
        ArrayList<ArrayList<Card>> cardsArray = new ArrayList<>(n); //no deciding hand or deck as flexible arrayList
        for (int i=0; i<n;i++){cardsArray.add(new ArrayList<>());}
        for (int cIndex =0; cIndex<4; cIndex++){
            for (int pIndex=0; pIndex<n; pIndex++ ){
                Card nextCard = pack.remove(0);
                cardsArray.get(pIndex).add(nextCard); //int addcount also redundant
            }
        }
        return cardsArray;
    }    
    
    //no need to differentiate between hand and deck so dealToHand and dealToDeck are now removed
    
    
    
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
        ArrayList<Card> initialPack = new ArrayList<>();
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
            
        cmdScan.close();   
        ArrayList<Thread> playerArray = new ArrayList<>();
        
        //iterates over handArray, distributing each card to it in a round robin fashion
        ArrayList<ArrayList<Card>> handArray = deal(initialPack, n);
        
        //performs same iterations but this time on the decks in a round robin fashion
        ArrayList<ArrayList<Card>> deckArray =  deal(initialPack, n);
        ArrayList<CardDeck<Card>> cardDecks = new ArrayList<>(); 
        
        for (int decks=0;decks<n;decks++){cardDecks.add(new CardDeck<>(deckArray.get(decks)));}
        for (int q=0; q<n;q++){
            //z made to find nth member of ArrayList for left deck
            int z = (q == n-1)?0:q+1;
            playerArray.add(new Thread (new Player((q+1),handArray.get(q),cardDecks.get(q),cardDecks.get(z))));
            playerArray.get(q).setName("Player " + (q+1));
            
        }
        
        for (Thread player: playerArray){player.start();}


    }
    }
