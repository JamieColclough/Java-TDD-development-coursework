 import java.util.ArrayList;
/**
 * MockGameWinner Class
 * mock of Game class that simulates a Winner event
 * whilst an other player is the waiting state
 * @version 2.1
 */
public class MockGameWinner
{
    public static void main (String args[])
    {
        ArrayList<Card> cardDeck1Cards = new ArrayList<Card>();
        cardDeck1Cards.add(new Card(1));
        cardDeck1Cards.add(new Card(2));
        
        final CardDeck<Card> cardDeck1 = new CardDeck<Card>(cardDeck1Cards);
        final CardDeck<Card> cardDeck2 = new CardDeck<Card>(new ArrayList<Card>());
        
        ArrayList<Card> player1Cards = new ArrayList<Card>();
        player1Cards.add(new Card(1));
        player1Cards.add(new Card(1));
        player1Cards.add(new Card(1));        
        player1Cards.add(new Card(1));        
        
        ArrayList<Card> player2Cards = new ArrayList<Card>();
        player2Cards.add(new Card(1));
        player2Cards.add(new Card(3));
        player2Cards.add(new Card(4));        
        player2Cards.add(new Card(5));        
        
        Player player1 = new Player(1, player1Cards, cardDeck1, cardDeck2);
        Player player2 = new Player(2, player2Cards, cardDeck2, cardDeck1);
                                                              // cardDeck2 is empty
        Thread a = new Thread(player1);                       // so player2 will go
        Thread b = new Thread(player2);                       // into waiting state                                  
        
        a.setName("Player 1");
        b.setName("Player 2");
        
        b.start();
        // what for thread b (player 2) to get into waiting state
        while(!(b.getState() == Thread.State.WAITING)){} 
        // once player 2 is in waiting state, start player 1
        a.start(); 
        //player 1 has a winningHand to begin with, so will exit run before entering 
        //the while loop. It will wake up player 2 before it exits by calling 
        //rightDeck.gameInterruption() which notifies the waiting player 2, causing 
        //it to wake up and exit too, otherwise player 2 would wait indefinitely   
    }
}


