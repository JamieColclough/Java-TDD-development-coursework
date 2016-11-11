import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Arrays;
package game;
/**
 * CardDeck class
 * @version 1.2
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


    public CardDeck(int maxNumberOfCards)
    {
        cards = new Card[maxNumberOfCards];
        empty = true;
    }
    
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
        // is the maximum the card deck can hold in this.cards, not the actuall number of 
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
    }

    /**
     * Method tells other threads using this particular deck that the game has been interrupted by
     * setting static atomicboolean gameInterrupted to true
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
     */
    public synchronized Card takeCard() throws InterruptedException
    {
        while (empty && !gameInterrupted.get() )
        {
            
            
                System.out.println("im waiting");
                wait();
            
            
        }

        if(gameInterrupted.get())
        {throw new InterruptedException();}
        Card card = cards[0]; // take the first card
        
        for(int i=0; i<=(numberOfCards-2); i++)
        {
            cards[i] = cards[i+1];
            cards[i+1] = null;
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
        cards[numberOfCards] = card; // add it after the last the card entry to be at 'bottom'
        numberOfCards++;             // of deck
        empty = false;
        notifyAll();
    }

}
