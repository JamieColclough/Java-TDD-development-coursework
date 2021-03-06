package game;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Arrays;
/**
 * Player implements runnable interface
 * @version 1.4
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
    private final Card[] hand = new Card[5];  // 5, because rules specify you take a card before placing card
    private int cardsInHand = 0;              // so you need an extra space (players usually have 4 cards)
    private final int preference;
    private final CardDeck leftDeck;
    private final CardDeck rightDeck;

    /**
     * Constructor for a Player
     * @param preference        the players preferred number, also the players number
     * @param cards             cards dealt to player, the players hand
     * @param leftDeck          deck of cards the player will take a card from
     * @param rightDeck         deck of cards the player will place a card on
     */    
    public Player(int preference, Card[] cards, CardDeck leftDeck, CardDeck rightDeck)
    {
        assert preference > 0;
        assert cards.length < 5;  
        // should only be given 4
        cardsInHand = 0;
        for(int i=0; i<4; i++)
        {
            // while loop is used to remove 'gaps' where null entries may occur between card entries
            // in a card array. - this won't happen in our game, but could happen if the class was to
            // to be used in a different context.
            if(cards[i] != null)
            {
                this.hand[cardsInHand] = cards[i];
                cardsInHand++;
            }
        }    
        this.preference = preference;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
    }    
    
    /**
     * Method adds a card to the player hand
     * (needed to add a single card to the hand once it has been taken from the left cardDeck)
     * @param card      the card to be added
     * @return          true if the card is added, false if it can not be
     */
    public boolean addCard(Card card)
    {
        if (cardsInHand == 5){return false;} // can't add anymore cards, hand is full
        hand[cardsInHand] = card;
        cardsInHand++;
        return true;     
    }
    
    /**
     * Method checks if the player's current hand is a winning hand where all
     * cards have the same value. It does this by comparing all card values in
     * the hand to the first cards value
     * @return          true if hand is a winning hand, else false
     */
    public boolean winningHand()
    {
        int value = hand[0].getValue();
        boolean winningHand = true;
        for (int i=0; i<cardsInHand; i++)
        {   
            if (hand[i].getValue() != value)
            {   
                winningHand = false;
            }
        }
        
        return winningHand;
    }

    /**
     * Method returns first card whose value is not the same as the players preference.
     * should be called after winningHand() check, if the hand is a 'winning hand' consisting entirely of cards
     * that are the players preference this method will return null. A player cannot give a preferred
     * card if there are none.
     * @return          non preferred card, or null if no such card was found
     */
    public Card nonPreferedCard()
    {
        Card nonPrefered = null;
        
        if (!winningHand()){ // checks that there are non preferred cards before trying to take one.
            int index= 0; // index of non-preferred
            for (int i=0; i<cardsInHand; i++)
            {
                if (hand[i].getValue() != preference)
                { //takes the first non prefered card
                    nonPrefered = hand[i];
                    index = i;
                    break;
                }
            }
        
            hand[index] = null;
            for(int i = index; i<=(cardsInHand - 2); i++)
            { // shuffles cards down after the space the non preferred card is taken from is set to null
              // it stops NullPointerExceptions from being thrown when doing winningHand() checks.
                hand[i] = hand[i+1];
                hand[i+1] = null;
            }
            cardsInHand--;
        }
        return nonPrefered;
    }

    @Override
    /**
     * Method compares an object for equivalency with the player instance.
     * used so that assertEquals() used in junit testing has an accurate equals method to call when
     * asserting that players are the same, this judges players to be the same if they are in the same
     * state, (so same cardDecks and same hand) not just same player number/preference.
     * @param obj       object to be tested for equivalency with player instance
     * @return          true if object is equivalent, else false
     */
    public boolean equals(Object obj)
    {
        if(obj instanceof Player){
            Player player = ((Player) obj);
            if(Arrays.equals(this.hand, player.hand) &&
               this.preference == player.preference &&
               this.rightDeck.equals(player.rightDeck)&&
               this.leftDeck.equals(player.leftDeck)&&
               this.cardsInHand == player.cardsInHand)
               {return true;}
        }
        return false;
    }
    
    /**
     * Method allows player to run and play the game, loop is repeated until a winner
     * is found, a different player is interrupted, or this player is interrupted
     */
    @Override
    public void run()
    {
        System.out.println("Player " + preference + " started run");
        assert cardsInHand == 4;
        if(winningHand())
        {
            // below: only updates if currently no winner (WINNER is false)
            if(WINNER.compareAndSet(false,winningHand()))
            // compareAndSet used as it combines an atomic read and write, so that only one player
            // at a time can read the WINNER value and change the value. as the reading 
            // and writing are carried out sychronously in compareAndSet(), only one player is able to
            // declare it's the winner.
                System.out.println("Player " + preference + " wins");
        }           

        while (!WINNER.get() && ALL_ALIVE.get()&& !Thread.currentThread().isInterrupted()) 
        // will stop running if a winner found, or not all threads
        // are alive, (meaning a players thread was interrupted)
        // or this player has been interrupted
        {
            try{        

                
                addCard(leftDeck.takeCard());   // takeCard uses wait() and can throw InterruptedException
                rightDeck.placeCard(nonPreferedCard());
                assert cardsInHand == 4;
                System.out.println("Player " + preference + " taken and placed card");
            
                if(winningHand())
                {
                    // below: only updates if currently no winner (WINNER is false)
                    if(WINNER.compareAndSet(false,winningHand()))
                    {System.out.println("Player " + preference + " wins");}
                    break;
                }
            }
            catch(InterruptedException e){ // thrown from takeCard, caught here so we can stop the player
                                           // straight away, rather than have it exit takeCard() and try to
                                           // carry out the remainder of the run method.
                break;
            }
        }
         ALL_ALIVE.set(false);
         
         // below: tell the rightDeck that the game has been interrupted, if a thread is waiting for
         // this cardDeck to have cards placed on it, it will not stop waiting and throw its own 
         // InterruptedException         
         rightDeck.gameInterruption();
         System.out.println("Player " + preference + " exiting run");
    }
}
