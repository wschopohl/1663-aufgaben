import java.util.Random;

public class Labyrinth {
	
	private Field[][] map;
	
	public class Coords {
		public int x;
		public int y;
		
		public Coords(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	public static void main(String[] args) {
		Labyrinth labyrinth = new Labyrinth(20,10);
		labyrinth.setRandomWalls();
		labyrinth.setStart(labyrinth.new Coords(0,0));
		labyrinth.setFinish(labyrinth.new Coords(19,9));
		labyrinth.setRandomTraps(30);
		labyrinth.print();
		System.out.println("Labyrinth hat " + labyrinth.countTraps() + " Fallen.");
	}
	
	/* initialize new Labyrinth of given dimensions */
	public Labyrinth(int sizeX, int sizeY) {
		map = new Field[sizeX][sizeY];
		for(int y = 0; y < sizeY(); y++) {
			for(int x = 0; x < sizeX(); x++) {
				map[x][y] = new Field(Field.Type.empty);
			}
		}
		setOuterWalls();
	}
	
	/* set a wall between two fields */
	public void setWall(Coords field1, Coords field2) {
		// System.out.println(this.sizeY());
		Field.Wall wall1 = Field.Wall.bottom;
		Field.Wall wall2 = Field.Wall.bottom;
		if(field1.x == field2.x) {
			if(field1.y < field2.y) wall1 = Field.Wall.bottom;
			else wall1 = Field.Wall.top;
			wall2 = (wall1 == Field.Wall.bottom ? Field.Wall.top : Field.Wall.bottom);
		}
		else if(field1.y == field2.y) {
			if(field1.x < field2.x) wall1 = Field.Wall.right;
			else wall1 = Field.Wall.left;
			wall2 = (wall1 == Field.Wall.right ? Field.Wall.left : Field.Wall.right);
		}
		map[field1.x][field1.y].setWall(wall1, true);
		map[field2.x][field2.y].setWall(wall2, true);
	}
	
	/* set a wall on an outside field */
	public void setWall(Coords coords) {
		Field field = map[coords.x][coords.y];
		if(coords.x == 0) field.setWall(Field.Wall.left, true);
		if(coords.y == 0) field.setWall(Field.Wall.top, true);
		if(coords.x == sizeX()-1) field.setWall(Field.Wall.right, true);
		if(coords.y == sizeY()-1) field.setWall(Field.Wall.bottom, true);
	}
	
	/* set a trap */
	public void setTrap(Coords coords) {
		map[coords.x][coords.y].setType(Field.Type.trap);
	}
	
	/* set the start */
	public void setStart(Coords coords) {
		map[coords.x][coords.y].setType(Field.Type.start);
	}
	
	/* set the finish */
	public void setFinish(Coords coords) {
		map[coords.x][coords.y].setType(Field.Type.finish);
	}
	
	/* check if field is empty */
	public boolean empty(Coords coords) {
		return isType(coords, Field.Type.empty);
	}
	
	/* check if field has specified type */
	public boolean isType(Coords coords, Field.Type type) {
		return map[coords.x][coords.y].hasType(type);
	}
	
	/* output labyrinth to terminal */
	public void print() {
		byte bits[][] = generateBitField();
		for(int y = 0; y < bits[0].length; y++) {
			// set top walls
			for(int x = 0; x < bits.length; x++) {
				//System.out.print(map[x][y].toChar());
				System.out.print(getAscii(bits[x][y]));
			}
			System.out.println();
		}
	}
	
	/* this translates the labyrinth to a coded version with walls for
	 * ascii representation */
	private byte[][] generateBitField() {
		byte bits[][] = new byte[sizeX() * 2 + 1][sizeY() * 2 + 1];
		for(int y = 0; y < sizeY(); y++) {
			for(int x = 0; x < sizeX(); x++) {
				int fx = x * 2;
				int fy = y * 2;
				Field field = map[x][y];
				if(field.hasWall(Field.Wall.left)) {
					bits[fx][fy]   |= wall2Byte(Field.Wall.bottom);
					bits[fx][fy+1] |= wall2Byte(Field.Wall.bottom) | wall2Byte(Field.Wall.top);
					bits[fx][fy+2] |= wall2Byte(Field.Wall.top);
				}
				if(field.hasWall(Field.Wall.top)) {
					bits[fx][fy]   |= wall2Byte(Field.Wall.right);
					bits[fx+1][fy] |= wall2Byte(Field.Wall.right) | wall2Byte(Field.Wall.left);
					bits[fx+2][fy] |= wall2Byte(Field.Wall.left);
				}
				if(field.hasWall(Field.Wall.right)) {
					bits[fx+2][fy]   |= wall2Byte(Field.Wall.bottom);
					bits[fx+2][fy+1] |= wall2Byte(Field.Wall.bottom) | wall2Byte(Field.Wall.top);
					bits[fx+2][fy+2] |= wall2Byte(Field.Wall.top);
				}
				if(field.hasWall(Field.Wall.bottom)) {
					bits[fx][fy+2]   |= wall2Byte(Field.Wall.right);
					bits[fx+1][fy+2] |= wall2Byte(Field.Wall.right) | wall2Byte(Field.Wall.left);
					bits[fx+2][fy+2] |= wall2Byte(Field.Wall.left);
				}
				bits[fx+1][fy+1] = field.toByte();
			}
		}
		return bits;
	}
	
	/* convert the byte representation of a map tile to a printable character */
	private String getAscii(byte code) {
		switch(code) {
			case  0:  return " ";
			case  1: case 4: case 5:
					  return "═";
			case 2: case 8: case 10:
					  return "║";
			case 12:  return "╔";
			case  9:  return "╗";
			case  6:  return "╚";
			case  3:  return "╝";
			case 13:  return "╦";
			case 14:  return "╠";
			case  7:  return "╩";
			case 11:  return "╣";
			case 15:  return "╬";
			case 21:  return "X";
			case 22:  return "S";
			case 23:  return "F";
		}
		return String.format("%02d", code);
	}
	
	/* set all the outer walls */
	private void setOuterWalls() {
		int sizeX = sizeX();
		int sizeY = sizeY();
		for(int x = 0; x < sizeX; x++) {
			setWall(new Coords(x,0));
			setWall(new Coords(x,sizeY - 1));
		}
		for(int y = 0; y < sizeY; y++) {
			setWall(new Coords(0,y));
			setWall(new Coords(sizeX - 1,y));
		}
	}
	
	/* randomly sets walls in the whole labyrinth */
	public void setRandomWalls() {
		Random rand = new Random();
		for(int y = 0; y < sizeY() - 1; y++) {
			for(int x = 0; x < sizeX() - 1; x++) {
				int walls = rand.nextInt(2);
				if(walls == 1) setWall(new Coords(x,y), new Coords(x+1,y));
				walls = rand.nextInt(2);
				if(walls == 1) setWall(new Coords(x,y), new Coords(x,y+1));
			}
		}
	}
	
	/* count all traps in the labyrinth */
	public int countTraps() {
		int count = 0;
		for(Field[] row: map)
			for(Field field: row)
				if(field.hasType(Field.Type.trap)) count ++;
		return count;
	}
	
	/* randomly sets traps in the whole labyrinth, amount specified by num */
	public void setRandomTraps(int num) {
		Random rand = new Random();
		int trapsSet = 0;
		while(trapsSet < num) {
			int x = rand.nextInt(sizeX());
			int y = rand.nextInt(sizeY());
			Field field = map[x][y];
			if(field.isEmpty()) {
				field.setTrap();
				trapsSet++;
			}
		}
	}
	
	/* get the width of the fields */
	private int sizeX() {
		return map.length;
	}
	
	/* get the height of the fields */
	private int sizeY() {
		return map[0].length;
	}
	
	/* convert a wall type to a bit representation */
	private byte wall2Byte(Field.Wall wall) {
		if(wall == Field.Wall.left)  return 0b0001;
		if(wall == Field.Wall.top)   return 0b0010;
		if(wall == Field.Wall.right) return 0b0100;
		return 0b1000;
	}
}