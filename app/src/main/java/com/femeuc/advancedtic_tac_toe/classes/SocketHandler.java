package com.femeuc.advancedtic_tac_toe.classes;

import android.bluetooth.BluetoothSocket;

public class SocketHandler {
    private static BluetoothSocket socket;

    public static synchronized BluetoothSocket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(BluetoothSocket socket){
        SocketHandler.socket = socket;
    }
}
