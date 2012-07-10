import java.util.*;

/**
 * Represents a table to play blackjack. The table has a dealer and a player
 * and it manages the game play. 
 */

public class Table{
	private Player player;  //The player at the table
	private Dealer dealer;  //The dealer at the table
	private int pot;        //The tables pot
	
	private Scanner scan = new Scanner(System.in); //scanner to get user input
	
	/**
	 * The possible moves a player can make
	 * HIT makes the player ask for another card
	 * STAND ends the players turn and the turn 
	 * switches to the dealer
	 * DOUBLE allows you to split your hand, only
	 * allowed if the two cards in the hand have
	 * the same value. Splits the cards into two
	 * hands and allows you to HIT/STAND on each
	 * hand
	 */
	private enum Move{
		HIT, STAND, DOUBLE;
	}
	
	
	//private constructor is used in the factory method.
	private Table(Player player, Dealer dealer){
		this.player = player;
		this.dealer = dealer;
	}
	
	//Makes a player join a table
	public static Table createTable(Player player){
		Dealer dealer = Dealer.startGame(Hand.newHand(), Deck.createDeck());
		dealer.setPlayer(player);
		player.setDealer(dealer);
		Table t = new Table(player, dealer);
		player.sitAtTable(t);
		return t;
	}
	
	//Creates a new game to begin playing at
	public static Table newTable(){
		Player player = Player.joinGame(Hand.newHand(), 500);
		return createTable(player);
	}
	
	//Changes the amount in the pot
	public synchronized void setPot(int pot){
		this.pot = pot;
	}
	
	//Gets the value in the tables pot
	public synchronized int getPot(){
		return pot;
	}
	
	@Override public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("------------------------------\n");
		sb.append(player);
		sb.append("\n");
		sb.append("\n\nDealer card:\n");
		sb.append(dealer.displayPlayerView());
		sb.append("------------------------------\n");
		return sb.toString();
	}
	
	/**
	 * Gets player input and returns an enum based
	 * on the players response
	 * @return Moves
	 */
	private Move getPlayerResponse(){
		String choice = "";
		System.out.println("Please make a choice:\nHit\nStand");
		while(true){
			choice = scan.nextLine();
			choice = choice.toLowerCase();
			switch(choice.charAt(0)){
				case 'h':
					return Move.HIT;
				case 's':
					return Move.STAND;
				case 'd':
					return Move.DOUBLE;
				default:
					System.out.println("Please choose one of the following:\n\tHit\n\tStand\n\tDouble");
					continue;
			}
		}
	}
	
	
	/**
	 * First determines the hands current value, with aces having
	 * a value of 1. Then if the player has aces in his hand, the
	 * method loops through the number of aces in the hand. For 
	 * each ace in the players hand, determine if the hand value +
	 * 10 is less than or equal to 21. If it is less than 22, then
	 * add 10 to the players hand value. If the hand value is > 21
	 * the player has busted. Subtract proper money and print a 
	 * message to the player. Then print the hand, followed by the
	 * hand value
	 */
	private int playerTurn(){
		Move move;
		do{
			int handValue = player.viewHand().getHandValue();
			if(player.viewHand().hasAce())
				for(int i = 0, n = player.viewHand().countAces(); i < n; ++i)
					if((handValue + 10) <= 21)
						synchronized(this){
							handValue += 10;
						}
			if(handValue > 21){
				player.lost();
				return -1;
			}
			System.out.println(this);
			move = getPlayerResponse();
			switch(move){
				case HIT:
					player.hit();
					System.out.println(this);
					break;
				case STAND:
					return handValue;
			}
		} while(!(move.equals(Move.STAND)));
		return -1;
	}
	
	/**
	 * The dealer follows a fairly simple algorithm to determine what it's
	 * move will be. If the current hand value is < 17 the dealer hits. The
	 * dealer will stand on any value over 17
	 */
	 private int dealerTurn(){
		int handValue;
		do{
			handValue = dealer.viewHand().getHandValue();
			System.out.println("------------------------------");
			System.out.println("Dealer hand:\n");
			System.out.println(dealer.viewHand() + "\n" + dealer.viewHand().getHandValue());
			System.out.println("------------------------------");
			if(dealer.viewHand().hasAce())
				for(int i = 0, n = dealer.viewHand().countAces(); i < n; ++i)
					if((handValue + 10) <= 21)
						synchronized(this){
							handValue += 10;
						}
			if(handValue > 21){
				player.won(false);
				return -1;
			}
			if(dealer.viewHand().getHandValue() > 17)
				return dealer.viewHand().getHandValue();
			dealer.hit();
			System.out.println("------------------------------");
			System.out.println("Dealer hand:\n");
			System.out.println(dealer.viewHand() + "\n" + dealer.viewHand().getHandValue());
			System.out.println("------------------------------");
		} while(dealer.viewHand().getHandValue() <= 17);
		return handValue;
	}
	
	/**
	 * This method plays a single hand. It first takes a bet, then deals the cards. After the cards
	 * are dealt, determines whether either the player or dealer has gotten black jack. If they have
	 * that player wins the hand. If no one gets black jack, then the players turn goes, followed by
	 * the dealers turn.
	 */
	private void playHand(){
		System.out.println("Place your bet:");
		int bet;
		try{
			bet = Integer.parseInt(scan.nextLine());
		} catch(NumberFormatException nfe){
			System.out.println("Invalid bet -- 50 used");
			bet = 50;
		}
		player.placeBet(bet);
		player.viewHand().bust();
		dealer.viewHand().bust();
		dealer.deal();
		System.out.println(this);
		int playerHand, dealerHand;
		playerHand = player.viewHand().getHandValue();
		dealerHand = dealer.viewHand().getHandValue();
		//Check for wins/ties with black jacks
		if(player.viewHand().hasAce() && dealer.viewHand().hasAce())
			if(playerHand == 11 && dealerHand == 11){
				player.push();
				return;
			}
			else if(playerHand == 11){
				player.won(true);
				return;
			}
			else if(dealerHand == 11){
				player.lost();
				return;
			}
		if(player.viewHand().hasAce())
			if(playerHand == 11){
				player.won(true);
				return;
			}
		if(dealer.viewHand().hasAce())
			if(dealerHand == 11){
				player.lost();
				return;
			}
		//now player turn and dealer turn
		playerHand = playerTurn();
		if(playerHand == -1){
			player.lost();
			return;
		}
		dealerHand = dealerTurn();
		if(dealerHand == -1){
			player.won(false);
			return;
		}
		if(dealerHand > playerHand){
			player.lost();
			return;
		}
		else if(dealerHand < playerHand){
			player.won(false);
			return;
		}
		else if(dealerHand == playerHand){
			player.push();
			return;
		}
	}

	 /**
	  * This method delegates much of the game to other helper methods.
	  * This method begins with a do while loop. The do while plays the
	  * game until the player runs out of chips. When he does, the do
	  * loop asks  whether player wants to play again. If so, refresh
	  * player money and continue, if no, end game.
	  */
	public void startGame(){
		while(true){
			while(player.getPot() > 0)
				playHand();
			String response;
			do{
				System.out.println("Would you like to continue playing? (y/n):");
				response = scan.nextLine().toLowerCase();
				if(response.charAt(0) == 'y')
					player.refreshPot();
				else if(response.charAt(0) == 'n')
					return;
			} while(!(response.charAt(0) == 'y' || response.charAt(0) == 'n'));
		}
	}
		
	public static void main(String[] args){
		Table t = Table.newTable();
		t.startGame();
	}
}