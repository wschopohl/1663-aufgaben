public class Field {
	public enum Type {empty, start, finish, trap}
	public enum Wall {left, top, right, bottom}
	
	private Type type;
	private boolean[] walls;
	
	public Field(Type type) {
		this.type = type;
		walls = new boolean[4];
	}
	
	public void setWall(Wall wall, boolean state) {
		walls[wall.ordinal()] = state;
	}
	
	public boolean hasWall(Wall wall) {
		return walls[wall.ordinal()];
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public void setTrap() {
		setType(Type.trap);
	}
	
	public Type getType() {
		return type;
	}
	
	public boolean hasType(Type type) {
		return this.type == type;
	}
	
	public boolean isEmpty() {
		return hasType(Type.empty);
	}
	
	public byte toByte() {
		switch(type) {
		case start:
			return 22;
		case finish:
			return 23;
		case trap:
			return 21;
		}
		return 0;
	}
}