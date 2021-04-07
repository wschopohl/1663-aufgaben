public class Field {
	enum Type {empty, start, finish, trap}
	
	public int x;
	public int y;
	
	public Field(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int mapX() {
		return x*2-1;
	}
	public int mapY() {
		return y*2-1;
	}
	
	public static byte type2Byte(Type type) {
		if(type == Field.Type.empty) return 0;
		if(type == Field.Type.trap) return 21;
		if(type == Field.Type.start) return 22;
		if(type == Field.Type.finish) return 23;
		return 0;
	}
}