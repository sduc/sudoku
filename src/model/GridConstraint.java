package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public abstract class GridConstraint {
	
	private Grid<Integer> grid;
	private Collection<GridCoord> gridElements;
	
	abstract public boolean isValid();
	
	public GridConstraint(Grid<Integer> grid, Collection<GridCoord> coords) {
		this.grid = grid;
		this.gridElements = new ArrayList<>(coords.size());
		gridElements.addAll(coords);
	}
	
	public Collection<GridCoord> getConstraintedCoord() {
		return this.gridElements;
	}
	
	public Grid<Integer> getGrid() {
		return this.grid;
	}
	
	public static GridConstraint buildConstraint(
			ConstraintType type,
			Grid<Integer> grid,
			Collection<GridCoord> coords) {
		switch (type) {
		case Unique:
			return new GridUniqueConstraint(grid, coords);
		default:
			return null;
		}
	}

}

class GridUniqueConstraint extends GridConstraint {
	
	public GridUniqueConstraint(Grid<Integer> grid, Collection<GridCoord> coords) {
		super(grid, coords);
	}

	@Override
	public boolean isValid() {
		HashSet<Integer> seen = new HashSet<>();
		for (GridCoord gc : super.getConstraintedCoord()) {
			if (!seen.add(super.getGrid().get(gc)))
				return false;
		}
		return true;
	}
	
}
