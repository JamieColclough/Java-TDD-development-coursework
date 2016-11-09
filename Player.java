
public class Player implements Runnable
{
    private static volatile boolean winner = false;
    private Card[] hand = new Card[5];
    private int cardsInHand = 0;
    private int preference;
    private CardDeck leftDeck;
    private CardDeck rightDeck;


    public Player(int preference, CardDeck leftDeck, CardDeck rightDeck)
    {
        this.preference = preference;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
    }

    public boolean addCard(Card card)
    {
        if (cardsInHand == 5){return false;} // cant add anymore cards, hand is full
        hand[cardsInHand] = card;
        cardsInHand++;
        return true;     
    }
    
    public boolean winningHand()
    {
        int value = hand[0].getValue();
        System.out.println("value of card one :" + value);
        boolean winningHand = true;
        for (int i=0; i<cardsInHand; i++)
        {   
            if (hand[i].getValue() != value)
            {   System.out.println("value of card :" + hand[i].getValue());
                winningHand = false;
            }
        }
        System.out.println("winningHand = "+ winningHand);
        return winningHand;
    }

    public Card nonPreferedCard()
    {
        // must be called after winning hand check
        Card nonPrefered = null;
        int index= 0; // index of non-prefered
        for (int i=0; i<cardsInHand; i++)
        {
            if (hand[i].getValue() != preference)
            {
                nonPrefered = hand[i];
                index = i;
                break;
            }
        }
        
        hand[index] = null;
        for(int i = index; i<=(cardsInHand - 2); i++)
        {
            hand[i] = hand[i+1];
            hand[i+1] = null;
        }
        cardsInHand--;
        return nonPrefered;
    }
    
    public void run()
    {
        System.out.println("working");
        if(winningHand())
        {   
            System.out.println("in first if statement");
            winner = true;
            System.out.println("Player" + preference + " wins");
            return;
        }
        while (!winner)
        {
            System.out.println("in while loop");
            addCard(leftDeck.takeCard());
            rightDeck.placeCard(nonPreferedCard());
            assert cardsInHand == 4;
            System.out.println("taken and placed card");
            
            
            
            if(winningHand())// && (!winner) last ckeck to make sure no one else has declared 
            {
                winner = true;
                System.out.println("Player" + preference + " wins");
                break;
            }
        }
        System.out.println("got out of while loop");
    }
}
