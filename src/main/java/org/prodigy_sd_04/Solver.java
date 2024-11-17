package org.prodigy_sd_04;

public class Solver {
    //    we will need a 2D array to hold the solved matrix
    private int[][] solved = new int[9][9];

    public Solver() {
    }

    boolean solve(int[][] matrix, int row, int col) {
        if (row == 9 && col == 8) {
            solved = matrix;
            return true;
        }
        if (row == 9) {
            col++;
            row = 0;
        }
        if (matrix[row][col] != 0) return solve(matrix, row + 1, col);
        for (int num = 1; num <= 9; num++) {
            if (ok_or_not(matrix, row, col, num)) {
                matrix[row][col] = num;
                if (solve(matrix, row + 1, col)) return true;
            }
            matrix[row][col] = 0;
        }
        return false;
    }

    boolean ok_or_not(int[][] matrix, int row, int col, int num) {
        for (int colX = 0; colX < 9; colX++) {
            if (matrix[row][colX] == num) return false;
            if (matrix[colX][col] == num) return false;
        }

        int rowStart = row - (row % 3), colStart = col - (col % 3);
        for (int rowQ = 0; rowQ < 3; rowQ++) {
            for (int colQ = 0; colQ < 3; colQ++) {
                if (matrix[rowQ + rowStart][colQ + colStart] == num) return false;
            }
        }
        return true;
    }

    int[][] getSolved() {
        return solved;
    }
}

