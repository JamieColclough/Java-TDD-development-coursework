package game;
/**
 * MockGameInterruption Class
 * mock of Game class that simulates a interruption event
 * whilst an other player is the waiting state
 * @version 1.1
 */
public class MockGameInterruption
{
    public static void main (String args[])
    {
        Card[] cardDeck1Cards = new Card[8];
        cardDeck1Cards[0] = new Card(1);
        cardDeck1Cards[1] = new Card(2);
        
        final CardDeck cardDeck1 = new CardDeck(cardDeck1Cards);
        final CardDeck cardDeck2 = new CardDeck(new Card[8]);
        
        Card[] player1Cards = new Card[4];
        player1Cards[0] = new Card(0);
        player1Cards[1] = new Card(0);
        player1Cards[2] = new Card(1);
        player1Cards[3] = new Card(1);
        
        Card[] player2Cards = new Card[4];
        player2Cards[0] = new Card(1);
        player2Cards[1] = new Card(3);
        player2Cards[2] = new Card(4);
        player2Cards[3] = new Card(5);
        
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
        a.interrupt();        
        // player 1 is interrupted before getting a chance to place a card, on rightDeck
        // so player 2 is left in waiting state, before player 1 exits run it calls
        // rightDeck.gameInterruption() which notifies the waiting player 2, causing it
        // to wake up and exit run 
    }
}
