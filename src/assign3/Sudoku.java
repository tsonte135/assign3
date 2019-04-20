package assign3;

import java.util.*;



/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	private Sudoku sudoku;
	private int[][] grid;
	private int[][] tmpGrid;
	private ArrayList<Spot> spotList;
	private String solutionText;
	private int result;
	private long time;
	
	private class Spot implements Comparable{
		
		private int row, col, value, numOfPossDigits;
		private Set<Integer> possibleNumbers;
		
		
		public Spot(int spotRow, int spotCol){
			row = spotRow;
			col = spotCol;
			numOfPossDigits = possibleNumSet().size();
			value = grid[row][col];
		}

		public Set<Integer> possibleNumSet(){
			possibleNumbers = new HashSet<Integer>();
			for(int i = 1; i <= SIZE; i++){
				possibleNumbers.add(i);
			}
			
			for(int j = 0; j < tmpGrid[0].length; j++){
				if(possibleNumbers.contains(tmpGrid[row][j])){
					possibleNumbers.remove(tmpGrid[row][j]);
				}
			}
			
			for(int i = 0; i < tmpGrid.length; i++){
				if(possibleNumbers.contains(tmpGrid[i][col])){
					possibleNumbers.remove(tmpGrid[i][col]);
				}
			}
			
			int squareRow = row / PART * PART;
			int squareCol = col / PART * PART;
			
			for(int i = squareRow;  i < squareRow + PART; i++){
				for(int j = squareCol; j < squareCol + PART; j++){
					if(possibleNumbers.contains(tmpGrid[i][j])){
						possibleNumbers.remove(tmpGrid[i][j]);
					}
				}
			}
			return possibleNumbers;
		}
		
		public void set(int spotInt){
			tmpGrid[row][col] = spotInt;
		}
		
		@Override
		public int compareTo(Object o) {
	        Spot cell = (Spot)o;

	        if(this.numOfPossDigits == cell.numOfPossDigits){
	        	return 0;    	
	        }else{
	        	if(this.numOfPossDigits < cell.numOfPossDigits){
	        		return -1;
	        	}else{
	        		return 1;
	        	}
	        }
	    }		
	}
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	
	

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		grid = new int[SIZE][SIZE];
		grid = ints;
		tmpGrid = new int[SIZE][SIZE];
		spotList = new ArrayList<Spot>();
		
		for(int i = 0; i < tmpGrid.length; i++){
			for(int j = 0; j < tmpGrid[0].length; j++){
				tmpGrid[i][j] = grid[i][j];
			}
		}
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[0].length; j++){
				if(grid[i][j] == 0){
					Spot cell = new Spot(i, j);
					spotList.add(cell);
				}
			}
		}

		Collections.sort(spotList);
	}
	
	public Sudoku(String text){
		this(textToGrid(text));
	}
	
	@Override
	public String toString(){
		StringBuilder buff = new StringBuilder();
		
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[0].length; j++){
				buff.append(grid[i][j]);
				if(j != grid[0].length - 1){
					buff.append(" ");
				}	
			}
			buff.append("\n");
		}
		return buff.toString();
	
	}
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		result = 0;
		long start = System.currentTimeMillis();
		rec(0);
		long finish = System.currentTimeMillis();
		time = finish - start;
		return result; 
	}
	
	private void rec(int ind){
		if(result >= MAX_SOLUTIONS){
			return;
		}
		
		if(ind == spotList.size()){
			if(result == 0) {
				Sudoku res = new Sudoku(tmpGrid);
				solutionText = res.toString();
				
			}
			result++;
			return;
		}
		
		Spot curr = spotList.get(ind);
		Set<Integer> assignDigits = curr.possibleNumSet();
		for(int j : assignDigits){
			curr.set(j);
			rec(ind + 1);
			curr.set(0);
		}
	}
	
	public String getSolutionText() {
		if(result == 0){
			return " ";
		}
		return solutionText;
	}
	
	public long getElapsed() {
		return time;
	}

}
