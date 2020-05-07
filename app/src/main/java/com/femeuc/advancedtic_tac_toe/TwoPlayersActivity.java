package com.femeuc.advancedtic_tac_toe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TwoPlayersActivity extends AppCompatActivity {
    private Alliance alliance = Alliance.CROSS;
    private Alliance winner = null;

    private Board board = new Board();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_players);

    }

    public void updateGame(View v) {
        markSquare(v);
        v.setClickable(false);
        setSquareAlliance(v);
        if(checkForWinner()) {
            winner = this.alliance;
            stopGame();
        }
        setAlliance(alliance.getOppositeAlliance());
    }

    private void markSquare(View v) {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            if(alliance.isCross()) {
                v.setBackgroundDrawable(getResources().getDrawable(R.drawable.cross_sign));
            } else {
                v.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_sign));
            }
        } else {
            if(alliance.isCross()) {
                v.setBackground(getResources().getDrawable(R.drawable.cross_sign));
            } else {
                v.setBackground(getResources().getDrawable(R.drawable.circle_sign));
            }
        }

    }

    private void setSquareAlliance(View v) {
        String id = v.getResources().getResourceEntryName(v.getId());
        int index = Integer.parseInt(id.toLowerCase().replace("btn", ""));
        board.gameBoard[index - 1].setAlliance(alliance);
    }

    private boolean checkForWinner() {
        for(int i = 0; i < Board.AMOUNT_OF_COLUMNS; i++) {  // This for loop analyses the columns
            if(checkForWinnerOnColumn(i)) {
                return true;
            }
        }
        for(int i = 0; i < Board.AMOUNT_OF_ROWS; i++) {  // This for loop analyses the rows
            if(checkForWinnerOnRow(i)) {
                return true;
            }
        }
        for(int i = 0; i < Board.AMOUNT_OF_ASCENDING_DIAGONALS; i++) {  // This for loop analyses the ascending diagonals
            if(checkForWinnerOnAscendingDiagonal(i)) {
                return true;
            }
        }
        for(int i = 0; i < Board.AMOUNT_OF_DESCENDING_DIAGONALS; i++) { // This for loop analyses the descending diagonals
            if(checkForWinnerOnDescendingDiagonal(i)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkForWinnerOnColumn(int columnNumber) {
        int symbolsInSequence = 0; // symbol here refers to the cross symbol ( X ) or the circle symbol ( O ) which must appear in sequence for a win
        for (int i = 0; i < Board.AMOUNT_OF_SQUARES; i++) {
            if(Board.getColumn(columnNumber)[i]) {  // The column to be checked is determined by the columnNumber parameter
                if(board.gameBoard[i].getAlliance() == this.alliance) {
                    symbolsInSequence += 1;
                } else {
                    symbolsInSequence = 0;
                }
                if(symbolsInSequence == 5)
                    return true;
            }
        }
        return false;
    }

    private boolean checkForWinnerOnRow(int rowNumber) {
        int symbolsInSequence = 0;
        for(int i = 0; i < Board.AMOUNT_OF_SQUARES; i++) {
            if(Board.getRow(rowNumber)[i]) {
                if(board.gameBoard[i].getAlliance() == this.alliance) {
                    symbolsInSequence += 1;
                } else {
                    symbolsInSequence = 0;
                }
                if(symbolsInSequence == 5)
                    return true;
            }
        }
        return false;
    }

    private boolean checkForWinnerOnAscendingDiagonal(int ascendingDiagonalNumber) {
        int symbolsInSequence = 0;
        for(int i = 0; i < Board.AMOUNT_OF_SQUARES; i++) {
            if(Board.getAscendingDiagonal(ascendingDiagonalNumber)[i]) {
                if(board.gameBoard[i].getAlliance() == this.alliance) {
                    symbolsInSequence += 1;
                } else {
                    symbolsInSequence = 0;
                }
                if(symbolsInSequence == 5)
                    return true;
            }
        }
        return false;
    }

    private boolean checkForWinnerOnDescendingDiagonal(int descendingDiagonalNumber) {
        int symbolsInSequence = 0;
        for(int i = 0; i < Board.AMOUNT_OF_SQUARES; i++) {
            if(Board.getDescendingDiagonal(descendingDiagonalNumber)[i]) {
                if(board.gameBoard[i].getAlliance() == this.alliance) {
                    symbolsInSequence += 1;
                } else {
                    symbolsInSequence = 0;
                }
                if(symbolsInSequence == 5)
                    return true;
            }
        }
        return false;
    }

    private void setAlliance(Alliance al) {
        alliance = al;
    }

    private void stopGame() {
        ConstraintLayout rootView = findViewById(R.id.rootView);
        int count = rootView.getChildCount();
        for (int i=0; i< count - 1; i++) {
            LinearLayout row = (LinearLayout) rootView.getChildAt(i);
            for(int j = 0; j < row.getChildCount(); j++) {
                View v = row.getChildAt(j);
                if (v instanceof Button) {
                    Button btn = (Button)v;
                    btn.setClickable(false);
                }
            }
        }
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TwoPlayersActivity.this);
        View view = getLayoutInflater().inflate(R.layout.winner_alert_dialog, null);
        TextView tv = view.findViewById(R.id.winnerAlert);
        tv.setText(getString(R.string.winner_alert) + " " + winner.toString());
        Button okBtn = view.findViewById(R.id.okButton);
        mBuilder.setView(view);
        final AlertDialog dialog = mBuilder.create();
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

}
