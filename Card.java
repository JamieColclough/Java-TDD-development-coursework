package game;

/**
 * Card class
 * @version 1.2
 */
public class Card{
    private int value;

    /**
     * Constructor for a Card
     * @param value         cards face value
     */
    public Card(int value){
        this.value = value;
    }

    /**
     * Method gets the cards value
     * @return              cards face value
     */
    public int getValue(){
        return value;
    }
    
    @Override
    /**
     * Method compares an object for equivalency with the card instance
     * @param obj           object to be tested for equivalency with card instance
     * @return              true if object is equivalent, else false
     */
    public boolean equals(Object obj){
        if (obj instanceof Card){
	   if(this.value == ((Card) obj).value)
                return true;
        }
        return false;
    }
}