
public class CardDeck
{
    private final Card[] cards;
    private volatile boolean empty;
    private volatile int numberOfCards;

    public CardDeck(int maxNumberOfCards)
    {
        cards = new Card[maxNumberOfCards];
        empty = true;
    }

    public synchronized Card takeCard()
    {
        while (empty)
        {
            try
            {
                System.out.println("im waiting");
                wait();
            }
            catch(InterruptedException e){}
        }
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
    
    public synchronized void placeCard(Card card)
    {
        assert numberOfCards < cards.length;
        cards[numberOfCards] = card;
        numberOfCards++;
        empty = false;
        notifyAll();
    }
}
