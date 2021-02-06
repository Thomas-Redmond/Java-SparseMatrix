
/* C1908527
 *
 * Optionally, if you have any comments regarding your submission, put them here.
 * For instance, specify here if your program does not generate the proper output or does not do it in the correct manner.
 */

import java.util.*;
import java.io.*;

// A class that represents a dense vector and allows you to read/write its elements
class DenseVector {
	private int[] elements;

	public DenseVector(int n) {
		elements = new int[n];
	}

	public DenseVector(String filename) {
		File file = new File(filename);
		ArrayList<Integer> values = new ArrayList<Integer>();

		try {
			Scanner sc = new Scanner(file);

			while (sc.hasNextInt()) {
				values.add(sc.nextInt());
			}

			sc.close();

			elements = new int[values.size()];
			for (int i = 0; i < values.size(); ++i) {
				elements[i] = values.get(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Read an element of the vector
	public int getElement(int idx) {
		return elements[idx];
	}

	// Modify an element of the vector
	public void setElement(int idx, int value) {
		elements[idx] = value;
	}

	// Return the number of elements
	public int size() {
		return (elements == null) ? 0 : (elements.length);
	}

	// Print all the elements
	public void print() {
		if (elements == null) {
			return;
		}

		for (int i = 0; i < elements.length; ++i) {
			System.out.println(elements[i]);
		}
	}
}



// A class that represents a sparse matrix
public class SparseMatrix {
	// Auxiliary function that prints out the command syntax
	public static void printCommandError() {
		System.err.println("ERROR: use one of the following commands");
		System.err.println(" - Read a matrix and print information: java SparseMatrix -i <MatrixFile>");
		System.err.println(" - Read a matrix and print elements: java SparseMatrix -r <MatrixFile>");
		System.err.println(" - Transpose a matrix: java SparseMatrix -t <MatrixFile>");
		System.err.println(" - Add two matrices: java SparseMatrix -a <MatrixFile1> <MatrixFile2>");
		System.err.println(" - Matrix-vector multiplication: java SparseMatrix -v <MatrixFile> <VectorFile>");
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			printCommandError();
			System.exit(-1);
		}

		if (args[0].equals("-i")) {
			if (args.length != 2) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			System.out.println("Read matrix from " + args[1]);
			System.out.println("The matrix has " + mat.getNumRows() + " rows and " + mat.getNumColumns() + " columns");
			System.out.println("It has " + mat.numNonZeros() + " non-zeros");
		} else if (args[0].equals("-r")) {
			if (args.length != 2) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			System.out.println("Read matrix from " + args[1] + ":");
			mat.print();
		} else if (args[0].equals("-t")) {
			if (args.length != 2) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			System.out.println("Read matrix from " + args[1]);
			SparseMatrix transpose_mat = mat.transpose();
			System.out.println();
			System.out.println("Matrix elements:");
			mat.print();
			System.out.println();
			System.out.println("Transposed matrix elements:");
			transpose_mat.print();
		} else if (args[0].equals("-a")) {
			if (args.length != 3) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat1 = new SparseMatrix();
			mat1.loadEntries(args[1]);
			System.out.println("Read matrix 1 from " + args[1]);
			System.out.println("Matrix elements:");
			mat1.print();

			System.out.println();
			SparseMatrix mat2 = new SparseMatrix();
			mat2.loadEntries(args[2]);
			System.out.println("Read matrix 2 from " + args[2]);
			System.out.println("Matrix elements:");
			mat2.print();
			SparseMatrix mat_sum1 = mat1.add(mat2);

			System.out.println();
			mat1.multiplyBy(2);
			SparseMatrix mat_sum2 = mat1.add(mat2);

			mat1.multiplyBy(5);
			SparseMatrix mat_sum3 = mat1.add(mat2);

			System.out.println("Matrix1 + Matrix2 =");
			mat_sum1.print();
			System.out.println();

			System.out.println("Matrix1 * 2 + Matrix2 =");
			mat_sum2.print();
			System.out.println();

			System.out.println("Matrix1 * 10 + Matrix2 =");
			mat_sum3.print();
		} else if (args[0].equals("-v")) {
			if (args.length != 3) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			DenseVector vec = new DenseVector(args[2]);
			DenseVector mv = mat.multiply(vec);

			System.out.println("Read matrix from " + args[1] + ":");
			mat.print();
			System.out.println();

			System.out.println("Read vector from " + args[2] + ":");
			vec.print();
			System.out.println();

			System.out.println("Matrix-vector multiplication:");
			mv.print();
		}
	}

	void merge(ArrayList<Entry> Array, int start, int mid, int end){
		// finding lengths of subLists
		int lengthL = mid - start + 1;
    int lengthR = end - mid;

    ArrayList<Entry> L = new ArrayList<Entry>(lengthL); // making temp arrays of length L
    ArrayList<Entry> R = new ArrayList<Entry>(lengthR); // making temp array of length R

		// copy values to temp arrays
    for (int i =0; i < lengthL; i++){
      L.add(i, Array.get(start + i));
		}
    for (int j = 0; j < lengthR; j++){
      R.add(j, Array.get(mid + 1 + j));
		}

    // Merging the temp arrays
    int i = 0, j = 0;
    int pointer = start;

		// make comparisons for sorting
    while (i < lengthL && j < lengthR){
        if (L.get(i).getPosition() <= R.get(j).getPosition()){
          Array.set(pointer, L.get(i));
          i++;
        } else {
          Array.set(pointer, R.get(j));
          j++;
        }
        pointer++;
    }
		// shove leftovers in right order (L)
    while (i < lengthL){
      Array.set(pointer, L.get(i));
      i++;
      pointer++;
    }
		// shove leftover R values at end
    while (j < lengthR){
      Array.set(pointer, R.get(j));
      j++;
      pointer++;
    }
		// no return values as Java ALWAYS references objects
	}

	void sort(ArrayList<Entry> Array, int start, int end){
		if (start < end){
			int mid = (start + end) / 2;
			sort(Array, start, mid);
			sort(Array, mid+1, end);

			merge(Array, start, mid, end);
		}
		// no return values as java ALWAYS references objects
	}

	// Loading matrix entries from a text file
	// You need to complete this implementation
	public void loadEntries(String filename) {
		File file = new File(filename);

		try {
			Scanner sc = new Scanner(file);
			numRows = sc.nextInt();
			numCols = sc.nextInt();
			entries = new ArrayList<Entry>();

			while (sc.hasNextInt()) {
				// Read the row index, column index, and value of an element
				int row = sc.nextInt();
				int col = sc.nextInt();
				int val = sc.nextInt();

				// Add your code here to add the element into data member entries
				Entry newEntry = new Entry((row * numCols + col), val); // creating new Entry element
				entries.add(0, newEntry); // adds newEntry to list at primary position - will shift existing values right by one
			}

			// Add your code here for sorting non-zero elements
			sort(entries, 0, entries.size()-1);
			// entries now sorted using SORT and MERGE functions on line 232, and 186 respectively

		} catch (Exception e) {
			e.printStackTrace();
			numRows = 0;
			numCols = 0;
			entries = null;
		}
	}

	// Default constructor
	public SparseMatrix() {
		numRows = 0;
		numCols = 0;
		entries = null;
	}

	// A class representing a pair of column index and elements
	private class Entry {
		private int position; // Position within row-major full array representation
		private int value; // Element value

		// Constructor using the column index and the element value
		public Entry(int pos, int val) {
			this.position = pos;
			this.value = val;
		}

		// Copy constructor
		public Entry(Entry entry) {
			this(entry.position, entry.value);
		}

		// Read column index
		int getPosition() {
			return position;
		}

		// Set column index
		void setPosition(int pos) {
			this.position = pos;
		}

		// Read element value
		int getValue() {
			return value;
		}

		// Set element value
		void setValue(int val) {
			this.value = val;
		}
	}


	// Adding two matrices
	public SparseMatrix add(SparseMatrix M) {
		// Add your code here
		// initilize new matrix, and allow for entries to be added
		SparseMatrix added = new SparseMatrix();
		added.entries = new ArrayList<Entry>();

		// using two pointers traverse lists
		// check relative position, if smaller place smaller first, increment that lists counter
		// if equal combine, increment counter (checking if zero)
		// if bigger place other first
		int i = 0, j = 0, index = 0; // index is input position in final array
		added.numCols = this.numCols;
		added.numRows = this.numRows;
		while(i < M.entries.size() && j < this.entries.size()){
			if( (M.entries.get(i).getValue() + this.entries.get(j).getValue() == 0 ) && ( M.entries.get(i).getPosition() == this.entries.get(j).getPosition()) ) {
				// if sum results in zero then SKIP
				// placing first means no further checks for zeros need to be made
				i++;
				j++;
			}
			else if(M.entries.get(i).getPosition() < this.entries.get(j).getPosition() ){
				// if M position is earlier
				Entry newEntry = new Entry(M.entries.get(i).getPosition(), M.entries.get(i).getValue());
				added.entries.add(index, newEntry);
				index++;
				i++;
			} else if (M.entries.get(i).getPosition() == this.entries.get(j).getPosition() ){
				// if position equal AND sum of values not zero
				Entry newEntry = new Entry(M.entries.get(i).getPosition(), M.entries.get(i).getValue() + this.entries.get(j).getValue());
				added.entries.add(index, newEntry);
				index++;
				i++;
				j++;
			} else {
				// bigger so being placed first
				Entry newEntry = new Entry(this.entries.get(j).getPosition(), this.entries.get(j).getValue());
				added.entries.add(index, newEntry);
				j++;
				index++;
			}
		}

		// escape while loop = one or both of the maximum limits for i or j has been reached.
		// checking if leftovers remaining in one or other
		// implementing if so

		if(i < M.entries.size()){
			while(i < M.entries.size()){
				Entry newEntry = new Entry(M.entries.get(i).getPosition(), M.entries.get(i).getValue());
				added.entries.add(index, newEntry);
				i++;
				index++;
			}
		} else if( j < this.entries.size()){
			while(j < this.entries.size()){
				Entry newEntry = new Entry(this.entries.get(j).getPosition(), this.entries.get(j).getValue());
				added.entries.add(index, newEntry);
				j++;
				index++;
			}
		}
		return added;
	}

	// Transposing a matrix
	public SparseMatrix transpose() {
		// Add your code here
		 SparseMatrix transposed = new SparseMatrix();
		 transposed.entries = new ArrayList<Entry>();
		 transposed.numRows = this.numCols;
		 transposed.numCols = this.numRows;

		 for(int i = 0; i < entries.size(); i++){
			 // i is pointer
			 int oldPosition = this.entries.get(i).getPosition();
			 int oldRow = oldPosition / this.numCols; // old row co-ord given by integer division
			 int oldCol = oldPosition % this.numCols; // old col co-ord given by remainder division
			 Entry newEntry = new Entry(oldCol * transposed.numCols + oldRow, this.entries.get(i).getValue());
			 transposed.entries.add(i, newEntry);
		 }
		 transposed.sort(transposed.entries, 0, transposed.entries.size()-1);
		 return transposed;
	}

	// Matrix-vector multiplication
	public DenseVector multiply(DenseVector v) {
		// Add your code here
		// Initate new dense vector, populate with 0
		// traverse entries, do comblicate maths to deduce row = row number of sum product
		// deduce col value, multiply by relevent v.something, add to value in row position
		DenseVector newVector = new DenseVector(this.numRows); // new product vector with all values set to zero
		for(int i = 0; i < this.entries.size(); i++){
			int rowIndex; // row data added to in product
			int colIndex; // col index of data to be multiplied by

			rowIndex = this.entries.get(i).getPosition() / this.numCols; // Position value = numCol * row + col, using integer division gives numCol again
			colIndex = this.entries.get(i).getPosition() % this.numCols; // remainder division of above

			newVector.setElement(rowIndex, (newVector.getElement(rowIndex) + this.entries.get(i).getValue() * v.getElement(colIndex)) );
			// rather than overwrite old value, new value set to old + multiplied values
		}

		return newVector;
	}

	// Return the number of non-zeros
	public int numNonZeros() {
		// Add your code here
		return (entries == null) ? 0 : (entries.size());
	}

	// Multiply the matrix by a scalar, and update the matrix elements
	public void multiplyBy(int scalar) {
		if(scalar == 0){
			// only way of products being zero is multiplier is zero = all elements produced will be zero
			// so set all elements to null, and reset array
			this.entries.clear();
			this.entries = new ArrayList<Entry>();
		} else {
			for(int i = 0; i < this.entries.size(); i++){
				this.entries.get(i).setValue(this.entries.get(i).getValue() * scalar);
			}
		}
	}

	// Number of rows of the matrix
	public int getNumRows() {
		return this.numRows;
	}


	// Number of columns of the matrix
	public int getNumColumns() {
		return this.numCols;
	}

	// Output the elements of the matrix, including the zeros
	// Do not modify this method
	public void print() {
		int n_elem = numRows * numCols;
		int pos = 0;

		for (int i = 0; i < entries.size(); ++i) {
			int nonzero_pos = entries.get(i).getPosition();

			while (pos <= nonzero_pos) {
				if (pos < nonzero_pos) {
					System.out.print("0 ");
				} else {
					System.out.print(entries.get(i).getValue());
					System.out.print(" ");
				}

				if ((pos + 1) % this.numCols == 0) {
					System.out.println();
				}

				pos++;
			}
		}

		while (pos < n_elem) {
			System.out.print("0 ");
			if ((pos + 1) % this.numCols == 0) {
				System.out.println();
			}

			pos++;
		}
	}

	private int numRows; // Number of rows
	private int numCols; // Number of columns
	private ArrayList<Entry> entries; // Non-zero elements
}
