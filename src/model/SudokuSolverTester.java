package model;

public class SudokuSolverTester {

	public static void main(String[] args) {
		SudokuGrid sudoku = SudokuGrid.buildSudoku();
		sudoku.feedValues(new int[][] {
				{0,0,0,2,6,0,7,0,1},
				{6,8,0,0,7,0,0,9,0},
				{1,9,0,0,0,4,5,0,0},
				{8,2,0,1,0,0,0,4,0},
				{0,0,4,6,0,2,9,0,0},
				{0,5,0,0,0,3,0,2,8},
				{0,0,9,3,0,0,0,7,4},
				{0,4,0,0,5,0,0,3,6},
				{7,0,3,0,1,8,0,0,0}
			});
		System.out.println(sudoku + "\n");
		try {
			SudokuSolver solver = new SudokuSolver(sudoku);
			System.out.println(solver.solve());
		} catch (InvalidSudokuException e) {
			e.printStackTrace();
		}
	}
	
}
