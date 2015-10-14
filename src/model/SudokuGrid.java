package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class SudokuGrid implements Grid<Integer> {

	private int [][] grid;
	private Collection<GridConstraint> sudokuConstraints;
	private int sqrtDim;
	private HashMap<GridCoord, Collection<GridCoord>> shareConstraint;
	
	public SudokuGrid(int sqrtDim) {
		this.sqrtDim = sqrtDim;
		int dim = sqrtDim * sqrtDim;
		grid = new int [dim][dim];
		this.sudokuConstraints = new LinkedList<GridConstraint>();
		this.shareConstraint = new HashMap<>();
		initConstraints();
	}
	
	public void feedValues(int [][] values) {
		if (values == null || 
				values.length != grid.length || 
				values[0].length != grid[0].length) {
			return;
		}
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				grid[i][j] = values[i][j];
			}
		}
	}
	
	public static SudokuGrid buildSudoku() {
		return new SudokuGrid(3);
	}
	
	public int dimension() {
		return grid.length;
	}
	
	private void initConstraints() {
		for(int i = 0; i < this.dimension(); ++i) {
			addRowConstraint(i);
			addColConstraint(i);
		}
		
		for(int i = 0; i < dimension() - sqrtDim; i+=sqrtDim) {
			for(int j = 0; j < dimension() - sqrtDim; j+=sqrtDim) {
				addSquareConstraint(i, j);
			}
		}
	}
	
	private void addRowConstraint(int row) {
		ArrayList<GridCoord> rowCoords = new ArrayList<>(this.dimension());
		for (int col = 0; col < this.dimension(); ++col) {
			rowCoords.add(new GridCoord(row, col));
		}
		this.sudokuConstraints.add(GridConstraint.buildConstraint(
				ConstraintType.Unique, this, rowCoords));
	}
	
	private void addColConstraint(int col) {
		ArrayList<GridCoord> colCoords = new ArrayList<>(this.dimension());
		for (int row = 0; row < this.dimension(); ++row) {
			colCoords.add(new GridCoord(row, col));
		}
		this.sudokuConstraints.add(GridConstraint.buildConstraint(
				ConstraintType.Unique, this, colCoords));
	}
	
	private void addSquareConstraint(int x, int y) {
		ArrayList<GridCoord> coords = new ArrayList<>(this.dimension());
		for(int i = 0; i < this.sqrtDim; ++i) {
			for(int j = 0; j < this.sqrtDim; ++j) {
				coords.add(new GridCoord(x+i,y+j));
			}
		}
		this.sudokuConstraints.add(GridConstraint.buildConstraint(
				ConstraintType.Unique, this, coords));
	}
	
	public boolean isValid() {
		for (GridConstraint constr : this.sudokuConstraints) {
			if (!constr.isValid()) return false;
		}
		return true;
	}
	
	public Collection<GridCoord> sharingConstraintsCoords(GridCoord coord) {
		if (!shareConstraint.containsKey(coord)) { 
			LinkedList<GridCoord> list = new LinkedList<>();
			for (GridConstraint constr : this.sudokuConstraints) {
				if (constr.getConstraintedCoord().contains(coord)) {
					list.addAll(constr.getConstraintedCoord());
					list.remove(coord);
				}
			}
			shareConstraint.put(coord, list);
		}
		return shareConstraint.get(coord);
	}

	@Override
	public Integer get(GridCoord gc) {
		return grid[gc.x][gc.y];
	}

	@Override
	public void set(GridCoord gc, Integer value) {
		grid[gc.x][gc.y] = value;
	}

	@Override
	public boolean isCoordSet(GridCoord gc) {
		return this.get(gc) > 0 && this.get(gc) <= this.dimension();
	}
	
	@Override
	public String toString() {
		String ret = "";
		for (int i = 0; i < grid.length; i++) {
			ret += Arrays.toString(grid[i]);
			if (i != grid.length - 1) ret += "\n";
		}
		return ret;
	}
	
}
