package model;

public class GridCoord {
	
	int x;
	int y;
	
	public GridCoord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof GridCoord) {
			GridCoord other = (GridCoord) obj;
			return this.x == other.x && this.y == other.y;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return x + y * 10;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

}
