package threadingca;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class ThreadingCA {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Scanner cmdScan = new Scanner(System.in);
        System.out.println("Enter Value of n ");
        int n = cmdScan.nextInt();
        
        System.out.println("text file:  ");
        String textFile = cmdScan.next();
        FileReader fr = new FileReader(textFile);
        Scanner fileScan = new Scanner(fr);
        Card[] initialPack = new Card[8*n];
        System.out.println(8*n);
        int addCount = 0;
        while (fileScan.hasNextLine()) {            
            Card nextCard = new Card(fileScan.nextInt());
            initialPack[addCount] = nextCard;
            addCount++;
        }
    }
}
    
