package com.femeuc.advancedtic_tac_toe.classes;

public class Board {
    public final static int AMOUNT_OF_SQUARES = calculateAmountOfSquares();
    public final static int AMOUNT_OF_ROWS = 15;
    public final static int AMOUNT_OF_COLUMNS = 12;
    public final static int AMOUNT_OF_ASCENDING_DIAGONALS = 18;
    public static final int AMOUNT_OF_DESCENDING_DIAGONALS = 18;

    public Square[] gameBoard;

    private static final boolean[] FIRST_COLUMN = initCol(0);
    private static final boolean[] SECOND_COLUNN = initCol(1);
    private static final boolean[] THIRD_COLUMN = initCol(2);
    private static final boolean[] FOURTH_COLUMN = initCol(3);
    private static final boolean[] FIFTH_COLUMN = initCol(4);
    private static final boolean[] SIXTH_COLUMN = initCol(5);
    private static final boolean[] SEVENTH_COLUMN = initCol(6);
    private static final boolean[] EIGHTH_COLUMN = initCol(7);
    private static final boolean[] NINTH_COLUMN = initCol(8);
    private static final boolean[] TENTH_COLUMN = initCol(9);
    private static final boolean[] ELEVENTH_COLUMN = initCol(10);
    private static final boolean[] TWELFTH_COLUMN = initCol(11);

    private static final boolean[] FIRST_ROW = initRow(0);
    private static final boolean[] SECOND_ROW = initRow(12);
    private static final boolean[] THIRD_ROW = initRow(24);
    private static final boolean[] FOURTH_ROW = initRow(36);
    private static final boolean[] FIFTH_ROW = initRow(48);
    private static final boolean[] SIXTH_ROW = initRow(60);
    private static final boolean[] SEVENTH_ROW = initRow(72);
    private static final boolean[] EIGHTH_ROW = initRow(84);
    private static final boolean[] NINTH_ROW = initRow(96);
    private static final boolean[] TENTH_ROW = initRow(108);
    private static final boolean[] ELEVENTH_ROW = initRow(120);
    private static final boolean[] TWELFTH_ROW = initRow(132);
    private static final boolean[] THIRTEENTH_ROW = initRow(144);
    private static final boolean[] FOURTEENTH_ROW = initRow(156);
    private static final boolean[] FIFTEENTH_ROW = initRow(168);

    private static final boolean[] FIRST_ASCENDING_DIAGONAL = initAscendingDiagonal(48);
    private static final boolean[] SECOND_ASCENDING_DIAGONAL = initAscendingDiagonal(60);
    private static final boolean[] THIRD_ASCENDING_DIAGONAL = initAscendingDiagonal(72);
    private static final boolean[] FOURTH_ASCENDING_DIAGONAL = initAscendingDiagonal(84);
    private static final boolean[] FIFTH_ASCENDING_DIAGONAL = initAscendingDiagonal(96);
    private static final boolean[] SIXTH_ASCENDING_DIAGONAL = initAscendingDiagonal(108);
    private static final boolean[] SEVENTH_ASCENDING_DIAGONAL = initAscendingDiagonal(120);
    private static final boolean[] EIGHTH_ASCENDING_DIAGONAL = initAscendingDiagonal(132);
    private static final boolean[] NINTH_ASCENDING_DIAGONAL = initAscendingDiagonal(144);
    private static final boolean[] TENTH_ASCENDING_DIAGONAL = initAscendingDiagonal(156);
    private static final boolean[] ELEVENTH_ASCENDING_DIAGONAL = initAscendingDiagonal(168);
    private static final boolean[] TWELFTH_ASCENDING_DIAGONAL = initAscendingDiagonal(169);
    private static final boolean[] THIRTEENTH_ASCENDING_DIAGONAL = initAscendingDiagonal(170);
    private static final boolean[] FOURTEENTH_ASCENDING_DIAGONAL = initAscendingDiagonal(171);
    private static final boolean[] FIFTEENTH_ASCENDING_DIAGONAL = initAscendingDiagonal(172);
    private static final boolean[] SIXTEENTH_ASCENDING_DIAGONAL = initAscendingDiagonal(173);
    private static final boolean[] SEVENTEENTH_ASCENDING_DIAGONAL = initAscendingDiagonal(174);
    private static final boolean[] EIGHTEENTH_ASCENDING_DIAGONAL = initAscendingDiagonal(175);

    private static final boolean[] FIRST_DESCENDING_DIAGONAL = initDescendingDiagonal(120);
    private static final boolean[] SECOND_DESCENDING_DIAGONAL = initDescendingDiagonal(108);
    private static final boolean[] THIRD_DESCENDING_DIAGONAL = initDescendingDiagonal(96);
    private static final boolean[] FOURTH_DESCENDING_DIAGONAL = initDescendingDiagonal(84);
    private static final boolean[] FIFTH_DESCENDING_DIAGONAL = initDescendingDiagonal(72);
    private static final boolean[] SIXTH_DESCENDING_DIAGONAL = initDescendingDiagonal(60);
    private static final boolean[] SEVENTH_DESCENDING_DIAGONAL = initDescendingDiagonal(48);
    private static final boolean[] EIGHTH_DESCENDING_DIAGONAL = initDescendingDiagonal(36);
    private static final boolean[] NINTH_DESCENDING_DIAGONAL = initDescendingDiagonal(24);
    private static final boolean[] TENTH_DESCENDING_DIAGONAL = initDescendingDiagonal(12);
    private static final boolean[] ELEVENTH_DESCENDING_DIAGONAL = initDescendingDiagonal(0);
    private static final boolean[] TWELFTH_DESCENDING_DIAGONAL = initDescendingDiagonal(1);
    private static final boolean[] THIRTEENTH_DESCENDING_DIAGONAL = initDescendingDiagonal(2);
    private static final boolean[] FOURTEENTH_DESCENDING_DIAGONAL = initDescendingDiagonal(3);
    private static final boolean[] FIFTEENTH_DESCENDING_DIAGONAL = initDescendingDiagonal(4);
    private static final boolean[] SIXTEENTH_DESCENDING_DIAGONAL = initDescendingDiagonal(5);
    private static final boolean[] SEVENTEENTH_DESCENDING_DIAGONAL = initDescendingDiagonal(6);
    private static final boolean[] EIGHTEENTH_DESCENDING_DIAGONAL = initDescendingDiagonal(7);

    public Board() {
        this.gameBoard = initBoard();
    }

    private Square[] initBoard() {
        Square[] squares = new Square[AMOUNT_OF_SQUARES];
        for(int i = 0; i < AMOUNT_OF_SQUARES; i++) {
            squares[i] = new Square(i);
        }
        return squares;
    }

    private static int calculateAmountOfSquares() {
        return AMOUNT_OF_ROWS * AMOUNT_OF_COLUMNS;
    }

    private static boolean[] initCol(int columnNumber) {
        final boolean[] column = new boolean[AMOUNT_OF_SQUARES];
        do {
            column[columnNumber] = true;
            columnNumber += AMOUNT_OF_COLUMNS;
        } while(columnNumber < AMOUNT_OF_SQUARES);
        return column;
    }

    private static boolean[] initRow(int rowStart) {
        final boolean[] row = new boolean[AMOUNT_OF_SQUARES];
        int rowSquare = rowStart;
        do {
            row[rowSquare] = true;
            rowSquare += 1;
        } while(rowSquare < rowStart + AMOUNT_OF_COLUMNS);
        return row;
    }

    private static boolean[] initAscendingDiagonal(int ascendingDiagonalStartCoordinate) {
        final boolean[] ascendingDiagonal = new boolean[AMOUNT_OF_SQUARES];
        ascendingDiagonal[ascendingDiagonalStartCoordinate] = true;
        do {
            ascendingDiagonalStartCoordinate -= (AMOUNT_OF_COLUMNS - 1);
            ascendingDiagonal[ascendingDiagonalStartCoordinate] = true;
        } while(!getRow(0)[ascendingDiagonalStartCoordinate] && !getColumn(AMOUNT_OF_COLUMNS - 1)[ascendingDiagonalStartCoordinate]);
        return ascendingDiagonal;
    }

    private static boolean[] initDescendingDiagonal(int descendingDiagonalStartCoordinate) {
        final boolean[] descendingDiagonal = new boolean[AMOUNT_OF_SQUARES];
        descendingDiagonal[descendingDiagonalStartCoordinate] = true;
        do {
            descendingDiagonalStartCoordinate += (AMOUNT_OF_COLUMNS + 1);
            descendingDiagonal[descendingDiagonalStartCoordinate] = true;
        } while(!getRow(AMOUNT_OF_ROWS - 1)[descendingDiagonalStartCoordinate] && !getColumn(AMOUNT_OF_COLUMNS - 1)[descendingDiagonalStartCoordinate]);
        return descendingDiagonal;
    }

    public static boolean[] getColumn(int columnNumber) {
        switch (columnNumber) {
            case 0:
                return FIRST_COLUMN;
            case 1:
                return SECOND_COLUNN;
            case 2:
                return THIRD_COLUMN;
            case 3:
                return FOURTH_COLUMN;
            case 4:
                return FIFTH_COLUMN;
            case 5:
                return SIXTH_COLUMN;
            case 6:
                return SEVENTH_COLUMN;
            case 7:
                return EIGHTH_COLUMN;
            case 8:
                return NINTH_COLUMN;
            case 9:
                return TENTH_COLUMN;
            case 10:
                return ELEVENTH_COLUMN;
            case 11:
                return TWELFTH_COLUMN;
            default:
                throw new RuntimeException("Invalid column number");
        }
    }

    public static boolean[] getRow(int rowNumber) {
        switch (rowNumber) {
            case 0:
                return FIRST_ROW;
            case 1:
                return SECOND_ROW;
            case 2:
                return THIRD_ROW;
            case 3:
                return FOURTH_ROW;
            case 4:
                return FIFTH_ROW;
            case 5:
                return SIXTH_ROW;
            case 6:
                return SEVENTH_ROW;
            case 7:
                return EIGHTH_ROW;
            case 8:
                return NINTH_ROW;
            case 9:
                return TENTH_ROW;
            case 10:
                return ELEVENTH_ROW;
            case 11:
                return TWELFTH_ROW;
            case 12:
                return THIRTEENTH_ROW;
            case 13:
                return FOURTEENTH_ROW;
            case 14:
                return FIFTEENTH_ROW;
            default:
                throw new RuntimeException("Invalid row number");
        }
    }

    public static boolean[] getAscendingDiagonal(int ascendingDiagonalNumber) {
        switch (ascendingDiagonalNumber) {
            case 0:
                return FIRST_ASCENDING_DIAGONAL;
            case 1:
                return SECOND_ASCENDING_DIAGONAL;
            case 2:
                return THIRD_ASCENDING_DIAGONAL;
            case 3:
                return FOURTH_ASCENDING_DIAGONAL;
            case 4:
                return FIFTH_ASCENDING_DIAGONAL;
            case 5:
                return SIXTH_ASCENDING_DIAGONAL;
            case 6:
                return SEVENTH_ASCENDING_DIAGONAL;
            case 7:
                return EIGHTH_ASCENDING_DIAGONAL;
            case 8:
                return NINTH_ASCENDING_DIAGONAL;
            case 9:
                return TENTH_ASCENDING_DIAGONAL;
            case 10:
                return ELEVENTH_ASCENDING_DIAGONAL;
            case 11:
                return TWELFTH_ASCENDING_DIAGONAL;
            case 12:
                return THIRTEENTH_ASCENDING_DIAGONAL;
            case 13:
                return FOURTEENTH_ASCENDING_DIAGONAL;
            case 14:
                return FIFTEENTH_ASCENDING_DIAGONAL;
            case 15:
                return SIXTEENTH_ASCENDING_DIAGONAL;
            case 16:
                return SEVENTEENTH_ASCENDING_DIAGONAL;
            case 17:
                return EIGHTEENTH_ASCENDING_DIAGONAL;
            default:
                throw new RuntimeException("Invalid ascending diagonal number");
        }
    }

    public static boolean[] getDescendingDiagonal(int descendingDiagonalNumber) {
        switch (descendingDiagonalNumber) {
            case 0:
                return FIRST_DESCENDING_DIAGONAL;
            case 1:
                return SECOND_DESCENDING_DIAGONAL;
            case 2:
                return THIRD_DESCENDING_DIAGONAL;
            case 3:
                return FOURTH_DESCENDING_DIAGONAL;
            case 4:
                return FIFTH_DESCENDING_DIAGONAL;
            case 5:
                return SIXTH_DESCENDING_DIAGONAL;
            case 6:
                return SEVENTH_DESCENDING_DIAGONAL;
            case 7:
                return EIGHTH_DESCENDING_DIAGONAL;
            case 8:
                return NINTH_DESCENDING_DIAGONAL;
            case 9:
                return TENTH_DESCENDING_DIAGONAL;
            case 10:
                return ELEVENTH_DESCENDING_DIAGONAL;
            case 11:
                return TWELFTH_DESCENDING_DIAGONAL;
            case 12:
                return THIRTEENTH_DESCENDING_DIAGONAL;
            case 13:
                return FOURTEENTH_DESCENDING_DIAGONAL;
            case 14:
                return FIFTEENTH_DESCENDING_DIAGONAL;
            case 15:
                return SIXTEENTH_DESCENDING_DIAGONAL;
            case 16:
                return SEVENTEENTH_DESCENDING_DIAGONAL;
            case 17:
                return EIGHTEENTH_DESCENDING_DIAGONAL;
            default:
                throw new RuntimeException("Invalid descending diagonal number");
        }
    }

}
