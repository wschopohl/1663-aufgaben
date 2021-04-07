import java.util.Random;

public class Labyrinth {
	
	enum WallBit {left, right, top, bottom} 
	
	private byte[][] map;
	
	public static void main(String[] args) {
		Labyrinth labyrinth = new Labyrinth(20,10);
		labyrinth.setRandomWalls();
		labyrinth.setStart(new Field(1,1));
		labyrinth.setFinish(new Field(20,10));
		labyrinth.setRandomTraps(30);
		labyrinth.print();
		System.out.println("Labyrinth hat " + labyrinth.countTraps() + " Fallen.");
	}
	
	/* initialize new Labyrinth of given dimensions */
	public Labyrinth(int sizeX, int sizeY) {
		map = new byte[sizeX * 2 + 1][sizeY * 2 + 1];
		setOuterWalls();
	}
	
	/* set a wall between two fields */
	public void setWall(Field field1, Field field2) {
		// System.out.println(this.sizeY());
		if(field1.x == field2.x) {
			if(field1.y < field2.y) setWallBits(field1, WallBit.bottom);
			else setWallBits(field1, WallBit.top);
		}
		else if(field1.y == field2.y) {
			if(field1.x < field2.x) setWallBits(field1, WallBit.right);
			else setWallBits(field1, WallBit.right);
		}
	}
	
	/* set a wall on an outside field */
	public void setWall(Field field) {
		if(field.x == 1) {
			this.setWallBits(field, WallBit.left);
		}
		if(field.y == 1) {
			this.setWallBits(field, WallBit.top);
		}
		if(field.x == sizeX()) {
			this.setWallBits(field, WallBit.right);
		}
		if(field.y == sizeY()) {
			this.setWallBits(field, WallBit.bottom);
		}
	}
	
	/* set a trap */
	public void setTrap(Field field) {
		map[field.mapX()][field.mapY()] = Field.type2Byte(Field.Type.trap);
	}
	
	/* set the start */
	public void setStart(Field field) {
		map[field.mapX()][field.mapY()] = Field.type2Byte(Field.Type.start);
	}
	
	/* set the finish */
	public void setFinish(Field field) {
		map[field.mapX()][field.mapY()] = Field.type2Byte(Field.Type.finish);
	}
	
	/* check if field is empty */
	public boolean empty(Field field) {
		return isType(field, Field.Type.empty);
	}
	
	/* check if field has specified type */
	public boolean isType(Field field, Field.Type type) {
		return (map[field.mapX()][field.mapY()] == Field.type2Byte(type));
	}
	
	/* output labyrinth to terminal */
	public void print() {
		for(int mapY = 0; mapY < map[0].length; mapY++) {
			for(int mapX = 0; mapX < map.length; mapX++) {
				System.out.print(getChar(mapX,mapY));
			}
			System.out.println();
		}
	}
	
	/* convert the byte representation of a map tile to a printable character */
	private String getChar(int mapX, int mapY) {
		switch(map[mapX][mapY]) {
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
		return String.format("%02d", map[mapX][mapY]);
	}
	
	/* set all the outer walls */
	private void setOuterWalls() {
		int sizeX = sizeX();
		int sizeY = sizeY();
		for(int x = 1; x <= sizeX; x++) {
			setWall(new Field(x,1));
			setWall(new Field(x,sizeY));
		}
		for(int y = 1; y <= sizeY; y++) {
			setWall(new Field(1,y));
			setWall(new Field(sizeX,y));
		}
	}
	
	/* randomly sets walls in the whole labyrinth */
	public void setRandomWalls() {
		Random rand = new Random();
		for(int y = 1; y < sizeY(); y++) {
			for(int x = 1; x < sizeX(); x++) {
				int walls = rand.nextInt(3);
				if(walls == 1) setWall(new Field(x,y), new Field(x+1,y));
				else if(walls == 2) setWall(new Field(x,y), new Field(x,y+1));
			}
		}
	}
	
	/* count all traps in the labyrinth */
	public int countTraps() {
		int count = 0;
		for(int y = 1; y <= sizeY(); y++) {
			for(int x = 1; x <= sizeX(); x++) {
				Field field = new Field(x,y);
				if(isType(field,Field.Type.trap)) count++;
			}
		}
		return count;
	}
	
	/* randomly sets traps in the whole labyrinth, amount specified by num */
	public void setRandomTraps(int num) {
		Random rand = new Random();
		int trapsSet = 0;
		while(trapsSet < num) {
			int x = rand.nextInt(sizeX())+1;
			int y = rand.nextInt(sizeY())+1;
			Field field = new Field(x,y);
			if(empty(field)) {
				setTrap(field);
				trapsSet++;
			}
		}
	}
	
	/* internal function to set bits corresponding to a wall */
	private void setWallBits(Field field, WallBit wall) {
		int mapX = field.mapX();
		int mapY = field.mapY();
		byte bit = wallBit2Byte(wall);
		if(wall == WallBit.left) {
			map[mapX-1][mapY]   |= (wallBit2Byte(WallBit.top) | wallBit2Byte(WallBit.bottom));
			map[mapX-1][mapY-1] |= wallBit2Byte(WallBit.bottom);
			map[mapX-1][mapY+1] |= wallBit2Byte(WallBit.top);
		}
		else if(wall == WallBit.top) {
			map[mapX][mapY-1]   |= wallBit2Byte(WallBit.left) | wallBit2Byte(WallBit.right);
			map[mapX-1][mapY-1] |= wallBit2Byte(WallBit.right);
			map[mapX+1][mapY-1] |= wallBit2Byte(WallBit.left);
		}
		else if(wall == WallBit.right) {
			map[mapX+1][mapY]   |= wallBit2Byte(WallBit.top) | wallBit2Byte(WallBit.bottom);
			map[mapX+1][mapY-1] |= wallBit2Byte(WallBit.bottom);
			map[mapX+1][mapY+1] |= wallBit2Byte(WallBit.top);
		}
		else if(wall == WallBit.bottom) {
			map[mapX][mapY+1]   |= wallBit2Byte(WallBit.left) | wallBit2Byte(WallBit.right);
			map[mapX-1][mapY+1] |= wallBit2Byte(WallBit.right);
			map[mapX+1][mapY+1] |= wallBit2Byte(WallBit.left);
		}
	}
	
	/* get the width of the fields */
	private int sizeX() {
		return (int)Math.floor(map.length / 2);
	}
	
	/* get the height of the fields */
	private int sizeY() {
		return (int)Math.floor(map[0].length / 2);
	}
	
	/* convert a wall type to a bit representation */
	private byte wallBit2Byte(WallBit bit) {
		if(bit == WallBit.left)  return 0b0001;
		if(bit == WallBit.top)   return 0b0010;
		if(bit == WallBit.right) return 0b0100;
		return 0b1000;
	}

}