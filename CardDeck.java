package game;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Arrays;
/**
 * CardDeck class
 * @version 1.5
 */
public class CardDeck
{
    private final AtomicBoolean gameInterrupted = new AtomicBoolean(false);
    // AtomicBoolean used so that it can only be read and set by one thread at a
    // time, ensures most updated value is read
    private final Card[] cards;
    private volatile boolean empty;
    // needed rather than just checking isEmpty() becasuse it needs to be updated
    // in way that allows the other threads sharing this card deck to see the change
    // it allows for a notify method to be used to wake up threads waiting for card
    // deck to be non empty (so that they can take a card)    
    private volatile int numberOfCards;
    
    /**
     * Constructor for a CardDeck
     * @param cards         initial array of cards for the card deck, its total length will be
     *                      equal to the maximum number of cards the deck can hold
     */
    public CardDeck(Card[] cards)
    {
        this.cards = new Card[cards.length];
        numberOfCards = 0;
        
        // iterate through to remove null 'gaps' between card entries, this does not occur in our
        // program but it is possible if class used for other purposes.
        // also allows correct numberOfCards variable to be set, can not use cards.length as this
        // is the maximum the card deck can hold in this.cards, not the actual number of 
        // initial cards
        for(int i=0; i<cards.length; i++)
        {
            if(cards[i] != null)
            {
                this.cards[numberOfCards] = cards[i];
                numberOfCards++;
            }
        }
        if(numberOfCards != 0){empty = false;}
        else{empty = true;}
    }

    /**
     * Method tells other threads using this particular deck that the game has been interrupted by
     * setting static AtomicBoolean gameInterrupted to true
     * it also notifies threads that this has happened so that threads waiting for a card
     * to placed on this deck stop waiting.
     */
    public synchronized void gameInterruption()
    {
        gameInterrupted.set(true);
        notifyAll();
    }

    /**
     * Method tries to take the first (top) card from cards array in CardDeck at index 0
     * if cardDeck is empty, the thread accessing this method will go into
     * waiting state, relinquishing lock,
     * throws InterruptionException if wait() is interrupted or gameInterrupted
     * 
     * @return          first (top) card
     * @throws java.lang.InterruptedException
     */
    public synchronized Card takeCard() throws InterruptedException
    {
        while (empty && !gameInterrupted.get() )
        {
            
            
                System.out.println(Thread.currentThread().getName() +" is waiting");
                wait();
            
            
        }

        if(gameInterrupted.get())
        {throw new InterruptedException();}
        Card card = cards[0]; // take the first card
        
        if (numberOfCards > 1) // if the cardDeck has more than 1, cards will have to
        {                     // be shuffled down when first one is removed
            for(int i=0; i<=(numberOfCards-2); i++)
            {
                cards[i] = cards[i+1];
                cards[i+1] = null;
            }
        }
        else // if the cardDeck has only have one card, just set the first
        {    // entry  to null, on need to shuffle down
            cards[0] = null;
        }
        numberOfCards--;      

        if (numberOfCards == 0)
            {   
                empty = true;
            }
        return card;
    }
    
    /**
     * Method places a card on to the (bottom) of the cards array, the bottom of the cardDeck
     * @param card          card to be placed on cardDeck
     */
    public synchronized void placeCard(Card card)
    {
        assert numberOfCards < cards.length;
        // assert this because adding more than cards.length should not actually occur in the game
        // the cardDeck may hold more than it did initialy, but its length was set to the maximum
        // when it was constructed
        cards[numberOfCards] = card; // add it after the last card entry to be at 'bottom'
        numberOfCards++;             // of deck
        empty = false;
        notifyAll();
    }

    @Override
    /**
     * Method compares an object for equivalency with CardDeck instance
     * @param obj           object to be tested for equivalency with CardDeck instance
     * @return              true if the object is equivalent, else false
     */
    public boolean equals(Object obj)
    {
        // used for junit testing to decide if a cardDeck is equal to another
        if(obj instanceof CardDeck){
            CardDeck cardDeck = ((CardDeck) obj);
            if(Arrays.equals(this.cards, cardDeck.cards) && 
                this.empty == cardDeck.empty )
                return true;
        }
        return false;
    }
}
