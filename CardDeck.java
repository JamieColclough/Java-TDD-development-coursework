package game;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.ArrayList;
/**
 * CardDeck class with generics
 * Can only take classes that extend card,
 * so it still works as a 'Card' deck
 * @version 2.1
 */
public class CardDeck<T extends Card>
{
    private final AtomicBoolean gameInterrupted = new AtomicBoolean(false);
    private final ArrayList<T> cards;
    private volatile boolean empty; 
    // needed rather than just checking cards.isEmpty() becasuse the attribute needs to be updated
    // in way that allows the other threads sharing this card deck to see the change
    // the attribute allows for a notify method to be used to wake up threads waiting for cardDeck to be non empty (so that they can take a card)
    /**
     * Constructor for a CardDeck
     * @param cards         initial ArrayList of cards for the card deck, no max size is given
     *                      as ArrayLists can have varying length
     */    
    public CardDeck(ArrayList<T> cards)
    {
        this.cards = new ArrayList<T>();
        for(T card : cards)
        // cards arrayList could still potentially have null values in the middle so
        // remove these.
        {
            if (card != null)
            {
                this.cards.add(card);
            }
        }
        
        if (cards.isEmpty())
        {empty = true;}
        else
        {empty = false;}
    }
    /**
     * Method tells other threads using this particular deck that the game has been interrupted by
     * setting atomicboolean gameInterrupted to true
     * and notifiying threads that this has happened so that threads waiting for a card
     * to placed on this deck stop waiting.
     */   
    public synchronized void gameInterruption()
    {
        gameInterrupted.set(true);
        notifyAll();
    }
    /**
     * Method tries to take the first (top) card from cards ArrayList in CardDeck at index 0
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
            
            
                System.out.println(Thread.currentThread().getName() + " is waiting");
                wait();
            
            
        }
        if(gameInterrupted.get())
        // this means that at least one other player has been interrupted
        // which means the game has been interrupted and this thread should throw
        // an exception and exit
        {throw new InterruptedException();}
        
        Card card = cards.get(0);// take the first card
        cards.remove(0);
        if (cards.isEmpty())
            {   
                empty = true;
            }        
        return card;
    }
    
    /**
     * Method places a card on to the (bottom) of the cards ArrayList, (the bottom of the cardDeck)
     * @param card          card to be placed on cardDeck
     */    
    public synchronized void placeCard(T card)
    {
        cards.add(card);
        empty = false;
        notifyAll();
    }
    @Override
    /**
     * Method compares an object for equivalency with CardDeck instance.
     * used for junit testing to decide if a cardDeck is equal to another
     * @param obj           object to be tested for equivalency with CardDeck instance
     * @return              true if the object is equivalent, else false
     */    
    public boolean equals(Object obj)
    {
        if(obj instanceof CardDeck){
            CardDeck cardDeck = ((CardDeck) obj);
            if(this.cards.equals(cardDeck.cards) &&
                this.empty == cardDeck.empty)
                return true;
        }
        return false;
    }
}