package model;

public interface Grid<T> {
	
	public abstract T get(GridCoord gc);
	public abstract void set(GridCoord gc, T value);
	public abstract boolean isCoordSet(GridCoord gc);

}
