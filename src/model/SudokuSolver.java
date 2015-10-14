package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class SudokuSolver {
	
	private SudokuGrid sudoku;
	private Grid<LinkedList<Integer>> availableValueGrid;
	private Collection<GridCoord> unsetCoords;
	private boolean solved = false;
	
	public SudokuSolver(SudokuGrid sudoku) throws InvalidSudokuException {
		this.sudoku = sudoku;
		this.availableValueGrid = new AvailableValueGrid(sudoku.dimension());
		this.unsetCoords = new HashSet<GridCoord>();
		initSolver();
	}
	
	private void initSolver() throws InvalidSudokuException {
		for (int i = 0; i < sudoku.dimension(); ++i) {
			for (int j = 0; j < sudoku.dimension(); j++) {
				GridCoord coord = new GridCoord(i, j);

				if (!sudoku.isCoordSet(coord))
					unsetCoords.add(coord);
				else {
					boolean ret = updateAvailableValueGrid(
							coord, 
							sudoku.get(coord),
							new Stack<GridCoord>());
					if (!ret) throw new InvalidSudokuException();
				}
			}
		}
	}
	
	public SudokuGrid solve() {
		solveHelper();
		return sudoku;
	}
	
	private void solveHelper() {
		GridCoord mostConstr = getMostConstraintCoord();
		if (mostConstr == null) {
			return;
		}
		
		for (int i = 0; i < availableValueGrid.get(mostConstr).size(); i++) {
			int possibleValue = availableValueGrid.get(mostConstr).get(i);
			makeMove(mostConstr, possibleValue);
			Stack<GridCoord> updatedCoords = new Stack<>();
			
			if (!updateAvailableValueGrid(mostConstr, possibleValue, updatedCoords)) {	
				unmakeMove(mostConstr, possibleValue, updatedCoords);
			} else if (this.isSolution()) {
				this.solved = true;
				return;
			} else {
				solveHelper();
				if (solved) 
					return;
				else 
					unmakeMove(mostConstr, possibleValue, updatedCoords);
			}
		}
	}
	
	private GridCoord getMostConstraintCoord() {
		GridCoord ret = null;
		int min = sudoku.dimension() + 1;
		for (GridCoord coord : this.unsetCoords) {
			if (ret == null || availableValueGrid.get(coord).size() < min) {
				ret = coord;
				min = availableValueGrid.get(coord).size();
			}
			if (min == 0)
				return null;
		}
		return ret;
	}
	
	private void makeMove(GridCoord coord, int value) {
		this.sudoku.set(coord, value);
		System.out.println(sudoku + "\n");
		System.out.println(this.availableValueGrid);
		this.unsetCoords.remove(coord);
		System.out.println(unsetCoords);
	}
	
	private void unmakeMove(GridCoord coord, int value, Stack<GridCoord> updatedCoords) {
		while(!updatedCoords.empty()) {
			this.availableValueGrid.get(updatedCoords.pop()).add(value);
		}
		this.unsetCoords.add(coord);
		this.sudoku.set(coord, 0);
	}
	
	private boolean updateAvailableValueGrid(
			GridCoord coord, 
			int value, 
			Stack<GridCoord> updatedCoords) {		
		for (GridCoord gc : this.sudoku.sharingConstraintsCoords(coord)) {
			if(this.availableValueGrid.get(gc).remove(new Integer(value)))
				updatedCoords.push(gc);
			if(this.availableValueGrid.get(gc).isEmpty()) {
				return false;
			}	
		}
		return true;
	}
	
	private boolean isSolution() {
		return (this.unsetCoords.size() == 0 && 
				this.sudoku.isValid());
	}

}

class AvailableValueGrid implements Grid<LinkedList<Integer>> {
	
	ArrayList<ArrayList<LinkedList<Integer>>> grid;
	
	public AvailableValueGrid(int dim) {
		grid = new ArrayList<>(dim);
		for (int i = 0; i < dim; i++) {
			grid.add(new ArrayList<LinkedList<Integer>>(dim));
			for (int j = 0; j < dim; j++) {
				grid.get(i).add(range(dim));
			}
		}
	}
	
	private LinkedList<Integer> range(int n) {
		LinkedList<Integer> ll = new LinkedList<>();
		for(int i = 1; i <= n; i++)
			ll.addLast(i);
		return ll;
	}
	
	@Override
	public LinkedList<Integer> get(GridCoord gc) {
		return grid.get(gc.x).get(gc.y);
	}
	@Override
	public void set(GridCoord gc, LinkedList<Integer> value) {
		grid.get(gc.x).set(gc.y, value);
	}
	@Override
	public boolean isCoordSet(GridCoord gc) {
		return get(gc) != null;
	}
	
	@Override
	public String toString() {
		return this.grid.toString();
	}
	
}
