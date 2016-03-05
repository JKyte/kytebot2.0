package responses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BotResponses {
	
	private Random rand;
	private HashMap<Integer, String> greetings;
	private HashMap<Integer, String> farewells;
	
	private ArrayList<String> joinActions;
	
	public BotResponses(){
		rand = new Random();
		loadGreetings();
		loadFarewells();
		loadJoinAction();
	}

	private void loadJoinAction() {
		joinActions = new ArrayList<String>();
		joinActions.add("glances up from his newspaper disapprovingly");
		joinActions.add("looks up from drafting a writ of execution" );
		joinActions.add("goes back to play Europa Universalis IV");
		joinActions.add("stirs a glass of scotch");
		joinActions.add("wonders where the whiskey is");
		joinActions.add("yearns for a Master of Coin");
		joinActions.add("wonders where torchbot is");
		joinActions.add("makes a note of the last time Torchwood was seen");
		joinActions.add("turns over in his sleep");
		joinActions.add("holds out a piece of dead code");
		joinActions.add("wishes for more features");
		joinActions.add("ponders existence");
		joinActions.add("adjusts his bowtie");
		joinActions.add("puts away his foil");
		joinActions.add("makes faces in a mirror");
		joinActions.add("practices his microexpressions");
	}

	private void loadGreetings() {
		greetings = new HashMap<Integer, String>();
		greetings.put(1, "Hello");
		greetings.put(2, "Hi");
		greetings.put(3, "Hey");
		greetings.put(4, "Sup");
		greetings.put(5, "Greetings");
	}
	
	private void loadFarewells() {
		farewells = new HashMap<Integer, String>();
		farewells.put(1, "Fare thee well,");
		farewells.put(2, "Goodbye");
		farewells.put(3, "Later");
		farewells.put(4, "Bye");
		farewells.put(5, "Farewell");
	}
	
	public String getRandomJoinAction(){
		return joinActions.get( rand.nextInt(joinActions.size()));
	}
	
	public String getRandomGreeting(){
		return greetings.get(1+rand.nextInt(greetings.size()));
	}

	public String getBotGreeting( String nick ){
		return getRandomGreeting() + " " + nick;
	}
		
	public String getRandomFarewell(){
		return farewells.get(1+rand.nextInt(farewells.size()));
	}

	public String getBotFarewell( String nick ){
		return getRandomFarewell() + " " + nick;
	}
}
