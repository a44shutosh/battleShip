package battleshipgamegame;

public enum TypeOfShips {

    BATTLESHIP(4),
    CRUISER(3),
	DESTROYER(2),
    SUBMARINE(1);
	
	private int size;
	
	private TypeOfShips(int size) {
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}
}


package battleshipgame;

public enum Layout {
	HORIZONTAL,
	VERTICAL;
}


package battleshipgame;

public abstract class Ship {
	private Layout layout;
	private TypeOfShips typeOfShips;
	private Coordinates coordinates;
	
	public Ship(Coordinates coordinates, Layout layout, TypeOfShips typeOfShips) {
		this.coordinates = coordinates;
		this.layout = layout;
		this.typeOfShips = typeOfShips;
	}
	
	public Layout getLayout() {
		return layout;
	}
	
	public TypeOfShips gettypeOfShips() {
		return typeOfShips;
	}
	
	public Coordinates getCoordinates() {
		return coordinates;
	}
}


package battleshipgame;

public class Destroyer extends Ship {

	public Destroyer(Coordinates coordinates, Layout layout) {
		super(coordinates, layout, TypeOfShips.DESTROYER);
	}

}


package battleshipgame;

public class Cruiser extends Ship {

	public Cruiser(Coordinates coordinates, Layout layout) {
		super(coordinates, layout, TypeOfShips.CRUISER);
	}
	
}


package battleshipgame;

public class BattleShip extends Ship {

	public BattleShip(Coordinates coordinates,Layout layout) {
		super(coordinates, layout, TypeOfShips.BATTLESHIP);
	}

}

package battleshipgame;

public class Submarine extends Ship {

	public BattleShip(Coordinates coordinates,Layout layout) {
		super(coordinates, layout, TypeOfShips.SUBMARINE);
	}

}


package battleshipgame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Board {
	String playerName;
	public final static int SIZE = 20;
	private Set<Coordinates> coordinates = new HashSet<Coordinates>();
	
	Map<TypeOfShips, Integer> remaining = new HashMap<TypeOfShips, Integer>() {{
		put(TypeOfShips.DESTROYER, 1);
		put(TypeOfShips.CRUISER, 1);
		put(TypeOfShips.BATTLESHIP, 1);
        put(TypeOfShips.SUBMARINE, 1);

	}};
	
	public Board(String playerName) {
		this.playerName = playerName;
	}
	
	public boolean layShip(Ship ship) {
		Coordinates c = ship.getCoordinates();
		
		if(c.getX() < 0 || c.getX() > SIZE) return false;
		if(c.getY() < 0 || c.getY() > SIZE) return false;
		
		if(ship.getLayout() == Layout.HORIZONTAL && c.getX() + ship.getTypeOfShips().getSize() >= SIZE) return false;
		if(ship.getLayout() == Layout.VERTICAL && c.getY() + ship.getTypeOfShips().getSize() >= SIZE) return false;
		
		Set<Coordinates> tmp = new HashSet<Coordinates>();
		if(ship.getLayout() == Layout.HORIZONTAL) {
			for(int x = ship.getCoordinates().getX(); x < ship.getCoordinates().getX() + ship.getTypeOfShips().getSize(); ++x) {
				Coordinates tmpC = new Coordinates(x, ship.getCoordinates().getY());
				tmp.add(tmpC);
				if(coordinates.contains(tmpC)) return false;
			}
		} else {
			for(int y = ship.getCoordinates().getY(); y < ship.getCoordinates().getY() + ship.getTypeOfShips().getSize(); ++y) {
				Coordinates tmpC = new Coordinates(ship.getCoordinates().getX(), y);
				tmp.add(tmpC);
				if(coordinates.contains(tmpC)) return false;
			}
		}
		
		TypeOfShips type = ship.getTypeOfShips();
		if(remaining.get(type) <= 0) return false;
		remaining.put(type, remaining.get(type) - 1);
		
		coordinates.addAll(tmp);
		
		return true;
	}
	
	public boolean destroy(Coordinates c) {
		if(!coordinates.contains(c)) return false;
		coordinates.remove(c);
		return true;
	}
	
	public int progressGame() {
		return coordinates.size();
	}
}


package battleshipgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
	List<Board> boards;
	int turn = 0;
	
	Layout getLayout() {
		return ( new Random().nextBoolean() ? Layout.HORIZONTAL : Layout.VERTICAL);
	}
	
	void boardFill(Board board) {
		while(board.remaining.get(TypeOfShips.DESTROYER) > 0) {
			board.layShip(new Destroyer(new Coordinates(new Random().nextInt(Board.SIZE), new Random().nextInt(Board.SIZE)),  getLayout()));
		}
		while(board.remaining.get(TypeOfShips.CRUISER) > 0) {
			board.layShip(new Cruiser(new Coordinates(new Random().nextInt(Board.SIZE), new Random().nextInt(Board.SIZE)),  getLayout()));
		}
		while(board.remaining.get(TypeOfShips.BATTLESHIP) > 0) {
			board.layShip(new BattleShip(new Coordinates(new Random().nextInt(Board.SIZE), new Random().nextInt(Board.SIZE)),  getLayout()));
		}
        while(board.remaining.get(TypeOfShips.SUBMARINE) > 0) {
			board.layShip(new Submarine(new Coordinates(new Random().nextInt(Board.SIZE), new Random().nextInt(Board.SIZE)),  getLayout()));
		}
		
	}
	
	public Game(String p1, String p2) {
		boards = new ArrayList<Board>();
		boards.add(new Board(p1));
		boards.add(new Board(p2));
		boardFill(boards.get(0));
		boardFill(boards.get(1));
	}
	
	boolean play() {
		Coordinates c = new Coordinates(new Random().nextInt(Board.SIZE), new Random().nextInt(Board.SIZE));
		System.out.println(turn + " " + c.getX() + " " + c.getY());
		boards.get(turn % 2).destroy(c);
		boolean output = boards.get(turn % 2).progressGame() > 0;
		turn ++;
		return output;
	}
}


package battleshipgame;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		Game game = new Game("NewsBytes", "Ashutosh");
		while(game.play());
	}

}