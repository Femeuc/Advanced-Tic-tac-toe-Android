package com.femeuc.advancedtic_tac_toe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class BluetoothMultiplayerBoard extends AppCompatActivity {

    boolean teste = false;

    private Alliance alliance = Alliance.CROSS;
    private Alliance winner = null;
    private Alliance thisDeviceAlliance;
    private Board board = new Board();
    private View chosenSquare = null;
    private String chosenSquareId = null;
    ArrayList<View> lastMarkedSquares = new ArrayList<>();

    TextView circleTextView, crossTextView;

    ConnectedThread connectedThread;

    public static final int MESSAGE_RECEIVED = 1;
    private boolean IS_DEVICE_HOST;
    private boolean IS_PLAYER_READY_TO_NEW_GAME = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_multiplayer_board);

        Intent intent = getIntent();
        IS_DEVICE_HOST = intent.getBooleanExtra(BluetoothMultiplayer.IS_DEVICE_HOST, true);
        thisDeviceAlliance = IS_DEVICE_HOST ? Alliance.CROSS : Alliance.CIRCLE;

        findViewsById();
        makeLayoutAdjustments();
        highlightCurrentPlayer();

        connectedThread = new ConnectedThread(SocketHandler.getSocket());
        connectedThread.start();
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    // Send the obtained bytes to the UI activity.
                    handler.obtainMessage(MESSAGE_RECEIVED, numBytes, -1, mmBuffer).sendToTarget();
//                    readMsg.sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);

                // Share the sent message with the UI activity.
//                Message writtenMsg = handler.obtainMessage(
//                        MESSAGE_RECEIVED, -1, -1, mmBuffer);
//                writtenMsg.sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_RECEIVED:
                    byte[] readBuff = (byte[]) msg.obj;
                    final String opponentMessage = new String(readBuff, 0, msg.arg1);
                    if(!opponentMessage.equals(Alliance.CROSS.toString())
                            && !opponentMessage.equals(Alliance.CIRCLE.toString())
                            && !opponentMessage.equals("ready")) {
                        getOpponentMove(opponentMessage);
                    } else if(opponentMessage.equals("ready")) {
                        teste = true;
                        opponentBeginsNewGame();
                    } else {
                        winner = thisDeviceAlliance.getOppositeAlliance();
                        stopGame();
                    }
                    break;
            }
            return true;
        }
    });

    private void getOpponentMove(String opponentMove) { //todo
        ConstraintLayout rootView = findViewById(R.id.bluetoothMultiplayerBoardRootView);
        int count = rootView.getChildCount();
        for (int i=0; i< count - 1; i++) {
            LinearLayout row = (LinearLayout) rootView.getChildAt(i);
            for(int j = 0; j < row.getChildCount(); j++) {
                final View v = row.getChildAt(j);

                String id = v.getResources().getResourceEntryName(v.getId());
                if(id.equals(opponentMove)) {
                    executeOpponentMove(v);
                }
            }
        }
    }


    private void executeOpponentMove(final View v) {
        markSquare(v);

        v.setClickable(false);
//        board.gameBoard[getButtonIdNumber(getViewId(v)) - 1].setAlliance(alliance.getOppositeAlliance()); // Assigns opposite alliance to opponent move
        board.gameBoard[getButtonIdNumber(getViewId(v)) - 1].setAlliance(thisDeviceAlliance.getOppositeAlliance());
//        if(checkForWinner()) {
//            winner = this.alliance.getOppositeAlliance();
//            stopGame();
//        }
//        lastMarkedSquares.add(chosenSquare);
        setAlliance(alliance.getOppositeAlliance());
        lastMarkedSquares.add(v);
        highlightCurrentPlayer();
        highlightLastMove();
    }

    private void opponentBeginsNewGame() {
        clearBoardInUI();
        IS_DEVICE_HOST = !IS_DEVICE_HOST;
        thisDeviceAlliance = IS_DEVICE_HOST ? Alliance.CROSS : Alliance.CIRCLE;
        alliance = Alliance.CROSS;
        lastMarkedSquares = new ArrayList<>();
        resetBoardSquares();
        makeLayoutAdjustments();
        highlightCurrentPlayer();
        changeNewGameButtonToConfirmButton();
    }

    private int getButtonIdNumber(String viewId) {
        return Integer.parseInt(viewId.toLowerCase().replace("btn", ""));
    }

    private String getViewId(View v) {
        return v.getResources().getResourceEntryName(v.getId());
    }

    public void chooseSquare(View v) {
        if((IS_DEVICE_HOST && this.alliance.isCross()) || (!IS_DEVICE_HOST && !this.alliance.isCross())) {
            if(chosenSquare != null) {
                undoMarkSquare(chosenSquare);
            }
            markSquare(v);
            chosenSquare = v;
        } else {
            Toast.makeText(this, "It is not your turn", Toast.LENGTH_SHORT).show();
        }
    }

    public void confirmMove(View v) {
        if((IS_DEVICE_HOST && this.alliance.isCross()) || (!IS_DEVICE_HOST && !this.alliance.isCross())) {
            if(chosenSquare == null) {
                Toast.makeText(this, "You must choose a square first", Toast.LENGTH_SHORT).show();
                return;
            }
            chosenSquare.setClickable(false);
            setSquareAlliance(chosenSquare);
            if(checkForWinner()) {
                winner = this.alliance;
                connectedThread.write(winner.toString().getBytes());
                stopGame();
            }
            sendMoveToOpponentDevice();
            setAlliance(alliance.getOppositeAlliance());
            lastMarkedSquares.add(chosenSquare);
            highlightCurrentPlayer();
            highlightLastMove();
            chosenSquare = null;
        } else {
            Toast.makeText(this, "It is not your turn", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMoveToOpponentDevice() {
        String moveMessage = chosenSquareId;
        connectedThread.write(moveMessage.getBytes());
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
        chosenSquareId = getViewId(v);
        board.gameBoard[getButtonIdNumber(getViewId(v)) -1].setAlliance(alliance);
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
        ConstraintLayout rootView = findViewById(R.id.bluetoothMultiplayerBoardRootView);
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
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(BluetoothMultiplayerBoard.this);
        View view = getLayoutInflater().inflate(R.layout.winner_alert_dialog, null);
        TextView tv = view.findViewById(R.id.winnerAlert);
        String victoryOrDefeatAlert = thisDeviceAlliance == winner ? "VICTORY" : "DEFEAT";
        tv.setText(victoryOrDefeatAlert + "\n" + getString(R.string.winner_alert) + " " + winner.toString());
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
        changeConfirmButtonToNewGameButton();
    }

    private void changeConfirmButtonToNewGameButton() {
        Button btn = (Button) findViewById(R.id.confirmButton);
        btn.setText(R.string.new_game);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });
    }

    private void changeNewGameButtonToConfirmButton() {
        Button btn = (Button) findViewById(R.id.confirmButton);
        btn.setText("Confirm");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmMove(v);
            }
        });
    }

    private void newGame() {
        clearBoardInUI();
        IS_DEVICE_HOST = !IS_DEVICE_HOST;
        thisDeviceAlliance = IS_DEVICE_HOST ? Alliance.CROSS : Alliance.CIRCLE;
        alliance = Alliance.CROSS;
        lastMarkedSquares = new ArrayList<>();
        resetBoardSquares();
        makeLayoutAdjustments();
        highlightCurrentPlayer();
        connectedThread.write("ready".getBytes());
        changeNewGameButtonToConfirmButton();
    }

    private void clearBoardInUI() {
        ConstraintLayout rootView = findViewById(R.id.bluetoothMultiplayerBoardRootView);
        int count = rootView.getChildCount();
        for (int i=0; i< count - 1; i++) {
            LinearLayout row = (LinearLayout) rootView.getChildAt(i);
            for(int j = 0; j < row.getChildCount(); j++) {
                View v = row.getChildAt(j);
                if (v instanceof Button) {
                    Button btn = (Button)v;
                    btn.setClickable(true);
                    undoMarkSquare(btn);
                }
            }
        }
    }

    private void resetBoardSquares() {
        for(int i = 0; i < Board.AMOUNT_OF_SQUARES; i++) {
            board.gameBoard[i].setAlliance(null);
        }
    }

    private void undoMarkSquare(View v) {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
        } else {
            v.setBackground(getResources().getDrawable(R.drawable.border));
        }
    }

    private void highlightCurrentPlayer() {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            if(alliance.isCross()) {
                findViewById(R.id.crossTextView).setBackgroundDrawable(getResources().getDrawable(R.drawable.active_player));
                findViewById(R.id.circleTextView).setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
            } else {
                findViewById(R.id.circleTextView).setBackgroundDrawable(getResources().getDrawable(R.drawable.active_player));
                findViewById(R.id.crossTextView).setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
            }
        } else {
            if(alliance.isCross()) {
                findViewById(R.id.crossTextView).setBackground(getResources().getDrawable(R.drawable.active_player));
                findViewById(R.id.circleTextView).setBackground(getResources().getDrawable(R.drawable.border));
            } else {
                findViewById(R.id.circleTextView).setBackground(getResources().getDrawable(R.drawable.active_player));
                findViewById(R.id.crossTextView).setBackground(getResources().getDrawable(R.drawable.border));
            }
        }
    }

    private void highlightLastMove() {
        if(lastMarkedSquares.size() == 0)
            return;
        View v = lastMarkedSquares.get(lastMarkedSquares.size() - 1);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            if(alliance.getOppositeAlliance().isCross()) {
                v.setBackgroundDrawable(getResources().getDrawable(R.drawable.highlighted_last_move_cross));
                if(lastMarkedSquares.size() >= 2) {
                    lastMarkedSquares.get(lastMarkedSquares.size() - 2).setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_sign));
                }
            } else {
                v.setBackgroundDrawable(getResources().getDrawable(R.drawable.highlighted_last_move_circle));
                if(lastMarkedSquares.size() >= 2) {
                    lastMarkedSquares.get(lastMarkedSquares.size() - 2).setBackgroundDrawable(getResources().getDrawable(R.drawable.cross_sign));
                }
            }
        } else {
            if(alliance.getOppositeAlliance().isCross()) {
                v.setBackground(getResources().getDrawable(R.drawable.highlighted_last_move_cross));
                if(lastMarkedSquares.size() >= 2) {
                    lastMarkedSquares.get(lastMarkedSquares.size() - 2).setBackground(getResources().getDrawable(R.drawable.circle_sign));
                }
            } else {
                v.setBackground(getResources().getDrawable(R.drawable.highlighted_last_move_circle));
                if(lastMarkedSquares.size() >= 2) {
                    lastMarkedSquares.get(lastMarkedSquares.size() - 2).setBackground(getResources().getDrawable(R.drawable.cross_sign));
                }
            }
        }
    }




    private void findViewsById() {
        circleTextView = findViewById(R.id.circleTextView);
        crossTextView = findViewById(R.id.crossTextView);
    }

    private void makeLayoutAdjustments() {
        if(IS_DEVICE_HOST) {
            crossTextView.setText("YOU");
            circleTextView.setText("OPPONENT");
        } else {
            crossTextView.setText("OPPONENT");
            circleTextView.setText("YOU");
        }
    }
}
