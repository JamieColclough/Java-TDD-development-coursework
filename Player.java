package game;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.ArrayList;
/**
 * Player implements runnable interface
 * @version 2.1
 */
public class Player implements Runnable
{
    private final static AtomicBoolean WINNER = new AtomicBoolean(false);
    // AtomicBoolean used for WINNER so that compareAndSet() can be used to check if 
    // there is a winner and declare an new one in one synchronized method, so that
    // only one player thread can declare itself the winner, and most up to date value is seen
    private final static AtomicBoolean ALL_ALIVE = new AtomicBoolean(true);
    // AtomicBoolean used so that only one player thread can read/write at a time,
    // again this allows only most up to date value to be seen.
    private final ArrayList<Card> hand = new ArrayList<>(); 
    private final int preference;
    private final CardDeck<Card> leftDeck;
    private final CardDeck<Card> rightDeck;
    /**
     * Constructor for a Player
     * @param preference        the players preferred number, also the players number
     * @param hand              cards dealt to player, the players hand
     * @param leftDeck          deck of cards the player will take a card from
     * @param rightDeck         deck of cards the player will place a card on
     */    
    public Player(int preference, ArrayList<Card> hand, CardDeck<Card> leftDeck, CardDeck<Card> rightDeck)
    {
        assert preference > 0;
        assert hand.size() < 5;  
        // should only be given 4
        
        for(Card card: hand)
        // hand ArrayList may still contain null entries, this process removes them
        {
            if(card != null)
            {this.hand.add(card);}
        }
        
        this.preference = preference;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
    }    
    
    // addCard method removed, unnecessary when using genetic ArrayList<Card> for hand
    // as it has .add() method directly associated with it
    
    /**
     * Method checks if the players current hand is a winning hand where all
     * cards have the same value
     * by comparing card values in the hand to the first cards value
     * @return          true if hand is a winning hand, else false
     */
    public boolean winningHand()
    {
        int value = hand.get(0).getValue();
        boolean winningHand = true;
        for (Card card : hand)
        {   
            if (card.getValue() != value)
            {   
                winningHand = false;
            }
        }
        
        return winningHand;
    }
    /**
     * Method returns first card whose value is not the same as the players preference
     * should be called after winningHand() check, if the hand is a 'winning hand' consisting of cards
     * that are the players preference this method will return null. A player cannot give a preferred
     * card if there are none.
     * @return          non preferred card, or null if no such card was found
     */    
    public Card nonPreferedCard()
    {
        Card nonPrefered = null;
        
        if (!winningHand()){ // checks that there are non preferred cards before trying to take one.
            for(Card card : hand)
            {
                if(card.getValue() != preference)
                {
                    nonPrefered = card;
                    hand.remove(card);
                    // hand.remove() removes the card from the hand, and automatically
                    // moves the cards down so that there are no null 'gaps'
                    break;
                }
            }
        }
        return nonPrefered;
    }
    @Override
    /**
     * Method compares an object for equivalency with the player instance
     * used so that assertEquals() used in junit testing has an accurate equals method to call when
     * asserting that players are the same, this judges players to be the same if they are in the same 
     * state, (so same card decks and same hand) not just same player number/preference.
     * @param obj       object to be tested for equivalency with player instance
     * @return          true if object is equivalent, else false
     */    
    public boolean equals(Object obj)
    {
        if(obj instanceof Player){
            Player player = ((Player) obj);
            if(this.hand.equals(player.hand) &&
               this.preference == player.preference &&
               this.rightDeck.equals(player.rightDeck)&&
               this.leftDeck.equals(player.leftDeck))
               {return true;}
        }
        return false;
    }
    @Override
    /**
     * Method allows player to run and play the game, loop is repeated until a winner
     * is found, a different player is interrupted, or this player is intertupted
     */    
    public void run()
    {
        System.out.println("Player " + preference + " started run");
        assert hand.size() == 4;
        if(winningHand())
        {
            // below: only updates if currently no winner (WINNER is false)
            if(WINNER.compareAndSet(false,winningHand()))
            // compareAndSet used as it combines an atomic read and write, so that only one player
            // at a time can read the WINNER value and change the value at time. Because the reading 
            // and writting are carried out sychronously in compareAndSet(), only one player is able to
            // declare its the winner.
                System.out.println("Player " + preference + " wins");
            
        }           
        while (!WINNER.get() && ALL_ALIVE.get()&& !Thread.currentThread().isInterrupted()) 
        // will stop running if a winner found, or not all threads
        // are alive, (meaning a players thread was interrupted) or this player is interrupted
        {
            try{          
                            
                
                hand.add(leftDeck.takeCard());   // takeCard uses wait() and can throw InterruptedException
                rightDeck.placeCard(nonPreferedCard());
                assert hand.size() == 4;
                System.out.println("Player " + preference + " taken and placed card");
            
            
            
                if(winningHand())
                {
                    // below: only updates if currently no winner (WINNER is false)
                    if(WINNER.compareAndSet(false,winningHand()))
                    {System.out.println("Player " + preference + " wins");}
                    break;
                }
            }
            catch(InterruptedException e){ // thrown from takeCard (caught here so we can stop the  player
                                           // straight away, rather than have it exit takeCard() and try to
                                           // carry out the remainder of the run method.
                break;
            }
        }
         ALL_ALIVE.set(false);
         
         // below: tell the rightDeck that game has been interupted, if a thread is waiting for
         // this deck to have cards placed on it, it will not stop waiting and throw its own 
         // interruptedException           
         rightDeck.gameInterruption();
         System.out.println("Player " + preference + " exiting run");
    }
}