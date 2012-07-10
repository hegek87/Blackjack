import java.util.*;

public class Player implements Person{
	private Person dealer;   //The delear that you are playing against
	private final Hand hand; //The players hand of cards
	private int pot;         //The amount of money the player has to gamble with
	private Table table;
	
	/**
	 * private Player class constructor
	 * @param Hand
	 * @param int
	 */
	private Player(Hand hand, int pot){
		this.hand = hand;
		this.pot = pot;
	}
	
	/**
	 * Static factory method to join a game
	 * @param Hand, int
	 * Forwards to the private constructor
	 */
	public static Player joinGame(Hand hand, int pot){
		Player p = new Player(hand, pot);
		return p;
	}
	
	//Sets the table of the player
	public synchronized void sitAtTable(Table table){
		this.table = table;
	}
	
	//bets on hand before starting
	public void placeBet(int bet){
		if(bet > pot)
			bet = pot;
		table.setPot(bet);
	}
	
	//The changes that are made when a player wins a hand
	public synchronized void won(boolean blackJack){
		System.out.println("You won!");
		int tablePot = table.getPot();
		if(blackJack){
			System.out.println("Player Hand:\n" + hand);
			pot += (3 * tablePot);
		}
		else{
			pot += (2 * tablePot);
		}
		System.out.println(pot);
		table.setPot(0);
	}
	
	//The changes made when a player loses a hand
	public synchronized void lost(){
		System.out.println("You lost!");
		pot -= table.getPot();
		System.out.println(pot);
		table.setPot(0);
	}
	
	public void push(){
		System.out.println("Push");
		table.setPot(0);
	}
		
	
	//simply adds a card to the hand
	public Card hit(){
		System.out.println("Player has hit");
		Card c = dealer.dealNext();
		hand.addCard(c);
		return c;
	}
	
	@Override public String toString(){
		return hand.toString() + "\nHand Value: " + hand.getHandValue() + "\nPot: " + pot;
	}
	
	/**
	 * Sets the dealer the player plays against, synchronized
	 * method since the dealer is mutable. The dealer is unknown
	 * at construction time
	 * @param Person
	 */
	public synchronized void setDealer(Person dealer){
		this.dealer = dealer;
	}
	
	//gives a view of the hand
	public Hand viewHand(){
		return hand;
	}
	
	public int getPot(){
		return pot;
	}
	
	public synchronized void refreshPot(){
		pot = 500;
	}
	
	//unsupported for Player
	public Card dealNext(){
		throw new UnsupportedOperationException("Not available");
	}
	
	public static void main(String[] args){
		ArrayList<Card> sample = new ArrayList<Card>();
		Hand sampleHand = new Hand(sample);
		Player a = Player.joinGame(sampleHand, 30);
		Player b = Player.joinGame(sampleHand, 20);
		a.setDealer(b);
		b.setDealer(a);
	}
}