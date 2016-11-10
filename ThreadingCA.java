package threadingca;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *ThreadingCA application class
 *@ version 1.2
 */

public class ThreadingCA {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Scanner cmdScan = new Scanner(new InputStreamReader(System.in));
        
        //While loop guard to repeatedly query user until correct file given
        int n = 0;
        while ((n < 2) || (n > 7)){
            System.out.println("Enter Value of n (Must be Integer between 2 and 7):");
            try {
                 n = Integer.parseInt(cmdScan.next()); //if n isn't an integer
            }catch(NumberFormatException e){System.out.println("Invalid type of n, Must be int");}
        
        }
        
        
        String textFile;
        FileReader fr = null;
        while (fr == null) {       
            System.out.println("text file:  "); 
            try{
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
        Card[][] handArray = new Card[n][4];
        
        addCount = 0;
        for(int hIndex=0; hIndex<4; hIndex++){
            for (int pIndex=0; pIndex<n; pIndex++ ){
                handArray[pIndex][hIndex] = initialPack[addCount];
                addCount++;
            }
            }   
        
        //performs same iterations but this time on the decks in a round robin fashion
        Card[][] deckArray =  new Card[n][4*n];
        
        addCount = 0;
        for(int hIndex=0; hIndex<4; hIndex++){
            for (int pIndex=0; pIndex<n; pIndex++ ){
                deckArray[pIndex][hIndex] = initialPack[addCount];
                addCount++;
            }
        }
        
        
        
    }
    }