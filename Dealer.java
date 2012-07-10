import java.util.*;

public class Dealer implements Person{
	private Person player;   //The player the dealer is playing against
	private final Hand hand; //The dealers hand
	private Deck deck;       //The deck the dealer deals from
	
	/**
	 * Private constructor to be used in static factory method
	 * @param Hand, Deck
	 */
	private Dealer(Hand hand, Deck deck){
		this.hand = hand;
		this.deck = deck;
	}
	
	//Static factory method
	public static Dealer startGame(Hand hand, Deck deck){
		Dealer d = new Dealer(hand, deck);
		return d;
	}
	
	//adds a card to the dealers hand
	public Card hit(){
		System.out.println("Dealer has hit");
		Card c = dealNext();
		hand.addCard(c);
		return c;
	}
	
	//Deals the next card from the deck
	public Card dealNext(){
		Card c = deck.removeNext();
		return c;
	}
	
	//Creates a new deck if the deck is empty
	public synchronized void newDeck(){
		if(deck.empty())
			deck = Deck.createDeck();
	}
	
	//sets up the player for the dealer to play against
	public synchronized void setPlayer(Person player){
		this.player = player;
	}
	
	//deals card to the player and the dealer
	public void deal(){
		for(int i = 0; i < 2; ++i){
			hit();
			player.hit();
		}
	}
	
	//displays the dealer hand as viewable by the player
	public Card displayPlayerView(){
		return hand.viewFirst();
	}
	
	public Hand viewHand(){
		return hand;
	}
	
	@Override public String toString(){
		return hand.toString() + "\nHand Value: " + hand.getHandValue() + "\n";
	}
	
	public static void main(String[] args){
		Deck d = Deck.createDeck();
		
		Hand h = new Hand(new ArrayList<Card>());
		Hand h1 = new Hand(new ArrayList<Card>());
		
		Dealer dealer = Dealer.startGame(h, d);
		Player player = Player.joinGame(h1, 500);
		
		dealer.setPlayer(player);
		player.setDealer(dealer);
		dealer.deal();
		
		System.out.println(dealer);
		System.out.println(player);
		
		player.hit();
		player.hit();
		
		System.out.println(dealer);
		System.out.println(player);
	}
}	